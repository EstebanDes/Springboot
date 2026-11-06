
pipeline {
    agent any

    stages {

        stage('Verificar Java') {
            steps {
                echo 'Verificando Java 21...'
                bat 'java -version'
            }
        }

        stage('Verificar Gradle') {
            steps {
                echo 'Verificando Gradle Wrapper...'
                bat 'gradlew.bat --version'
            }
        }

        stage('Verificar credenciales') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'mysql-user',
                        variable: 'DB_USER'
                    ),
                    string(
                        credentialsId: 'mysql-password',
                        variable: 'DB_PASSWORD'
                    ),
                    string(
                        credentialsId: 'jwt-secret',
                        variable: 'JWT_SECRET'
                    )
                ]) {
                    bat '''
                        @echo off

                        if "%DB_USER%"=="root" (
                            echo Usuario MySQL correcto
                        ) else (
                            echo ERROR: Usuario MySQL distinto de root
                            exit /b 1
                        )

                        if "%DB_PASSWORD%"=="" (
                            echo ERROR: DB_PASSWORD vacia
                            exit /b 1
                        )

                        if "%JWT_SECRET%"=="" (
                            echo ERROR: JWT_SECRET vacia
                            exit /b 1
                        )

                        echo Variables configuradas
                    '''
                }
            }
        }

        stage('Compilar y probar') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'mysql-user',
                        variable: 'DB_USER'
                    ),
                    string(
                        credentialsId: 'mysql-password',
                        variable: 'DB_PASSWORD'
                    ),
                    string(
                        credentialsId: 'jwt-secret',
                        variable: 'JWT_SECRET'
                    )
                ]) {
                    echo 'Compilando Spring Boot...'
                    bat 'gradlew.bat clean build'
                }
            }
        }
    }

    post {
        success {
            echo 'BUILD SUCCESSFUL'
            echo 'Compilacion y pruebas exitosas'
        }

        failure {
            echo 'BUILD FAILED'
            echo 'Revisar Console Output y reporte de pruebas'
        }

        always {
            echo 'Pipeline finalizado'
        }
    }
}
