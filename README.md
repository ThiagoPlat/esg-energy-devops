# Projeto - Cidades ESG Inteligentes

**Plataforma Integrada de Gestão e Eficiência Energética ESG** — API REST (Java 21 + Spring Boot 3 + MongoDB) com ciclo de vida totalmente automatizado: build, testes, containerização e deploy em *staging* e *produção*.

A API expõe CRUD das 5 coleções do modelo ESG (`unidades_operacionais`, `dispositivos_iot`, `leituras_consumo`, `incidentes_alertas`, `acoes_eficiencia`), um pipeline de agregação (`/api/leituras/resumo`) e uma regra de negócio de classificação de alertas (`/api/alertas/avaliar`).

**Integrantes:** _[Nome – RM]_, _[Nome – RM]_

## Estrutura

```
esg-energy-devops/
├── Dockerfile
├── docker-compose.yml            # local (API + MongoDB)
├── docker-compose.staging.yml    # override: staging local
├── docker-compose.prod.yml       # override: produção local
├── .env.example
├── mongo-init/init.js            # seed das coleções
├── pom.xml
├── src/main | src/test
├── docs/CHECKLIST.md | docs/prints/
├── .github/workflows/ci.yml | cd.yml
└── README.md
```

## Como executar localmente com Docker

Pré-requisitos: Docker + Docker Compose v2.

```bash
cp .env.example .env            # ajuste MONGO_PASSWORD
docker compose up --build       # sobe MongoDB (com seed) + API
```

Verifique:

| O quê | URL |
|-------|-----|
| Health | http://localhost:8080/actuator/health |
| Ambiente | http://localhost:8080/api/info |
| Swagger | http://localhost:8080/swagger-ui/index.html |
| Unidades | http://localhost:8080/api/unidades |
| Resumo (agregação) | http://localhost:8080/api/leituras/resumo |
| Avaliar alerta | http://localhost:8080/api/alertas/avaliar?consumoKwh=150&limiteKwh=100 |

Parar: `docker compose down` (mantém dados) ou `docker compose down -v` (apaga o volume e refaz o seed na próxima subida).

**Simular staging e produção na sua máquina** (projetos Compose isolados, cada um com seu volume):

```bash
API_PORT=8081 docker compose -p esg-staging -f docker-compose.yml -f docker-compose.staging.yml up -d --build
API_PORT=8082 docker compose -p esg-prod    -f docker-compose.yml -f docker-compose.prod.yml    up -d --build
curl localhost:8081/api/info   # {"environment":"staging",...}
curl localhost:8082/api/info   # {"environment":"production",...}
```

Rodar os testes sem Docker: `mvn verify` (Java 21).

## Pipeline CI/CD

**Ferramenta:** GitHub Actions (`.github/workflows/`). **Registry:** Docker Hub. **Hospedagem:** Azure Web App for Containers (um app para staging, outro para produção) com banco MongoDB Atlas separado por ambiente.

Fluxo de branches: `feature/*` → PR → `develop` (**staging**) → PR → `main` (**produção**).

| Workflow | Gatilho | Etapas |
|----------|---------|--------|
| `ci.yml` | PR para `develop`/`main` e push em `feature/**` | checkout → Java 21 (cache Maven) → `mvn verify` (build + testes) → upload dos relatórios → build da imagem Docker (sem push) |
| `cd.yml` | push em `develop` ou `main` | testes → build + push da imagem (`:<sha>` e `:<branch>`) → **deploy staging** (se `develop`) ou **deploy produção** (se `main`, com aprovação manual) → smoke test em `/actuator/health` |

### Configuração necessária no GitHub

*Settings → Secrets and variables → Actions*

| Tipo | Nome | Valor |
|------|------|-------|
| Secret | `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN` | usuário e access token do Docker Hub |
| Secret | `AZURE_PROFILE_STAGING` | publish profile (XML) do Web App de staging |
| Secret | `AZURE_PROFILE_PROD` | publish profile (XML) do Web App de produção |
| Variable | `AZURE_WEBAPP_STAGING` / `AZURE_WEBAPP_PROD` | nomes dos Web Apps |

*Settings → Environments*: crie `staging` e `production` (em `production`, marque **Required reviewers**).

*No Azure (cada Web App → Environment variables)*: `MONGODB_URI` (string do Atlas do ambiente), `APP_ENV` (`staging` / `production`) e `WEBSITES_PORT=8080`.

Proteja `develop` e `main` (*Settings → Branches*) exigindo o check **CI** antes do merge: se algum teste falhar, o merge é bloqueado.

## Containerização

`Dockerfile` (multi-stage):

```dockerfile
# ---------- Etapa 1: build (Maven + JDK 21) ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /opt/app

# Copia só o pom primeiro: o cache de dependências só é invalidado se o pom mudar
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
# Os testes já rodam no pipeline de CI; aqui só empacotamos
RUN mvn -B -q clean package -DskipTests

# ---------- Etapa 2: runtime (somente JRE, imagem leve) ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app

# Segurança: não executar como root
RUN addgroup -S esg && adduser -S esg -G esg
COPY --from=build /opt/app/target/app.jar app.jar
USER esg

# Ambiente padrão; sobrescrito por docker-compose / App Settings do Azure
ENV APP_ENV=local \
    JAVA_OPTS="-XX:MaxRAMPercentage=75"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

Estratégias adotadas:

- **Multi-stage**: o JDK + Maven ficam só na etapa de build; a imagem final tem apenas o JRE Alpine (leve e com menor superfície de ataque).
- **Cache de camadas**: `pom.xml` é copiado e as dependências baixadas antes do código-fonte.
- **Usuário não-root** e **HEALTHCHECK** apontando para o Actuator.
- **Config por variável de ambiente** (`MONGODB_URI`, `APP_ENV`): a mesma imagem sobe em qualquer ambiente; nenhum segredo vai para a imagem (`.dockerignore` exclui `.env`).

`docker-compose.yml`: serviços `mongo` e `api`; rede `esg-net`; volume nomeado `mongo_data` (persistência); seed montado em `/docker-entrypoint-initdb.d`; `depends_on` com `service_healthy` (a API só sobe depois do Mongo responder ao ping); credenciais vindas do `.env`.

## Prints do funcionamento

> Coloque as imagens em `docs/prints/` e referencie aqui.

| Evidência | Arquivo |
|-----------|---------|
| Pipeline CI (build + testes) verde | `docs/prints/01-ci.png` |
| Pipeline CD – staging | `docs/prints/02-cd-staging.png` |
| Pipeline CD – produção (com aprovação) | `docs/prints/03-cd-production.png` |
| Staging funcionando (`/api/info` + Swagger) | `docs/prints/04-staging.png` |
| Produção funcionando (`/api/info` + Swagger) | `docs/prints/05-production.png` |
| Imagem no Docker Hub | `docs/prints/06-dockerhub.png` |

Links: Staging: _[URL]_ · Produção: _[URL]_

## Tecnologias utilizadas

Java 21 · Spring Boot 3.3 (Web, Data MongoDB, Validation, Actuator) · springdoc-openapi (Swagger) · MongoDB 7 / MongoDB Atlas · JUnit 5 + Mockito + MockMvc · Maven · Docker (multi-stage) · Docker Compose · GitHub Actions · Docker Hub · Azure Web App for Containers.

## Checklist de entrega

| Item | OK |
|------|----|
| Projeto compactado em .ZIP com estrutura organizada | ☐ |
| Dockerfile funcional | ☐ |
| docker-compose.yml ou arquivos Kubernetes | ☐ |
| Pipeline com etapas de build, teste e deploy | ☐ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☐ |
| Deploy realizado nos ambientes staging e produção | ☐ |
