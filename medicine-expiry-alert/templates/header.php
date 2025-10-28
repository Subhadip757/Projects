<?php
// templates/header.php
require_once __DIR__ . '/../config/auth.php';

// Require login for all pages using this header
requireLogin();
?>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Medical Inventory System</title>

  <!-- Bootstrap 5 CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">

  <!-- Custom CSS -->
  <link rel="stylesheet" href="/medicine-expiry-alert/assets/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container">
    <a class="navbar-brand d-flex align-items-center" href="/medicine-expiry-alert/index.php"><i class="bi bi-capsule me-2"></i>MedInv</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navCollapse">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navCollapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link<?php if(basename($_SERVER['PHP_SELF'])=='index.php') echo ' active'; ?>" href="/medicine-expiry-alert/index.php">Dashboard</a></li>
        <li class="nav-item"><a class="nav-link<?php if(basename($_SERVER['PHP_SELF'])=='products.php') echo ' active'; ?>" href="/medicine-expiry-alert/products.php">All Products</a></li>
        <?php if (getCurrentUserId() == 1): // Show for first user (admin) ?>
        <?php endif; ?>
      </ul>
      
      <ul class="navbar-nav align-items-lg-center">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-person-circle me-2"></i>
            <span><?php echo htmlspecialchars(getCurrentUsername()); ?></span>
          </a>
          <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
            <li><h6 class="dropdown-header">Signed in as<br><strong><?php echo htmlspecialchars(getCurrentUsername()); ?></strong></h6></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="/medicine-expiry-alert/logout.php"><i class="bi bi-box-arrow-right me-2"></i>Logout</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>

<main class="container my-4">