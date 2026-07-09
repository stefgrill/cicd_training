pipeline {
    agent none

    environment {
        varAvailiableEverywhere = 'Content of the variable'
    }

    stages {
        stage('Parallel Executed Stages') {
            parallel {
                stage('Setup') {
                    agent {
                        label 'generic'
                    }
                    steps {
                        echo 'Starting to sleep in Setup Stage'
                        sleep 15
                        input 'Continue with Setup?'
                        echo 'Hello World from Agent 1'
                    }
                }
                stage('Hello from Grl') {
                    agent {
                        label 'java'
                    }
                    steps {
                        echo 'Starting to sleep in Hello World Stage'
                        sleep 15
                        input 'Continue with Hello World?'
                        echo 'Hello World from Agent 2'
                    }
                }
            }
        }
    }
    
    post {
        
        success {
            echo 'Pipeline Successfully Completed'
        }
        failure {
            echo 'Pipeline failed'
        }
        always {
            echo 'Pipeline Completed from own Fork'
            echo "varAvailiableEverywhere: ${env.varAvailiableEverywhere}"
        }
    }
    
}