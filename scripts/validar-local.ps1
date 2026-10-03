param([switch]$TestarPersistencia)
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
$config = @{}
Get-Content .env | ForEach-Object {
    if ($_ -match '^([A-Z_]+)=(.*)$') { $config[$Matches[1]] = $Matches[2] }
}
$port = if ($config.SERVER_PORT) { $config.SERVER_PORT } else { '8080' }
$base = "http://localhost:$port"
$user = if ($config.API_SECURITY_USER) { $config.API_SECURITY_USER } else { 'admin' }
$basic = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("${user}:$($config.API_SECURITY_PASSWORD)"))
$headers = @{ Authorization = "Basic $basic" }
$health = Invoke-WebRequest "$base/actuator/health"
$healthText = if ($health.Content -is [byte[]]) { [Text.Encoding]::UTF8.GetString($health.Content) } else { $health.Content }
if ($health.StatusCode -ne 200 -or ($healthText | ConvertFrom-Json).status -ne 'UP') { throw 'Health não está UP' }
Write-Output "GET /actuator/health: HTTP 200, status UP (sem autenticação)"
foreach ($route in @('/areas','/equipamentos','/alertas','/leituras','/relatorios/consumo-diario')) {
    $response = Invoke-WebRequest "$base$route" -Headers $headers
    if ($response.StatusCode -ne 200) { throw "Falha em $route" }
    Write-Output "GET ${route}: HTTP 200; resposta $($response.Content)"
}
try { Invoke-WebRequest "$base/areas" | Out-Null; throw 'API aceitou acesso sem autenticação' }
catch { if ([int]$_.Exception.Response.StatusCode -ne 401) { throw }; Write-Output 'GET /areas sem autenticação: HTTP 401' }
docker compose ps
if ($LASTEXITCODE -ne 0) { throw 'Compose ps falhou' }
docker volume inspect agrisat-ia_agrisat-oracle-data --format 'Volume={{.Name}} Driver={{.Driver}}'
docker network inspect agrisat-ia_agrisat-network --format 'Network={{.Name}} Driver={{.Driver}} Containers={{len .Containers}}'
if ($LASTEXITCODE -ne 0) { throw 'Inspeção de rede falhou' }
if ($TestarPersistencia) {
    $marker = 'EVIDENCIA-PERSISTENCIA-' + (Get-Date -Format 'yyyyMMdd-HHmmss')
    $body = @{ nome = $marker; descricao = 'Registro local criado para comprovar persistência do volume Oracle' } | ConvertTo-Json
    $created = Invoke-RestMethod "$base/areas" -Method Post -Headers $headers -ContentType 'application/json' -Body $body
    if (-not $created.idArea) { throw 'Registro não foi criado' }
    $equipmentBody = @{ nome = $marker; status = 'ATIVO'; limiteConsumo = 123.45; idArea = $created.idArea } | ConvertTo-Json
    $equipment = Invoke-RestMethod "$base/equipamentos" -Method Post -Headers $headers -ContentType 'application/json' -Body $equipmentBody
    $equipmentList = Invoke-RestMethod "$base/equipamentos" -Headers $headers
    $savedEquipment = $equipmentList | Where-Object { $_.idEquipamento -eq $equipment.idEquipamento }
    if ($savedEquipment.limiteConsumo -ne 123.45) { throw 'Round-trip NUMERIC Oracle falhou' }
    Write-Output "NUMERIC Oracle: equipamento=$($equipment.idEquipamento), limiteConsumo=123.45 gravado e consultado pela API."
    Write-Output "Antes de recriar Oracle: idArea=$($created.idArea), nome=$marker"
    docker compose up -d --force-recreate oracle-db
    if ($LASTEXITCODE -ne 0) { throw 'Recriação do Oracle falhou' }
    docker compose up -d --wait --wait-timeout 900
    if ($LASTEXITCODE -ne 0) { throw 'Compose não ficou saudável após recriação' }
    $areaList = Invoke-RestMethod "$base/areas" -Headers $headers
    $after = $areaList | Where-Object { $_.idArea -eq $created.idArea }
    if ($after.nome -ne $marker) { throw 'Registro não persistiu' }
    Write-Output "Depois de recriar Oracle: idArea=$($after.idArea), nome=$($after.nome). PERSISTENCIA APROVADA. Volume preservado."
}
