pipeline {
    agent any
    tools {
        maven 'M2_HOME' 
        jdk 'JAVA_HOME'
    }
    environment {
        SCANNER_HOME = tool 'sonar-scanner'
    }
    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Git') {
            steps {
                checkout scm
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') { // Remplace par le nom de ta configuration SonarQube si nécessaire
                    sh "${SCANNER_HOME}/bin/sonar-scanner -Dsonar.projectKey=foyer -Dsonar.projectName='foyer'"
                }
            }
        }
    }
}
