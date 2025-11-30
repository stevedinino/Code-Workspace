<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Retreat Registrants</title>
  <link rel="icon" href="images/favicon.png" type="image/png" />
  <link rel="stylesheet" href="styles/layout.css" />
</head>
<body>
  <div class="guests-wrapper">
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
      <form method="POST" action="clear.php" 
            onsubmit="return document.getElementById('confirmText').value === 'CLEAR';">
        <p>Type <strong>CLEAR</strong> in the box below to confirm:</p>
        <input type="text" id="confirmText" placeholder="Type CLEAR to confirm" />
        <br />
        <button type="submit">Clear All Registrations</button>
      </form>
    </div>
  </div>
</body>
</html>