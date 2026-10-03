# RELATÓRIO FINAL DA ATIVIDADE DEVOPS — AGRISAT IA

> RELATÓRIO HISTÓRICO: o bloqueio Docker abaixo foi resolvido. Consulte [RELATORIO-RETOMADA.md](RELATORIO-RETOMADA.md) para resultados atuais e [GIT-ACTIONS-RETOMADA.md](GIT-ACTIONS-RETOMADA.md) para publicação/CI.

Data: 03/10/2026. Estado: PARCIAL / BLOQUEADO POR AÇÃO HUMANA. A atividade não está concluída.

## 1. Aplicação

Java: Temurin 21.0.11; JAVA_HOME configurado.  
Spring: Spring Boot 3.5.14.  
Banco: Oracle previsto no Compose; H2 exclusivo dos testes. Oracle operacional ainda não comprovado.  
Frontend: HTML/CSS/JavaScript preservado. Protótipo demonstrativo sem integração funcional com a API.

## 2. Testes locais

Total: 7. Passaram: 7. Falharam: 0. Erros: 0. Ignorados: 0.  
Comando: .\\mvnw.cmd -B -ntp clean test. Tempo: 47,039 s.  
STATUS: APROVADOS. Health público e API protegida verificados por MockMvc no contexto Spring/H2.

## 3. Build

Maven Wrapper: 3.9.15. Comando: clean package com testes ativos.  
JAR: target/energia-esg-api-0.0.1-SNAPSHOT.jar. Tamanho: 92.311.469 bytes.  
Tempo: 49,532 s. Os 7 testes passaram também nesta execução.  
STATUS: APROVADO.

## 4. Docker

Imagem planejada: agrisat-api:local. Build tentou executar, mas Docker Desktop não iniciou.  
Compose: sintaxe aprovada por docker compose config --quiet com placeholders temporários.  
Aplicação: configurada, não executada em container.  
Banco: gvenzl/oracle-free:23-slim, FREEPDB1; não executado.  
Volume: agrisat-oracle-data em /opt/oracle/oradata; persistência não testada.  
Network: agrisat-network bridge, configurada; execução não testada.  
Variáveis: senhas obrigatórias via ambiente, sem defaults de senha no Compose.  
STATUS: BLOQUEADO. HypervisorPresent=False e VirtualizationFirmwareEnabled=False. WSL atualizado e tentativa de reinício Docker executada. Recursos opcionais Windows exigem elevação administrativa para inspeção. Não houve reinício do computador.

## 5. Pipeline

GitHub Actions implementado com BUILD → TEST AND PACKAGE → DOCKER BUILD → STAGING → PRODUCTION.  
BUILD: clean compile. TEST: clean verify. Docker: multi-stage com testes ativos.  
Deploy: SSH com imagem do mesmo SHA, Compose --wait, leitura autenticada /areas e health público.  
YAML e dependências aprovados; actionlint 1.7.12 terminou com código 0. ShellCheck e Pyflakes não estavam disponíveis e foram desabilitados nessa verificação.  
Build remoto: PENDENTE. Tests remoto: PENDENTE. Docker remoto: PENDENTE.  
STATUS: IMPLEMENTADO E REVISADO ESTATICAMENTE, EXECUÇÃO REMOTA PENDENTE.

## 6. Staging

URL: não obtida. Health público: não testado.  
Deploy automatizado: comandos implementados, ainda não executados.  
Environment previsto: staging. Não foi criado/verificado nesta sessão.  
STATUS: PENDENTE DE INFRAESTRUTURA/CONFIGURAÇÃO EXTERNA.

## 7. Production

URL: não obtida. Health público: não testado.  
Deploy automatizado: comandos implementados, depende de staging, ainda não executado.  
Environment previsto: production. Não foi criado/verificado nesta sessão.  
STATUS: PENDENTE DE INFRAESTRUTURA/CONFIGURAÇÃO EXTERNA.

## 8. Evidências

Nenhum screenshot foi obtido ou inventado. documentacao/prints permanece sem capturas.

Evidências textuais reais:
- documentacao/evidencias/resultados/01-maven-test.txt
- documentacao/evidencias/resultados/02-maven-package.txt
- documentacao/evidencias/resultados/03-validacao-estatica.txt
- documentacao/evidencias/resultados/04-actionlint.txt

GUIA-EVIDENCIAS.md contém as 14 capturas exigidas, comandos, resultado esperado, nomes e destino, mais Oracle/Flyway, volume/network e persistência. Logs não foram tratados como screenshots.

## 9. README

Atualizado com os títulos acadêmicos, comandos, conteúdo Docker, Compose, pipeline, segurança, integrante, estado real e checklist.  
STATUS: TEXTO ATUALIZADO; PRINTS E URLS REAIS PENDENTES.

## 10. PPT/PDF

Arquivo: documentacao/apresentacao/AgriSat-IA-DevOps.pdf.  
PDF de dez páginas, renderizado e visualmente inspecionado. Inclui arquitetura, pipeline, comandos, desafios, resultados reais e checklist.  
CONTEUDO-APRESENTACAO.md contém título/texto/visual/evidência/fala para cada um dos dez slides.  
STATUS: PDF PARCIAL VÁLIDO; EVIDÊNCIAS VISUAIS DE CI E DEPLOYS PENDENTES. PPTX não gerado.

## 11. ZIP

Arquivo previsto: AgriSat-IA-DevOps-FINAL.zip.  
Destino previsto: pasta entrega, fora da raiz do repositório.  
Conteúdo validado: não se aplica; não gerado.  
STATUS: PENDENTE. Somente será criado quando todos os requisitos forem validados.

## 12. Checklist do professor

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☐ |
| docker-compose.yml ou arquivos Kubernetes | ☐ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☐ |

Os arquivos existem/configurações foram revisadas, mas nenhum requisito composto foi marcado como entrega concluída sem validação operacional/evidências.

## 13. Pendências

1. Habilitar virtualização/hipervisor e validar Docker Desktop no PC.
2. Build de imagem e Compose operacional com API/Oracle/Nginx healthy.
3. Validar V1 Flyway, schema JPA, GETs reais, health e persistência em Oracle.
4. Configurar servidores SSH, GitHub Environments, secrets e URLs.
5. Depois dos critérios locais aprovados, revisar arquivos, commit e push main.
6. Acompanhar CI remoto e comprovar deploy staging/production com URLs públicas.
7. Capturar prints reais e incorporar ao README/PDF.
8. Gerar e conferir o ZIP final.

Não houve commit, push, deploy, criação de conta, contratação ou cobrança. Git preservado: main e origin https://github.com/Magnols/AgriSat-IA.git. Último commit existente: def8026. README modificado e arquivos DevOps novos permanecem sem staging/commit.

## 14. AÇÃO NECESSÁRIA DO USUÁRIO

### AÇÃO MANUAL NECESSÁRIA — DOCKER / VIRTUALIZAÇÃO

1. Salve seus trabalhos.
2. Abra Gerenciador de Tarefas > Desempenho > CPU > Virtualização.
3. Se Desabilitada, habilite Intel VT-x/Virtualization Technology ou AMD SVM na BIOS/UEFI. A tecla depende do fabricante.
4. Confirme Plataforma de Máquina Virtual e Subsistema do Windows para Linux nos recursos Windows. Operações administrativas exigem elevação.
5. Reinicie o PC quando solicitado.
6. Abra Docker Desktop e aguarde Engine running.
7. Rode docker info e confirme seção Server sem erro.
8. Volte ao Codex para retomar as validações. Não envie senhas.

### AÇÃO MANUAL NECESSÁRIA — STAGING/PRODUCTION

Abra documentacao/ACAO-MANUAL-DEPLOY.md: contém passos de Azure for Students, VM x64 Ubuntu, acesso SSH, .env no servidor, portas e proteção de credenciais. Alternativa: VMs já fornecidas pela FIAP.

1. Abra https://azure.microsoft.com/free/students/ e conclua login/verificação acadêmica somente se elegível, sem cartão.
2. Não faça upgrade nem use Pay-As-You-Go. Se pedir cartão/pagamento, pare.
3. No portal https://portal.azure.com/, prepare VMs Linux para staging/production dentro dos créditos, após revisar a estimativa e limite de gastos. Recursos consomem créditos.
4. Registre IP/DNS e usuário; prepare Docker e .env protegido de cada servidor conforme o guia.
5. Abra https://github.com/Magnols/AgriSat-IA/settings/environments > New environment e configure staging e production.
6. Em cada um, crie DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY e DEPLOY_KNOWN_HOSTS como Environment secrets; DEPLOY_URL como Environment variable.
7. Verifique fingerprint SSH por console confiável. Nunca envie chave privada neste chat.
8. Informe ao Codex somente a configuração concluída e as URLs. Deploys ainda precisarão ser executados e testados.

STATUS DA ATIVIDADE: PRECISA DE CORREÇÕES / VALIDAÇÕES PENDENTES.
