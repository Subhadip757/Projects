<?php
require __DIR__ . '/config/auth.php';

// Logout user
logoutUser();

// Redirect to login page
header('Location: login.php');
exit;

