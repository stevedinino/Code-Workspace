<?php
$csvFile = 'registrations.csv';
file_put_contents($csvFile, ''); // Overwrite with empty content
header('Location: registrants.php');
exit;
?>