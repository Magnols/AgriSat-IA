# AgriSat IA — Cidades ESG Inteligentes

## Visão geral da solução

AgriSat IA reúne um protótipo agrícola em HTML/CSS/JavaScript e uma API Spring Boot de gestão energética. O backend conserva o identificador energia-esg-api e oferece áreas, equipamentos, leituras, alertas e relatórios de consumo diário.

O frontend utiliza dados simulados e navegação demonstrativa. Não estão implementados autenticação Google, SMS, integração com satélites ou modelos de IA; o protótipo ainda não consome a API. A publicação estática existente não comprova deploy público do backend.

Diretório local de trabalho: C:\ESD\AgriSat. A cópia original no OneDrive foi preservada. Resultados atuais e pendências: [validação técnica](documentacao/VALIDACAO.md).

## Arquitetura do sistema

Nginx serve o frontend na porta 8081. A API Java executa na porta 8080 e acessa Oracle pelo DNS interno oracle-db, porta 1521, service FREEPDB1. Flyway versiona o schema; Hibernate valida sua estrutura. H2 é exclusivo do perfil de testes.

Compose conecta os três serviços pela rede bridge agrisat-network. Oracle persiste em agrisat-oracle-data, montado em /opt/oracle/oradata. Sua porta publicada é restrita a 127.0.0.1.

O projeto Compose local mantém o nome agrisat-ia após a mudança de diretório, preservando o volume anterior. Deploys usam nomes agrisat-staging e agrisat-production e configurações isoladas.

## Tecnologias utilizadas

Java 21, Spring Boot 3.5.14, Maven Wrapper 3.9.15, Spring Web, Security, Validation, Actuator, JPA/Hibernate, Flyway, Oracle JDBC, JUnit, Mockito, H2, Docker, Docker Compose, Nginx, GitHub Actions, HTML, CSS e JavaScript. As dependências Spring Cloud existentes foram preservadas; Eureka permanece desabilitado por padrão.

## Pré-requisitos

Docker Engine Linux e Docker Compose operacionais, memória disponível para Oracle/JVM e Git. Java 21/JAVA_HOME são necessários para testes Maven fora dos containers. O Maven Wrapper baixa dependências quando necessário.

## Como executar localmente com Docker

~~~powershell
cd C:\ESD\AgriSat
# Somente se .env não existir:
Copy-Item .env.example .env
# Configure senhas fortes e distintas no .env, sem publicá-las.
docker info
docker compose config --quiet
docker build -t agrisat-api:local .
docker compose build
docker compose up -d --no-build --wait --wait-timeout 900
docker compose ps
curl.exe --fail http://localhost:8080/actuator/health
~~~

Health público deve retornar HTTP 200 e status UP. Frontend local: http://localhost:8081. Para instalar uma nova cópia, copie .env.example apenas quando não houver .env; nunca sobrescreva credenciais ou volumes existentes.

Para consultas autenticadas sem colocar senha no histórico:

~~~powershell
curl.exe --fail --user admin http://localhost:8080/areas
curl.exe --fail --user admin http://localhost:8080/equipamentos
./scripts/validar-local.ps1
~~~

O curl solicita a senha. O script lê .env internamente e não imprime credenciais. Sem autenticação, rotas de negócio retornam 401. JSON malformado e parâmetros incompatíveis retornam 400.

## Variáveis de ambiente

| Variável | Finalidade |
|---|---|
| DB_USER | Usuário Oracle criado pelo container |
| DB_PASSWORD | Senha Oracle obrigatória |
| API_SECURITY_USER | Usuário Basic Auth |
| API_SECURITY_PASSWORD | Senha da API obrigatória e distinta da senha Oracle |
| SERVER_PORT | Porta publicada da API; interna 8080 |
| FRONTEND_PORT | Porta publicada do Nginx |
| DB_PORT | Porta Oracle publicada somente em localhost |
| API_IMAGE | Imagem/tag usada na implantação |

.env.example contém apenas placeholders. .env real é ignorado. No servidor, mantenha .env com permissão 600. Não compartilhe docker compose config expandido nem docker inspect completo: podem revelar senhas.

Para executar o JAR fora do Compose, exporte SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD, API_SECURITY_USER e API_SECURITY_PASSWORD. Copiar .env não exporta variáveis para Java. O padrão Oracle é FREEPDB1 e ddl-auto=validate.

## Containerização

O Dockerfile multi-stage usa Maven/Temurin 21 para executar ./mvnw -B -ntp clean package, com todos os testes. O runtime Temurin JRE 21 Alpine contém somente o JAR e executa como usuário agrisat, não root. WORKDIR /app, EXPOSE 8080, ENTRYPOINT java -jar app.jar e healthcheck wget em /actuator/health.

.dockerignore exclui Git, credenciais locais, builds, IDEs, documentação e entregas do contexto. No diretório C:\ESD\AgriSat, o build lê os arquivos locais diretamente, sem o contorno temporário usado no OneDrive.

Compose aguarda o Oracle saudável antes de iniciar a API e a API saudável antes do Nginx. Volumes, rede, dependências e variáveis estão no arquivo docker-compose.yml.

## Pipeline CI/CD

GitHub Actions executa em push/PR de main e workflow_dispatch:

1. BUILD: Java21, checkout, cache Maven e clean compile.
2. TEST AND PACKAGE: clean verify, testes e relatórios Surefire.
3. DOCKER BUILD: imagem multi-stage com testes e release identificada pelo SHA.
4. STAGING: transporte SSH da mesma imagem, Compose e health público.
5. PRODUCTION: implantação após sucesso de staging e nova validação.

PR não realiza deploy. Não há skipTests. Release inclui imagem, Compose, frontend e scripts; staging e produção reutilizam a imagem validada.

A última execução remota comprovada é [37753337819](https://github.com/Magnols/AgriSat-IA/actions/runs/37753337819), commit 325c180: build/testes/Docker passaram; staging falhou por DEPLOY_HOST e produção não executou. Isso não valida automaticamente alterações locais posteriores.

## Ambientes staging e produção

Não há URL pública da API validada em nenhum dos ambientes. Ambos dependem de infraestrutura autorizada e configuração externa. Procedimento: [operação dos ambientes](documentacao/ACAO-MANUAL-DEPLOY.md).

Secrets por environment: DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY e DEPLOY_KNOWN_HOSTS. Variable: DEPLOY_URL. Valide a fingerprint SSH por canal confiável; o workflow usa StrictHostKeyChecking. Production depende de staging e o primeiro deploy exige autorização humana explícita. Configure proteção/aprovação antes de habilitar a execução.

Use HTTPS antes de transmitir Basic Auth publicamente. Não exponha Oracle à internet. Uma abertura de portal Azure não comprova servidor ou deploy.

## Testes automatizados

~~~powershell
.\mvnw.cmd clean verify
~~~

Os sete testes existentes foram preservados. Três novos testes verificam nome inválido, JSON malformado e identificador não numérico. A execução local registrou 10 testes, zero falhas, erros e ignorados, com BUILD SUCCESS. O JAR é target/energia-esg-api-0.0.1-SNAPSHOT.jar.

Testes Spring usam H2; integração Oracle/Flyway é verificada separadamente na stack real. Não remova dados para repetir persistência. O script -TestarPersistencia cria registros e recria o container Oracle preservando o volume; execute somente quando essa intervenção for desejada e autorizada.

## Evidências de funcionamento

[Índice das evidências](documentacao/evidencias/INDICE.md) identifica origem, data, commit, resultados e limitações. Os logs atuais em documentacao/evidencias/resultados comprovam comandos reais. Capturas anteriores de CI foram preservadas, inclusive staging reprovado e produção skipped; não são prints de deploy bem-sucedido.

A captura contextual enviada pelo responsável mostra interfaces abertas, mas não confirma vínculo AB#2, servidor ou deploy. As telas do diretório novo e do terminal dependem de captura gráfica disponível.

Azure Boards registra planejamento e rastreabilidade. Commit 60db9e7 contém AB#2; o vínculo efetivo no Issue #2 e Parent Epic #1 precisa ser confirmado em sessão autenticada.

## Estrutura do repositório

~~~text
AgriSat/
├── .github/workflows/
├── .mvn/wrapper/
├── src/main/                 # API e migração Flyway
├── src/test/                 # Testes existentes e validação HTTP
├── frontend/                 # Protótipo demonstrativo
├── documentacao/             # Operação, validação e evidências
├── scripts/                  # Deploy e consultas de validação
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .env.example
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
~~~

A cópia estática do frontend na raiz foi mantida para compatibilidade com a hospedagem anterior. Não houve mudança desses caminhos remotos. Relatórios antigos foram arquivados em documentacao/historico, ignorada no Git e excluída do pacote atual.

Responsável: Magno Pereira da Silva, RM 565548, Análise e Desenvolvimento de Sistemas, FIAP. Avisos de uso acadêmico e copyright existentes foram preservados no frontend.
