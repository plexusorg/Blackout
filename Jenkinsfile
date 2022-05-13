pipeline {
    agent any
    stages {
        stage("build") {
            steps {
                withGradle {
                    sh "./gradlew build --no-daemon"
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: "build/libs/*-SNAPSHOT.jar", fingerprint: true
            discordSend description: "Jenkins", link: env.BUILD_URL, result: currentBuild.currentResult, title: JOB_NAME, webhookURL: env.WEBHOOK_URL
            cleanWs()
        }
    }
}