# Validação do pacote - 08/10/2026

Pacote: ../entrega/AgriSat-IA-DevOps-FIAP-Entrega-Final.zip. Autorizado pela missão mais recente mesmo com pendências externas; não constitui atividade integralmente concluída.

O gerador listou os arquivos candidatos pelo Git (tracked + untracked não ignorados), verificou fontes e exclusões, criou o ZIP, reabriu, testou CRC de todos os membros, comparou a lista exata de arquivos e repetiu a varredura de segurança sobre o conteúdo extraído em memória. Nenhum arquivo temporário de build foi incluído.

Conteúdo obrigatório confirmado: Dockerfile, docker-compose.yml, src, pom.xml, mvnw, mvnw.cmd, .mvn, .github/workflows/ci-cd.yml, .github/workflows/deploy.yml, README.md, .env.example, .dockerignore, .gitignore, frontend, scripts, documentacao, relatório de validação e novo PDF de 14 páginas. PDF original e prints históricos preservados e identificados como históricos.

Exclusões confirmadas: .git, .env real e variações, target, node_modules, IDEs, logs locais, chaves privadas, backups de migração e arquivos ZIP/RAR antigos. As evidências textuais .txt foram incluídas deliberadamente, sem senhas reais. Senhas locais foram comparadas internamente com os arquivos textuais e texto extraído dos PDFs sem exibir seus valores. Varredura heurística não é garantia absoluta.

Na composição final, este arquivo também faz parte do pacote. O manifesto externo ../entrega/AgriSat-IA-DevOps-FIAP-Entrega-Final.manifest.txt registra quantidade de membros, lista completa e SHA-256 do ZIP efetivamente entregue. O hash fica fora do ZIP para evitar autorreferência. Revalidação CRC e exclusões não depende da presença do manifesto dentro do arquivo.

Pendências explícitas: servidor e GitHub Secrets/URLs, staging, produção com autorização humana, associação Azure Boards autenticada e capturas locais/Azure/deploy. Regenerar o pacote após validá-las. Não entregar o ZIP afirmando que CI/CD completo ou deploys passaram.
