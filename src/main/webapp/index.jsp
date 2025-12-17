<!DOCTYPE html>
<html>
  <head>
    <title>TaceMe IAST Application</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
      }
      .welcome-container {
        background-color: white;
        padding: 50px;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
        text-align: center;
      }
      h1 {
        color: #333;
        margin-bottom: 20px;
      }
      p {
        color: #666;
        margin-bottom: 30px;
      }
      a {
        display: inline-block;
        padding: 12px 30px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        transition: background-color 0.3s;
      }
      a:hover {
        background-color: #0056b3;
      }
      .info {
        margin-top: 30px;
        padding: 20px;
        background-color: #f8f9fa;
        border-radius: 5px;
        text-align: left;
      }
      .info h3 {
        margin-top: 0;
        color: #007bff;
      }
      .info ul {
        margin: 10px 0;
        padding-left: 20px;
      }
    </style>
  </head>
  <body>
    <div class="welcome-container">
      <h1>Welcome to TaceMe IAST</h1>
      <p>Interactive Application Security Testing with Fortify</p>
      <a href="login">Get Started</a>

      <div class="info">
        <h3>About This Application</h3>
        <ul>
          <li>Built for Fortify IAST agent testing</li>
          <li>Contains intentional vulnerabilities for tracing</li>
          <li>SQL Injection testing endpoints</li>
          <li>User authentication flow</li>
          <li>RESTful API endpoints</li>
        </ul>
      </div>
    </div>
  </body>
</html>
