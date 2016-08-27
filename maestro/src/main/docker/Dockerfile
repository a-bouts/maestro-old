FROM java:8-jre

RUN echo "deb http://http.debian.net/debian jessie-backports main" >> /etc/apt/sources.list.d/backports.list

RUN apt-get update && apt-get install -y --no-install-recommends apt-transport-https \
		ca-certificates \
	&& rm -rf /var/lib/apt/lists/*


ENV DOCKER_BUCKET get.docker.com
ENV DOCKER_VERSION 1.11.2
ENV DOCKER_SHA256 05ceec7fd937e1416e5dce12b0b6e1c655907d349d52574319a1e875077ccb79

RUN set -x \
	&& curl -fSL "https://${DOCKER_BUCKET}/builds/Linux/x86_64/docker-${DOCKER_VERSION}.tgz" -o docker.tgz \
	&& echo "${DOCKER_SHA256} *docker.tgz" | sha256sum -c - \
	&& tar -xzvf docker.tgz \
	&& mv docker/* /usr/local/bin/ \
	&& rmdir docker \
	&& rm docker.tgz \
	&& docker -v

RUN curl -L https://github.com/docker/compose/releases/download/1.8.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose \
	&& chmod +x /usr/local/bin/docker-compose

RUN useradd \
  -d /var/lib/maestro \
  -m \
  -U \
  maestro

#USER maestro
WORKDIR /var/lib/maestro

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "maestro.jar"]

ADD target/maestro.jar maestro.jar
