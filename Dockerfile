# Étape 1 : construire le projet avec Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers du projet
COPY pom.xml .
COPY src ./src

# Construire le jar
RUN mvn clean package -DskipTests

# Étape 2 : créer l'image finale avec Java
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copier le jar construit depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port (par défaut 8080)
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java","-jar","app.jar"]
