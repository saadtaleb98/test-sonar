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

     stage('SonarQube Analysis') {
    def mvn = tool 'sonar-scanner';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=foyer -Dsonar.projectName='foyer'"
    }
  }
     

}