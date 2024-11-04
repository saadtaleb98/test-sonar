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
                sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.url=http://192.168.137.231:9000/ -Dsonar.login=squ_10014107f6e70d46c8bb2ede1d22862603aebd47 -Dsonar.projectName=foyer \
                -Dsonar.projectKey=foyer '''
                
            }
        }
    }
}
