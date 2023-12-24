# 1) Build dependencies
FROM maven:3.8-eclipse-temurin-17 AS build-dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# 2) Build app
FROM build-dependencies AS build
WORKDIR /app
COPY src src
RUN mvn package -DskipTests

# 3) Copy and RUN
FROM eclipse-temurin:17 AS finish-app
LABEL authors="algeps"
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]