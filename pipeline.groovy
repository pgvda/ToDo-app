pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/pgvda/ToDo-app.git'
        BRANCH = 'main'
        APP_NAME = 'ToDo-App'
        FRONTEND_IMAGE = 'vidushs/devop_frontend:latest'
        BACKEND_IMAGE = 'vidushs/devop_backend:latest'
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credential', usernameVariable: 'pgvidushadilshan@gmail.com', passwordVariable: '19991109@Vidusha#')])  {
                    script {
                        bat "docker login -u ${pgvidushadilshan@gmail.com} -p ${19991109@Vidusha#}"
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
