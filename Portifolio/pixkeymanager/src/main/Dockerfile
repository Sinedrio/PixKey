# Use uma imagem base com o JDK
FROM openjdk:17-jdk-slim

# Adicione o arquivo JAR da sua aplicação à imagem
COPY build/libs/pixkeymanager-0.0.1-SNAPSHOT.jar /app/pixkeymanager.jar

# Define o diretório de trabalho
WORKDIR /app

# Define a porta em que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "pixkeymanager.jar"]
