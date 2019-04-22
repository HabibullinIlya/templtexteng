def projectName = "template_text_engine"
def deploymentServiceName = "templatetextengine"
def deploymentDatabaseName = "postgres"
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
                        app.push("1.1")
                        app.push("latest")
                    }
                }
            }
        }
        stage('update database'){
            steps{
                script{
                    def ifIsDBPresent = sh(script: "kubectl get deployments | grep ${deploymentDatabaseName}| wc -l| tr -d '\n'", returnStdout: true)
                    if(ifIsDBPresent == "0"){
                        sh "kubectl create -f ./k8sconfigs/postgres-configmap.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-storage.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-deployment.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-service.yaml"
                    }

                    def result = sh(script: "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase --url=jdbc:postgresql://192.168.99.100:30080/postgres\
                    --driver=org.postgresql.Driver \
                    --username=postgres --password=\"postgres\" \
                    --changeLogFile=./src/main/resources/initDb.sql update" ,returnStdout: true)
                }
                post{
                    failure{
                        script{
                            //rollback
                        }
                    }

                }
            }
        }
        stage('deploy to k8s') {    
            steps {
                script {
                    def isExist = sh(script: "kubectl get deployments | grep ${deploymentServiceName}| wc -l| tr -d '\n'", returnStdout: true)
                    echo "existin = ${isExist}"
                    if (isExist == "0") {
                        echo "get deployements ${projectName}"
                        sh "kubectl run ${deploymentServiceName} --image=docker.io/habibullinilya/${projectName} --port=8080"
                        sh "kubectl expose deployments/${deploymentServiceName} --type=NodePort --port 8080"
                        sh "kubectl describe services/${deploymentServiceName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${deploymentServiceName} ${deploymentServiceName}=docker.io/habibullinilya/${projectName}"
                    }
                }

            }
        }
    }
}