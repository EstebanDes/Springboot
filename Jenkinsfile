
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
                bat 'gradlew.bat clean build'
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
