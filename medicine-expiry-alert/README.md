# Medicine Expiry Alert System

A beautiful, secure web-based medical inventory management system to track medicine expiry dates and stock levels.

## Features

- 🔐 **Secure Authentication** - Login/logout with session management
- � **User Registration** - Beautiful signup process with validation
- � **Daoshboard** - Overview of inventory status and alerts
- 💊 **Product Management** - Add, edit, delete medical products
- ⚠️ **Expiry Alerts** - Track products expiring within 30 days
- 📉 **Low Stock Alerts** - Monitor inventory levels
- 🎨 **Beautiful UI** - Modern Bootstrap 5 interface with dark mode support
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile
- 🛡️ **Password Security** - Strength indicator and secure hashing

## Quick Setup

1. **Database Setup**

   ```bash
   # Run the database initialization
   php init_db.php
   ```

2. **Create Admin User**

   - Visit `setup.php` in your browser
   - Create your first admin account

3. **Login & Signup**
   - Visit `login.php` or the main page
   - Use your admin credentials or create new accounts via `signup.php`

## File Structure

- `config/` - Database and authentication configuration
- `templates/` - Header and footer templates
- `assets/css/` - Custom styling
- `login.php` - Beautiful login interface
- `signup.php` - User registration with validation
- `logout.php` - Secure logout
- `setup.php` - Initial admin user creation
- `index.php` - Main dashboard
- `products.php` - Product management

## Security Features

- Password hashing with PHP's `password_hash()`
- Session-based authentication
- SQL injection protection with prepared statements
- XSS protection with `htmlspecialchars()`
- CSRF protection ready

## Usage

1. **First Time Setup**: Run `setup.php` to create your admin user
2. **Login**: Access the system through `login.php`
3. **Dashboard**: View expiry alerts and low stock warnings
4. **Manage Products**: Add, edit, and delete medical inventory
5. **Logout**: Secure logout from any page

The system automatically tracks expiring medicines and low stock levels, providing real-time alerts on the dashboard.
