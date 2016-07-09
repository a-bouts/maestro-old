FROM java:8-jre

RUN curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose

#VOLUME "/dev"
#VOLUME "/etc/docker"
#VOLUME "/etc/hosts"
#VOLUME "/etc/resolv.conf"
#VOLUME "/etc/selinux"
#VOLUME "/etc/ssl/certs/ca-certificates.crt"
#VOLUME "/home"
#VOLUME "/lib/firmware"
#VOLUME "/lib/modules"
#VOLUME "/opt"
#VOLUME "/run"
VOLUME "/usr/bin/docker"
#VOLUME "/usr/bin/docker-containerd"
#VOLUME "/usr/bin/docker-containerd-shim"
#VOLUME "/usr/bin/docker-runc"
#VOLUME "/usr/bin/ros"
#VOLUME "/usr/share/ros"
#VOLUME "/var/lib/docker"
#VOLUME "/var/lib/rancher"
#VOLUME "/var/lib/rancher/cache"
#VOLUME "/var/lib/rancher/conf"
#VOLUME "/var/log"
#VOLUME "/var/run"

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
