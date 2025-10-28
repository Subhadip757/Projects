<?php
require __DIR__ . '/config/database.php';
require __DIR__ . '/templates/header.php';

$totalStmt = $pdo->query("SELECT COUNT(*) AS cnt FROM products");
$total = $totalStmt->fetch()['cnt'] ?? 0;

$lowStmt = $pdo->query("SELECT COUNT(*) AS cnt FROM products WHERE quantity <= low_stock_threshold");
$lowCount = $lowStmt->fetch()['cnt'] ?? 0;

$today = (new DateTime())->format('Y-m-d');
$thirty = (new DateTime('+30 days'))->format('Y-m-d');

$expiringStmt = $pdo->prepare("SELECT * FROM products WHERE expiry_date BETWEEN :today AND :thirty ORDER BY expiry_date ASC");
$expiringStmt->execute([':today' => $today, ':thirty' => $thirty]);
$expiring = $expiringStmt->fetchAll();

$expiredStmt = $pdo->prepare("SELECT * FROM products WHERE expiry_date < :today ORDER BY expiry_date ASC");
$expiredStmt->execute([':today' => $today]);
$expired = $expiredStmt->fetchAll();
$expiredCount = count($expired);

$lowListStmt = $pdo->query("SELECT * FROM products WHERE quantity <= low_stock_threshold ORDER BY quantity ASC");
$lowList = $lowListStmt->fetchAll();
?>

<div class="row mb-4">
  <div class="col-lg-4 col-md-6 mb-3">
    <div class="card p-3">
      <div class="d-flex justify-content-between align-items-center fade-up">
        <div>
          <h6 class="mb-1">Total Products</h6>
          <h3 class="mb-0 count-up" data-target="<?php echo (int)$total; ?>"><?php echo (int)$total; ?></h3>
        </div>
        <div><i class="bi bi-box-seam" style="font-size:32px"></i></div>
      </div>
    </div>
  </div>

  <div class="col-lg-4 col-md-6 mb-3">
    <div class="card card-expiring p-3">
      <div class="d-flex justify-content-between align-items-center fade-up">
        <div>
          <h6 class="mb-1">Expiring within 30 days</h6>
          <h3 class="mb-0 count-up" data-target="<?php echo count($expiring); ?>"><?php echo count($expiring); ?></h3>
        </div>
        <div><span class="badge badge-expiring px-3 py-2">Soon</span></div>
      </div>
    </div>
  </div>

  <div class="col-lg-4 col-md-12 mb-3">
    <div class="card card-expired p-3">
      <div class="d-flex justify-content-between align-items-center fade-up">
        <div>
          <h6 class="mb-1">Already Expired</h6>
          <h3 class="mb-0 count-up" data-target="<?php echo $expiredCount; ?>"><?php echo $expiredCount; ?></h3>
        </div>
        <div><span class="badge badge-expired px-3 py-2">Expired</span></div>
      </div>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-lg-6 mb-4">
    <div class="card">
      <div class="card-header">
        <strong>Expiry Alerts (within 30 days)</strong>
        <a href="products.php?status=expiring_30" class="btn btn-sm btn-outline-secondary float-end">View Expiring</a>
      </div>
      <div class="card-body table-wrap">
        <?php if (empty($expiring)): ?>
          <div class="alert alert-success mb-0">No products expiring within 30 days.</div>
        <?php else: ?>
          <table class="table table-sm table-hover">
            <thead>
              <tr>
                <th>Name</th>
                <th>Batch</th>
                <th>Expiry</th>
                <th>Qty</th>
              </tr>
            </thead>
            <tbody>
              <?php foreach ($expiring as $p): 
                $isExpired = (new DateTime($p['expiry_date'])) < new DateTime($today);
                ?>
                <tr class="<?php echo $isExpired ? 'table-danger' : ''; ?>">
                  <td><?php echo htmlspecialchars($p['product_name']); ?></td>
                  <td><?php echo htmlspecialchars($p['batch_no']); ?></td>
                  <td><?php echo htmlspecialchars($p['expiry_date']); ?></td>
                  <td><?php echo (int)$p['quantity']; ?></td>
                </tr>
              <?php endforeach; ?>
            </tbody>
          </table>
        <?php endif; ?>
      </div>
    </div>
  </div>

  <div class="col-lg-6 mb-4">
    <div class="card">
      <div class="card-header"><strong>Low Stock Alerts</strong> <a href="products.php?status=low_stock" class="btn btn-sm btn-outline-secondary float-end">View Low Stock</a></div>
      <div class="card-body table-wrap">
        <?php if (empty($lowList)): ?>
          <div class="alert alert-info mb-0">No low stock products.</div>
        <?php else: ?>
          <table class="table table-sm table-hover">
            <thead>
              <tr>
                <th>Name</th>
                <th>Qty</th>
                <th>Threshold</th>
                <th>Supplier</th>
              </tr>
            </thead>
            <tbody>
              <?php foreach ($lowList as $p): ?>
                <tr class="table-warning">
                  <td><?php echo htmlspecialchars($p['product_name']); ?></td>
                  <td><?php echo (int)$p['quantity']; ?></td>
                  <td><?php echo (int)$p['low_stock_threshold']; ?></td>
                  <td><?php echo htmlspecialchars($p['supplier']); ?></td>
                </tr>
              <?php endforeach; ?>
            </tbody>
          </table>
        <?php endif; ?>
      </div>
    </div>
  </div>
</div>

<?php require __DIR__ . '/templates/footer.php'; ?>