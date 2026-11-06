
pipeline {
    agent any

    stages {
        stage('Verificar Java') {
            steps {
                bat 'java -version'
            }
        }

        stage('Compilar y probar') {
            steps {
                withCredentials([
                    string(credentialsId: 'mysql-user', variable: 'DB_USER'),
                    string(credentialsId: 'mysql-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'jwt-secret', variable: 'JWT_SECRET')
                ]) {
                    bat 'gradlew.bat clean build'
                }
            }
        }
    }

    post {
        success {
            echo 'Compilacion y pruebas exitosas'
        }

        failure {
            echo 'La compilacion o las pruebas fallaron'
        }
    }
}
