# Conteúdo da apresentação - AgriSat IA DevOps

Versão parcial em 03/10/2026. Resultados comprovados e pendências explicitados. PDF/PPT definitivo precisa incorporar capturas reais de CI e ambientes.

## SLIDE 1 - AgriSat IA - DevOps

### Texto

Magno Pereira da Silva | RM 565548
Análise e Desenvolvimento de Sistemas - FIAP
Projeto: Cidades ESG Inteligentes
Estado da atividade: validação parcial, com Docker e deploys pendentes.

### Elementos visuais

Logo original do AgriSat IA, preservando proporções. Capa com fundo claro e tipografia verde.

### Print/evidência correspondente

Identificação do integrante e repositório. Não exige screenshot de execução.

### Fala sugerida

Apresento a aplicação ESG e as práticas DevOps implementadas. Diferencio os resultados já executados das validações que ainda dependem do computador e de infraestrutura externa.

## SLIDE 2 - Projeto e contexto ESG

### Texto

O protótipo AgriSat IA apresenta monitoramento agrícola e recomendações demonstrativas.
A API energia-esg-api gerencia áreas, equipamentos, leituras e alertas de consumo energético.
O frontend utiliza dados simulados e ainda não consome a API.

### Elementos visuais

Descrição das duas partes da aplicação. Usar print real do frontend após execução, sem sugerir integração inexistente.

### Print/evidência correspondente

06-aplicacao.png pendente.

### Fala sugerida

O contexto é eficiência operacional e sustentabilidade. O protótipo agrícola e a API de energia compõem a entrega acadêmica. Os dados de tela são demonstrativos, uma limitação explicitada na documentação.

## SLIDE 3 - Arquitetura da aplicação

### Texto

Frontend HTML/CSS/JavaScript servido por Nginx.
API Spring Boot 3.5.14, Java 21 e autenticação HTTP Basic.
Oracle Free com service FREEPDB1 e schema inicial gerenciado por Flyway.
H2 apenas nos testes. Rede bridge e volume persistente no Compose.

### Elementos visuais

Diagrama arquitetural com frontend e API como componentes independentes, API conectada ao Oracle. Evitar seta frontend→API que sugira integração funcional.

### Print/evidência correspondente

05-compose-ps.png e 15-oracle-flyway.png pendentes.

### Fala sugerida

A rede interna permite que a API encontre o Oracle pelo DNS oracle-db. Flyway aplica a versão inicial do schema. Os testes usam H2 e não comprovam por si só a execução em Oracle.

## SLIDE 4 - Containerização com Docker

### Texto

Build multi-stage: Maven/Temurin 21 executa Maven Wrapper clean package com testes.
Runtime: Temurin JRE 21 Alpine, usuário agrisat sem root e porta 8080.
Comando: docker build -t agrisat-api:local .
Imagem planejada: agrisat-api:local. Build e tamanho ainda não validados.

### Elementos visuais

Trechos reais do Dockerfile em tipografia monoespaçada. Print do docker build somente após execução bem-sucedida.

### Print/evidência correspondente

03-docker-build.png pendente.

### Fala sugerida

O estágio de build gera o JAR e executa testes. O runtime recebe apenas o artefato. O daemon Docker não iniciou neste PC, portanto não apresento uma imagem criada como resultado aprovado.

## SLIDE 5 - Orquestração Docker Compose

### Texto

Serviços configurados: oracle-db, api e frontend.
Volume agrisat-oracle-data guarda /opt/oracle/oradata.
Rede agrisat-network conecta os serviços.
Variáveis obrigatórias: DB_PASSWORD e API_SECURITY_PASSWORD.
Compose config --quiet: aprovado. Execução e persistência: pendentes.

### Elementos visuais

Tabela com serviço, função e dependência. Mostrar configuração real de volume/rede, omitindo senhas.

### Print/evidência correspondente

04-compose-build.png, 05-compose-ps.png, 16-volume-network.png e 17-persistencia.png pendentes.

### Fala sugerida

A sintaxe Compose foi validada, mas não confundo isso com os serviços funcionando. A API aguarda health do banco. A persistência será demonstrada por consultas antes e depois de um reinício sem excluir volume.

## SLIDE 6 - Pipeline CI/CD

### Texto

GitHub Actions: push/PR em main e execução manual.
Sequência: BUILD, TEST AND PACKAGE, DOCKER BUILD, STAGING, PRODUCTION.
PR executa CI sem deploy. Production depende de staging.
A mesma imagem identificada pelo SHA segue para os ambientes via artifact e SSH.
Execução remota ainda pendente.

### Elementos visuais

Fluxograma das etapas e destaque da dependência entre os ambientes.

### Print/evidência correspondente

08-actions-build.png, 09-actions-test.png e 10-actions-docker.png pendentes.

### Fala sugerida

O workflow não ignora testes. Substituí os jobs de mensagens por comandos reais de transferência, Compose e health. A execução no GitHub ainda precisa ser comprovada após validação Docker e push.

## SLIDE 7 - Build e testes automatizados

### Texto

clean test: 7 testes, 7 sucessos, zero falhas, erros ou ignorados. Tempo total: 47,039 s.
clean package: BUILD SUCCESS em 49,532 s, com os 7 testes ativos.
JAR: energia-esg-api-0.0.1-SNAPSHOT.jar.
Cobertura: AreaService, EquipamentoService, contexto Spring, health público e proteção da API.

### Elementos visuais

Trechos dos logs reais, claramente rotulados como saída textual. Prints de terminal permanecem pendentes.

### Print/evidência correspondente

Logs reais resultados/01-maven-test.txt e resultados/02-maven-package.txt. Prints 01/02 pendentes.

### Fala sugerida

As duas execuções foram reais. O primeiro erro veio de marcadores BOM nos fontes e foi corrigido. A suíte atual confirma regras dos serviços e configuração HTTP. Os resultados em H2 não substituem os testes operacionais em Oracle.

## SLIDE 8 - Staging e produção

### Texto

Deploy implementado via SSH com Compose e configuração por GitHub Environment.
Staging: sem servidor/URL validada. Production: sem servidor/URL validada.
Secrets por ambiente: DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY e DEPLOY_KNOWN_HOSTS.
Variável: DEPLOY_URL. Senhas API/Oracle ficam no .env protegido do servidor.
Deploy falha se faltar configuração ou health público não indicar UP.

### Elementos visuais

Tabela staging/production com status PENDENTE. Inserir URLs e prints reais após deploy.

### Print/evidência correspondente

11-deploy-staging.png, 12-staging-funcionando.png, 13-deploy-production.png e 14-production-funcionando.png pendentes.

### Fala sugerida

Os nomes no YAML não são evidência de deploy. A infraestrutura externa ainda não foi criada. A alternativa documentada é Azure for Students, sujeita a elegibilidade, sem cartão e com consumo limitado aos créditos.

## SLIDE 9 - Desafios encontrados e soluções

### Texto

Compilação: BOM inválido em fontes. Solução aplicada: normalização UTF-8 sem BOM.
Pipeline: testes ignorados e deploys apenas com echo. Solução: testes ativos e deploy SSH com health.
Docker: kernel WSL ausente e virtualização reportada desativada. Atualização WSL executada; validação depende do ambiente.
Segurança: remover senhas padrão do Compose e exigir variáveis.

### Elementos visuais

Tabela problema, correção e resultado comprovado. Não usar screenshot fictício de erro/solução.

### Print/evidência correspondente

Logs Maven, arquivos corrigidos e diagnóstico do computador. Prints correspondentes pendentes.

### Fala sugerida

Corrigi o que pertence ao projeto e registrei as limitações do PC. Habilitação de virtualização e reinício podem exigir intervenção humana. A ausência de deploy permanece visível no checklist.

## SLIDE 10 - Resultados e checklist

### Texto

Comprovado: 7 testes aprovados, JAR criado e sintaxe Compose aprovada.
Pendente: imagem Docker, Oracle/Flyway, persistência, CI remoto, ambientes públicos e prints.
README e roteiro técnico atualizados. PDF preparado como versão parcial.
ZIP final, commit e push aguardam as condições definidas para a entrega.

### Elementos visuais

Checklist acadêmico completo. Usar caixas vazias para requisitos ainda sem evidência final.

### Print/evidência correspondente

Checklist no README e neste documento. Nenhum screenshot criado nesta etapa.

### Fala sugerida

A atividade ainda não está concluída. O próximo passo é habilitar o Docker e executar a validação operacional. Depois vêm CI remoto, deploys e capturas reais; somente então o ZIP final será gerado.

## Checklist obrigatório

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☐ |
| docker-compose.yml ou arquivos Kubernetes | ☐ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☐ |
