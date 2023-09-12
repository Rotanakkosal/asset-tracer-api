pipeline {
     agent any
     tools{
          maven 'maven'
     }
     stages{
          stage("Build"){
               steps{
                    echo "Building..."
                    sh "mvn clean install -Dmaven.test.skip=true"
                    
                    echo "Building Docker Image"
                    sh "docker build -t asset-tracer-api ."
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
                    sh "docker run -d -p 8090:8080 --name asset-tracer-api asset-tracer-api"
               }
          }
     }
}