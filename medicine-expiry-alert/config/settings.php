<?php
// Application settings

// Registration settings
define('REGISTRATION_ENABLED', true); // Set to false to disable public registration
define('REQUIRE_EMAIL_VERIFICATION', false); // Future feature
define('DEFAULT_USER_ROLE', 'user'); // Default role for new users

// Security settings
define('MIN_PASSWORD_LENGTH', 6);
define('MIN_USERNAME_LENGTH', 3);
define('SESSION_TIMEOUT', 3600); // 1 hour in seconds

// Application settings
define('APP_NAME', 'Medical Inventory System');
define('APP_VERSION', '1.0.0');
?>