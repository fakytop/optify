# Paso 1: Usar una imagen de Java 17 (o la versión que uses)
FROM eclipse-temurin:21-jdk-alpine

# Paso 2: Crear un directorio para la app
WORKDIR /app

# Paso 3: Copiar el archivo JAR generado por Maven al contenedor
# Nota: Asegúrate de que el nombre coincida con lo que genera tu 'mvn clean package'
COPY target/app-0.0.1-SNAPSHOT.jar app.jar

# Paso 4: Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]