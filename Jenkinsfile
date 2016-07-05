node {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    withMaven {
      dir('maestro') {
        sh 'mvn clean verify'
      }
    }

    dir('maestro') {
        stash name: 'binary', includes: "target/maestro.jar"
    }
    dir('maestro/src/main/docker') {
        stash name: 'dockerfile', includes: 'Dockerfile'
    }
}

node('docker') {
    unstash 'dockerfile'
    unstash 'binary'

    stage 'Building Docker Image'
    image = docker.build("nocloud/maestro:develop")
}

node('docker') {
    stage 'Publishing Docker Image'
    // requirement: local docker registry available on port 5000
    docker.withRegistry('https://no-cloud.fr:5000', '') {
        image.push("develop")
    }
}



// Custom step
def withMaven(def body) {
    def javaHome = tool name: 'jdk-8', type: 'hudson.model.JDK'
    def maven = tool 'maven-3.3.9'

    withEnv(["JAVA_HOME=${javaHome}", "PATH+MAVEN=${maven}/bin"]) {
        body.call()
    }
}
