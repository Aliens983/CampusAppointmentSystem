### Step 1: Update pom.xml

Add the following properties inside the <project> tag. 
This approach avoids long command-line arguments and centralizes configuration.

```
<properties>
    <!-- 1. SonarQube Server URL -->
    <sonar.host.url>http://localhost:9000</sonar.host.url>
    
    <!-- 2. Project Identification (Change these for each project) -->
    <sonar.projectKey>YOUR_PROJECT_KEY</sonar.projectKey>
    <sonar.projectName>Your Project Name</sonar.projectName>
    
    <!-- 3. Security: Load Token from Environment Variable (Never hard-code!) -->
    <!-- suppress UnresolvedMavenProperty -->
    <sonar.token>${env.SONAR_TOKEN}</sonar.token>
    
    <!-- 4. Source & Test Paths (Standard Spring Boot Structure) -->
    <sonar.sources>src/main/java</sonar.sources>
    <sonar.tests>src/test/java</sonar.tests>
    <sonar.java.binaries>target/classes</sonar.java.binaries>
    
    <!-- 5. Coverage & Test Reports (Requires JaCoCo & Surefire plugins) -->
    <sonar.coverage.jacoco.xmlReportPaths>target/site/jacoco/jacoco.xml</sonar.coverage.jacoco.xmlReportPaths>
    <sonar.junit.reportPaths>target/surefire-reports</sonar.junit.reportPaths>
</properties>
```

### Step 2: Set Environment Variable (设置环境变量)

To prevent **sensitive information leakage (敏感信息泄露)**, store your token in an environment variable instead of the code.

#### For PowerShell (Windows):

```
# Temporary (Current Session Only)
$env:SONAR_TOKEN="sqp_your_actual_token_here"

# Permanent (Add to System Environment Variables via GUI)
# Search "Edit the system environment variables" -> Environment Variables -> New User Variable
# Name: SONAR_TOKEN
# Value: sqp_your_actual_token_here
```

## 3. Execution Command (执行命令)

Once configured, run the standard Maven lifecycle. The `verify` phase ensures tests run and reports are generated before analysis.

```
mvn clean verify sonar:sonar
```

