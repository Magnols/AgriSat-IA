$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
$files = @(git -c core.quotepath=false ls-files --cached --others --exclude-standard)
$prohibited = @($files | Where-Object { $_ -match '(^|/)(\.env|target|node_modules|\.git)(/|$)' })
if ($prohibited.Count) { throw ('Arquivos locais indevidos: ' + ($prohibited -join ', ')) }
$secrets = @()
if (Test-Path .env) {
    Get-Content .env | ForEach-Object {
        if ($_ -match '^(DB_PASSWORD|API_SECURITY_PASSWORD)=(.+)$') { $secrets += $Matches[2] }
    }
}
$hits = @()
foreach ($file in $files) {
    if ($file -match '\.(png|jpe?g|pdf|ico|woff2?|ttf|gif|jar|zip)$') { continue }
    $content = Get-Content -LiteralPath $file -Raw
    foreach ($secret in $secrets) {
        if ($content.Contains($secret)) { $hits += $file; break }
    }
    if ($content -match '-----BEGIN (RSA |EC |OPENSSH )?PRIVATE KEY-----|gh[pousr]_[A-Za-z0-9_]{20,}|AKIA[0-9A-Z]{16}|AIza[0-9A-Za-z_-]{20,}') { $hits += $file }
}
if ($hits.Count) { throw ('Revisar arquivos com possível segredo (valores omitidos): ' + (($hits | Sort-Object -Unique) -join ', ')) }
git check-ignore .env target
Write-Output "SEGURANCA: sem credenciais locais ou padrões de chave/token nos $($files.Count) arquivos candidatos ao Git. Inspeção heurística, não garantia absoluta."
