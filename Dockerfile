FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /manager
COPY . /manager/.
RUN mvn -f /manager/pom.xml clean package -Dmaven.test.skip=false

FROM eclipse-temurin:17-jre-alpine
WORKDIR /manager
COPY --from=builder /target/*.jar /manager/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/manager/*.jar"]