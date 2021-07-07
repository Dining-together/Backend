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
                   checkout scm
                   echo 'Git Checkout Success!'
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
            }
        }
    }
}