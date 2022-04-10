pipeline {
  agent any

  stages {
    stage("First") {
      steps {
        echo "Hello ${env.BUILD_DISPLAY_NAME} ${env.JOB_NAME} ${env.BUILD_NUMBER}"
      }
    }
  }
}
