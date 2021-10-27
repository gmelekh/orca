FROM maven:3.8.3-openjdk-11
COPY * .
RUN mvn clean package
COPY  target/orca-*.jar /opt/orca/
COPY src/main/resources/application.yaml /opt/config/orca/application.yaml

WORKDIR /opt/orca

ENTRYPOINT ["java", "-jar", "/opt/orca/orca-0.0.1-SNAPSHOT.jar","--spring.config.location=file:/opt/config/orca/application.yaml"]
