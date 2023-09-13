#!/usr/bin/env groovy
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
          stage("Deploy") {
               steps {
                    script {
                         def existingContainerId = sh(script: 'docker ps -q -f name="${DOCKER_IMAGE}"', returnStatus: false, returnStdout: true)
                         
                         if (existingContainerId) {
                              echo "Stopping and removing existing container ${existingContainerId}"
                              sh "docker stop ${existingContainerId}"
                              sh "docker rm ${existingContainerId}"
                         } else {
                              echo "No existing container found."
                         }

                         echo "Deploying container..."
                         sh "docker run -d -p 8090:8080 --name \${DOCKER_IMAGE} \${DOCKER_IMAGE}"
                         sh "docker ps | grep \${DOCKER_IMAGE}"
                    }
               }
               }


     }
}
