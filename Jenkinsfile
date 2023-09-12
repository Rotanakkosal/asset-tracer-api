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
                    sh "sudo usermod -aG docker $USER"
                    echo "Building Docker Image"
                    sh "sudo docker build -t asset-tracer-api ."
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
                    sh "sudo docker run -d -p 8090:8080 --name asset-tracer-api asset-tracer-api"
               }
          }
     }
}
