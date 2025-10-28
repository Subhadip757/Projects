<?php
require __DIR__ . '/config/database.php';

// Check if users table exists and has any users
try {
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM users");
    $userCount = $stmt->fetch()['count'];
    
    if ($userCount > 0) {
        die('Setup already completed. Users exist in the system.');
    }
} catch (PDOException $e) {
    // Table doesn't exist, create it
    $createTable = "
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) UNIQUE NOT NULL,
            password_hash VARCHAR(255) NOT NULL,
            email VARCHAR(100),
            full_name VARCHAR(100),
            is_active BOOLEAN DEFAULT TRUE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_username (username),
            INDEX idx_email (email)
        )
    ";
    $pdo->exec($createTable);
}

$error = '';
$success = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username'] ?? '');
    $password = $_POST['password'] ?? '';
    $confirmPassword = $_POST['confirm_password'] ?? '';
    $email = trim($_POST['email'] ?? '');
    
    if (empty($username) || empty($password)) {
        $error = 'Username and password are required.';
    } elseif (strlen($password) < 6) {
        $error = 'Password must be at least 6 characters long.';
    } elseif ($password !== $confirmPassword) {
        $error = 'Passwords do not match.';
    } else {
        try {
            $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
            $stmt = $pdo->prepare("INSERT INTO users (username, password_hash, email, full_name) VALUES (:username, :password_hash, :email, :full_name)");
            $stmt->execute([
                ':username' => $username,
                ':password_hash' => $hashedPassword,
                ':email' => $email,
                ':full_name' => 'Administrator'
            ]);
            
            $success = 'Admin user created successfully! You can now <a href="login.php">login</a>.';
        } catch (PDOException $e) {
            if ($e->getCode() == 23000) {
                $error = 'Username already exists.';
            } else {
                $error = 'Error creating user: ' . $e->getMessage();
            }
        }
    }
}
?>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Setup - Medical Inventory System</title>
  
  <!-- Bootstrap 5 CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
  
  <!-- Custom CSS -->
  <link rel="stylesheet" href="/medicine-expiry-alert/assets/css/style.css">
  
  <style>
    .setup-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .setup-card {
      max-width: 500px;
      width: 100%;
    }
  </style>
</head>
<body>
  <div class="setup-container">
    <div class="setup-card card shadow-lg">
      <div class="card-body p-5">
        <div class="text-center mb-4">
          <i class="bi bi-gear-fill" style="font-size: 3rem; color: #0d6efd;"></i>
          <h2 class="mt-3 mb-1">System Setup</h2>
          <p class="text-muted">Create your admin account</p>
        </div>
        
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
          <form method="post" action="setup.php">
            <div class="mb-3">
              <label for="username" class="form-label">Admin Username</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-person"></i></span>
                <input type="text" class="form-control" id="username" name="username" 
                       value="<?php echo htmlspecialchars($_POST['username'] ?? ''); ?>" required autofocus>
              </div>
            </div>
            
            <div class="mb-3">
              <label for="email" class="form-label">Email (Optional)</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                <input type="email" class="form-control" id="email" name="email" 
                       value="<?php echo htmlspecialchars($_POST['email'] ?? ''); ?>">
              </div>
            </div>
            
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-lock"></i></span>
                <input type="password" class="form-control" id="password" name="password" required>
              </div>
              <div class="form-text">Minimum 6 characters</div>
            </div>
            
            <div class="mb-4">
              <label for="confirm_password" class="form-label">Confirm Password</label>
              <div class="input-group">
                <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                <input type="password" class="form-control" id="confirm_password" name="confirm_password" required>
              </div>
            </div>
            
            <button type="submit" class="btn btn-primary w-100 mb-3">
              <i class="bi bi-check-circle me-2"></i>Create Admin Account
            </button>
          </form>
        <?php endif; ?>
        
        <div class="text-center text-muted small">
          <i class="bi bi-shield-check me-1"></i>This will create your first admin user
        </div>
      </div>
    </div>
  </div>
  
  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>