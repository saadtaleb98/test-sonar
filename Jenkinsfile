pipeline {
    agent any
    tools {
        maven 'M2_HOME' //
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
                //echo 'Pulling from Git repository...'
                //git branch: 'main', url: 'https://github.com/saadtaleb98/dorm-application.git'
                //echo 'Checkout finished, starting Maven...'
                checkout scm
            }
        }


        stage('SonarQube Analysis') {
                            steps {
                                withSonarQubeEnv('sonar-server') {
                                    sh '''
                                        mvn clean verify sonar:sonar \
                                        -Dsonar.projectKey=dorm \
                                        -Dsonar.projectName='dorm'
                                    '''
                                }
                            }
                        }


       stage("quality gate") {
                    steps {
                        script {
                            waitForQualityGate abortPipeline: false, credentialsId: 'Sonar-token'
                        }
                    }
        }
        stage('Maven Clean and Compile') {
                    steps {
                        sh 'mvn clean install'
                    }
        }

        stage('Maven Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Publish Report') {
                    steps {
                        // Publier le rapport de couverture JaCoCo
                        jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/test/**'
                    }
                }
         stage('OWASP FS SCAN') {
                    steps {
                        dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-Check'
                        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                    }
         }
         stage('Test') {
               steps {
                 echo 'Testing...'
                 sh 'chmod +x mvnw'
                 snykSecurity(
                   snykInstallation: 'Snyk',
                   snykTokenId: 'organization-snyk-api-token',
                   additionalArguments: '--debug',
                   failOnIssues: false
                 )
               }
             }







        stage('Deploy to Nexus') {
            steps {
                // Déployer l'artefact sur Nexus, en sautant les tests
                sh 'mvn deploy -DskipTests'
            }
        }

         stage('TRIVY FS SCAN') {
                    steps {
                        sh "trivy fs . > trivyfs.txt"
                    }
         }
        stage('Docker Build & Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker', toolName: 'docker') {
                        sh 'docker build -t dorm .'
                        sh 'docker tag dorm saadtaleb25314/dorm'
                        sh 'docker push saadtaleb25314/dorm'
                    }
                }
            }
        }
        stage("TRIVY") {
            steps {
                sh "trivy --scanners vuln --format table image saadtaleb25314/dorm  > trivyimage.html"
            }
        }
        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // Construire et démarrer les services avec Docker Compose
                    sh 'docker-compose up -d'
                }
            }
        }
    }
    post {
    always {
        script {
            def jobName = env.JOB_NAME
            def buildNumber = env.BUILD_NUMBER
            def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
            def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

            def body = """
                <html>
                <body>
                <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                <h2>${jobName} - Build ${buildNumber}</h2>
                <div style="background-color: ${bannerColor}; padding: 10px;">
                <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                </div>
                <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                </div>
                </body>
                </html>
            """

            emailext (
                subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                body: body,
                to: 'saadeddinetaleb98@gmail.com',
                from: 'saadeddinetaleb98@gmail.comm',
                replyTo: 'saadeddinetaleb98@gmail.com',
                mimeType: 'text/html',
                attachmentsPattern: 'trivyimage.html, trivyfs.txt'
            )
        }
    }
}
}