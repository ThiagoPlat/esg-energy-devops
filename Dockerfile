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
