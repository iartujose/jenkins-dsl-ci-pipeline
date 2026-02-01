pipeline {
  agent any

  tools {
    maven 'Maven3'    // must match the name you configured in Jenkins Global Tool Config
  }

  environment {
    // Optional: DockerHub creds id (set in Jenkins credentials store), adjust as needed
    DOCKER_CRED = 'dockerhub-cred'
    DOCKER_IMAGE = "raghavender5/jenkins-pipeline-demo:${env.BUILD_ID}"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        echo "Running mvn clean package..."
        sh 'mvn -B clean package'
      }
    }

    stage('Test') {
      steps {
        echo "Running tests..."
        sh 'mvn -B test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('Optional: Build & Push Docker (if credentials present)') {
      when {
        expression {
          // only run if the Docker credential exists in Jenkins (set to true manually)
          return env.DOCKER_CRED != null && env.DOCKER_CRED != ''
        }
      }
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            sh "docker build -t ${DOCKER_IMAGE} ."
            sh "docker push ${DOCKER_IMAGE}"
          }
        }
      }
    }
  }

  post {
    success {
      echo "Build ${env.BUILD_ID} succeeded."
    }
    failure {
      echo "Build ${env.BUILD_ID} failed."
    }
    always {
      cleanWs()
    }
  }
}

