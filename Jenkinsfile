

def projectName = "template_text_engine"
def deploymentServiceName = "templates"
def deploymentDatabaseName = "postgresdb"

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
                    sh "echo docker build"
                    app = docker.build("habibullinilya/${projectName}")
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
                    def ifIsDBPresent = sh(script: "kubectl get deployments | grep ${deploymentDatabaseName}| wc -l| tr -d '\n'",
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
                    def ip = sh(script:" kubectl get service postgres -o jsonpath=\"{.status.loadBalancer.ingress[*].ip}\"",
                                returnStdout:true)

                    def result = sh(script: "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase --url=jdbc:postgresql://${ip}:5432/${deploymentDatabaseName} \
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
                        sh "kubectl apply -f ./k8sconfigs/templates-configmap.yaml"
                        //sh "kubectl run ${deploymentServiceName} --image=docker.io/habibullinilya/${projectName} --port=8080"
                        sh "kubectl apply -f ./k8sconfigs/templates-deployment.yaml"
                        sh "kubectl apply -f ./k8sconfigs/templates-service.yaml"
                        sh "kubectl describe services/${deploymentServiceName}"
                    } else {
                        echo "else"
                        sh "kubectl set image deployments/${deploymentServiceName} ${deploymentServiceName}=docker.io/habibullinilya/${projectName}"
                    }

                    sleep (time: 90, unit: "SECONDS")

                    def pods = sh(script:"kubectl get po -o 'jsonpath={.items[*].metadata.name}'", returnStdout:true)
                    
                    if(areReadyPods(filterPods(pods, deploymentServiceName))){
                        sh "echo ready"
                        sh "seccessful deploy "
                        
                    }else{
                        sh "echo not ready"
                        sh "/home/ilya/Загрузки/liquibase-3.6.3-bin/liquibase \
                         --url=jdbc:postgresql://192.168.99.100:30080/${deploymentDatabaseName}\
                         --driver=org.postgresql.Driver --username=postgres --password=\"postgres\"\
                         --changeLogFile=./src/main/resources/initDb.sql rollbackCount 1"

                        sh "kubectl rollout undo deployments/${deploymentServiceName}"

                        error('error when deploying app')
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
    if(sh(script: "kubectl get po ${podName} -o \'jsonpath={.status.conditions[?(@.type==\"Ready\")].status}\'",
                         returnStdout: true)=="False"){
        print("${podName}  not ready")
        return false
    }else{
        print("${podName} ready")
        return true
    }
}