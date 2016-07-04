FROM java:8-jre

RUN useradd \
  -d /var/lib/maestro \
  -m \
  -U \
  maestro

USER maestro
WORKDIR /var/lib/maestro

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "maestro.jar"]

ADD target/maestro.jar maestro.jar
