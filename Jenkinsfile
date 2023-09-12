pipeline {
     agent any
     tools{
          maven 'Maven'
     }
     stages{
          stage("Build"){
               steps{
                    echo "Building..."
                    sh "java -version"
                    sh "mvn clean install -Dmaven.test.skip=true"

                    echo "adding $USER to docker group"
                    echo "Building Docker Image"
                    sh "ocker build -t asset-tracer-api ."
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
