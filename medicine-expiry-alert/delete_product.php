<?php
require __DIR__ . '/config/database.php';

$id = isset($_GET['id']) ? intval($_GET['id']) : 0;
if ($id <= 0) {
    header('Location: products.php?error=invalid_id');
    exit;
}

$del = $pdo->prepare("DELETE FROM products WHERE id = :id");
$del->execute([':id' => $id]);

header('Location: products.php?deleted=1');
exit;