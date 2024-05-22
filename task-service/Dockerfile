FROM maven:latest AS stage1
WORKDIR /task-service
COPY pom.xml /task-service
RUN mvn dependency:resolve
COPY . /task-service
RUN mvn clean install
RUN mvn package -DskipTests

FROM openjdk:21-jdk as final
COPY --from=stage1 /task-service/target/*.jar task-service-0.1.jar
EXPOSE 8081
CMD ["java", "-jar", "task-service-0.1.jar"]