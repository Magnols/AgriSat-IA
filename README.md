# Projeto - Cidades ESG Inteligentes

## AgriSat IA

## Sobre o projeto

AgriSat IA é a aplicação ESG utilizada para implementar práticas DevOps nesta atividade da FIAP. O protótipo agrícola em HTML/CSS/JavaScript acompanha uma API Spring Boot de eficiência energética: áreas, equipamentos, leituras, alertas e relatórios. O frontend ainda é demonstrativo, com dados simulados, e não possui integração funcional com a API. O backend conserva o nome técnico `energia-esg-api`.

**Situação verificável em 05/10/2026:** resultados locais comprovados nos logs anteriores. GitHub Actions BUILD, TEST AND PACKAGE e DOCKER BUILD aprovados para 0b5dfe2. Staging falhou por DEPLOY_HOST ausente; production não executou. Não há ZIP final.

## Arquitetura

Frontend Nginx independente, API Spring Boot Java 21 e Oracle FREEPDB1. A API usa JDBC/Flyway; serviços Compose compartilham rede bridge, Oracle usa volume persistente. H2 exclusivo dos testes. Staging e produção usam projetos Compose e configurações independentes; podem compartilhar um host x64 com memória suficiente e portas distintas.

## Como executar localmente com Docker

Pré-requisitos: Git e Docker Desktop com engine Linux operacional. Java 21 é necessário para comandos Maven fora de Docker. O Oracle requer memória disponível e pode levar vários minutos na primeira inicialização.

No PowerShell:

```powershell
git clone https://github.com/Magnols/AgriSat-IA.git
cd AgriSat-IA
Copy-Item .env.example .env
notepad .env
# Substitua DB_PASSWORD e API_SECURITY_PASSWORD por senhas locais fortes e distintas.
docker info
docker compose config --quiet
docker build -t agrisat-api:local .
docker compose build
docker compose up -d --wait --wait-timeout 900
docker compose ps
docker compose logs --tail=100
curl.exe --fail http://localhost:8080/actuator/health
```

Health deve retornar HTTP 200 e `{"status":"UP"}`. Frontend: `http://localhost:8081`. Porta do banco: 1521, vinculada somente a localhost. Para outra porta pública da API, altere SERVER_PORT no .env; a porta interna permanece 8080.

Não compartilhe `docker compose config` sem redigir valores: o comando completo expande senhas. Use `--quiet` para validar.

Para validar a API sem alterar dados:

```powershell
# Informe a senha quando o curl solicitar. Não coloque a senha no histórico.
curl.exe --fail --user admin http://localhost:8080/areas
curl.exe --fail --user admin http://localhost:8080/equipamentos
curl.exe --fail --user admin http://localhost:8080/alertas
curl.exe --fail --user admin http://localhost:8080/relatorios/consumo-diario
```

Sem autenticação, os endpoints de negócio retornam 401. As cinco consultas GET (/areas, /equipamentos, /alertas, /leituras e /relatorios/consumo-diario) responderam HTTP 200 no Oracle real. /areas sem autenticação respondeu 401. Registros EVIDENCIA-PERSISTENCIA são dados criados exclusivamente para validação local.

## Testes

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

JAR: `target/energia-esg-api-0.0.1-SNAPSHOT.jar` (92.311.659 bytes). Última execução após correção NUMERIC: 7 passaram, zero falhas/erros/ignorados. Maven clean test: 1min40s; clean package: 1min29s, também com os 7 testes. H2 em modo Oracle é exclusivo dos testes. A compatibilidade Oracle foi verificada separadamente no Compose.

Para iniciar o JAR com um Oracle existente, exporte SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD, API_SECURITY_USER e API_SECURITY_PASSWORD no terminal. Copiar .env sozinho não exporta variáveis para Java.

## Pipeline CI/CD

### Integração Azure Boards e GitHub

Planejamento informado: organização RM565548, projeto AgriSat-IA, Epic #1 e Issue #2. Commits relacionados ao backend/documentação usam AB#2 para rastreabilidade. A associação deve aparecer no Work Item após publicação, não é presumida apenas pela mensagem. Procedimento e verificação em [AZURE-BOARDS-GITHUB](documentacao/AZURE-BOARDS-GITHUB.md). Azure Boards não substitui a ferramenta CI/CD oficial, GitHub Actions. Não foram recriados Work Items.

Ferramenta: GitHub Actions. Configuração em `.github/workflows/ci-cd.yml` e workflow reutilizável `deploy.yml`.

PUSH/PR em main ou workflow_dispatch executam:
1. BUILD: Maven clean compile com Java 21 e cache Maven.
2. TEST AND PACKAGE: Maven clean verify, testes ativos e relatório Surefire.
3. DOCKER BUILD: build multi-stage com testes ativos e imagem identificada pelo SHA do commit.
4. STAGING: transferência SSH da mesma imagem construída, Docker Compose e verificação pública de health.
5. PRODUCTION: mesma operação após sucesso de staging, usando configuração isolada.

PR executa CI e Docker, mas não deploy. Push/main ou execução manual pode executar deploy. Jobs falham se testes, health ou configuração obrigatória falharem. Não há bypass de testes nem deploy baseado apenas em echo.

A imagem é transportada como artifact `release`, contendo `image.tar.gz`, Compose, frontend e scripts. Não exige registry externo. Relatórios ficam no artifact `test-reports`.

**CI remoto comprovado:** [execução 37299189626](https://github.com/Magnols/AgriSat-IA/actions/runs/37299189626), commit 0b5dfe2. BUILD, TEST AND PACKAGE e DOCKER BUILD passaram. clean verify: 7 testes, zero falhas/erros/ignorados. Staging falhou na configuração obrigatória DEPLOY_HOST; production foi skipped. Environments staging/Production já existem, mas secrets e variables estavam vazios na consulta de 05/10. O pipeline completo não está aprovado.

## Containerização

O Dockerfile usa dois estágios: Maven/Temurin Java 21 para executar Maven Wrapper clean package, e Temurin JRE 21 Alpine para executar o JAR. O usuário de runtime não é root. Porta interna: 8080. Healthcheck: actuator/health. Build aprovado com os 7 testes ativos. Conteúdo da imagem: aproximadamente 157 MB; Docker informa cerca de 461 MB de uso em disco incluindo camadas descompactadas.

Trechos principais:

```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src src
RUN chmod +x mvnw && ./mvnw -B -ntp clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S agrisat && adduser -S agrisat -G agrisat
COPY --from=build --chown=agrisat:agrisat /workspace/target/energia-esg-api-0.0.1-SNAPSHOT.jar app.jar
USER agrisat
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

O arquivo completo inclui HEALTHCHECK. .dockerignore exclui Git, segredos locais, builds, IDEs e logs do contexto.

## Docker Compose

- Aplicação: API Spring Boot e frontend Nginx.
- Banco: `gvenzl/oracle-free:23-slim`, service name `FREEPDB1`, usuário de aplicação `agrisat_user`.
- Variáveis: DB_PASSWORD e API_SECURITY_PASSWORD obrigatórias, demais parâmetros em .env.example.
- Volume: `agrisat-oracle-data` em `/opt/oracle/oradata`.
- Rede: `agrisat-network`, driver bridge. JDBC usa o DNS interno `oracle-db`.
- Inicialização: API aguarda health do Oracle. Frontend aguarda health da API.
- Flyway: `V1__create_tables.sql`; Hibernate configurado para validar o schema.
- Isolamento: nomes de containers gerenciados por projeto Compose, permitindo ambientes separados.

A sintaxe, build e inicialização passaram. Os três serviços ficaram healthy. Flyway V1 aplicada com success=1, schema validado pelo Hibernate e comunicação API/Oracle comprovada por GETs e gravação/leitura de limiteConsumo=123.45. Os campos Double usam mapeamento JDBC NUMERIC com precisão 10 e escala 2, alinhado ao schema existente. A V1 aplicada foi preservada.

A imagem Oracle baixada informa versão 23.26.3 (tag 23-slim). Flyway emite aviso de versão Oracle mais nova que a cobertura testada, mas validou/aplicou V1 e não bloqueou o startup. Esse aviso permanece documentado.

### Persistência

Depois de validar Oracle/Flyway, registre as mesmas linhas e o histórico Flyway antes/depois de reiniciar serviços:

```powershell
docker compose exec oracle-db sh -c 'printf "select \"version\", \"success\" from \"flyway_schema_history\";\nselect count(*) from AREA;\nexit;\n" | sqlplus -s "$APP_USER/$APP_USER_PASSWORD@//localhost/FREEPDB1"'
docker compose restart oracle-db
docker compose up -d --wait --wait-timeout 900
# Repita a consulta e compare o resultado.
```

Persistência comprovada em 03/10/2026: o container Oracle foi recriado sem remover volume; a área id=2, EVIDENCIA-PERSISTENCIA-20261003-163315, permaneceu acessível pela API. O valor NUMERIC 123.45 também foi gravado/lido. Reprodução: `./scripts/validar-local.ps1 -TestarPersistencia`. Esse comando cria registros de evidência locais. Não use `down -v`, pois remove dados.

## Prints do funcionamento

Roteiro completo: [GUIA-EVIDENCIAS](documentacao/evidencias/GUIA-EVIDENCIAS.md). Salvar capturas reais em `documentacao/prints` e inserir links abaixo somente depois de existirem.

- Testes/build: logs reais em `documentacao/evidencias/resultados`; prints 01 e 02 pendentes.
- Docker/Compose: prints 03 a 07 pendentes.
- GitHub Actions: [BUILD](documentacao/prints/08-github-actions-build.png), [TEST AND PACKAGE](documentacao/prints/09-github-actions-tests.png), [DOCKER BUILD](documentacao/prints/10-github-actions-docker.png), capturas reais da execução 37299189626.
- Staging: prints 11 e 12 pendentes.
- Production: prints 13 e 14 pendentes.

Logs e trechos transcritos são evidências textuais, não capturas de tela. Não há imagens inventadas.

## Staging

URL: pendente de deploy real. Ambiente GitHub previsto: `staging`.
Preparação manual detalhada em [ACAO-MANUAL-DEPLOY](documentacao/ACAO-MANUAL-DEPLOY.md).
Deploy só é aprovado se Compose estiver healthy, GET autenticado /areas responder e a URL pública de health indicar UP.

## Produção

URL: pendente de deploy real. Ambiente GitHub previsto: `production`.
Production depende do sucesso de staging, com banco/volume, diretório e credenciais independentes. Servidores devem usar HTTPS antes de transmitir Basic Auth publicamente.

## Tecnologias utilizadas

Java 21, Spring Boot 3.5.14, Spring Web, JPA/Hibernate, Security, Validation, Actuator, Spring Cloud, Flyway, Oracle JDBC, Maven Wrapper 3.9.15, H2 para testes, JUnit, Mockito, Docker, Docker Compose, GitHub Actions, HTML, CSS, JavaScript e Nginx. Oracle e Nginx executados e saudáveis neste PC.

## Estrutura do projeto

```text
AgriSat-IA/
├── .github/workflows/  # CI/CD e deploy reutilizável
├── .mvn/wrapper/
├── src/main/           # Java, configurações e Flyway
├── src/test/           # Serviços, health e autenticação
├── frontend/           # Protótipo atual
├── scripts/deploy.sh
├── documentacao/       # Evidências e apresentação
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── .dockerignore
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

O frontend permanece também na raiz para preservar caminhos da publicação estática existente. Essas duas cópias devem ser mantidas sincronizadas até que a configuração de hospedagem permita consolidá-las.

## Segurança

.env.example contém placeholders. .env, chaves e builds são ignorados. Credenciais da API/banco ficam no .env local ou no servidor, com acesso restrito. Nunca publique sua saída completa de configuração ou inspeção de containers.

Cada GitHub Environment utiliza quatro secrets: DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY e DEPLOY_KNOWN_HOSTS. DEPLOY_URL é uma variável do Environment. Host keys devem ser verificadas por canal confiável. O workflow usa StrictHostKeyChecking, não expõe chaves nos logs e remove a chave temporária ao terminar.

API exige Basic Auth e health é público. Use HTTPS para acesso autenticado em ambientes públicos. Não haverá deploy enquanto faltar a infraestrutura. A validação Docker local passou; acompanhe publicação e CI em documentacao/GIT-ACTIONS-RETOMADA.md. Resultados atuais em documentacao/RELATORIO-RETOMADA.md. O PDF existente é uma versão parcial histórica e precisa atualizar resultados/prints antes da entrega.

## Integrante

Magno Pereira da Silva
RM 565548
Análise e Desenvolvimento de Sistemas
FIAP

## Checklist da entrega

Marcar somente após execução/evidência. Dockerfile e Compose passaram na execução local; os requisitos de CI/deploy e documentação final permanecem pendentes.

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☑ |
| docker-compose.yml ou arquivos Kubernetes | ☑ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☐ |
