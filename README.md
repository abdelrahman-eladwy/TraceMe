# TaceMe IAST - Fortify Application

A Java web application designed for **Fortify IAST (Interactive Application Security Testing)** agent integration. This application runs on Apache Tomcat and includes intentional security vulnerabilities for tracing and testing purposes.

## Overview

This project demonstrates how to:

- Integrate Fortify IAST agent with Tomcat
- Trace application security vulnerabilities in real-time
- Test IAST detection capabilities
- Monitor application security posture

## Features

- **User Authentication System** - Login/logout functionality
- **RESTful API** - User management endpoints
- **Database Integration** - H2 in-memory database
- **Security Testing** - Intentional vulnerabilities (SQL Injection, etc.)
- **Fortify IAST Integration** - Agent configuration and tracing

## Prerequisites

- **Java 8+** (JDK 8 or higher)
- **Apache Maven 3.6+**
- **Apache Tomcat 8.5+** or **9.x**
- **Fortify IAST Agent** (fortify-iast-agent.jar)
- **Fortify SSC** (Software Security Center) access

## Project Structure

```
TaceMe/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── taceme/
│       │           ├── model/
│       │           │   └── User.java
│       │           ├── service/
│       │           │   ├── UserService.java
│       │           │   └── AuthService.java
│       │           └── servlet/
│       │               ├── UserServlet.java
│       │               ├── LoginServlet.java
│       │               └── DashboardServlet.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── jsp/
│           │   │   ├── login.jsp
│           │   │   ├── dashboard.jsp
│           │   │   └── error.jsp
│           │   └── web.xml
│           └── index.jsp
├── fortify/
│   ├── fortify-iast.properties
│   └── INTEGRATION_GUIDE.md
├── scripts/
│   ├── start-tomcat-with-fortify.bat (Windows)
│   └── start-tomcat-with-fortify.sh (Linux)
├── pom.xml
└── README.md
```

## Quick Start

### 1. Build the Application

```bash
mvn clean package
```

This creates `target/taceme-iast.war`

### 2. Obtain Fortify IAST Agent

Download the Fortify IAST agent from Micro Focus:

- Contact your Fortify representative
- Download `fortify-iast-agent.jar`
- Place in `<TOMCAT_HOME>/fortify/`

### 3. Configure Fortify

Copy configuration files to Tomcat:

```bash
# Windows
copy fortify\* %TOMCAT_HOME%\fortify\

# Linux
cp fortify/* $TOMCAT_HOME/fortify/
```

Edit `<TOMCAT_HOME>/fortify/fortify-iast.properties`:

- Update `ssc.url` with your SSC server URL
- Update `ssc.token` with your authentication token

### 4. Deploy to Tomcat

```bash
# Windows
copy target\taceme-iast.war %TOMCAT_HOME%\webapps\

# Linux
cp target/taceme-iast.war $TOMCAT_HOME/webapps/
```

### 5. Start Tomcat with Fortify Agent

**Windows:**

```bash
scripts\start-tomcat-with-fortify.bat
```

**Linux:**

```bash
chmod +x scripts/start-tomcat-with-fortify.sh
./scripts/start-tomcat-with-fortify.sh
```

### 6. Access the Application

Open your browser: **http://localhost:8080/taceme-iast/**

**Test Credentials:**

- Username: `admin` / Password: `admin123`
- Username: `user1` / Password: `password`
- Username: `demo` / Password: `demo`

## Manual Tomcat Configuration

If you prefer manual setup instead of using the startup scripts:

### Windows (setenv.bat)

Create `<TOMCAT_HOME>/bin/setenv.bat`:

```batch
@echo off
set FORTIFY_AGENT=C:\apache-tomcat\fortify\fortify-iast-agent.jar
set FORTIFY_CONFIG=C:\apache-tomcat\fortify\fortify-iast.properties

set CATALINA_OPTS=%CATALINA_OPTS% -javaagent:%FORTIFY_AGENT%
set CATALINA_OPTS=%CATALINA_OPTS% -Dfortify.config=%FORTIFY_CONFIG%
set CATALINA_OPTS=%CATALINA_OPTS% -Dfortify.app.name=TaceMe-IAST-App
set CATALINA_OPTS=%CATALINA_OPTS% -Xmx1024m
```

### Linux (setenv.sh)

Create `<TOMCAT_HOME>/bin/setenv.sh`:

```bash
#!/bin/bash
FORTIFY_AGENT=/opt/tomcat/fortify/fortify-iast-agent.jar
FORTIFY_CONFIG=/opt/tomcat/fortify/fortify-iast.properties

export CATALINA_OPTS="$CATALINA_OPTS -javaagent:$FORTIFY_AGENT"
export CATALINA_OPTS="$CATALINA_OPTS -Dfortify.config=$FORTIFY_CONFIG"
export CATALINA_OPTS="$CATALINA_OPTS -Dfortify.app.name=TaceMe-IAST-App"
export CATALINA_OPTS="$CATALINA_OPTS -Xmx1024m"
```

Make it executable:

```bash
chmod +x <TOMCAT_HOME>/bin/setenv.sh
```

## Testing IAST Tracing

### 1. Login to Application

- Navigate to http://localhost:8080/taceme-iast/
- Login with test credentials

### 2. Trigger Vulnerability Tests

- Click **"Load Users"** - Normal database query
- Click **"Test SQL Injection"** - Triggers SQL injection vulnerability

### 3. Check Fortify SSC Dashboard

- Login to your Fortify SSC
- Navigate to your application
- View detected vulnerabilities
- Check trace data and severity

## Application Endpoints

| Endpoint     | Method   | Description                    |
| ------------ | -------- | ------------------------------ |
| `/`          | GET      | Welcome page                   |
| `/login`     | GET/POST | User authentication            |
| `/dashboard` | GET      | Main dashboard (requires auth) |
| `/api/users` | GET      | Get all users or user by ID    |
| `/api/users` | POST     | Create new user                |

## Intentional Vulnerabilities

This application contains **intentional security vulnerabilities** for testing:

1. **SQL Injection** - In `UserService.getUserById()`
2. **SQL Injection** - In `AuthService.authenticate()`
3. **Weak Password Storage** - Plain text passwords
4. **Session Management** - Basic session handling

⚠️ **WARNING:** Do NOT deploy this application in production environments!

## Verification

### Check Agent Loading

View Tomcat logs:

```bash
# Windows
type %TOMCAT_HOME%\logs\catalina.out

# Linux
tail -f $TOMCAT_HOME/logs/catalina.out
```

Look for:

```
Fortify IAST Agent initialized
Agent connected to SSC
Tracing enabled for application: TaceMe-IAST-App
```

### Check Running Processes

```bash
# Windows
jps -v | findstr fortify

# Linux
jps -v | grep fortify
```

## Troubleshooting

### Agent Not Loading

- Verify JAR path in setenv.bat/sh
- Check Java version (must be 8+)
- Ensure proper file permissions
- Check for typos in paths

### Application Not Accessible

- Verify Tomcat is running: `netstat -an | findstr 8080`
- Check application deployed: Look for `taceme-iast` folder in webapps
- Review Tomcat logs for errors

### No Data in Fortify SSC

- Verify SSC URL is correct
- Check authentication token is valid
- Ensure network connectivity to SSC
- Verify agent is loaded (check logs)

### Build Errors

```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean package -DskipTests
```

## Configuration Options

### Fortify Properties

Key settings in `fortify-iast.properties`:

| Property        | Description                                |
| --------------- | ------------------------------------------ |
| `ssc.url`       | Fortify SSC server URL                     |
| `ssc.token`     | Authentication token                       |
| `trace.enabled` | Enable/disable tracing                     |
| `trace.level`   | Logging level (VERBOSE, INFO, WARN, ERROR) |
| `detect.sqli`   | Enable SQL injection detection             |
| `sampling.rate` | Percentage of requests to trace (1-100)    |

## Development

### Build Commands

```bash
# Compile only
mvn compile

# Run tests
mvn test

# Package WAR
mvn package

# Clean build
mvn clean package

# Install to local repo
mvn install
```

### IDE Setup

**Eclipse:**

```bash
mvn eclipse:eclipse
```

**IntelliJ IDEA:**

- File → Open → Select pom.xml
- Import as Maven project

## Security Notes

🔒 **Important Security Considerations:**

1. This application is for **testing purposes only**
2. Contains **intentional vulnerabilities** - DO NOT use in production
3. Default credentials are **hardcoded** - change for any non-test use
4. No encryption on sensitive data
5. Fortify SSC tokens should be **secured** and not committed to version control

## Resources

- **Fortify Documentation:** https://www.microfocus.com/documentation/fortify/
- **Tomcat Documentation:** https://tomcat.apache.org/
- **Maven Documentation:** https://maven.apache.org/

## Support

For issues related to:

- **Application:** Check logs and troubleshooting section
- **Fortify IAST:** Contact Micro Focus support
- **Tomcat:** Refer to Apache Tomcat documentation

## License

This is a sample application for testing purposes.

## Author

TaceMe IAST Project

---

**Happy Testing! 🔐**
#   T r a c e M e  
 #   T r a c e M e  
 