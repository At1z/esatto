FROM maven:3.8-openjdk-17 as build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env .

ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "/wait-for-it.sh postgres:5432 -- java -jar app.jar"]