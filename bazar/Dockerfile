# Usa la imagen oficial de OpenJDK 17
FROM openjdk:17-jdk

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el script wait-for-it
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x wait-for-it.sh  # Da permisos de ejecución al script

# Copia el archivo JAR en el contenedor
ARG JAR_FILE=target/Bazar-0.0.1.jar
COPY ${JAR_FILE} app_bazar.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para esperar a la base de datos antes de iniciar la aplicación
CMD ["sh", "-c", "./wait-for-it.sh bazar_db:3306 --timeout=60 --strict -- java -jar app_bazar.jar"]