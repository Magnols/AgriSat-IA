# AÇÃO MANUAL NECESSÁRIA - STAGING/PRODUCTION

## Plataforma proposta

Azure for Students, se houver elegibilidade: https://azure.microsoft.com/free/students/  
Portal: https://portal.azure.com/

A Microsoft anuncia crédito estudantil sem cartão. Use apenas a assinatura Azure for Students com limite de gastos ativo. Se aparecer solicitação de cartão, oferta paga, upgrade ou assinatura Pay-As-You-Go, interrompa. Não há garantia de elegibilidade nem de disponibilidade de VM. Uma VM Linux x86_64 já disponibilizada pela FIAP também atende à mesma estratégia SSH.

Não foi criada conta, assinatura, VM ou cobrança. Não foram criados GitHub Environments/secrets.

## 1. Ativar acesso e provisionar infraestrutura

1. Abra Azure for Students e clique em Start free / Começar gratuitamente.
2. Faça login com sua conta Microsoft e conclua a verificação acadêmica com o e-mail FIAP.
3. Confirme que a assinatura chama Azure for Students, mostra saldo de crédito e mantém o limite de gastos. Não faça upgrade.
4. No portal, abra Virtual machines > Create > Azure virtual machine.
5. Escolha essa assinatura, grupo de recursos agrisat-academico, Ubuntu Server 24.04 LTS e arquitetura x64. Não escolha ARM sem validar a imagem Oracle.
6. Crie duas VMs: agrisat-staging e agrisat-production. Escolha tamanho com memória suficiente para Oracle + JVM + Nginx (recomendado 8 GiB por VM) apenas se os créditos cobrirem a estimativa exibida. VMs/discos consomem créditos; não são recursos permanentemente gratuitos.
7. Use autenticação SSH por chave, usuário agrisat e gere chaves distintas. Salve localmente a chave privada. Não a envie neste chat.
8. Deixe SSH restrito ao seu IP para provisionar. Planeje depois acesso dos runners GitHub com regras revisadas: seus IPs são dinâmicos. Não abra 1521 para internet.
9. Em Networking, permita acesso público de demonstração à porta 8080 (health) e 8081 (frontend), ou configure HTTPS/reverse proxy em 443. Não use Basic Auth em HTTP público.
10. Clique Review + create somente depois de revisar o consumo de créditos e ausência de cobrança automática.
11. Registre o IP/DNS público e usuário SSH de cada VM. Configure desligamento automático e acompanhe créditos. Discos podem consumir créditos mesmo com VM desligada.

## 2. Preparar cada servidor

1. Em sua VM, abra Connect > Native SSH. Conecte pelo terminal seguindo o comando exibido.
2. Instale Docker Engine e Compose usando https://docs.docker.com/engine/install/ubuntu/ e confira docker info e docker compose version. O usuário de deploy deve ter permissão de executar Docker sem prompt interativo.
3. Crie o diretório correspondente:
   - staging: mkdir -p ~/agrisat/staging && chmod 700 ~/agrisat/staging
   - production: mkdir -p ~/agrisat/production && chmod 700 ~/agrisat/production
4. Crie ~/agrisat/staging/.env ou ~/agrisat/production/.env com editor no servidor:
   SERVER_PORT=8080
   FRONTEND_PORT=8081
   DB_PORT=1521
   DB_USER=agrisat_user
   DB_PASSWORD=<senha forte exclusiva do banco desse ambiente>
   API_SECURITY_USER=admin
   API_SECURITY_PASSWORD=<outra senha forte exclusiva>
5. Execute chmod 600 no arquivo .env. Não inclua API_IMAGE no arquivo: o script recebe a imagem pelo SHA.
6. Valide a chave pública em ~/.ssh/authorized_keys e registre a host key SSH. Compare o fingerprint pela console confiável da VM antes de aceitar a saída de ssh-keyscan.
7. Teste acesso SSH por chave e execução docker info. O deploy falhará caso senha/elevação sejam solicitadas.
8. Defina URL pública da API sem barra final, com a porta efetiva. URL ainda não será considerada funcionando antes do deploy.

## 3. GitHub Environments e configuração

1. Abra https://github.com/Magnols/AgriSat-IA/settings/environments com login proprietário.
2. Clique New environment, informe staging e salve.
3. Em Environment secrets > Add environment secret, crie os quatro secrets abaixo.
4. Em Environment variables > Add environment variable, crie DEPLOY_URL.
5. Repita para production com valores desse segundo servidor.
6. Configure restrição de deploy para main. Se usar aprovação obrigatória para production, documente a aprovação: o job ficará aguardando aprovação antes do deploy automatizado.

| Nome | Tipo | Valor a obter |
|---|---|---|
| DEPLOY_HOST | Secret de cada Environment | IP/DNS do servidor, sem usuário/porta |
| DEPLOY_USER | Secret de cada Environment | usuário SSH, ex. agrisat |
| DEPLOY_SSH_KEY | Secret de cada Environment | chave privada completa do deploy, sem passphrase interativa |
| DEPLOY_KNOWN_HOSTS | Secret de cada Environment | linha known_hosts cuja fingerprint foi verificada pela console |
| DEPLOY_URL | Variável de cada Environment | URL pública da API sem barra final |

Os mesmos nomes são usados nos dois Environments, com valores independentes. Os antigos STAGING_HOST/STAGING_USER/STAGING_SSH_KEY e PRODUCTION_* foram substituídos pela configuração por Environment. As senhas Oracle/API ficam no .env de cada servidor e não precisam ser GitHub Secrets nesta estratégia.

## 4. Continuar a validação

1. Volte ao Codex informando somente que os Environments foram configurados e as URLs públicas, nunca chaves ou senhas.
2. Depois que Docker local passar, o código pode receber commit/push e iniciar o workflow.
3. Abra https://github.com/Magnols/AgriSat-IA/actions.
4. Aguarde BUILD, TEST AND PACKAGE e DOCKER BUILD.
5. STAGING deve aplicar Compose e aprovar health público. PRODUCTION depende desse sucesso.
6. Capture prints reais seguindo o guia de evidências.
7. Só depois acrescente URLs comprovadas ao README/PDF e gere o ZIP final.

## Docker neste PC

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
