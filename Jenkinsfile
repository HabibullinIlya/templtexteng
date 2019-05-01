

def projectName = "template_text_engine"
def deploymentServiceName = "templates"
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
                        app.push("5.0")
                        app.push("latest")
                    }
                }
            }

        }
        stage('update database'){
            steps{
                script{
                    def ifIsDBPresent = sh(script: "kubectl get deployments | grep ${deploymentDatabaseName}| wc -l| tr -d '\n'",
                             returnStdout: true)
                    if(ifIsDBPresent == "0"){
                        sh "kubectl create -f ./k8sconfigs/postgres-configmap.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-storage.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-deployment.yaml"
                        sh "kubectl create -f ./k8sconfigs/postgres-service.yaml"
                    }

                    def result = sh(script: "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase --url=jdbc:postgresql://192.168.99.100:30080/postgresdb\
                    --driver=org.postgresql.Driver \
                    --username=postgres --password=\"postgres\" \
                    --changeLogFile=./src/main/resources/initDb.sql update" ,returnStdout: true)
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
                        sh "kubectl create -f ./k8sconfigs/templates-configmap.yaml"
                        //sh "kubectl run ${deploymentServiceName} --image=docker.io/habibullinilya/${projectName} --port=8080"
                        sh "kubectl create -f ./k8sconfigs/templates-deployment.yaml"
                        sh "kubectl expose deployments/${deploymentServiceName} --type=NodePort --port 8080"
                        sh "kubectl describe services/${deploymentServiceName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${deploymentServiceName} ${deploymentServiceName}=docker.io/habibullinilya/${projectName}"
                    }

                    sleep (time: 60, unit: "SECONDS")

                    def pods = sh(script:"kubectl get po -o 'jsonpath={.items[*].metadata.name}'", returnStdout:true)
                    if(areReadyPods(filterPods(pods, deploymentServiceName)){
                        sh "echo ready"
                    }else{
                        sh "echo not ready"
                    }
                }
            }
        }
    }
}

def filterPods(String filteredString, filter){
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
    if(sh(script: "kubectl get po ${podName} -o 'jsonpath={.status.conditions[?(@.type==\"Ready\")].status}'",
                         returnStdout: true)=="false"){
        return false
    }else{
        return true
    }
}