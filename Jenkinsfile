/* Declarative pipeline must be enclosed within a pipeline block */
pipeline {
    agent any

 /**
     * stages contain one or more stage directives
     */
    stages {
        // Checkout Git reporitory
        stage('Checkout Git') {
            steps {
                   git 'https://github.com/Dining-together/Backend.git'
            }
        }

        // Build reporitory using Maven tool
        stage('Build') {
            steps {
                echo "mvn -Dmaven.test.failure.ignore clean package"
            }
        }

        // Unit Test using Junit and archive results for analysis
        stage('Unit Test and Archive Results'){
            steps {
                junit '**/target/surefire-reports/TEST-*.xml'
     		    archive 'target/*.jar'
            }
        }

        // Automation Testing using selenium or SOAPUI
        stage('Automation Test'){
            parallel {
                //Automation Testing using Firefox browser
                stage('Automated Test Firefox'){
                    steps {
                    echo 'Automation Testing using Firefox browser...'
                    }
                }
                //Automation Testing using Chrome browser
                stage('Automated Test Chrome'){
                    steps {
                    echo 'Automation Testing using Chrome browser...'
                    }
                }
            }

        }

        // Code Coverage analysis using Sonarqube scanner
	    stage('Code Coverage analysis with Sonarqube') {
    	    steps {
		    echo 'Code Coverage analysis with Sonarqube...'
		    /**
                script {
                 scannerHome = tool 'SonarScanner';
                }
            withSonarQubeEnv('SonarQube') {
		        bat "${scannerHome}/bin/sonar-runner.bat -e -Dsonar.host.url=${SONAR_URL} -Dsonar.projectName=SimpleMavenProject -Dsonar.sources=. -Dsonar.projectKey=SimpleMavenProject:SimpleMavenProject -Dsonar.java.binaries=${BuildLocation}/TFS_Pipeline_Project/target/test-classes/test"
                } */
            }
        }

        // SonarQube Quality Gate check
	    stage ("SonarQube Quality Gate") {
            steps {
                /*script {
                    def qualitygate = waitForQualityGate()
                    if (qualitygate.status != "OK") {
                        error "Pipeline aborted due to quality gate coverage failure: ${qualitygate.status}"
                    }
                }*/
                echo 'SonarQube Quality Gate check...'
            }
	    }

        // Artifactory configuration details
        stage ('Artifactory configuration') {
            steps {
		    echo 'Artifactory repository...'
                /*
                rtServer (
                    id: "ARTIFACTORY_SERVER",
                    url: SERVER_URL,
                    credentialsId: CREDENTIALS
                ) */
/*
                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: "libs-release-local",
                    snapshotRepo: "libs-snapshot-local"
                )
                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: "libs-release",
                    snapshotRepo: "libs-snapshot"
                ) */
            }
        }

        // Execute Mavan & Publish Build Informations
        stage ('Execute & Publish Build Info') {
            steps {
		    echo 'Execute & Publish Build Info...'
		    /*
                rtMavenRun (
                    tool: M3, // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
                rtPublishBuildInfo (
                    serverId: "ARTIFACTORY_SERVER"
                ) */
            }
        }

        // Security Scanning (dynamic analysis using Burp Suite)
        stage('Security Scanning'){
            steps {
                echo 'Security Scanning (dynamic analysis using Burp Suite)...'
            }
        }

        // Deploy code using Configuration Manager
        stage('Deploy'){
            steps {
                echo 'Deploy code using Configuration Manager...'
            }
        }
    }
}