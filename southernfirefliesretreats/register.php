<?php
$csvFile = 'registrations.csv';

// Collect form data
$name = isset($_POST['name']) ? trim($_POST['name']) : '';
$address = isset($_POST['address']) ? trim($_POST['address']) : '';
$phone = isset($_POST['phone']) ? trim($_POST['phone']) : '';
$email = isset($_POST['email']) ? trim($_POST['email']) : '';
$message = isset($_POST['message']) ? trim($_POST['message']) : '';
$timestamp = date('Y-m-d H:i:s');
$ip = $_SERVER['REMOTE_ADDR'] ?? 'unknown';

// Validate required fields
if ($name && $address && $phone && $email) {
    $row = [$name, $address, $phone, $email, $message, $timestamp, $ip];
    $file = fopen($csvFile, 'a');
    if ($file) {
        fputcsv($file, $row);
        fclose($file);
        echo '<!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8">
          <meta http-equiv="refresh" content="2;url=index.html">
          <title>Registration Successful</title>
          <link rel="stylesheet" href="styles/layout.css" />
        </head>
        <body>
          <div class="message-container">
            <div class="message">
              <h2>Thank you for registering!</h2>
              <p>You’ll be redirected to the home page shortly.</p>
            </div>
          </div>
        </body>
        </html>';
    } else {
        echo "<h2>Error: Unable to write to file.</h2>";
    }
} else {
    echo "<h2>Error: All fields except 'Special Request' are required.</h2>";
}
?>