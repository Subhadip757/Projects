# E-commerce Payment Service Setup Instructions

## Prerequisites
1. Java 21 or higher
2. MySQL database running on localhost:3306
3. Razorpay account (for payment processing)
4. Gmail account (for email notifications)

## Configuration Steps

### 1. Database Setup
- Ensure MySQL is running on localhost:3306
- The application will automatically create the `ecomDB` database if it doesn't exist
- Default credentials: username=`root`, password=`subhadip9681`

### 2. Razorpay Configuration
1. Sign up at [Razorpay Dashboard](https://dashboard.razorpay.com/)
2. Get your API keys from the dashboard
3. Update `src/main/resources/application.properties`:
   ```
   razorpay.key_id=rzp_test_your_actual_key_id_here
   razorpay.key_secret=your_actual_key_secret_here
   ```

### 3. Email Configuration
1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password for this application
3. Update `src/main/resources/application.properties`:
   ```
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password_here
   ```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Payment Service Endpoints

#### 1. Create Payment Order
- **POST** `/api/payment/create-order`
- **Body:**
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "productName": "Laptop",
    "amount": 50000.0,
    "currency": "INR"
  }
  ```

#### 2. Update Order Status
- **POST** `/api/payment/update-order?paymentId=pay_xxx&orderId=order_xxx&status=SUCCESS`

#### 3. Get Order by Order ID
- **GET** `/api/payment/order/{orderId}`

#### 4. Get Order by Payment ID
- **GET** `/api/payment/payment/{paymentId}`

#### 5. Health Check
- **GET** `/api/payment/health`

## Testing the Payment Flow

1. **Create Order**: Use the create-order endpoint to create a payment order
2. **Process Payment**: Use Razorpay's frontend integration to process the payment
3. **Update Status**: Use the update-order endpoint to update payment status
4. **Email Notification**: Successful payments will trigger email notifications

## Important Notes

- All amounts are in paise (multiply by 100 for Razorpay)
- Email notifications are sent only for successful payments
- The service includes comprehensive error handling and validation
- CORS is enabled for all origins (`*`)

## Troubleshooting

1. **Database Connection Issues**: Check MySQL is running and credentials are correct
2. **Razorpay Errors**: Verify API keys are correct and account is active
3. **Email Issues**: Check Gmail app password and SMTP settings
4. **Validation Errors**: Ensure all required fields are provided with valid data

## Security Considerations

- Never commit real API keys to version control
- Use environment variables for production deployments
- Consider using Razorpay's webhook verification for production
- Implement proper authentication and authorization for production use
