<?php
require __DIR__ . '/config/database.php';

try {
    // Create users table
    $createUsersTable = "
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
    $pdo->exec($createUsersTable);
    
    // Create products table
    $createProductsTable = "
        CREATE TABLE IF NOT EXISTS products (
            id INT AUTO_INCREMENT PRIMARY KEY,
            product_name VARCHAR(255) NOT NULL,
            batch_no VARCHAR(100) NOT NULL,
            expiry_date DATE NOT NULL,
            quantity INT NOT NULL DEFAULT 0,
            low_stock_threshold INT NOT NULL DEFAULT 10,
            supplier VARCHAR(255),
            category VARCHAR(100),
            unit_price DECIMAL(10,2),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            INDEX idx_expiry_date (expiry_date),
            INDEX idx_quantity (quantity),
            INDEX idx_product_name (product_name)
        )
    ";
    $pdo->exec($createProductsTable);
    
    echo "Database tables created successfully!\n";
    echo "Next steps:\n";
    echo "1. Run setup.php to create your admin user\n";
    echo "2. Login and start managing your medical inventory\n";
    
} catch (PDOException $e) {
    echo "Error creating tables: " . $e->getMessage() . "\n";
}
?>