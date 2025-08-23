FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/chatbot-1.0.0-shaded.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


