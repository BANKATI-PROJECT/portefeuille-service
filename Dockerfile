FROM openjdk:17-oracle
COPY target/*.jar  app.jar
EXPOSE 8099
ENTRYPOINT ["java","-jar", "app.jar"]