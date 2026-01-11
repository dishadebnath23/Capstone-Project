pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/dishadebnath23/Capstone-Project.git'
            }
        }

        stage('Build & Test Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean verify'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('backend') {
                    sh 'docker build -t corporate-banking-backend:latest .'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Backend build, tests & Docker image created successfully'
        }
    }
}
