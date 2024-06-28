pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/pgvda/ToDo-app.git'
        BRANCH = 'main'
        APP_NAME = 'ToDo-App'
        FRONTEND_IMAGE = 'group21/toodfrontend:latest'
        BACKEND_IMAGE = 'group21/tood:latest'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Clone Repository') {
            steps {
                retry(3) {
                    git branch: "${BRANCH}", url: "${REPO_URL}"
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker-compose build'
                    } else {
                        bat 'docker-compose build'
                    }
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'duckerhubpassword', variable: 'mernapp')]) {
                    script {
                        bat "docker login -u ndissanayake -p ${mernapp}"
                    }
                }
            }
        }

        stage('Add tag to Image Frontend') {
            steps {
                bat 'docker tag devops-frontend:latest vidushs/devop_frontend:latest'
            }
        }

        stage('Add tag to Image Backend') {
            steps {
                bat 'docker tag devops-backend:latest vidushs/devop_backend:latest'
            }
        }

        stage('Push Image Frontend') {
            steps {
                bat 'docker push vidushs/devop_frontend:latest'
            }
        }

        stage('Push Image Backend') {
            steps {
                bat 'docker push vidushs/devop_backend:latest'
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    bat 'docker-compose down'
                    bat 'docker-compose up -d'
                }
            }
        }
    }
}