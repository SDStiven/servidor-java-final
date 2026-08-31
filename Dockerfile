FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvm clean package -dskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

copy --from=build /app/target/*.jar app.jar

RUN mkdir -p upload/images

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]