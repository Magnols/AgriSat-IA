# Guia de evidências reais - AgriSat IA

Todos os prints devem ser capturas reais, legíveis, com comando/URL e resultado. Salve em `documentacao/prints`. Não capture senhas, .env, private keys ou páginas de secrets. Os logs Maven existentes em `resultados` são evidências textuais e não screenshots.

| Nº | O que abrir / comando | O que precisa aparecer | Nome sugerido |
|---|---|---|---|
| 01 | PowerShell na raiz: .\\mvnw.cmd clean test | BUILD SUCCESS; 7 testes; zero falhas, erros e ignorados | 01-maven-test.png |
| 02 | PowerShell: .\\mvnw.cmd clean package | BUILD SUCCESS e JAR criado | 02-maven-package.png |
| 03 | PowerShell: docker build -t agrisat-api:local .; docker images agrisat-api | build concluído, tag e tamanho | 03-docker-build.png |
| 04 | PowerShell: docker compose build | build concluído sem erro | 04-compose-build.png |
| 05 | PowerShell: docker compose ps | oracle-db, api e frontend running/healthy | 05-compose-ps.png |
| 06 | Navegador: http://localhost:8081; GET autenticado /areas no terminal | frontend carregado e API HTTP 200. Dados frontend são simulados | 06-aplicacao.png |
| 07 | Navegador ou curl.exe --fail http://localhost:8080/actuator/health | HTTP 200 e status UP sem autenticação | 07-actuator-health.png |
| 08 | GitHub > Actions > CI/CD AgriSat IA > execução > BUILD | run/SHA e job verde; comando clean compile | 08-actions-build.png |
| 09 | Mesmo run > TEST AND PACKAGE | job verde e 7 testes sem falhas/erros/ignorados | 09-actions-test.png |
| 10 | Mesmo run > DOCKER BUILD | build verde e artifact release | 10-actions-docker.png |
| 11 | Mesmo run > STAGING > deploy | transferência/apply/health aprovados, environment staging | 11-deploy-staging.png |
| 12 | Navegador na URL staging/actuator/health e frontend real | URL pública real e UP; acesso funcional | 12-staging-funcionando.png |
| 13 | Mesmo run > PRODUCTION > deploy | staging anterior aprovado e deploy production verde | 13-deploy-production.png |
| 14 | Navegador na URL production/actuator/health e frontend real | URL pública real e UP; acesso funcional | 14-production-funcionando.png |

Se mudar portas, use as portas efetivas. Para cada imagem anote data, comando, SHA quando aplicável e resultado no README. Inclua o link real do run GitHub, não somente um recorte sem identificação.

## Oracle, Flyway e persistência

Evidências adicionais recomendadas:
- 15-oracle-flyway.png: log da API mostrando conexão Oracle e aplicação de V1 (ou schema já em versão 1 em reinício).
- 16-volume-network.png: docker volume ls e docker network ls, com recursos do projeto.
- 17-persistencia.png: mesma contagem de dados e histórico Flyway antes/depois de restart, sem remoção de volume.

Inspecione somente campos necessários do container. docker inspect completo pode revelar senhas. Use o procedimento de persistência do README.

## Estado atual

01/02: comandos aprovados, logs disponíveis; screenshots ainda não obtidos.  
03/04/05/06/07: comandos Docker, Compose, frontend HTTP 200 e health UP aprovados. Logs reais em resultados/05-docker-build.txt, 07-compose-build.txt, 08-compose-up.txt e 10-api-persistencia.txt. Screenshots ainda pendentes: captura nativa indisponível/timeout e navegador automatizado bloqueou localhost. Não foram criados prints falsos. Persistência aprovada após recriação do container Oracle, preservando volume e área id=2.  
08/09/10: screenshots reais salvos como 08-github-actions-build.png, 09-github-actions-tests.png e 10-github-actions-docker.png. Jobs aprovados na execução 37299189626, commit 0b5dfe2. Logs reais 15-actions-build.txt, 16-actions-test-package.txt e 17-actions-docker.txt.
11 a 14: pendentes. Staging falhou por DEPLOY_HOST ausente; production foi skipped. Log real em 18-actions-staging-failure.txt. Não confundir imagem de falha com evidência de deploy aprovado.

## Arquivos textuais existentes

- resultados/01-maven-test.txt: saída real da execução, incluindo tempo e resumo.
- resultados/02-maven-package.txt: saída real do package, incluindo JAR e testes.

Não substituem os prints exigidos na apresentação final.
# Atualização de evidências - 08/10/2026

Logs atuais 19-maven-verify, 20b-docker-build, 21b-compose-build, 22-compose-up, 23-actions-*, 24-api-persistencia, 26-oracle-migration, 27-api-final e 28-flyway-resumo estão em resultados/. São evidências textuais reais, não screenshots. Logs 20/21 registram tentativas malsucedidas; 25 inclui erros transitórios da recriação do Oracle, recuperados no log 27. Três prints CI históricos reais permanecem em ../prints.

Para repetir Docker neste OneDrive use `./scripts/build-docker-local.ps1` e `./scripts/build-docker-local.ps1 -Compose`; depois `docker compose up -d --no-build --wait --wait-timeout 900`. Nunca capture .env, Authorization, inspect completo ou config expandido.

Evidência adicional Azure Boards: abra https://dev.azure.com/RM565548/AgriSat-IA/_workitems/edit/2, faça login, confira Development/Links com commit 60db9e7 e Parent Epic #1; salve azure-boards-ab2.png nesta pasta. A associação ainda não foi confirmada pelo agente. Abra https://github.com/Magnols/AgriSat-IA/commit/60db9e7030dfc85833ae22c83f7242d30cf62507 para print commit-ab2.png sem dados sensíveis. Capturas locais e de deploy continuam pendentes; não produzir imagens ilustrativas para preenchê-las.

