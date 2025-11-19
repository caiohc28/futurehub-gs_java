# Etapa 1 — Build da aplicação (opcional se você já gerar o .jar no pipeline)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Gera o .jar
RUN mvn clean package -DskipTests

# Etapa 2 — Runtime do container
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Define a porta interna do container
EXPOSE 8081

# Comando para rodar
ENTRYPOINT ["java", "-jar", "app.jar"]
