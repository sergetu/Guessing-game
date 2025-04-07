FROM gradle:jdk17-alpine
WORKDIR app

COPY . .

RUN gradle clean bootJar
COPY build/libs/GuessingGame-0.0.1-SNAPSHOT.jar game.jar
ENTRYPOINT ["java","-jar","./game.jar"]