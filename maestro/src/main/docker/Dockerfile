FROM java:8-jre

RUN curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose

VOLUME "/etc/docker"
#VOLUME "/etc/hosts"
VOLUME "/etc/resolv.conf"
VOLUME "/etc/selinux"
VOLUME "/etc/ssl/certs/ca-certificates.crt.rancher"
VOLUME "/home"
VOLUME "/host/dev"
VOLUME "/lib/firmware"
VOLUME "/lib/modules"
VOLUME "/opt"
VOLUME "/run"
VOLUME "/sbin/halt"
VOLUME "/sbin/poweroff"
VOLUME "/sbin/reboot"
VOLUME "/sbin/shutdown"
VOLUME "/usr/bin/cloud-init"
VOLUME "/usr/bin/docker"
VOLUME "/usr/bin/docker-containerd"
VOLUME "/usr/bin/docker-containerd-shim"
VOLUME "/usr/bin/docker-containerd-shim.dist"
VOLUME "/usr/bin/docker-containerd.dist"
VOLUME "/usr/bin/docker-runc"
VOLUME "/usr/bin/docker-runc.dist"
VOLUME "/usr/bin/docker.dist"
VOLUME "/usr/bin/dockerlaunch"
VOLUME "/usr/bin/respawn"
VOLUME "/usr/bin/ros"
VOLUME "/usr/bin/switch-console"
VOLUME "/usr/bin/system-docker"
VOLUME "/usr/bin/user-docker"
VOLUME "/usr/sbin/netconf"
VOLUME "/usr/sbin/wait-for-docker"
VOLUME "/usr/share/ros"
VOLUME "/var/lib/docker"
VOLUME "/var/lib/rancher"
VOLUME "/var/lib/rancher/cache"
VOLUME "/var/lib/rancher/conf"
VOLUME "/var/log"
VOLUME "/var/run"

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
