def projectName = "template_text_engine"
def deploymentName = "templatetextengine"
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
        stage('update databae'){
            steps{
                script{
                    def result = sh(script: "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase --url=jdbc:postgresql://localhost:5432/testliqui2 \
                        --driver=org.postgresql.Driver \
                        --username=postgres --password=\"postgres\" \
                        --changeLogFile=./src/main/resources/initDb.sql update" ,returnStdout: true)
                }
            }
        }
        stage('deploy to k8s') {    
            steps {
                script {
                    def isExist = sh(script: "kubectl get deployments | grep ${deploymentName}| wc -l| tr -d '\n'", returnStdout: true)
                    echo "existin = ${isExist}"
                    if (isExist == "0") {
                        echo "get deployements ${projectName}"
                        sh "kubectl run ${deploymentName} --image=docker.io/habibullinilya/${projectName} --port=8080"
                        sh "kubectl expose deployments/${deploymentName} --type=NodePort --port 8080"
                        sh "kubectl describe services/${deploymentName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${deploymentName} ${deploymentName}=docker.io/habibullinilya/${projectName}"
                    }
                }

            }
        }
    }
}