pipeline {
  agent any

  stages {
    stage("First") {
      steps {
        echo "Hello ${env.BUILD_DISPLAY_NAME} ${env.JOB_NAME} ${env.BUILD_NUMBER}"
      }
    }

   stage("Build") {
     steps {
       checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: '50e470d7-d9c7-4c4b-811b-a0db2109e3de', url: 'https://github.com/jackhclee/scalala']]])
     }
   }
  }
}
