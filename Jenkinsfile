pipeline {
    agent none

    //Declare environment varibale that are valid everywhere
    environment {
        varAvailiableEverywhere = 'Content of the variable'
    }


    stages {
        //We need to declare an extra stage in which the individual stages are done in parallel.
        stage('Parallel Executed Stages') {
            parallel {
                stage('Setup') {
                    //Declare the agent for this stage. This is a declarative pipeline and we can declare the agent for each stage.
                    agent {
                        label 'generic'
                    }
                    steps {
                        echo 'Starting to sleep in Setup Stage'
                        sleep 15
                        //Input step is used to pause the pipeline and wait for user input before proceeding.
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
                        sh 'cat /etc/os-release || uname -a'
                        echo 'Hello World from Agent 2'
                    }
                }
            }
        }
    }
    
    //Post block is used to define actions that will be executed at the end of the pipeline.
    post {
        //Post block can have multiple conditions like success, failure, always, unstable, changed etc.
        success {
            echo 'Pipeline Successfully Completed'
        }
        failure {
            echo 'Pipeline failed with my own mistakes'
        }
        always {
            echo 'Pipeline Completed with own setup'
            echo "varAvailiableEverywhere: ${env.varAvailiableEverywhere}"
            mail to: 'steffen.grill@vector.com',
                subject: "Jenkins Pipeline ${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Die Pipeline ${env.JOB_NAME} mit Build #${env.BUILD_NUMBER} wurde mit dem Status ${currentBuild.currentResult} beendet."
        }
    }
    
}