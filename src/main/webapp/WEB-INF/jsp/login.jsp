<!DOCTYPE html>
<html>
  <head>
    <title>Login - TaceMe IAST</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
      }
      .login-container {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        width: 300px;
      }
      h2 {
        text-align: center;
        color: #333;
      }
      .form-group {
        margin-bottom: 15px;
      }
      label {
        display: block;
        margin-bottom: 5px;
        color: #555;
      }
      input[type="text"],
      input[type="password"] {
        width: 100%;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
      }
      button {
        width: 100%;
        padding: 10px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
      }
      button:hover {
        background-color: #0056b3;
      }
      .error {
        color: red;
        text-align: center;
        margin-bottom: 10px;
      }
      .info {
        margin-top: 15px;
        padding: 10px;
        background-color: #e7f3ff;
        border-radius: 4px;
        font-size: 12px;
      }
    </style>
  </head>
  <body>
    <div class="login-container">
      <h2>TaceMe IAST Login</h2>
      <% if (request.getAttribute("error") != null) { %>
      <div class="error"><%= request.getAttribute("error") %></div>
      <% } %>
      <form method="post" action="login">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" id="username" name="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" name="password" required />
        </div>
        <button type="submit">Login</button>
      </form>
      <div class="info">
        <strong>Test Credentials:</strong><br />
        admin / admin123<br />
        user1 / password<br />
        demo / demo
      </div>
    </div>
  </body>
</html>
