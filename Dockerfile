FROM maven:3.9.3-eclipse-temurin AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/crypto-stat-tracker-1.0.0.jar /app/app.jar

EXPOSE 8080

ENV POSTGRE_HOSTNAME=db
ENV POSTGRE_PORT=5432
ENV DB=currency_tracker_db
ENV DB_USERNAME=user
ENV DB_PASSWORD=pass

ENTRYPOINT ["java", "-jar", "/app/app.jar"]