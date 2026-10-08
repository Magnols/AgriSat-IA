# AÇÃO MANUAL NECESSÁRIA - STAGING/PRODUCTION

## Revalidação em 08/10/2026

Run 37751748406 confirmou novamente CI aprovado e staging bloqueado por DEPLOY_HOST. Ambos os environments existentes continuam sem secrets/variables. Nenhum servidor autorizado foi identificado. **O primeiro deploy de produção requer autorização humana explícita**, antes de habilitar secrets/execução, com proteção/aprovação adequada. Não preencha produção e execute o workflow automaticamente sem essa autorização. Não criar serviços pagos.

## Diagnóstico real em 05/10/2026

CI BUILD/TEST/DOCKER passou. Staging falhou com Configure DEPLOY_HOST no GitHub Environment. staging e Production existem, mas ambos sem secrets/variables. Autenticação GitHub operacional. Não criar environments duplicados. As orientações abaixo substituem a sugestão anterior de criar duas VMs obrigatoriamente.

## Infraestrutura mínima do workflow e alternativas

O workflow requer SSH TCP 22, Linux x86_64 com Docker Engine/Compose, disco persistente, recursos para Oracle/JVM, acesso de saída ao Docker Hub e uma URL pública por ambiente. Não exige serviços gerenciados Java nem duas VMs.

| Alternativa | Compatibilidade/decisão |
|---|---|
| Servidor Linux FIAP já disponível | Preferido se houver acesso SSH/Docker e memória suficiente, sem criar cobrança |
| Uma VM Azure for Students, duas stacks Compose | Recomendada se elegível, com créditos/limite ativo, sem cartão. Configuração abaixo |
| Duas VMs | Isolamento físico melhor, mas não obrigatório para esta demonstração e consome mais recursos |
| Oracle Cloud Free Tier comum | Não iniciar cadastro que exija cartão. Eventual acesso Oracle Academy depende de elegibilidade |
| PaaS web sem Docker Compose/SSH | Não atende ao workflow e ao Oracle container sem redesign; não selecionar arbitrariamente |

Uma VM com dois projetos Compose comprova dois ambientes lógicos independentes, não duas infraestruturas físicas. Confirmar eventual exigência adicional do professor. Cada ambiente terá Oracle, usuário/senhas, volume, rede e release próprios.

## Plataforma proposta

Azure for Students, se houver elegibilidade: https://azure.microsoft.com/free/students/  
Portal: https://portal.azure.com/

A Microsoft anuncia crédito estudantil sem cartão. Use apenas a assinatura Azure for Students com limite de gastos ativo. Se aparecer solicitação de cartão, oferta paga, upgrade ou assinatura Pay-As-You-Go, interrompa. Não há garantia de elegibilidade nem de disponibilidade de VM. Uma VM Linux x86_64 já disponibilizada pela FIAP também atende à mesma estratégia SSH.

Não foi criada conta, assinatura, VM ou cobrança nesta sessão. Os GitHub Environments existentes foram apenas inspecionados; nenhum secret foi criado.

## 1. Ativar acesso e provisionar infraestrutura

1. Abra Azure for Students e clique em Start free / Começar gratuitamente.
2. Faça login com sua conta Microsoft e conclua a verificação acadêmica com o e-mail FIAP.
3. Confirme que a assinatura chama Azure for Students, mostra saldo de crédito e mantém o limite de gastos. Não faça upgrade.
4. No portal, abra Virtual machines > Create > Azure virtual machine.
5. Escolha essa assinatura, grupo de recursos agrisat-academico, Ubuntu Server 24.04 LTS e arquitetura x64. Não escolha ARM sem validar a imagem Oracle.
6. Prepare UMA VM agrisat-devops-academico para as duas stacks. Recomendo 4 vCPU/16 GiB para margem de memória de dois Oracle + JVMs; confirme quota e estimativa cobertas pelos créditos antes de criar. Região sugerida Brazil South para acesso do Brasil, somente se disponível e compatível com seus créditos/quota. Disco do sistema mínimo 64 GiB. Não criar se pedir upgrade/cartão/pagamento; VMs/discos consomem créditos, não são permanentemente gratuitos. Não provisionamos nada automaticamente.
7. Em Administrator account, selecione SSH public key, usuário agrisat, Generate new key pair, tipo Ed25519 se oferecido, nome agrisat-deploy. Salve a chave privada localmente e restrinja acesso. Não a envie no chat. Pode usar chaves distintas por ambiente; a chave pública deve estar no authorized_keys do usuário correspondente.
8. Deixe SSH restrito ao seu IP para provisionar. Planeje depois acesso dos runners GitHub com regras revisadas: seus IPs são dinâmicos. Não abra 1521 para internet.
9. Em Networking/NSG, planeje portas TCP 18080/18081 (API/frontend staging) e 8080/8081 (production) para demonstração pública de health/frontend, ou HTTPS/reverse proxy 443. Não exponha 1521 nem 1522. SSH 22 deve admitir o runner autorizado no momento do deploy; não abrir para toda internet por conveniência. Para runner GitHub hospedado, seu IP é dinâmico: a regra precisa ser preparada/revisada antes do deploy; uma regra só para seu IP não basta. Não transmitir Basic Auth em HTTP público.
10. Clique Review + create somente depois de revisar o consumo de créditos e ausência de cobrança automática.
11. Copie IP/DNS público e usuário agrisat. Configure desligamento automático e acompanhe créditos. Discos podem consumir créditos mesmo com VM desligada. Não desligar durante validação/evidências.

## 2. Preparar o servidor e os dois ambientes

1. Em sua VM, abra Connect > Native SSH. Conecte pelo terminal seguindo o comando exibido.
2. Instale Docker Engine e Compose usando https://docs.docker.com/engine/install/ubuntu/ e confira docker info e docker compose version. O usuário de deploy deve ter permissão de executar Docker sem prompt interativo.
3. Crie o diretório correspondente:
   - staging: mkdir -p ~/agrisat/staging && chmod 700 ~/agrisat/staging
   - production: mkdir -p ~/agrisat/production && chmod 700 ~/agrisat/production
4. Crie os DOIS arquivos ~/agrisat/staging/.env e ~/agrisat/production/.env com editor no servidor. Valores comuns, com senhas diferentes por ambiente:
   DB_USER=agrisat_user
   DB_PASSWORD=<senha forte exclusiva do banco desse ambiente>
   API_SECURITY_USER=admin
   API_SECURITY_PASSWORD=<outra senha forte exclusiva>

   No staging: SERVER_PORT=18080, FRONTEND_PORT=18081, DB_PORT=1522.
   No production: SERVER_PORT=8080, FRONTEND_PORT=8081, DB_PORT=1521.
   Cada chave/valor ocupa uma linha; não copie placeholders como senha. O service name interno continua FREEPDB1 e a porta Oracle interna continua 1521 nos dois projetos.
5. Execute chmod 600 no arquivo .env. Não inclua API_IMAGE no arquivo: o script recebe a imagem pelo SHA.
6. Valide a chave pública em ~/.ssh/authorized_keys e registre a host key SSH. Compare o fingerprint pela console confiável da VM antes de aceitar a saída de ssh-keyscan.
7. Teste acesso SSH por chave e execução docker info. O deploy falhará caso senha/elevação sejam solicitadas.
8. Defina URL pública da API sem barra final: staging http://IP:18080, production http://IP:8080 (ou URLs HTTPS após proxy real). IP é placeholder. Não declarar URL funcional antes do deploy.

## 3. GitHub Environments e configuração

1. Abra https://github.com/Magnols/AgriSat-IA/settings/environments com login proprietário.
2. Clique no environment staging existente, não crie outro.
3. Em Environment secrets > Add environment secret, crie os quatro secrets abaixo.
4. Em Environment variables > Add environment variable, crie DEPLOY_URL.
5. Repita no environment Production existente. O nome é case-insensitive no GitHub e corresponde ao input production do workflow. Não renomear/recriar por causa da capitalização.
6. Configure restrição de deploy para main. Se usar aprovação obrigatória para production, documente a aprovação: o job ficará aguardando aprovação antes do deploy automatizado.

| Nome | Tipo | Valor a obter |
|---|---|---|
| DEPLOY_HOST | Secret de cada Environment | IP/DNS do servidor, sem usuário/porta |
| DEPLOY_USER | Secret de cada Environment | usuário SSH, ex. agrisat |
| DEPLOY_SSH_KEY | Secret de cada Environment | chave privada completa do deploy, sem passphrase interativa |
| DEPLOY_KNOWN_HOSTS | Secret de cada Environment | linha known_hosts cuja fingerprint foi verificada pela console |
| DEPLOY_URL | Variável de cada Environment | URL pública da API sem barra final |

Os mesmos nomes são usados nos dois Environments. Numa VM compartilhada, DEPLOY_HOST/DEPLOY_USER/DEPLOY_KNOWN_HOSTS podem coincidir; DEPLOY_URL e .env devem isolar ambientes. As senhas Oracle/API ficam nos .env do servidor, não no GitHub nem no chat.

## Mapa exato da release

ci-cd.yml gera release.tar.gz contendo image.tar.gz, docker-compose.yml, frontend e scripts. deploy.yml copia por scp para ~/agrisat/AMBIENTE/releases/SHA, extrai e executa bash scripts/deploy.sh AMBIENTE SHA. O script lê ~/agrisat/AMBIENTE/.env, carrega a imagem agrisat-api:SHA e executa Compose --project-name agrisat-AMBIENTE --env-file ... up -d --no-build --wait. Valida /areas dentro do container e health público pela DEPLOY_URL. Staging deve passar antes de production. Oracle, rede e volume ficam separados pelo nome do projeto. O frontend é montagem somente leitura da pasta da release.

Chave: se a VM foi criada com par SSH, use a chave privada correspondente no Environment secret (não no chat) e valide login remoto. Fingerprint: consulte console confiável e compare antes de salvar DEPLOY_KNOWN_HOSTS. O workflow usa StrictHostKeyChecking e SSH não interativo.

Fontes verificadas em 05/10: [Azure for Students](https://learn.microsoft.com/en-us/azure/education-hub/find-ids), [limite de gastos](https://learn.microsoft.com/en-us/azure/cost-management-billing/manage/spending-limit), [nomes de environments GitHub](https://docs.github.com/en/rest/deployments/environments), [Oracle Free Tier](https://www.oracle.com/bz/cloud/free/faq/).

## 4. Continuar a validação

1. Volte ao Codex informando somente que os Environments foram configurados e as URLs públicas, nunca chaves ou senhas.
2. Depois que Docker local passar, o código pode receber commit/push e iniciar o workflow.
3. Abra https://github.com/Magnols/AgriSat-IA/actions.
4. Aguarde BUILD, TEST AND PACKAGE e DOCKER BUILD.
5. STAGING deve aplicar Compose e aprovar health público. PRODUCTION depende desse sucesso.
6. Capture prints reais seguindo o guia de evidências.
7. Só depois acrescente URLs comprovadas ao README/PDF e gere o ZIP final.

## Docker neste PC — diagnóstico histórico, já resolvido

A atualização WSL foi executada. O primeiro diagnóstico retornou kernel ausente e VirtualizationFirmwareEnabled=False. Se Docker continuar sem iniciar:
1. Salve seus trabalhos.
2. Abra Gerenciador de Tarefas > Desempenho > CPU e confira Virtualização.
3. Se estiver Desabilitada, reinicie na BIOS/UEFI e habilite Intel VT-x/Virtualization Technology ou AMD SVM. O nome/tecla depende do fabricante.
4. No Windows, confirme em Ativar ou desativar recursos do Windows: Plataforma de Máquina Virtual e Subsistema do Windows para Linux.
5. Reinicie quando solicitado. Não reiniciamos automaticamente este computador.
6. Abra Docker Desktop e aguarde Engine running.
7. Execute docker info. Deve mostrar seção Server sem erro.
8. Volte ao Codex para validar build, Compose, Oracle, Flyway e persistência.

Fontes: https://azure.microsoft.com/free/students/ e https://docs.docker.com/desktop/setup/install/windows-install/.
