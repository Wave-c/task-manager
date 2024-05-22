FROM maven:latest AS stage1
WORKDIR /auth-service
COPY pom.xml /auth-service
RUN mvn dependency:resolve
COPY . /auth-service
RUN mvn clean install
RUN mvn package -DskipTests

FROM openjdk:21-jdk as final
COPY --from=stage1 /auth-service/target/*.jar auth-service-0.1.jar
EXPOSE 8080
CMD ["java", "-jar", "auth-service-0.1.jar"]