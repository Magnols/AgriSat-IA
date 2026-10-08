param([switch]$Compose)
$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
Set-Location $root
# OneDrive pode expor arquivos como reparse points incompatíveis com BuildKit.
# Apenas fontes necessários são copiados; .env e volumes nunca entram no contexto.
$context = Join-Path ([IO.Path]::GetTempPath()) ('agrisat-build-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $context | Out-Null
foreach ($entry in @('Dockerfile','pom.xml','mvnw','mvnw.cmd','.mvn','src')) {
    Copy-Item -LiteralPath (Join-Path $root $entry) -Destination $context -Recurse
}
Write-Output "Contexto temporário sem credenciais: $context"
if ($Compose) {
    $override = Join-Path $context 'compose-build.json'
    @{services=@{api=@{build=@{context=$context}}}} | ConvertTo-Json -Depth 5 | Set-Content -LiteralPath $override -Encoding utf8
    docker compose --project-directory $root -p agrisat-ia -f (Join-Path $root 'docker-compose.yml') -f $override build
} else {
    docker build -t agrisat-api:local $context
}
if ($LASTEXITCODE -ne 0) { throw 'Build Docker falhou' }
Write-Output 'Build aprovado. Contexto temporário mantido para auditoria; pode ser removido após revisão.'
