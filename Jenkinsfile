pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/dishadebnath23/Capstone-Project.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Backend build successful'
        }
        failure {
            echo '❌ Backend build failed'
        }
    }
}
