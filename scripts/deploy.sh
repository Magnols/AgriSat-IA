#!/usr/bin/env bash
set -euo pipefail
environment="${1:?staging ou production}"
sha="${2:?commit SHA}"
[[ "$environment" == staging || "$environment" == production ]]
[[ "$sha" =~ ^[a-f0-9]{40}$ ]]
env_file="$HOME/agrisat/$environment/.env"
test -f "$env_file" || { echo "Arquivo .env do ambiente ausente no servidor."; exit 1; }
export API_IMAGE="agrisat-api:$sha"
docker load --input image.tar.gz
docker compose --project-name "agrisat-$environment" --env-file "$env_file" config --quiet
docker compose --project-name "agrisat-$environment" --env-file "$env_file" up -d --no-build --wait --wait-timeout 900
docker compose --project-name "agrisat-$environment" --env-file "$env_file" ps
# Valida leitura autenticada usando credenciais do ambiente, sem escreve-las nos logs.
docker compose --project-name "agrisat-$environment" --env-file "$env_file" exec -T api sh -c \
  'wget -qO- --header="Authorization: Basic $(printf "%s:%s" "$API_SECURITY_USER" "$API_SECURITY_PASSWORD" | base64 | tr -d "\n")" http://localhost:8080/areas >/dev/null'
echo "Release operacional: $environment $sha"
