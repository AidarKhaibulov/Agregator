FROM openjdk:21-jdk
WORKDIR /usr/local/app
ADD services/app/agr-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar ./app.jar