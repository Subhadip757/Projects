<?php
require __DIR__ . '/config/database.php';
require __DIR__ . '/config/auth.php';

// Load settings if available
if (file_exists(__DIR__ . '/config/settings.php')) {
    require __DIR__ . '/config/settings.php';
    $registrationEnabled = REGISTRATION_ENABLED;
} else {
    $registrationEnabled = true; // Default to enabled if settings file doesn't exist
}

// If already logged in, redirect to dashboard
if (isLoggedIn()) {
    header('Location: index.php');
    exit;
}

$error = '';
$success = '';
$formData = [
    'username' => '',
    'email' => '',
    'full_name' => ''
];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $fullName = trim($_POST['full_name'] ?? '');
    $password = $_POST['password'] ?? '';
    $confirmPassword = $_POST['confirm_password'] ?? '';
    
    // Store form data for repopulation
    $formData = [
        'username' => $username,
        'email' => $email,
        'full_name' => $fullName
    ];
    
    // Validation
    if (empty($username) || empty($password)) {
        $error = 'Username and password are required.';
    } elseif (strlen($username) < 3) {
        $error = 'Username must be at least 3 characters long.';
    } elseif (strlen($password) < 6) {
        $error = 'Password must be at least 6 characters long.';
    } elseif ($password !== $confirmPassword) {
        $error = 'Passwords do not match.';
    } elseif (!empty($email) && !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $error = 'Please enter a valid email address.';
    } else {
        try {
            // Check if username already exists
            $checkStmt = $pdo->prepare("SELECT id FROM users WHERE username = :username");
            $checkStmt->execute([':username' => $username]);
            
            if ($checkStmt->fetch()) {
                $error = 'Username already exists. Please choose a different username.';
            } else {
                // Create new user - check if full_name column exists
                $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
                
                // Check table structure first
                $checkColumns = $pdo->query("DESCRIBE users");
                $columns = $checkColumns->fetchAll(PDO::FETCH_COLUMN);
                
                if (in_array('full_name', $columns)) {
                    // New table structure with full_name
                    $stmt = $pdo->prepare("INSERT INTO users (username, password_hash, email, full_name) VALUES (:username, :password_hash, :email, :full_name)");
                    $stmt->execute([
                        ':username' => $username,
                        ':password_hash' => $hashedPassword,
                        ':email' => $email,
                        ':full_name' => $fullName
                    ]);
                } else {
                    // Old table structure without full_name
                    $stmt = $pdo->prepare("INSERT INTO users (username, password_hash, email) VALUES (:username, :password_hash, :email)");
                    $stmt->execute([
                        ':username' => $username,
                        ':password_hash' => $hashedPassword,
                        ':email' => $email
                    ]);
                }
                
                $success = 'Account created successfully! You can now <a href="login.php" class="alert-link">login</a>.';
                
                // Clear form data on success
                $formData = ['username' => '', 'email' => '', 'full_name' => ''];
            }
        } catch (PDOException $e) {
            $error = 'Error creating account. Please try again.';
        }
    }
}
?>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Sign Up - Medical Inventory System</title>
  
  <!-- Bootstrap 5 CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
  
  <!-- Custom CSS -->
  <link rel="stylesheet" href="/medicine-expiry-alert/assets/css/style.css">
  
  <style>
    .signup-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      position: relative;
      overflow: hidden;
      padding: 20px 0;
    }
    .signup-container::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="white" opacity="0.1"/><circle cx="75" cy="75" r="1" fill="white" opacity="0.1"/><circle cx="50" cy="10" r="0.5" fill="white" opacity="0.1"/><circle cx="10" cy="60" r="0.5" fill="white" opacity="0.1"/><circle cx="90" cy="40" r="0.5" fill="white" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
    }
    .signup-card {
      max-width: 500px;
      width: 100%;
      position: relative;
      z-index: 1;
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.1);
    }
    .signup-card .card-body {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 0.5rem;
    }
    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
    }
    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      padding: 12px;
      font-weight: 500;
    }
    .btn-primary:hover {
      background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
      transform: translateY(-1px);
    }
    .btn-outline-primary {
      color: #667eea;
      border-color: #667eea;
    }
    .btn-outline-primary:hover {
      background-color: #667eea;
      border-color: #667eea;
    }
    .password-strength {
      height: 4px;
      background: #e9ecef;
      border-radius: 2px;
      margin-top: 5px;
      overflow: hidden;
    }
    .password-strength-bar {
      height: 100%;
      width: 0%;
      transition: all 0.3s ease;
      border-radius: 2px;
    }
    .strength-weak { background: #dc3545; width: 25%; }
    .strength-fair { background: #fd7e14; width: 50%; }
    .strength-good { background: #ffc107; width: 75%; }
    .strength-strong { background: #198754; width: 100%; }
  </style>
</head>
<body>
  <div class="signup-container">
    <div class="signup-card card shadow-lg">
      <div class="card-body p-5">
        <div class="text-center mb-4">
          <i class="bi bi-person-plus-fill" style="font-size: 3rem; color: #0d6efd;"></i>
          <h2 class="mt-3 mb-1">Create Account</h2>
          <p class="text-muted">Join the Medical Inventory System</p>
        </div>
        
        <?php if (!$registrationEnabled): ?>
          <div class="alert alert-warning" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i>Registration is currently disabled. Please contact your administrator.
          </div>
          <div class="text-center">
            <a href="login.php" class="btn btn-outline-primary">
              <i class="bi bi-arrow-left me-2"></i>Back to Login
            </a>
          </div>
        <?php else: ?>
          
          <?php if ($error): ?>
            <div class="alert alert-danger" role="alert">
              <i class="bi bi-exclamation-triangle me-2"></i><?php echo htmlspecialchars($error); ?>
            </div>
          <?php endif; ?>
          
          <?php if ($success): ?>
            <div class="alert alert-success" role="alert">
              <i class="bi bi-check-circle me-2"></i><?php echo $success; ?>
            </div>
          <?php else: ?>
            <form method="post" action="signup.php" id="signupForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="username" class="form-label">Username *</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                    <input type="text" class="form-control" id="username" name="username" 
                           value="<?php echo htmlspecialchars($formData['username']); ?>" required autofocus>
                  </div>
                  <div class="form-text">Minimum 3 characters</div>
                </div>
                
                <div class="col-md-6 mb-3">
                  <label for="full_name" class="form-label">Full Name</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-person-badge"></i></span>
                    <input type="text" class="form-control" id="full_name" name="full_name" 
                           value="<?php echo htmlspecialchars($formData['full_name']); ?>">
                  </div>
                </div>
              </div>
              
              <div class="mb-3">
                <label for="email" class="form-label">Email Address</label>
                <div class="input-group">
                  <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                  <input type="email" class="form-control" id="email" name="email" 
                         value="<?php echo htmlspecialchars($formData['email']); ?>">
                </div>
                <div class="form-text">Optional - for password recovery</div>
              </div>
              
              <div class="mb-3">
                <label for="password" class="form-label">Password *</label>
                <div class="input-group">
                  <span class="input-group-text"><i class="bi bi-lock"></i></span>
                  <input type="password" class="form-control" id="password" name="password" required>
                  <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                    <i class="bi bi-eye"></i>
                  </button>
                </div>
                <div class="password-strength">
                  <div class="password-strength-bar" id="strengthBar"></div>
                </div>
                <div class="form-text">Minimum 6 characters</div>
              </div>
              
              <div class="mb-4">
                <label for="confirm_password" class="form-label">Confirm Password *</label>
                <div class="input-group">
                  <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                  <input type="password" class="form-control" id="confirm_password" name="confirm_password" required>
                </div>
              </div>
              
              <button type="submit" class="btn btn-primary w-100 mb-3">
                <i class="bi bi-person-plus me-2"></i>Create Account
              </button>
            </form>
          <?php endif; ?>
          
          <div class="text-center">
            <p class="text-muted mb-2">Already have an account?</p>
            <a href="login.php" class="btn btn-outline-primary">
              <i class="bi bi-box-arrow-in-right me-2"></i>Login Instead
            </a>
          </div>
          
        <?php endif; ?>
      </div>
    </div>
  </div>
  
  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  
  <script>
    // Password visibility toggle
    document.getElementById('togglePassword').addEventListener('click', function() {
      const password = document.getElementById('password');
      const icon = this.querySelector('i');
      
      if (password.type === 'password') {
        password.type = 'text';
        icon.className = 'bi bi-eye-slash';
      } else {
        password.type = 'password';
        icon.className = 'bi bi-eye';
      }
    });
    
    // Password strength indicator
    document.getElementById('password').addEventListener('input', function() {
      const password = this.value;
      const strengthBar = document.getElementById('strengthBar');
      
      let strength = 0;
      if (password.length >= 6) strength++;
      if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
      if (password.match(/\d/)) strength++;
      if (password.match(/[^a-zA-Z\d]/)) strength++;
      
      strengthBar.className = 'password-strength-bar';
      if (strength === 1) strengthBar.classList.add('strength-weak');
      else if (strength === 2) strengthBar.classList.add('strength-fair');
      else if (strength === 3) strengthBar.classList.add('strength-good');
      else if (strength === 4) strengthBar.classList.add('strength-strong');
    });
    
    // Password confirmation validation
    document.getElementById('confirm_password').addEventListener('input', function() {
      const password = document.getElementById('password').value;
      const confirmPassword = this.value;
      
      if (confirmPassword && password !== confirmPassword) {
        this.setCustomValidity('Passwords do not match');
      } else {
        this.setCustomValidity('');
      }
    });
  </script>
</body>
</html>