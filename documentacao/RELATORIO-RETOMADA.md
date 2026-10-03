# RELATÓRIO FINAL DA ATIVIDADE DEVOPS — AGRISAT IA

Atualização: 03/10/2026. Substitui o estado histórico do relatório anterior. Parte local aprovada; atividade completa pendente.

## 1. Aplicação

Java: 21.0.11 no PC, 21.0.12.1 no runtime Docker. Spring Boot: 3.5.14. Banco: Oracle gvenzl/oracle-free:23-slim, versão executada 23.26.3, FREEPDB1. H2 apenas nos testes. Frontend: HTML/CSS/JavaScript, Nginx HTTP 200 em localhost:8081, protótipo sem integração funcional com a API.

## 2. Testes locais

Total: 7. Passaram: 7. Falharam: 0. Erros/ignorados: 0. clean test: 1min40s após correções. Também passaram os 7 no package e no Docker build. STATUS: APROVADOS. Não houve skipTests.

## 3. Build

Maven Wrapper 3.9.15. clean package: BUILD SUCCESS em 1min29s. JAR: target/energia-esg-api-0.0.1-SNAPSHOT.jar, 92.311.659 bytes. STATUS: APROVADO.

## 4. Docker

Imagem agrisat-api:local criada. Maven em Docker: 2min39s com testes ativos. Multi-stage Java 21, usuário agrisat não root. Conteúdo aproximadamente 157 MB, uso em disco aproximadamente 461 MB.
Compose build/up --wait aprovados. API, Oracle e frontend HEALTHY.
Volume agrisat-ia_agrisat-oracle-data, driver local, /opt/oracle/oradata. Network agrisat-ia_agrisat-network, bridge, três containers.
Flyway V1 aplicada, version=1 e success=1. Hibernate validate aprovado.
GETs autenticados /areas, /equipamentos, /alertas, /leituras e /relatorios/consumo-diario: HTTP 200. /areas sem autenticação: 401. /actuator/health público: 200 e UP.
NUMERIC Oracle: limiteConsumo=123.45 gravado e consultado pela API.
Persistência: Oracle recriado sem remover volume; área id=2, EVIDENCIA-PERSISTENCIA-20261003-163315, preservada e consultada. Dois pares de registros locais identificados como EVIDENCIA-PERSISTENCIA permanecem no banco.
Correção aplicada: campos Double de consumo explicitamente JDBC NUMERIC, precisão 10/escala 2. A migração V1 já aplicada foi preservada.
Avisos: Flyway informa Oracle mais novo que sua cobertura testada; Oracle registra deriva de relógio WSL; pool renovou conexões após recriação. Validação final passou.
STATUS: APROVADO LOCALMENTE.

## 5. Pipeline

GitHub Actions implementado com BUILD, TEST AND PACKAGE, DOCKER BUILD, STAGING e PRODUCTION. Actionlint/YAML aprovados na inspeção anterior. Production depende de staging. Mesma imagem SHA, SSH, Compose --wait, health público e leitura autenticada.
Build/Tests/Docker remotos: ainda não comprovados. Resultado Git/publicação em GIT-ACTIONS-RETOMADA.md.

## 6. Staging

URL: não obtida. Health/deploy automatizado: não executados. STATUS: PENDENTE DE INFRAESTRUTURA/SECRETS.

## 7. Production

URL: não obtida. Health/deploy automatizado: não executados, dependem de staging. STATUS: PENDENTE DE INFRAESTRUTURA/SECRETS.

## 8. Evidências

Logs reais em documentacao/evidencias/resultados: 01-maven-test.txt, 02-maven-package.txt, 05-docker-build.txt, 07-compose-build.txt, 08-compose-up.txt, 09-oracle-flyway.txt, 10-api-persistencia.txt, 11-api-leitura.txt, 12-compose-logs.txt e 14-seguranca.txt. Arquivos anteriores 03/04 registram validação estática/actionlint.
Screenshots ainda pendentes: captura nativa encontrou timeout/janela minimizada e navegador automatizado bloqueou localhost. Nenhum print foi inventado. Logs não são screenshots.

## 9. README

Resultados locais, comandos, Oracle e checklist atualizados. STATUS: TEXTO ATUALIZADO; prints e URLs públicas pendentes.

## 10. PPT/PDF

documentacao/apresentacao/AgriSat-IA-DevOps.pdf: dez páginas válidas, versão parcial histórica anterior a esta retomada. NÃO É A APRESENTAÇÃO FINAL. Atualizar resultados e incorporar prints CI/deploy antes da entrega. Este relatório é a fonte atual.

## 11. ZIP

AgriSat-IA-DevOps-FINAL.zip: NÃO GERADO, conforme regra de aguardar deploys comprovados.

## 12. Checklist do professor

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☑ |
| docker-compose.yml ou arquivos Kubernetes | ☑ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☐ |

## 13. Pendências

CI remoto, servidores/environments/secrets, URLs staging/production, prints reais, PDF final e ZIP.
Ambiente operacional: WSL 3.0.1.0, kernel 6.18.40.1-1, docker-desktop WSL2 running; Desktop 4.80.0; Engine/CLI 29.6.1; Compose 5.1.4.
Segurança: .env local com senhas aleatórias distintas ignorado; target ignorado; senhas padrão removidas de application.properties. Varredura dos candidatos Git sem credenciais locais ou padrões de chave/token. Nenhum valor real registrado no relatório. Não foram contratados serviços nem criadas contas/VMs.

## 14. AÇÃO NECESSÁRIA DO USUÁRIO

1. Siga documentacao/ACAO-MANUAL-DEPLOY.md. Use VMs FIAP existentes ou Azure for Students somente se elegível, sem cartão/upgrade/contratação paga.
2. Prepare dois servidores com Docker e .env protegido. Obtenha IP/DNS e usuário SSH. Não envie senha ou chave privada no chat.
3. Abra https://github.com/Magnols/AgriSat-IA/settings/environments, clique New environment e crie staging e production.
4. Em cada environment crie secrets DEPLOY_HOST, DEPLOY_USER, DEPLOY_SSH_KEY e DEPLOY_KNOWN_HOSTS. Confirme fingerprint por canal confiável.
5. Crie Environment variable DEPLOY_URL com URL pública da API de cada ambiente.
6. Informe somente configuração concluída e URLs públicas para comprovar os deploys.
7. Para prints locais, abra PowerShell na raiz e siga GUIA-EVIDENCIAS.md. Capture saídas reais com Win+Shift+S em documentacao/prints. Abra localhost:8081 e localhost:8080/actuator/health para capturas 06/07. Não capture .env nem configuração expandida.

STATUS DA ATIVIDADE: PARTE LOCAL APROVADA / ENTREGA COMPLETA PENDENTE.
