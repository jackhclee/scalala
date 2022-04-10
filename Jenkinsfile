pipeline {
  agent any

  stages {
    stage("First") {
      steps {
        echo "Hello ${env.JOB_NAME} ${env.BUILD_NUMBER}"
      }
    }
  }
}
