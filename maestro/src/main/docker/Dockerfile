FROM java:8-jre

RUN curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose

VOLUME [ "/dev", "/etc/docker", "/etc/hosts", "/etc/resolv.conf", "/etc/selinux", "/etc/ssl/certs/ca-certificates.crt", "/home", "/lib/firmware", "/lib/modules", "/opt", "/run", "/usr/bin/docker", "/usr/bin/docker-containerd", "/usr/bin/docker-containerd-shim", "/usr/bin/docker-runc", "/usr/bin/ros", "/usr/share/ros", "/var/lib/docker", "/var/lib/rancher", "/var/lib/rancher/cache", "/var/lib/rancher/conf", "/var/log", "/var/run" ]

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
