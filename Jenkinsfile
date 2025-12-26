pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Allure Report') {
            steps {
                allure includeProperties: false,
                       results: [[path: 'target/allure-results']]
            }
        }
    }
}
