# Validação técnica AgriSat IA

Data: 13/10/2026. Diretório definitivo de trabalho: C:\ESD\AgriSat. Original OneDrive preservado, sem exclusão. A migração copiou 328 arquivos, incluindo Git/ocultos e alterações locais anteriores, excluindo target, caches, node_modules e IDEs. HEAD preservado 325c1806ec422c37a7ea95258822a63bb9c89848; remote original Magnols/AgriSat-IA. Dockerfile e pom.xml agora são arquivos locais Archive, não reparse points.

## Alterações e reorganização

README reescrito nas 13 seções técnicas do produto; frontend/README corrigido para explicitar simulação e preservar aviso legal. Oracle padrão alinhado a FREEPDB1, ddl-auto=validate e open-in-view=false. JSON malformado/parâmetros incompatíveis retornam 400, sem mensagens internas, com três testes de regressão. Compose fixa name agrisat-ia para reutilizar o volume local; --project-name nos scripts de deploy continua isolando staging/produção.

Relatórios antigos, PDFs, conteúdo de apresentação, guia anterior e o contorno temporário OneDrive foram movidos de forma recuperável para documentacao/historico. Nada foi apagado do original. Essa pasta e .build-documentacao são ignoradas; não entram na entrega atual. A cópia estática do frontend na raiz foi mantida por compatibilidade com a hospedagem anterior.

## Resultados locais

| Requisito | Status | Comprovação |
|---|---|---|
| Migração e histórico Git | CONCLUÍDO | HEAD/remote iguais, original preservado, arquivos locais |
| Compilação e JAR | CONCLUÍDO | clean verify BUILD SUCCESS, target/energia-esg-api-0.0.1-SNAPSHOT.jar |
| Testes | CONCLUÍDO | 10 testes; 0 falhas, 0 erros, 0 ignorados; 01:28 min Maven local |
| Dockerfile/build direto | CONCLUÍDO | 10 testes ativos dentro da imagem; Maven 02:39 min; erro OneDrive resolvido |
| Compose e serviços | CONCLUÍDO | Três serviços healthy; API/frontend atualizados, Oracle não recriado |
| Volume e rede | CONCLUÍDO | agrisat-ia_agrisat-oracle-data preservado; bridge com três containers |
| Oracle/Flyway | CONCLUÍDO | FREEPDB1, V1 success=1 e schema atualizado |
| API health/autenticação | CONCLUÍDO | Health200UP, cinco GET200, sem auth401 e JSON malformado400 |
| Persistência | CONCLUÍDO | Prova histórica preservada; área id21 confirmada por SQL atual, sem nova gravação |
| Frontend local | CONCLUÍDO | HTTP200 no Nginx; protótipo explicitamente demonstrativo |
| Capturas locais no novo diretório | PENDENTE | Timeout da captura nativa após recuperação; procedimentos em evidencias/INDICE.md |
| Azure Boards AB#2 | BLOQUEADO | Sessão Chrome não compartilhada; vínculo Issue2/ParentEpic1 não confirmado |
| Deploy staging | BLOQUEADO | Servidor/secrets/URL não disponíveis |
| Deploy produção | BLOQUEADO | Staging, configuração e primeira autorização explícita |

Logs reais atuais 34 a 40 em documentacao/evidencias/resultados. JSON inválido foi testado sem criar registro. Nenhum volume foi removido; Oracle permaneceu em execução. A imagem tem cerca de 157MB de conteúdo e 461MB de disco. O runtime Docker executa com usuário não privilegiado.

## Git e CI remoto

Publicação autorizada pelo responsável em 13/10, condicionada à revisão. Os commits serão claros, sem reescrever histórico ou force push. A consulta remota anterior à publicação confirmou run37753337819, SHA325c180: BUILD/TEST/DOCKER success, STAGING failure por DEPLOY_HOST, PRODUCTION skipped. Environments staging/Production: zero secrets/variables. Esse run antigo possui sete testes; não valida as alterações locais com dez testes.

Consulte o relatório final na pasta entrega e o histórico Git para o hash publicado e a execução disparada após esta revisão. Novos resultados devem ser comprovados pelo run correspondente, não presumidos a partir do sucesso local.

## Segurança

.env necessário à execução foi copiado sem exibição e continua ignorado. ACL local restrita ao usuário, SYSTEM e administradores. Secrets/chaves não foram inseridos em fontes, YAML ou documentação. Varredura heurística dos candidatos Git procura credenciais locais e padrões de tokens; ZIP também verifica arquivos textuais e XML do Word. Não há garantia absoluta por heurística.

Não foram encontrados enunciados, prompts ou instruções do assistente nos fontes, scripts funcionais e workflows. Arquivos regeneráveis/temporários não serão versionados ou empacotados. Arquivos históricos continuam recuperáveis no novo diretório e no original.

## Documentação e entrega

Word: entrega/AgriSat-IA-Documentacao-DevOps.docx. ZIP: entrega/AgriSat-IA-FIAP-DevOps.zip. A revisão visual usa exportação real pelo Word instalado, pois o renderizador padrão não encontrou LibreOffice. As imagens de revisão são internas, não evidências artificiais de aplicativos. O manifesto de empacotamento registra membros, SHA256 e integridade.

A pasta entrega é ignorada no Git; seus artefatos são entregáveis locais. O ZIP inclui explicitamente o Word, mas não inclui .git, .env, target, node_modules, chaves, caches, histórico redundante, temporários ou outros ZIPs. A atividade completa continua BLOQUEADA pelos deploys obrigatórios e pela confirmação da gestão.
