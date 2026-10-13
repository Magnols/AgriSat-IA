# Índice de evidências

Validação da cópia local C:\ESD\AgriSat em 13/10/2026. Capturas e logs são identificados separadamente; imagens de falha ou job skipped não comprovam deploy aprovado.

## Execução local atual

| Evidência textual | Resultado |
|---|---|
| resultados/34-maven-c-esd-20261013.txt | clean verify: 10 testes, 0 falhas/erros/ignorados, JAR, BUILD SUCCESS, 01:28 min |
| resultados/35-docker-build-c-esd-20261013.txt | Build direto sem OneDrive, 10 testes executados em Docker, Maven 02:39 min |
| resultados/36-compose-build-c-esd-20261013.txt | Compose build aprovado; camadas validadas reutilizadas |
| resultados/37-compose-up-c-esd-20261013.txt | Oracle preservado; API e Nginx atualizados; três serviços healthy |
| resultados/38-api-c-esd-20261013.txt | Health 200 UP, cinco GETs autenticados 200, /areas sem autenticação 401, rede/volume |
| resultados/39-oracle-c-esd-20261013.txt | Flyway V1 success=1; área id21 persistida desde teste anterior |
| resultados/40-flyway-c-esd-20261013.txt | Migração validada, schema atualizado e startup em Oracle |

Uma requisição JSON malformada à API atual também retornou HTTP 400, sem criação de registro. Não houve recriação do Oracle, remoção de volume ou novo teste de gravação. A prova anterior de persistência está em resultados/24-api-persistencia-20261008.txt e foi confirmada por consulta SQL atual.

## Capturas reais preservadas

Em entrega-final há dez capturas obtidas anteriormente em 13/10 e três históricas reaproveitadas. As primeiras mostram estrutura/configurações publicadas, pipeline do commit 325c180 e frontend estático, não a estrutura local em C:\ESD\AgriSat. Nenhuma foi fabricada ou convertida em prova de uma execução diferente.

| Arquivo em entrega-final | Origem e significado |
|---|---|
| 01-estrutura-projeto.png | Árvore publicada no GitHub; não Explorer no diretório novo |
| 03-dockerfile.png | Visualizador real do Dockerfile publicado |
| 06-docker-compose-configuracao.png | Configuração publicada anterior; name agrisat-ia foi acrescentado localmente |
| 12-github-actions-overview.png | Run 37753337819 / SHA325c180; CI aprovado, staging falhou |
| 13a-github-actions-build.png | BUILD success do mesmo run |
| 13-github-actions-build-test.png | TEST AND PACKAGE success do mesmo run, com sete testes na versão publicada |
| 14-github-actions-docker.png | DOCKER BUILD success do mesmo run |
| 15-github-actions-staging.png | Configure DEPLOY_HOST; evidência de bloqueio |
| 16-github-actions-production.png | Job skipped; não houve deploy |
| 19-frontend-agrisat.png | Tela inicial do protótipo estático em Vercel; não API em produção |
| 21/22/23-historico-actions-*.png | Cópias sem alteração de documentacao/prints; run37299189626 |

contexto-enviado-20261013.png é a captura original enviada pelo responsável. Mostra interfaces abertas no Chrome e Docker Desktop, mas não nomes/estados dos containers, Parent/Child, vínculo AB#2 ou servidor/deploy. Portanto é evidência contextual, não aprovação desses requisitos.

## Capturas ainda necessárias

A captura nativa Windows apresentou FrameArrived/window capture timed out após nova seleção e repetição. Não foram geradas telas artificiais. Para concluir:

1. Abra C:\ESD\AgriSat no Explorer/VS Code e capture src, frontend, workflows, Dockerfile, Compose e documentação.
2. No PowerShell dessa pasta execute .\mvnw.cmd clean verify e capture 10 testes, zero falhas e BUILD SUCCESS. Execute docker build -t agrisat-api:local . e docker images agrisat-api, capturando a conclusão.
3. Capture docker compose ps e Docker Desktop > Containers, sem restart/prune/down -v. Em Images capture agrisat-api:local e tamanho.
4. Abra http://localhost:8080/actuator/health no navegador do PC e capture UP. Execute ./scripts/validar-local.ps1 SEM -TestarPersistencia para capturar GETs, sem credenciais.
5. Capture os logs atuais de Oracle/Flyway com origem/data; não exponha .env, cabeçalho Authorization, config expandido ou inspect completo.
6. Em sessão autenticada do Azure Boards, abra Issue2 e capture Parent Epic1 e Development/Links com o commit AB#2. A abertura do resumo do projeto no screenshot enviado não basta.
7. Após implantação real e autorizada, capture staging/produção, URLs públicas e health/API. Não substituir esses prints por nomes de jobs no YAML.

Publicação de alterações e primeiro deploy de produção dependem de autorização. Não foram contratados recursos ou criados secrets nesta etapa.
