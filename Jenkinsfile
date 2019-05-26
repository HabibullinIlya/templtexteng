def imageName = "template_text_engine"
def microserviceName = "templates_microservice"
def databaseName = "templates_database"
def externalDBIp

pipeline {
    agent any
    environment {
        DATABASE_NAME = "${databaseName}"
        MICROSERVICE_NAME = "${microserviceName}"
        IMAGE_NAME = "${imageName}"
    }
    stages {
        stage('Build') {
            steps {
                sh 'printenv'
                sh "chmod +x gradlew"
                sh "echo build"
                sh "./gradlew assemble"
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh "echo docker build"
                    app = docker.build("habibullinilya/${imageName}")
                }
            }
        }
        stage('Push Image to Registry') {
            steps {
                script {
                    sh "echo push image"
                    docker.withRegistry('', 'dockerhub') {
                        app.push("6.0")
                        app.push("latest")
                    }
                    
                }
            }
        }
        stage('update database'){
            steps{
                script{
                    def ifIsDBPresent = sh(script: "kubectl get deployments | grep ${databaseName}| wc -l| tr -d '\n'",
                             returnStdout: true)
                    if(ifIsDBPresent == "0"){
                        sh "kubectl apply -f ./k8sconfigs/postgres-configmap.yaml"
                        sh "kubectl apply -f ./k8sconfigs/postgres-storage.yaml"
                        sh "kubectl apply -f ./k8sconfigs/postgres-deployment.yaml"
                        sh "kubectl apply -f ./k8sconfigs/postgres-service.yaml"

                        sleep(time:90, unit: "SECONDS")
                    }
                    

                    // if(!areReadyPods(filterPods(pods, deploymentDatabaseName))){
                    //     error('error when create database')
                        
                    // }

                    externalDBIp = sh(script: " kubectl get service postgres -o jsonpath=\"{.status.loadBalancer.ingress[*].ip}\"",
                            returnStdout: true)

                    def result = sh(script: "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase --url=jdbc:postgresql://${externalDBIp}:5432/${databaseName} \
                    --driver=org.postgresql.Driver \
                    --username=postgres --password=\"postgres\" \
                    --changeLogFile=./src/main/resources/initDb.sql update" ,returnStdout: true)
                }
                
            }
        }
        stage('deploy to k8s') {  
            steps {
                script {
                    def isExist = sh(script: "kubectl get deployments | grep ${microserviceName}| wc -l| tr -d '\n'", returnStdout: true)
                    echo "existin = ${isExist}"
                    if (isExist == "0") {
                        echo "get deployements ${imageName}"
                        sh "kubectl apply -f ./k8sconfigs/templates-configmap.yaml"
                        sh "kubectl apply -f ./k8sconfigs/templates-deployment.yaml"
                        sh "kubectl apply -f ./k8sconfigs/templates-service.yaml"
                        sh "kubectl describe services/${microserviceName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${microserviceName} ${microserviceName}=docker.io/habibullinilya/${imageName}"
                    }

                    sleep (time: 90, unit: "SECONDS")

                    def pods = sh(script:"kubectl get po -o 'jsonpath={.items[*].metadata.name}'", returnStdout:true)
                    
                    if(areReadyPods(filterPods(pods, microserviceName))){
                        sh "echo ready"
                        sh "successful deploy "
                        
                    }else{
                        sh "echo not ready"
                        sh "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase \
                         --url=jdbc:postgresql://${externalDBIp}:5432/${databaseName}\
                         --driver=org.postgresql.Driver --username=postgres --password=\"postgres\"\
                         --changeLogFile=./src/main/resources/initDb.sql rollbackCount 1"

                        sh "kubectl rollout undo deployments/${microserviceName}"

                        error('error when deploying app')
                    }
                }
            }
        }
    }
}

def filterPods(String filteredString, String filter){
    def arr = filteredString.split()
    def filteredPodsList = []
    for(s in arr){
        if(s.contains(filter)){
            filteredPodsList.add(s)
        }
    }
    return filteredPodsList
}
def areReadyPods(arr){
    for(a in arr){
        if(!isReadyPod(a)){
            return false
        }
    }
    return true
}

def isReadyPod(podName){
    if(sh(script: "kubectl get po ${podName} -o \'jsonpath={.status.conditions[?(@.type==\"Ready\")].status}\'",
                         returnStdout: true)=="False"){
        print("${podName}  not ready")
        return false
    }else{
        print("${podName} ready")
        return true
    }
}