# RELATÓRIO FINAL DA ATIVIDADE DEVOPS - AGRISAT IA

Auditoria de 08/10/2026. Esta versão prevalece sobre relatórios históricos, que foram preservados. Nenhum deploy foi simulado. O nome "Final" do ZIP não significa conclusão dos requisitos externos.

## A. O que já estava pronto

Backend Java 21 / Spring Boot 3.5.14, Maven Wrapper 3.9.15, Oracle/Flyway, H2 exclusivo para testes, frontend HTML/CSS/JS, Dockerfile multi-stage e Compose com Nginx/API/Oracle, testes, workflows e documentação. Os resultados históricos foram revalidados, não presumidos. Frontend demonstrativo ainda não integrado funcionalmente à API.

## B. O que foi realizado nesta execução

Inspeção da raiz oficial, Git, fontes, Docker, workflows e documentação; testes/build; inicialização segura do Docker Desktop; confirmação WSL2 operacional; builds Docker/Compose; health e rotas; consulta Flyway e teste de persistência. Correção ambiental por contexto temporário seletivo no script scripts/build-docker-local.ps1, sem alterar Dockerfile, fontes de negócio ou .env. Documentação AB#2 útil, commit real e push seguro; consulta autenticada do GitHub Actions e environments. Novo PDF de 14 páginas, original preservado; pacote ZIP com pendências explícitas e validação separada.

## C. Resultados dos testes

`./mvnw.cmd -B -ntp clean verify`: BUILD SUCCESS; 7 testes, 7 aprovados, 0 falhas, 0 erros, 0 ignorados; tempo Maven 01:04 min. AreaServiceTest 2; EquipamentoServiceTest 2; EnergiaEsgApiApplicationTests 3. Sem skipTests. JAR target/energia-esg-api-0.0.1-SNAPSHOT.jar, 92.311.659 bytes. Evidência 19-maven-verify-20261008.txt.

Docker build direto falhou com invalid file request Dockerfile (reparse points OneDrive). Build dos mesmos fontes em contexto temporário passou; camadas Maven reutilizadas do cache, não testes novos nessa camada local. Compose build passou com override temporário do contexto, sem modificar o Compose oficial. Imagem agrisat-api:local: conteúdo 157 MB / disco 461 MB. Logs 20/20b/21/21b preservam tentativas e sucesso.

Compose up --no-build --wait: API, Oracle e frontend healthy. GET /actuator/health sem autenticação: 200 UP. GET /areas, /equipamentos, /alertas, /leituras e /relatorios/consumo-diario autenticados: 200; /areas sem autenticação: 401. Frontend localhost:8081: HTTP 200.

Oracle 23.26.3 / FREEPDB1: consulta flyway_schema_history confirmou versão 1, descrição create tables, success=1. Flyway validou uma migração e schema atualizado; V1 já aplicada, não executada novamente. Aviso de versão Oracle mais nova que suporte testado do Flyway registrado, sem falha de validação.

Volume agrisat-ia_agrisat-oracle-data (local), rede agrisat-ia_agrisat-network (bridge), 3 containers. Persistência: área/equipamento id 21, marcador EVIDENCIA-PERSISTENCIA-20261008-054005 e NUMERIC 123.45 consultados depois de recriar somente o container Oracle. Volume preservado; registros de evidência permanecem no banco local. Interrupção transitória durante recriação gerou ORA-12514 nos logs; validação final repetida passou. Logs 24 a 28 documentam o estado real.

## D. Estado do Git e GitHub

Raiz oficial: C:\Users\Usuário\OneDrive\Documents\FIAP - ON\AgríSat-IA\AgriSat-IA. Branch main; remote https://github.com/Magnols/AgriSat-IA.git. Estado inicial limpo, HEAD 3f36f02 com um commit local não enviado. Nenhum AB#2 anterior encontrado. Publicados com push fast-forward: 3f36f02 e 60db9e7030dfc85833ae22c83f7242d30cf62507. Commit AB#2 confirmado no remote/GitHub. Alterações desta auditoria são agrupadas em commit posterior, quando revisão e varredura terminarem; consulte git log/status para seu hash. Nenhum reset, clean, force push ou migração.

## E. Estado da integração Azure Boards

Organização RM565548/projeto AgriSat-IA; Epic #1 e Issue #2 informados pelo usuário, não recriados. Commit 60db9e7 contém AB#2 e alteração útil. API Azure Boards exigiu autenticação, portanto vínculo em Development/Links e relação do Issue com Epic continuam por confirmar. Guia AZURE-BOARDS-GITHUB.md inclui verificação manual.

## F. Estado do pipeline CI/CD

Execução atual comprovada: https://github.com/Magnols/AgriSat-IA/actions/runs/37751748406, commit 60db9e7.

| Job | Resultado real |
|---|---|
| BUILD (113226395492) | PASS |
| TEST AND PACKAGE (113226503446) | PASS: 7 testes, nenhum ignorado; Maven 18.885 s |
| DOCKER BUILD (113226734697) | PASS |
| STAGING / deploy (113227189746) | FAIL: Configure DEPLOY_HOST |
| PRODUCTION (113227276616) | SKIPPED |

Logs 23-actions-*.txt baixados da execução real. Workflow ponta a ponta NÃO aprovado. Checkout/setup-java/cache, build, verify, Docker build e artefato de release funcionaram. Deploy usa SSH verificado, Compose, health público e consulta API autenticada. Production depende de staging. Aviso de depreciação Node20 nas actions artifact v4 registrado, não é causa da falha.

## G. Staging e produção

Environments staging e Production existem, ambos com zero secrets e zero variables na consulta atual. Não há servidor autorizado identificável nas configurações disponíveis; Azure CLI não instalado. Sem URL_STAGING/URL_PRODUCTION, sem health público validado. Nenhuma VM, assinatura, conta, cobrança ou deploy criado. Primeiro deploy de produção exige autorização humana antes de habilitar execução.

## H. Arquivos gerados ou atualizados

README.md; scripts/build-docker-local.ps1; documentacao/AZURE-BOARDS-GITHUB.md; este relatório; notas atuais nos relatórios anteriores; guia de evidências e de deploy; novo PDF; logs 19 a 28; VALIDACAO-PACOTE-20261008.md. Três screenshots reais históricos 08/09/10 preservados em documentacao/prints. Não foram obtidas novas capturas locais/Azure/deploy; logs não são prints.

## I. ZIP e PDF

PDF novo: documentacao/apresentacao/AgriSat-IA-DevOps-20261008.pdf, 14 páginas com evidências reais e bloqueios explícitos; original preservado. Renderização e revisão visual realizadas. ZIP: ../entrega/AgriSat-IA-DevOps-FIAP-Entrega-Final.zip; validação de membros, integridade e exclusões em VALIDACAO-PACOTE-20261008.md. Pacote autorizado com pendências, não entrega integralmente concluída.

## J. Ações manuais restantes

1. Abra https://dev.azure.com/RM565548/AgriSat-IA/_workitems/edit/2 e faça login. Em Development/Links confirme commit 60db9e7 e Parent Epic #1. Capture documentacao/evidencias/azure-boards-ab2.png. Se não houver vínculo, verifique Project settings > GitHub connections, projeto e repositório corretos, conforme AZURE-BOARDS-GITHUB.md.
2. Providencie servidor Linux x86_64 autorizado com Docker/Compose e recursos para duas stacks Oracle/JVM. Prefira servidor FIAP existente. Se escolher Azure estudantil, siga ACAO-MANUAL-DEPLOY.md; interrompa ao pedir cartão/upgrade ou criar cobrança. Não envie chave ou senha no chat.
3. Prepare ~/agrisat/staging/.env e ~/agrisat/production/.env com chmod 600, portas/credenciais distintas conforme guia. Oracle apenas localhost. Confirme fingerprint SSH por canal confiável e obtenha URLs públicas HTTPS.
4. Abra https://github.com/Magnols/AgriSat-IA/settings/environments > staging. Em Environment secrets crie DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY, DEPLOY_KNOWN_HOSTS. Em Environment variables crie DEPLOY_URL. Não crie duplicados.
5. Antes de configurar Production para poder executar, autorize explicitamente o PRIMEIRO deploy de produção e estabeleça proteção/aprovação apropriada. Depois configure seus quatro secrets e DEPLOY_URL distintos. Se aprovação não estiver disponível, mantenha produção bloqueada até autorização explícita.
6. Em Actions > CI/CD AgriSat IA > Run workflow > main, execute. Confirme build/test/Docker, staging e health/API públicos; só depois produção autorizada. Informe apenas URLs públicas e que configuração está pronta.
7. Capture resultados reais locais seguindo GUIA-EVIDENCIAS.md, sem .env, cabeçalhos Authorization ou configuração expandida. Complete screenshots Azure e staging/produção. Depois atualize PDF/checklist e regenere ZIP para entrega integral.

## K. Auditoria dos requisitos

| Requisito | Status | Evidência | Pendência |
|---|---|---|---|
| Código-fonte | CONCLUÍDO E VALIDADO | Maven + fontes preservados | Nenhuma local |
| Dockerfile | CONCLUÍDO E VALIDADO | 20b / CI Docker | Build direto OneDrive usa contorno documentado |
| Docker Compose | CONCLUÍDO E VALIDADO | 21b / 22 / 27 | Nenhuma local |
| Volumes e redes | CONCLUÍDO E VALIDADO | 24 / 27 | Nenhuma local |
| Variáveis de ambiente | CONCLUÍDO E VALIDADO | Compose + .env.example + execução | Configuração remota pendente |
| Build automatizado | CONCLUÍDO E VALIDADO | CI BUILD / 19 | Nenhuma |
| Testes automatizados | CONCLUÍDO E VALIDADO | 7 locais + 7 CI | Nenhuma |
| Docker build no CI | CONCLUÍDO E VALIDADO | run 37751748406 | Nenhuma |
| Deploy staging | BLOQUEADO POR AÇÃO EXTERNA | Job falhou por DEPLOY_HOST | Servidor, secrets e URL |
| Deploy produção | BLOQUEADO POR AÇÃO EXTERNA | Job skipped | Staging, servidor, secrets, URL e autorização |
| Integração Azure Boards | CONCLUÍDO, MAS NÃO VALIDADO | AB#2 publicado; integração informada | Confirmar vínculo autenticado |
| Integração GitHub | CONCLUÍDO E VALIDADO | Push/remote/CI | Nenhuma |
| README | CONCLUÍDO E VALIDADO | Instruções + prints CI reais + pendências | Prints deploy quando houver |
| PDF técnico | CONCLUÍDO E VALIDADO | 14 páginas renderizadas | Incorporar deploys futuramente |
| Evidências | PENDENTE | Logs reais e 3 prints históricos | Capturas locais/Azure/deploy |
| ZIP final | CONCLUÍDO E VALIDADO | VALIDACAO-PACOTE-20261008.md | Regenerar após deploys para entrega completa |

## Checklist do professor

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☑ |
| Dockerfile funcional | ☑ |
| docker-compose.yml ou arquivos Kubernetes | ☑ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☑ |
| Documentação técnica com evidências (PDF ou PPT) | ☑ |
| Deploy realizado nos ambientes staging e produção | ☐ |

Segurança: varredura de candidatos Git e de conteúdo textual/PDF do ZIP, sem expor valores. Heurística, não garantia absoluta. .env, target e .git excluídos. Atividade integral permanece BLOQUEADA POR AÇÃO EXTERNA; a parte local foi concluída e validada.
