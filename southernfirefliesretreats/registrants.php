<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Retreat Registrants</title>
  <style>
    body {
      background-color: #fdf6f0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      padding: 40px;
    }
    h2 {
      text-align: center;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
      background-color: white;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    th, td {
      padding: 12px;
      border: 1px solid #ccc;
      text-align: left;
    }
    th {
      background-color: #4CAF50;
      color: white;
    }
    .button-container {
      text-align: center;
      margin-top: 20px;
    }
    button {
      background-color: #d9534f;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      font-size: 16px;
      cursor: pointer;
    }
    button:hover {
      background-color: #c9302c;
    }
  </style>
</head>
<body>
  <h2>Retreat Registrants</h2>

  <?php
  $csvFile = 'registrations.csv';
  if (file_exists($csvFile) && filesize($csvFile) > 0) {
      echo '<table>';
      echo '<tr><th>Name</th><th>Address</th><th>Phone</th><th>Email</th><th>Notes</th><th>Timestamp</th><th>IP</th></tr>';
      if (($handle = fopen($csvFile, 'r')) !== false) {
          while (($data = fgetcsv($handle)) !== false) {
              echo '<tr>';
              foreach ($data as $cell) {
                  echo '<td>' . htmlspecialchars($cell) . '</td>';
              }
              echo '</tr>';
          }
          fclose($handle);
      }
      echo '</table>';
  } else {
      echo '<p style="text-align:center;">No registrations yet.</p>';
  }
  ?>

  <div class="button-container">
    <form method="POST" action="clear.php" onsubmit="return confirm('Are you sure you want to clear all registrations?');">
      <button type="submit">Clear All Registrations</button>
    </form>
  </div>
</body>
</html>