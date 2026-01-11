pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven'
    }

    stages {

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('backend') {
                    sh 'docker build -t corporate-banking-backend:latest .'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build, Test & Docker image creation successful'
        }
        failure {
            echo '❌ Pipeline failed'
        }
    }
}
