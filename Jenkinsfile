def projectName = "templateengine"
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh "chmod +x gradlew"
                sh "echo build"
                sh "./gradlew assemble"
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    app = docker.build("habibullinilya/${projectName}")
                }
            }
        }
        stage('Push Image to Registry') {
            steps {
                script {
                    docker.withRegistry('', 'dockerhub') {
                        app.push("1.0")
                        app.push("latest")
                    }
                }
            }
        }
        stage('deploy to k8s') {
            steps {
                script {

                    sh "kubectl set image deployments/${projectName} ${projectName}=docker.io/habibullinilya/whereis"
                }


            }

        }
    }
}
