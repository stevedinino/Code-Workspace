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
          <style>
            body {
              background-color: #fdf6f0;
              font-family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif;
              display: flex;
              justify-content: center;
              align-items: center;
              height: 100vh;
              margin: 0;
            }
            .message {
              background-color: white;
              padding: 30px;
              border-radius: 10px;
              box-shadow: 0 4px 12px rgba(0,0,0,0.2);
              text-align: center;
            }
          </style>
        </head>
        <body>
          <div class="message">
            <h2>Thank you for registering!</h2>
            <p>You’ll be redirected to the home page shortly.</p>
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