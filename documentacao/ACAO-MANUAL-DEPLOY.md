# Operação dos ambientes remotos

Staging e produção ainda não estão implantados. Não há URLs públicas da API comprovadas. O último run consultado antes da publicação falhou em staging por DEPLOY_HOST ausente; produção não executou. A primeira implantação em produção requer autorização explícita.

## Servidor autorizado

Disponibilize Linux x86_64 com Docker Engine/Compose, disco persistente e memória suficiente para duas stacks Oracle/JVM. Um servidor institucional existente pode atender. Não criar VM, assinatura, cobrança ou serviços pagos sem autorização.

Prepare acesso SSH por chave, sem prompt interativo, e verifique a fingerprint por console ou canal confiável. Planeje o firewall para permitir apenas o acesso de deploy autorizado. Não exponha Oracle/1521 à internet. Use HTTPS para autenticação pública da API.

## Configuração de cada ambiente

Crie ~/agrisat/staging e ~/agrisat/production com acesso restrito. Em cada diretório crie .env com permissão 600, contendo DB_USER, DB_PASSWORD, API_SECURITY_USER e API_SECURITY_PASSWORD. Senhas fortes, distintas entre banco/API e entre os ambientes.

Portas de referência:

| Ambiente | API | Frontend | Oracle somente localhost |
|---|---|---|---|
| staging | 18080 | 18081 | 1522 |
| production | 8080 | 8081 | 1521 |

Configure SERVER_PORT, FRONTEND_PORT e DB_PORT nos .env correspondentes. O Oracle interno continua oracle-db:1521/FREEPDB1. Não fixe API_IMAGE no .env: o script recebe a imagem pelo SHA.

## GitHub Environments existentes

Abra https://github.com/Magnols/AgriSat-IA/settings/environments. Use staging e Production existentes, sem duplicá-los.

Em cada environment, configure quatro secrets:
- DEPLOY_HOST: IP/DNS do servidor, sem usuário ou porta.
- DEPLOY_USER: usuário SSH autorizado.
- DEPLOY_SSH_KEY: chave privada correspondente, inserida somente no GitHub.
- DEPLOY_KNOWN_HOSTS: host key cuja fingerprint foi verificada.

Em Environment variables, configure DEPLOY_URL: URL pública da API, sem barra final. Senhas Oracle/API permanecem nos .env protegidos do servidor. Não enviar chave ou senha em chat/documentação.

Antes de habilitar produção, obtenha autorização explícita para o primeiro deploy e configure aprovação/proteção adequada. Se a aprovação não estiver disponível, mantenha produção bloqueada até uma alternativa autorizada. Restrinja deploy a main.

## Aplicação e validação da release

O pipeline gera release.tar.gz com imagem, Compose, frontend e scripts, transporta por SSH para ~/agrisat/AMBIENTE/releases/SHA e executa scripts/deploy.sh. O script usa projetos agrisat-staging/production, preserva volumes, aguarda health e consulta /areas autenticado sem imprimir credenciais.

Abra Actions > CI/CD AgriSat IA > Run workflow > main somente quando a configuração estiver pronta. Confirme build, testes, Docker e staging; produção depende desse sucesso e da autorização. Não trocar essas etapas por echo.

Após cada deploy, confirme Compose healthy, health público UP, API autenticada HTTP200 e frontend acessível. Registre as URLs e capture resultados reais com run/SHA. Não declarar sucesso apenas porque o environment existe ou o YAML contém seu nome.

## Azure Boards

Abra https://dev.azure.com/RM565548/AgriSat-IA/_workitems/edit/2 em sessão autenticada. Confirme Parent Epic1 e Development/Links com o commit AB#2 publicado. Capture essas relações, sem dados sensíveis. A sessão aberta no Chrome não é automaticamente compartilhada com outra ferramenta.

## Comunicação segura

Para continuar, informe apenas que a configuração está pronta e as URLs públicas. Nunca envie passwords, PATs ou chaves privadas. Publicações e operações de produção só serão feitas dentro das autorizações fornecidas.
