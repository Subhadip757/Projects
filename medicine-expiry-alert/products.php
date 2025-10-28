<?php
require __DIR__ . '/config/database.php';
require __DIR__ . '/templates/header.php';

$q = trim($_GET['q'] ?? '');
$status = $_GET['status'] ?? '';
$allowedStatus = ['expiring_30','expired','low_stock','all',''];
if (!in_array($status, $allowedStatus)) { $status = ''; }

$page = max(1, intval($_GET['page'] ?? 1));
$perPage = max(1, min(50, intval($_GET['per_page'] ?? 10)));
$offset = ($page - 1) * $perPage;

$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'add') {
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
        $stmt = $pdo->prepare("INSERT INTO products (product_name, batch_no, expiry_date, quantity, supplier, low_stock_threshold) VALUES (:product_name, :batch_no, :expiry_date, :quantity, :supplier, :low)");
        $stmt->execute([
            ':product_name' => $product_name,
            ':batch_no' => $batch_no,
            ':expiry_date' => $expiry_date,
            ':quantity' => $quantity,
            ':supplier' => $supplier,
            ':low' => $low_stock_threshold
        ]);
        header('Location: products.php?added=1');
        exit;
    }
}

$allowedSorts = ['name_asc','name_desc','expiry_asc','expiry_desc'];
$sort = in_array($_GET['sort'] ?? '', $allowedSorts) ? $_GET['sort'] : 'name_asc';
$orderBy = 'product_name ASC';
if ($sort === 'name_desc') $orderBy = 'product_name DESC';
if ($sort === 'expiry_asc') $orderBy = 'expiry_date ASC';
if ($sort === 'expiry_desc') $orderBy = 'expiry_date DESC';

$where = [];
$params = [];
if ($q !== '') {
    $where[] = '(product_name LIKE :q OR batch_no LIKE :q OR supplier LIKE :q)';
    $params[':q'] = '%' . $q . '%';
}
$today = (new DateTime())->format('Y-m-d');
$thirty = (new DateTime('+30 days'))->format('Y-m-d');
if ($status === 'expired') {
    $where[] = 'expiry_date < :today';
    $params[':today'] = $today;
} elseif ($status === 'expiring_30') {
    $where[] = 'expiry_date BETWEEN :today AND :thirty';
    $params[':today'] = $today;
    $params[':thirty'] = $thirty;
} elseif ($status === 'low_stock') {
    $where[] = 'quantity <= low_stock_threshold';
}
$whereSql = empty($where) ? '' : ('WHERE ' . implode(' AND ', $where));


$countStmt = $pdo->prepare("SELECT COUNT(*) AS cnt FROM products $whereSql");
$countStmt->execute($params);
$totalCount = (int)($countStmt->fetch()['cnt'] ?? 0);
$totalPages = max(1, (int)ceil($totalCount / $perPage));
if ($page > $totalPages) { $page = $totalPages; $offset = ($page - 1) * $perPage; }

$allStmt = $pdo->prepare("SELECT * FROM products $whereSql ORDER BY $orderBy LIMIT :limit OFFSET :offset");
foreach ($params as $k => $v) { $allStmt->bindValue($k, $v); }
$allStmt->bindValue(':limit', $perPage, PDO::PARAM_INT);
$allStmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$allStmt->execute();
$products = $allStmt->fetchAll();
?>

<div class="d-flex justify-content-between align-items-center mb-3">
  <h3>All Products</h3>
  <div>
    <a class="btn btn-outline-secondary btn-sm" href="products.php?sort=name_asc">Sort: Name ↑</a>
    <a class="btn btn-outline-secondary btn-sm" href="products.php?sort=name_desc">Name ↓</a>
    <a class="btn btn-outline-secondary btn-sm" href="products.php?sort=expiry_asc">Expiry ↑</a>
    <a class="btn btn-outline-secondary btn-sm" href="products.php?sort=expiry_desc">Expiry ↓</a>
  </div>
</div>

<?php if (!empty($_GET['added'])): ?>
  <div class="alert alert-success">Product added successfully.</div>
<?php endif; ?>
<?php if (!empty($_GET['updated'])): ?>
  <div class="alert alert-success">Product updated successfully.</div>
<?php endif; ?>
<?php if (!empty($_GET['deleted'])): ?>
  <div class="alert alert-warning">Product deleted.</div>
<?php endif; ?>
<?php if (!empty($_GET['error'])): ?>
  <div class="alert alert-danger">An error occurred: <?php echo htmlspecialchars($_GET['error']); ?></div>
<?php endif; ?>

<?php if (!empty($errors)): ?>
  <div class="alert alert-danger">
    <ul class="mb-0">
      <?php foreach ($errors as $e) echo '<li>' . htmlspecialchars($e) . '</li>'; ?>
    </ul>
  </div>
<?php endif; ?>

<div class="card mb-4 fade-up">
  <div class="card-header d-flex justify-content-between align-items-center">
    <div class="d-flex align-items-center">
      <strong class="me-2">Products</strong>
      <button type="button" class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#addModal"><i class="bi bi-plus-lg"></i> Add</button>
    </div>
    <form class="d-flex" method="get" action="products.php">
      <input type="hidden" name="status" value="<?php echo htmlspecialchars($status); ?>" />
      <input type="hidden" name="sort" value="<?php echo htmlspecialchars($sort); ?>" />
      <input name="q" value="<?php echo htmlspecialchars($q); ?>" type="search" class="form-control form-control-sm me-2" placeholder="Search name, batch, supplier" />
      <select name="status" class="form-select form-select-sm me-2" onchange="this.form.submit()">
        <option value="" <?php if($status==='') echo 'selected'; ?>>All</option>
        <option value="expiring_30" <?php if($status==='expiring_30') echo 'selected'; ?>>Expiring in 30d</option>
        <option value="expired" <?php if($status==='expired') echo 'selected'; ?>>Expired</option>
        <option value="low_stock" <?php if($status==='low_stock') echo 'selected'; ?>>Low stock</option>
      </select>
      <button class="btn btn-outline-secondary btn-sm me-2" type="submit">Filter</button>
    </form>
  </div>
  <div class="card-body">
    <div class="d-flex align-items-center">
      <label class="me-2">Per page</label>
      <select class="form-select form-select-sm" onchange="location.href='products.php?<?php echo http_build_query(['sort'=>$sort,'status'=>$status,'q'=>$q]); ?>&per_page='+this.value" style="width:auto">
        <?php foreach ([10,20,30,50] as $pp): ?>
          <option value="<?php echo $pp; ?>" <?php if($perPage==$pp) echo 'selected'; ?>><?php echo $pp; ?></option>
        <?php endforeach; ?>
      </select>
    </div>
  </div>
</div>

<div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add New Product</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <form method="post" action="products.php">
        <input type="hidden" name="action" value="add">
        <div class="modal-body">
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Product Name</label>
              <input required name="product_name" type="text" class="form-control" />
            </div>
            <div class="col-md-6">
              <label class="form-label">Batch No</label>
              <input required name="batch_no" type="text" class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="form-label">Expiry Date</label>
              <input required name="expiry_date" type="date" class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="form-label">Quantity</label>
              <input required name="quantity" type="number" min="0" class="form-control" />
            </div>
            <div class="col-md-4">
              <label class="form-label">Low Stock Threshold</label>
              <input required name="low_stock_threshold" type="number" min="0" class="form-control" />
            </div>
            <div class="col-md-12">
              <label class="form-label">Supplier</label>
              <input name="supplier" type="text" class="form-control" />
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-success" type="submit" onclick="showLoader(true)">Add Product</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div class="card fade-up">
  <div class="card-body table-wrap">
    <?php if (empty($products)): ?>
      <div class="alert alert-info">No products found. Add your first product above.</div>
    <?php else: ?>
      <table class="table table-bordered table-hover table-sm">
        <thead>
          <tr>
            <th>Name</th>
            <th>Batch</th>
            <th>Expiry</th>
            <th>Qty</th>
            <th>Threshold</th>
            <th>Supplier</th>
            <th>Added</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <?php foreach ($products as $p):
            $isExpired = (new DateTime($p['expiry_date'])) < new DateTime();
            $isLow = $p['quantity'] <= $p['low_stock_threshold'];
          ?>
            <tr class="<?php echo $isExpired ? 'table-danger' : ($isLow ? 'table-warning' : ''); ?>">
              <td><?php echo htmlspecialchars($p['product_name']); ?></td>
              <td><?php echo htmlspecialchars($p['batch_no']); ?></td>
              <td><?php echo htmlspecialchars($p['expiry_date']); ?></td>
              <td><?php echo (int)$p['quantity']; ?></td>
              <td><?php echo (int)$p['low_stock_threshold']; ?></td>
              <td><?php echo htmlspecialchars($p['supplier']); ?></td>
              <td><?php echo htmlspecialchars($p['created_at']); ?></td>
              <td>
                <a class="btn btn-sm btn-primary" href="edit_product.php?id=<?php echo (int)$p['id']; ?>">Edit</a>
                <button class="btn btn-sm btn-danger" onclick="confirmDelete('delete_product.php?id=<?php echo (int)$p['id']; ?>')">Delete</button>
              </td>
            </tr>
          <?php endforeach; ?>
        </tbody>
      </table>
    <?php endif; ?>
    <?php if ($totalPages > 1): ?>
      <nav aria-label="Products pagination" class="mt-3">
        <ul class="pagination pagination-sm mb-0">
          <?php
            $base = 'products.php?sort=' . urlencode($sort) . '&status=' . urlencode($status) . '&q=' . urlencode($q) . '&per_page=' . $perPage . '&page=';
          ?>
          <li class="page-item <?php if($page<=1) echo 'disabled'; ?>"><a class="page-link" href="<?php echo $base . max(1,$page-1); ?>">Prev</a></li>
          <?php for ($i = 1; $i <= $totalPages; $i++): ?>
            <li class="page-item <?php if($i==$page) echo 'active'; ?>"><a class="page-link" href="<?php echo $base . $i; ?>"><?php echo $i; ?></a></li>
          <?php endfor; ?>
          <li class="page-item <?php if($page>=$totalPages) echo 'disabled'; ?>"><a class="page-link" href="<?php echo $base . min($totalPages,$page+1); ?>">Next</a></li>
        </ul>
      </nav>
    <?php endif; ?>
  </div>
</div>

<?php require __DIR__ . '/templates/footer.php'; ?>