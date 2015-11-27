FROM java:7
COPY server/target/jconfig-server.jar /opt/jconfig/jconfig-server.jar
WORKDIR /opt/jconfig
ENTRYPOINT ["java", "-jar", "jconfig-server.jar"]
