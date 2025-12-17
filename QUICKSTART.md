# Quick Setup Guide - TaceMe IAST

## Step-by-Step Setup (5 Minutes)

### 1. Build the Application

```bash
mvn clean package
```

✅ Creates: `target/taceme-iast.war`

### 2. Get Fortify IAST Agent

- Download `fortify-iast-agent.jar` from Micro Focus
- Place in: `C:\apache-tomcat\fortify\` (create folder if needed)

### 3. Copy Configuration

```bash
copy fortify\fortify-iast.properties C:\apache-tomcat\fortify\
```

### 4. Edit Configuration

Open: `C:\apache-tomcat\fortify\fortify-iast.properties`

Update these lines:

```properties
ssc.url=http://your-ssc-server:8080/ssc
ssc.token=your-token-here
```

### 5. Deploy Application

```bash
copy target\taceme-iast.war C:\apache-tomcat\webapps\
```

### 6. Configure Tomcat

Edit the startup script: `scripts\start-tomcat-with-fortify.bat`

Update line 9:

```batch
set TOMCAT_HOME=C:\your-tomcat-path
```

### 7. Start Tomcat

```bash
scripts\start-tomcat-with-fortify.bat
```

### 8. Test the Application

Open browser: `http://localhost:8080/taceme-iast/`

Login with:

- Username: `admin`
- Password: `admin123`

### 9. Verify Tracing

Check logs: `C:\apache-tomcat\logs\catalina.out`

Look for: "Fortify IAST Agent initialized"

### 10. View Results

- Login to Fortify SSC
- Navigate to application: "TaceMe-IAST-App"
- Click dashboard to see vulnerabilities

## Quick Test

Click these buttons in the application:

1. **Load Users** - Tests normal operation
2. **Test SQL Injection** - Triggers vulnerability for Fortify to detect

## Need Help?

- Application not starting? Check `README.md` Troubleshooting section
- Agent not loading? Verify paths in startup script
- No data in SSC? Check SSC URL and token in properties file

## Files You Need

✅ `fortify-iast-agent.jar` - The IAST agent  
✅ `fortify-iast.properties` - Configuration  
✅ `taceme-iast.war` - Your application  
✅ Valid SSC credentials

That's it! 🚀
