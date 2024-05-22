FROM maven:latest AS stage1
WORKDIR /discovery-server
COPY pom.xml /discovery-server
RUN mvn dependency:resolve
COPY . /discovery-server
RUN mvn clean install
RUN mvn package -DskipTests

FROM openjdk:21-jdk as final
COPY --from=stage1 /discovery-server/target/*.jar discovery-server-0.1.jar
EXPOSE 8761
CMD ["java", "-jar", "discovery-server-0.1.jar"]