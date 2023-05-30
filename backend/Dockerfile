# Use the Eclipse Temurin Java 17 JDK image to create a build artifact.
FROM eclipse-temurin:17.0.7_7-jdk AS build

# Install maven
RUN apt-get update && apt-get install -y maven

# Set the working directory.
WORKDIR /usr/src/app

# Copy the pom.xml file to download dependencies.
COPY pom.xml .

# Download the dependencies.
RUN mvn dependency:go-offline -B

# Copy the rest of the code.
COPY src ./src

# Build the application.
RUN mvn package -DskipTests

# Use the Eclipse Temurin Java 17 JRE image for the runtime base image.
FROM eclipse-temurin:17.0.7_7-jre

# Set the working directory.
WORKDIR /app

# Copy the jar file from the build stage.
COPY --from=build /usr/src/app/target/*.jar app.jar

# Run the application.
CMD ["java", "-jar", "app.jar"]
