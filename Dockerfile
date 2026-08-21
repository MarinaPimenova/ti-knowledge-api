FROM alpine/java:21-jre

LABEL org.opencontainers.image.source=https://github.com/MarinaPimenova/ti-knowledge-api
COPY build/libs/*.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
