FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests package

FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.war"]
