# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk
# Establece el directorio de trabajo dentro del contenedor
ARG JAR_FILE=target/Bazar-0.0.1.jar
# Copia el archivo .jar en el contenedor
COPY ${JAR_FILE} app_bazar.jar
# Expone el puerto en el que correrá la aplicación (ajústalo si es necesario)
EXPOSE 8080
# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app_bazar.jar"]