def projectName = "templateTextEngine"
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
                    def isExist = sh(script: "kubectl get deployments | grep ${projectName}| wc -l", returnStdout: true)
                    echo "$isExist"
                    if (isExist == 1) {
                        echo "get deployements ${temp}"
                        sh "kubectl run whereis --image=docker.io/habibullinilya/${projectName} --port=8080"
                        sh "kubeсtl get pods"
                        sh "kubectl expose deployments/${projectName}--type=NodePort --port 8080"
                        sh "kubectl describe services/${projectName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${projectName} ${projectName}=docker.io/habibullinilya/whereis"
                    }
                }

            }
        }
    }
}