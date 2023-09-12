pipeline {
     agent any
     environment{
          DOCKER_IMAGE = "asset-tracer-api"
     }
     tools{
          maven 'Maven'
     }
     stages{
            stage("Checkout") {
            steps {
                // Checkout the source code from the Git repository
                checkout scm
            }
        }
          stage("Build"){
               steps{
                    echo "Building..."
                    sh "mvn clean install -Dmaven.test.skip=true"
                    
                    echo "Building Docker Image"
                    sh "docker build -t \${DOCKER_IMAGE} ."
               }
          }
          stage("Test"){
               steps{
                    echo " Ot Dg Test Mx Te *_*"
               }
          }
          stage("Deploy"){
               steps{
                    script{
                              sh 'echo "Remove the exist container $(docker ps -a | grep ${DOCKER_IMAGE} | awk "{print $1}")"'
                              sh "docker rm -f \${DOCKER_IMAGE}"
                    }
                    echo "Deploying using Docker run"
                    sh "docker run -d -p 8090:8080 --name \${DOCKER_IMAGE} \${DOCKER_IMAGE}  "
                    sh "docker ps"
                    sh "docker ps | grep \${DOCKER_IMAGE} "
               }
          }
     }
}
