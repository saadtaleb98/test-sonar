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
    }

     

}