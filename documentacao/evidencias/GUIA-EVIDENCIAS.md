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
08 a 14: dependem de push autorizado pelas validações locais e configuração dos ambientes externos.

## Arquivos textuais existentes

- resultados/01-maven-test.txt: saída real da execução, incluindo tempo e resumo.
- resultados/02-maven-package.txt: saída real do package, incluindo JAR e testes.

Não substituem os prints exigidos na apresentação final.
