<!DOCTYPE html>
<html>
  <head>
    <title>Dashboard - TaceMe IAST</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background-color: #f4f4f4;
      }
      .header {
        background-color: #007bff;
        color: white;
        padding: 15px 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .container {
        max-width: 1200px;
        margin: 30px auto;
        padding: 0 20px;
      }
      .card {
        background-color: white;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      }
      h1,
      h2 {
        color: #333;
      }
      .user-list {
        margin-top: 20px;
      }
      table {
        width: 100%;
        border-collapse: collapse;
      }
      th,
      td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid #ddd;
      }
      th {
        background-color: #007bff;
        color: white;
      }
      .btn {
        padding: 8px 16px;
        background-color: #28a745;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        margin-right: 10px;
      }
      .btn:hover {
        background-color: #218838;
      }
      .logout {
        background-color: #dc3545;
      }
      .logout:hover {
        background-color: #c82333;
      }
    </style>
  </head>
  <body>
    <div class="header">
      <h1>TaceMe IAST Dashboard</h1>
      <div>
        Welcome, <%= session.getAttribute("username") %>
        <button class="btn logout" onclick="window.location.href='logout'">
          Logout
        </button>
      </div>
    </div>
    <div class="container">
      <div class="card">
        <h2>Application Status</h2>
        <p>
          <strong>Fortify IAST Agent:</strong>
          <span style="color: green">Active</span>
        </p>
        <p>
          This application is being traced by Fortify IAST for security
          analysis.
        </p>
      </div>

      <div class="card">
        <h2>User Management</h2>
        <button class="btn" onclick="loadUsers()">Load Users</button>
        <button class="btn" onclick="testSQLInjection()">
          Test SQL Injection
        </button>

        <div id="users" class="user-list"></div>
      </div>
    </div>

    <script>
      function loadUsers() {
        fetch("/taceme-iast/api/users")
          .then((response) => response.json())
          .then((data) => {
            displayUsers(data);
          })
          .catch((error) => console.error("Error:", error));
      }

      function displayUsers(users) {
        let html =
          "<table><tr><th>ID</th><th>Username</th><th>Email</th><th>Created At</th></tr>";
        users.forEach((user) => {
          html += `<tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.createdAt}</td>
                </tr>`;
        });
        html += "</table>";
        document.getElementById("users").innerHTML = html;
      }

      function testSQLInjection() {
        // This will trigger SQL injection vulnerability for IAST to detect
        fetch("/taceme-iast/api/users?id=1 OR 1=1")
          .then((response) => response.json())
          .then((data) => {
            alert("SQL Injection test executed. Check Fortify IAST results.");
            console.log(data);
          })
          .catch((error) => console.error("Error:", error));
      }
    </script>
  </body>
</html>
