FROM amazoncorretto:11
LABEL authors="allaslyu"
ARG JAR_FILE=account.jar
WORKDIR /opt/account
COPY build/libs/*.jar /opt/account/$JAR_FILE
RUN mkdir -p /opt/account/src/main/resources/account
ENTRYPOINT ["java", "-jar", "/opt/account/account.jar"]