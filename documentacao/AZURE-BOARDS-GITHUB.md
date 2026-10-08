# Rastreabilidade Azure Boards e GitHub

## Projeto e itens informados pelo aluno

- Organização: https://dev.azure.com/RM565548
- Projeto: https://dev.azure.com/RM565548/AgriSat-IA
- Epic #1: Desenvolvimento da Plataforma AgriSat IA.
- Issue #2: Implementar backend e API do AgriSat IA, vinculado ao Epic #1.
- Repositório: https://github.com/Magnols/AgriSat-IA, branch main.

A conexão Azure Boards/GitHub foi informada como autorizada pelo aluno. Esta auditoria não recriou nem alterou Work Items. A consulta ao Work Item em 08/10/2026 exigiu login, portanto a relação remota ainda deve ser confirmada no Azure Boards.

## Como rastrear uma alteração real

Este documento e a seção correspondente do README explicam a ligação entre requisitos, código e evidências. O commit de documentação utiliza AB#2 para associar a alteração útil ao Issue #2. A existência da referência no GitHub não basta para afirmar que o Azure Boards a processou.

1. No GitHub, abra o commit cuja mensagem contém AB#2 e confirme o hash e arquivos alterados.
2. Abra https://dev.azure.com/RM565548/AgriSat-IA/_workitems/edit/2 com sua conta autorizada.
3. Abra a seção Development/Desenvolvimento ou Links do Issue #2.
4. Confirme que aparece a relação com o commit GitHub recém-publicado. Verifique que o pai continua Epic #1.
5. Capture a tela real com título/id do Work Item e hash/link do commit, omitindo dados privados. Salve em documentacao/evidencias/azure-boards-ab2.png.
6. Se não aparecer, confira Project settings > GitHub connections: repositório Magnols/AgriSat-IA conectado ao projeto correto. Não crie outro Epic/Issue e não simule o vínculo por documentação.

## Relação com a entrega DevOps

Azure Boards registra planejamento e rastreabilidade. GitHub armazena o código e executa CI/CD. Cada evidência do pipeline deve indicar seu SHA e run. Uma integração Boards ativa não substitui testes, health ou deploy real de staging/produção.

## Segurança e produção

Não compartilhar PAT, chave privada ou senha no chat/README. Secrets são configurados nos GitHub Environments; credenciais Oracle/API permanecem nos .env protegidos. O primeiro deploy em produção exige aprovação humana explícita. Antes de habilitá-lo, configure revisão obrigatória no environment Production quando disponível, ou aguarde orientação para um bloqueio equivalente. Não disparar deploy de produção sem essa aprovação.
