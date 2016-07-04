node {
    stage 'Checkout'
    checkout scm

    stage 'Build'
    withJava {
      dir('maestro') {
        sh 'bash mvn install'
      }
    }

    dir('maestro/target') {
        archive "maestro.jar"
    }

    stash name: 'binary', includes: "maestro/target/maestro.jar"
    dir('maestro/src/main/docker') {
        stash name: 'dockerfile', includes: 'Dockerfile'
    }
}

node('docker') {
    unstash 'dockerfile'
    unstash 'binary'

    stage 'Building Docker Image'
    image = docker.build("nocloud/maestro:${env.BRANCH_NAME}")
}

node('docker') {
    stage 'Publishing Docker Image'
    // requirement: local docker registry available on port 5000
    //docker.withRegistry('http://vps242130.ovh.net:5000', '') {
    //    image.push("${env.BRANCH_NAME}")
    //}
}



// Custom step
def withJava(def body) {
    def javaHome = tool name: 'jdk-8', type: 'hudson.model.JDK'

    withEnv(["JAVA_HOME=${javaHome}"]) {
        body.call()
    }
}
