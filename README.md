# Project 13 — Jenkins Declarative Pipeline (Jenkinsfile)

This repository demonstrates a Jenkins **Declarative Pipeline** using `Jenkinsfile`. The pipeline:
- checks out the code
- builds with Maven
- runs unit tests
- archives the JAR and test reports
- optionally builds & pushes a Docker image if DockerHub credentials are configured in Jenkins

Repository: https://github.com/Raghavachari93/Jenkins-pipeline.git



# Project 13 – Jenkins Declarative Pipeline (Java + Maven)

Clean step-by-step guide to run this project with Jenkins.

---

## 0. Prerequisites

- Git
- Java (JDK 8+)
- Maven
- Docker (for running Jenkins + optional Docker build)
- GitHub account (or any Git repo)

---

## 1. Get the Project

```bash
git clone https://github.com/Raghavachari93/Jenkins-pipeline.git
cd Jenkins-pipeline
Project structure:

bash
Copy code
Jenkins-pipeline/
├── pom.xml
├── src/
│   ├── main/java/com/example/App.java
│   └── test/java/com/example/AppTest.java
├── Jenkinsfile
└── README.md
2. Test Locally (Optional but Recommended)
From project root:

bash
Copy code
mvn -B clean package
mvn -B test
Confirm:

Build succeeds

Tests pass

target/*.jar exists

3. Run Jenkins in Docker
3.1 Create Jenkins volume
bash
Copy code
docker volume create jenkins_home
3.2 Start Jenkins container
bash
Copy code
docker run -d --name jenkins \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
3.3 Get Jenkins admin password
bash
Copy code
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
3.4 Finish Jenkins setup
Open http://localhost:8080

Paste initial admin password

Click Install suggested plugins

Create admin user

Finish setup

4. Configure Tools in Jenkins
4.1 Maven tool
In Jenkins UI: Manage Jenkins → Tools

Under Maven:

Click Add Maven

Name: Maven3

Select Install automatically (or set a path)

Save

Name must match maven 'Maven3' in Jenkinsfile.

4.2 JDK tool (optional)
Same page (Manage Jenkins → Tools)

Under JDK:

Click Add JDK

Name: Default

Configure install or path as needed

Save

Name should match jdk 'Default' in Jenkinsfile (or remove that line if you don’t want to manage JDK via Jenkins).

5. Configure DockerHub Credentials (Optional)
Required only if you want to run the Docker build & push stage.

In Jenkins: Manage Jenkins → Credentials → System → Global credentials

Click Add Credentials

Kind: Username with password

ID: dockerhub-creds

Username: your Docker Hub username

Password: your Docker Hub password / PAT

Click OK

ID must match DOCKER_CRED = 'dockerhub-creds' in Jenkinsfile.

6. Create Pipeline Job from SCM
In Jenkins, click New Item

Name: jenkinsfile-pipeline (or any name)

Type: Pipeline

Click OK

6.1 Configure SCM
In the job configuration:

Scroll to Pipeline section

Definition: Pipeline script from SCM

SCM: Git

Repository URL:
https://github.com/Raghavachari93/Jenkins-pipeline.git

Branches to build: */main

Script Path: Jenkinsfile

Click Save

7. Run the Pipeline
Open the job you created

Click Build Now

Click the build number (e.g. #1)

Open Console Output to watch pipeline stages:

Checkout

Build (mvn clean package)

Test (mvn test)

Archive artifact

Optional: Docker build & push (if credentials configured)

8. Check Results
8.1 Test Results
Open the build (#1, #2, etc.)

Click Test Result (if shown) to view JUnit test status

8.2 Build Artifact
Open the build

Under Artifacts, confirm target/*.jar is archived

9. (Optional) Verify Docker Image
If Docker stage ran:

Check console log for docker push success

Open your Docker Hub account

Confirm image with tag similar to:

text
Copy code
raghavender5/jenkins-pipeline-demo:<BUILD_ID>
