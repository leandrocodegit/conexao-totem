# Utiliza uma imagem Maven com Java 17 (Eclipse Temurin)
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o código fonte para o container
COPY . .

# Executa o Maven para compilar o projeto
RUN mvn clean package -DskipTests

# Usa uma imagem mais leve para executar o JAR compilado
FROM eclipse-temurin:17-jdk

# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Copia o JAR do estágio de build para o estágio de runtime
COPY --from=builder /app/target/conexao-1.0.0.jar /app/conexao-1.0.0.jar

# Porta em que a aplicação irá rodar
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
EXPOSE 8080 5012

# Comando para executar a aplicação
CMD ["java", "-Xms2g", "-Xmx4g", "-jar", "/app/conexao-1.0.0.jar"]
