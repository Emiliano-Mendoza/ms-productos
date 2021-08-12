FROM openjdk:11.0.7-slim 
LABEL maintainer="eemmiii96@gmail.com" 
COPY target/productos-0.0.1-SNAPSHOT.jar /opt/productos-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/opt/productos-0.0.1-SNAPSHOT.jar"]