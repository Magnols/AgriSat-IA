"""Empacota fontes e documentação validada, sem credenciais ou artefatos locais."""
from pathlib import Path, PurePosixPath
import hashlib, re, subprocess, zipfile, sys
from xml.etree import ElementTree
from io import BytesIO

root=Path(__file__).resolve().parent.parent
output=root/'entrega/AgriSat-IA-FIAP-DevOps.zip'
word='entrega/AgriSat-IA-Documentacao-DevOps.docx'
if output.exists() and '--atualizar' not in sys.argv:
    raise SystemExit('Pacote existente preservado; use --atualizar após revisão.')
listed=subprocess.check_output(['git','-c','core.quotepath=false','ls-files','--cached','--others','--exclude-standard','-z'],cwd=root).decode('utf-8').split('\0')
files=set()
excluded={'.git','.env','target','node_modules','.idea','.vscode','logs','historico','.build-documentacao','entrega','__pycache__'}
for name in listed:
    if not name or not (root/name).is_file(): continue
    parts=PurePosixPath(name).parts
    if any(p in excluded for p in parts):continue
    if any(p.startswith('.env') and p!='.env.example' for p in parts):continue
    if name.startswith('documentacao/prints/'):continue
    if name.startswith('documentacao/evidencias/resultados/'):
        ordinal=re.match(r'.*/(\d+)',name)
        if ordinal and int(ordinal[1])<34 and int(ordinal[1])!=24:continue
    if PurePosixPath(name).suffix.lower() in {'.key','.pem','.p12','.jks','.zip','.rar','.log','.pyc','.class','.jar'}:continue
    files.add(name)
files.add(word)
required=['Dockerfile','docker-compose.yml','pom.xml','mvnw','mvnw.cmd','README.md','.env.example','.dockerignore','.gitignore','.github/workflows/ci-cd.yml','.github/workflows/deploy.yml',word,'documentacao/VALIDACAO.md','documentacao/evidencias/INDICE.md']
assert all(n in files and (root/n).is_file() for n in required)
assert all(any(n.startswith(prefix) for n in files) for prefix in ['src/','.mvn/','frontend/','scripts/'])
secret_values=[]
for line in (root/'.env').read_text(encoding='utf-8-sig').splitlines():
    k,s,v=line.partition('=')
    if s and any(w in k for w in ['PASSWORD','TOKEN','SECRET']) and v:secret_values.append(v)
pattern=re.compile(r'-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----|gh[pousr]_[A-Za-z0-9_]{20,}|AKIA[0-9A-Z]{16}|AIza[0-9A-Za-z_-]{20,}')
def validate(name,data):
    parts=PurePosixPath(name).parts
    assert not any(p in excluded-{'entrega'} for p in parts),name
    assert not any(p.startswith('.env') and p!='.env.example' for p in parts),name
    if name.endswith('.docx'):
        with zipfile.ZipFile(BytesIO(data)) as z:
            assert z.testzip() is None
            text='\n'.join(' '.join(ElementTree.fromstring(z.read(n)).itertext()) for n in z.namelist() if n.endswith('.xml'))
    elif name.endswith(('.png','.jpg','.jpeg','.ico','.gif')):return
    else:
        try:text=data.decode('utf-8-sig')
        except UnicodeDecodeError:return
    assert not any(v in text for v in secret_values),'Possível credencial local em '+name
    assert not pattern.search(text),'Possível padrão de credencial em '+name
for name in files:
    assert (root/name).resolve().is_relative_to(root),name
    validate(name,(root/name).read_bytes())
output.parent.mkdir(exist_ok=True)
with zipfile.ZipFile(output,'w',zipfile.ZIP_DEFLATED,compresslevel=9) as z:
    for name in sorted(files):z.write(root/name,name)
with zipfile.ZipFile(output) as z:
    assert z.testzip() is None
    assert sorted(z.namelist())==sorted(files)
    for name in z.namelist():validate(name,z.read(name))
sha=hashlib.sha256(output.read_bytes()).hexdigest()
output.with_suffix('.manifest.txt').write_text('PACOTE COM DEPLOYS EXTERNOS PENDENTES\n'+f'Arquivos: {len(files)}\nBytes: {output.stat().st_size}\nSHA256: {sha}\n\n'+'\n'.join(sorted(files))+'\n',encoding='utf-8')
print(f'ZIP VALIDADO: {len(files)} arquivos; SHA256 {sha}')
