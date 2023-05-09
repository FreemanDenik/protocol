FROM openjdk:17
ARG JAR_FILE=target/*.jar
ADD target/lib /lib
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=docker"]
# выполнить домануду чтобы создать образ с 17 джавой - docker build -t "protocol-game" .