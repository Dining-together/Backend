/* Declarative pipeline must be enclosed within a pipeline block */
/*
     * stages contain one or more stage directives
     */
node {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
        credentialsId: 'docker-hub',
        usernameVariable: 'DOCKER_USER_ID',
        passwordVariable: 'DOCKER_USER_PASSWORD']])
        {
        // Checkout Git reporitory
        stage('Checkout Git') {
                checkout scm
                echo 'Git Checkout Success!'
        }
        // Build reporitory using Maven tool
        stage('Build eureka') {
                def mvnHome = tool 'Maven'
                sh "cd eureka && ${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean compile package"
        }
        stage('Build gateway') {
                def mvnHome = tool 'Maven'
                sh "cd gateway && ${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean compile package"
        }
        stage('Build member') {
                def mvnHome = tool 'Maven'
                sh "cd member && ${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean compile package"
        }
        stage('Build auction') {
                def mvnHome = tool 'Maven'
                sh "cd auction && ${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean compile package"
        }
        stage('Build search') {
                def mvnHome = tool 'Maven'
                sh "cd search && ${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean compile package"
        }

        // Unit Test using Junit and archive results for analysis
        stage('Unit Test'){
                junit '**/target/surefire-reports/TEST-*.xml'
        }
        stage('Build image') {
            //     when {
            //     branch 'main'
            // }steps{

                sh(script: 'docker build -t ${DOCKER_USER_ID}/eureka:${BUILD_NUMBER} eureka')
                sh(script: 'docker build -t ${DOCKER_USER_ID}/gateway:${BUILD_NUMBER} gateway')
                sh(script: 'docker build -t ${DOCKER_USER_ID}/member:${BUILD_NUMBER} member')
                sh(script: 'docker build -t ${DOCKER_USER_ID}/auction:${BUILD_NUMBER} auction')
                sh(script: 'docker build -t ${DOCKER_USER_ID}/search:${BUILD_NUMBER} search')
            // }
        }

        stage('Push') {
            //     when {
            //     branch 'main'
            // }steps{
                sh(script: 'docker login -u ${DOCKER_USER_ID} -p ${DOCKER_USER_PASSWORD}')
                sh(script: 'docker push ${DOCKER_USER_ID}/eureka:${BUILD_NUMBER}')
                sh(script: 'docker push ${DOCKER_USER_ID}/gateway:${BUILD_NUMBER}')
                sh(script: 'docker push ${DOCKER_USER_ID}/member:${BUILD_NUMBER}')
                sh(script: 'docker push ${DOCKER_USER_ID}/auction:${BUILD_NUMBER}')
                sh(script: 'docker push ${DOCKER_USER_ID}/search:${BUILD_NUMBER}')
            // }
             }

        stage('Deploy') {

                sh "docker run -d -p 8761:8761 --network Dining-together\
                        --name eureka ${DOCKER_USER_ID}/eureka:${BUILD_NUMBER}"
                sh "docker run -d --network Dining-together  --name gateway -e \"eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/\" ${DOCKER_USER_ID}/gateway:${BUILD_NUMBER}"
                sh "docker run -d --network Dining-together \
                  --name member \
                -e \"eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/\" \
                ${DOCKER_USER_ID}/member:${BUILD_NUMBER}"
                sh "docker run -d --network Dining-together \
                  --name auction \
                -e \"eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/\" \
                ${DOCKER_USER_ID}/auction:${BUILD_NUMBER}"
                sh "docker run -d --network Dining-together \
                  --name search \
                -e \"eureka.client.serviceUrl.defaultZone=http://eureka:8761/eureka/\" \
                ${DOCKER_USER_ID}/search:${BUILD_NUMBER}"

             }

    }
}