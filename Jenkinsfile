pipeline {
    agent any
    stages {
        stage('Clean') { steps { sh 'mvn clean' } }
        stage('Install') { steps { sh 'mvn install -Pprod' } }
        stage('PMD') { steps { sh 'mvn pmd:pmd' } }
        stage('JaCoCo') { steps { sh 'mvn jacoco:report' } }
        stage('Javadoc') { steps { sh 'mvn javadoc:javadoc' } }
        stage('Site') { steps { sh 'mvn site' } }
        stage('Build & Push') { steps { script {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'U', passwordVariable: 'P')]) {
                env.DOCKER_IMAGE = sh(script: 'echo -n "${U}/teedy:${BUILD_NUMBER}"', returnStdout: true).trim()
                sh 'podman build -t ${DOCKER_IMAGE} .'
                sh 'echo "${P}" | podman login -u "${U}" --password-stdin docker.io'
                sh 'podman push ${DOCKER_IMAGE}'
                sh 'podman tag ${DOCKER_IMAGE} ${U}/teedy:latest && podman push ${U}/teedy:latest'
                sh 'podman logout docker.io'
            }
        } } }
        stage('Run Containers') { steps { script { withEnv(['JENKINS_NODE_COOKIE=dontKillMe']) {
            sh 'podman rm -f teedy_8084 teedy_8083 teedy_8082 || true'
            sh 'podman run -d -p 127.0.0.1:8084:8080 --name teedy_8084 ${DOCKER_IMAGE}'
            sh 'podman run -d -p 127.0.0.1:8083:8080 --name teedy_8083 ${DOCKER_IMAGE}'
            sh 'podman run -d -p 127.0.0.1:8082:8080 --name teedy_8082 ${DOCKER_IMAGE}'
        } } } }
    }
    post { always {
        archiveArtifacts artifacts: '**/target/site/**/*.*, **/target/**/*.jar, **/target/**/*.war', fingerprint: true
        junit '**/target/surefire-reports/*.xml'
        cleanWs()
    } }
}