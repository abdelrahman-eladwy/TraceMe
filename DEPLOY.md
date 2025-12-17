# traceMe - RHEL Deployment Guide

## Quick Deploy to RHEL with Tomcat 9 & Java 8

### 1. Build the Application

```bash
mvn clean package
```

This creates: `target/traceMe.war`

### 2. Deploy to Tomcat

```bash
# Copy WAR to Tomcat webapps
sudo cp target/traceMe.war /opt/tomcat/webapps/

# Or if Tomcat is in /usr/share
sudo cp target/traceMe.war /usr/share/tomcat/webapps/
```

### 3. Configure Fortify IAST Agent

#### Create Fortify directory:

```bash
sudo mkdir -p /opt/tomcat/fortify
```

#### Copy configuration:

```bash
sudo cp fortify/fortify-iast.properties /opt/tomcat/fortify/
```

#### Place the Fortify agent JAR:

```bash
# Copy your fortify-iast-agent.jar to:
sudo cp fortify-iast-agent.jar /opt/tomcat/fortify/
```

#### Edit configuration:

```bash
sudo vi /opt/tomcat/fortify/fortify-iast.properties
```

Update these values:

- `ssc.url` - Your Fortify SSC server URL
- `ssc.token` - Your authentication token

### 4. Configure Tomcat to Load Agent

Edit Tomcat setenv.sh:

```bash
sudo vi /opt/tomcat/bin/setenv.sh
```

Add these lines:

```bash
#!/bin/bash
FORTIFY_AGENT=/opt/tomcat/fortify/fortify-iast-agent.jar
FORTIFY_CONFIG=/opt/tomcat/fortify/fortify-iast.properties

export CATALINA_OPTS="$CATALINA_OPTS -javaagent:$FORTIFY_AGENT"
export CATALINA_OPTS="$CATALINA_OPTS -Dfortify.config=$FORTIFY_CONFIG"
export CATALINA_OPTS="$CATALINA_OPTS -Dfortify.app.name=traceMe"
export CATALINA_OPTS="$CATALINA_OPTS -Xmx1024m -Xms512m"
```

Make it executable:

```bash
sudo chmod +x /opt/tomcat/bin/setenv.sh
```

### 5. Restart Tomcat

```bash
# If using systemd:
sudo systemctl restart tomcat

# Or manually:
sudo /opt/tomcat/bin/shutdown.sh
sudo /opt/tomcat/bin/startup.sh
```

### 6. Verify Deployment

Check if app deployed:

```bash
ls -la /opt/tomcat/webapps/traceMe/
```

Check Tomcat logs:

```bash
tail -f /opt/tomcat/logs/catalina.out
```

Look for:

- "Fortify IAST Agent initialized"
- "Agent connected to SSC"
- "Tracing enabled for application: traceMe"

### 7. Access the Application

Open browser: **http://your-server:8080/traceMe/**

Login credentials:

- admin / admin123
- user1 / password
- demo / demo

### 8. Test Tracing

1. Login to the application
2. Click **"Load Users"**
3. Click **"Test SQL Injection"**
4. Check Fortify SSC for detected vulnerabilities

## Troubleshooting

### Check if Tomcat is running:

```bash
sudo systemctl status tomcat
# or
ps aux | grep tomcat
```

### Check if port 8080 is listening:

```bash
sudo netstat -tlnp | grep 8080
```

### View full logs:

```bash
sudo tail -n 100 /opt/tomcat/logs/catalina.out
```

### Verify Java version:

```bash
java -version
# Should show Java 8
```

### Check file permissions:

```bash
sudo chown -R tomcat:tomcat /opt/tomcat/webapps/traceMe*
sudo chown -R tomcat:tomcat /opt/tomcat/fortify/
```

## Application Endpoints

- `/` - Welcome page
- `/login` - Login page
- `/dashboard` - Main dashboard (requires login)
- `/api/users` - REST API (GET all users or GET by id)
- `/api/users` - REST API (POST create user)

## Notes

- Application runs at: `http://localhost:8080/traceMe/`
- Contains intentional vulnerabilities for IAST testing
- **DO NOT** use in production environments
- Agent JAR must be obtained from Micro Focus Fortify
