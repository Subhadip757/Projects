<?php
// edit_product.php - edit product by id
require __DIR__ . '/config/database.php';
require __DIR__ . '/templates/header.php';

$id = isset($_GET['id']) ? intval($_GET['id']) : 0;
if ($id <= 0) {
    echo '<div class="alert alert-danger">Invalid product ID.</div>';
    require __DIR__ . '/templates/footer.php';
    exit;
}

$stmt = $pdo->prepare("SELECT * FROM products WHERE id = :id");
$stmt->execute([':id' => $id]);
$product = $stmt->fetch();

if (!$product) {
    echo '<div class="alert alert-danger">Product not found.</div>';
    require __DIR__ . '/templates/footer.php';
    exit;
}

$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $product_name = trim($_POST['product_name'] ?? '');
    $batch_no = trim($_POST['batch_no'] ?? '');
    $expiry_date = trim($_POST['expiry_date'] ?? '');
    $quantity = intval($_POST['quantity'] ?? 0);
    $supplier = trim($_POST['supplier'] ?? '');
    $low_stock_threshold = intval($_POST['low_stock_threshold'] ?? 0);

    if ($product_name === '') $errors[] = "Product name is required.";
    if ($batch_no === '') $errors[] = "Batch number is required.";
    if ($expiry_date === '') $errors[] = "Expiry date is required.";
    else {
        $d = DateTime::createFromFormat('Y-m-d', $expiry_date);
        if (!$d) $errors[] = "Expiry date must be a valid date (YYYY-MM-DD).";
    }
    if ($quantity < 0) $errors[] = "Quantity cannot be negative.";

    if (empty($errors)) {
        $upd = $pdo->prepare("UPDATE products SET product_name=:product_name, batch_no=:batch_no, expiry_date=:expiry_date, quantity=:quantity, supplier=:supplier, low_stock_threshold=:low WHERE id=:id");
        $upd->execute([
            ':product_name' => $product_name,
            ':batch_no' => $batch_no,
            ':expiry_date' => $expiry_date,
            ':quantity' => $quantity,
            ':supplier' => $supplier,
            ':low' => $low_stock_threshold,
            ':id' => $id
        ]);

        header('Location: products.php?updated=1');
        exit;
    }
}
?>

<h3>Edit Product</h3>

<?php if (!empty($errors)): ?>
  <div class="alert alert-danger">
    <ul class="mb-0">
      <?php foreach ($errors as $e) echo '<li>' . htmlspecialchars($e) . '</li>'; ?>
    </ul>
  </div>
<?php endif; ?>

<div class="card">
  <div class="card-body">
    <form method="post" action="edit_product.php?id=<?php echo $id; ?>" class="row g-3">
      <div class="col-md-6">
        <label class="form-label">Product Name</label>
        <input required name="product_name" value="<?php echo htmlspecialchars($product['product_name']); ?>" type="text" class="form-control" />
      </div>
      <div class="col-md-6">
        <label class="form-label">Batch No</label>
        <input required name="batch_no" value="<?php echo htmlspecialchars($product['batch_no']); ?>" type="text" class="form-control" />
      </div>
      <div class="col-md-4">
        <label class="form-label">Expiry Date</label>
        <input required name="expiry_date" value="<?php echo htmlspecialchars($product['expiry_date']); ?>" type="date" class="form-control" />
      </div>
      <div class="col-md-4">
        <label class="form-label">Quantity</label>
        <input required name="quantity" value="<?php echo (int)$product['quantity']; ?>" type="number" min="0" class="form-control" />
      </div>
      <div class="col-md-4">
        <label class="form-label">Low Stock Threshold</label>
        <input required name="low_stock_threshold" value="<?php echo (int)$product['low_stock_threshold']; ?>" type="number" min="0" class="form-control" />
      </div>
      <div class="col-md-8">
        <label class="form-label">Supplier</label>
        <input name="supplier" value="<?php echo htmlspecialchars($product['supplier']); ?>" type="text" class="form-control" />
      </div>
      <div class="col-md-4 d-flex align-items-end">
        <button class="btn btn-primary w-100" type="submit">Update Product</button>
      </div>
    </form>
  </div>
</div>

<?php require __DIR__ . '/templates/footer.php'; ?>