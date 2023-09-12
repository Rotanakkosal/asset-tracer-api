pipeline {
     agent any
     environment{
          DOCKER_IMAGE = "asset-tracer-api"
     }
     tools{
          maven 'maven'
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
                    echo "Deploying using Docker run"
                    sh "docker run -d -p 8090:8080 --name \${DOCKER_IMAGE} \${DOCKER_IMAGE}  "
                    echo " docker ps"
                    echo "docker ps | grep \${DOCKER_IMAGE} "
               }
          }
     }
}
