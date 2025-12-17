# Stack Trace Enabled - traceMe Application

## What's Included

The application now has **built-in stack tracing** without requiring any Fortify configuration files:

### 1. **StackTraceFilter** (Applied to all requests)

- Logs every HTTP request with full stack trace
- Captures and logs all exceptions with complete stack traces
- Prints execution paths for IAST to analyze

### 2. **TraceContextListener** (Application startup)

- Logs JVM and system information
- Enables verbose logging on application start
- Confirms stack tracing is active

### 3. **MethodTracer** (Service layer tracing)

- Traces method entry/exit in services
- Logs method parameters and return values
- Tracks execution depth and thread information
- Automatically prints stack traces at key points

### 4. **Enhanced Services**

- `UserService.getUserById()` - Prints SQL query + stack trace
- `AuthService.authenticate()` - Prints auth SQL + stack trace
- All methods log entry, exit, and exceptions

## How It Works

When you deploy and run the application:

1. **On Startup** - Console shows:

```
========================================
  traceMe Application Starting
  Stack Tracing: ENABLED
  IAST Ready: YES
========================================
```

2. **On Every Request** - Console logs:

```
=== Request Trace ===
URI: /traceMe/api/users
Method: GET
Query: id=1
Stack Trace:
  at java.lang.Thread.getStackTrace(...)
  at com.traceme.filter.StackTraceFilter.logRequest(...)
  ...
```

3. **On SQL Execution** - Console shows:

```
>>> ENTER: UserService.getUserById(1)
[TRACE] Executing SQL: SELECT * FROM users WHERE id = 1
[TRACE] Stack at query execution:
java.lang.Thread.dumpStack(...)
  at com.traceme.service.UserService.getUserById(...)
  ...
<<< EXIT: UserService.getUserById => User@12345
```

4. **On Exceptions** - Full stack traces printed to stderr

## Deploy to RHEL

```bash
# 1. Build
mvn clean package

# 2. Deploy
sudo cp target/traceMe.war /opt/tomcat/webapps/

# 3. Start Tomcat
sudo systemctl restart tomcat

# 4. Watch logs (you'll see all stack traces)
sudo tail -f /opt/tomcat/logs/catalina.out
```

## Verify Stack Tracing

After deployment, check the logs:

```bash
sudo tail -f /opt/tomcat/logs/catalina.out | grep -E "TRACE|Stack|ENTER|EXIT"
```

You should see:

- ✅ "Stack Tracing: ENABLED"
- ✅ Request traces for every HTTP call
- ✅ Method entry/exit logs
- ✅ SQL query execution with stack traces
- ✅ Full exception stack traces

## Test It

1. Access: `http://your-server:8080/traceMe/`
2. Login with: `admin` / `admin123`
3. Click "Load Users" button
4. Click "Test SQL Injection" button
5. Watch the terminal/logs - you'll see detailed stack traces for every operation

## What Fortify IAST Will See

The agent can now trace:

- ✅ All HTTP requests and their execution paths
- ✅ SQL queries and their call stack (including SQL injection attempts)
- ✅ Method calls with parameters and return values
- ✅ Exception propagation paths
- ✅ Thread execution context
- ✅ Complete stack traces at every critical point

## No Configuration Needed

Everything is **built into the application code**:

- No `fortify-iast.properties` needed
- No `setenv.sh` modifications required
- Just deploy and the app is fully traceable
- Fortify agent (if present) will automatically capture all traces

## For Production

⚠️ **Important**: This verbose logging is for IAST testing only!

To disable for production, remove or comment out:

- `StackTraceFilter.java`
- `MethodTracer` calls in services
- Set development mode to false in `web.xml`
