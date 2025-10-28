<?php
// templates/footer.php
?>
</main>

<!-- Bootstrap JS bundle (with Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Loader overlay -->
<div id="loader-overlay" aria-hidden="true">
  <div class="spinner" role="status" aria-label="Loading"></div>
  
</div>

<!-- Toast container -->
<div class="toast-container toast-fixed"></div>

<!-- Small JS for delete confirm & UI helpers -->
<script>
function confirmDelete(url) {
  if (confirm('Are you sure you want to delete this product? This action cannot be undone.')) {
    showLoader(true);
    window.location = url;
  }
}

// Loader overlay controls
function showLoader(show) {
  var overlay = document.getElementById('loader-overlay');
  if (!overlay) return;
  if (show) overlay.classList.add('show'); else overlay.classList.remove('show');
}

// Simple toast helper
function showToast(message, type) {
  var container = document.querySelector('.toast-container');
  if (!container) return;
  var bg = 'bg-primary';
  if (type === 'success') bg = 'bg-success';
  if (type === 'warning') bg = 'bg-warning';
  if (type === 'danger') bg = 'bg-danger';
  var el = document.createElement('div');
  el.className = 'toast align-items-center text-white ' + bg;
  el.role = 'alert';
  el.innerHTML = '<div class="d-flex"><div class="toast-body">'+message+'</div><button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button></div>';
  container.appendChild(el);
  var t = new bootstrap.Toast(el, { delay: 2500 });
  t.show();
  el.addEventListener('hidden.bs.toast', function(){ el.remove(); });
}

// Count-up animation for numbers
function animateCountUp(el) {
  var target = parseInt(el.getAttribute('data-target') || el.textContent, 10) || 0;
  var duration = 600;
  var start = 0;
  var startTime = null;
  function step(ts) {
    if (!startTime) startTime = ts;
    var p = Math.min(1, (ts - startTime) / duration);
    var value = Math.floor(start + (target - start) * p);
    el.textContent = value;
    if (p < 1) requestAnimationFrame(step);
  }
  requestAnimationFrame(step);
}

// Enable tooltips
document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function(el){
  new bootstrap.Tooltip(el);
});

// (theme toggle removed)
</script>

</body>
</html>