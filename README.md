Shopping Center - Modern E-Commerce Application v2.0
A complete, modern e-commerce platform with **order tracking**, **shipping management**, and a beautiful dual-theme UI.

Quick Start:

### Run Application
bash
Windows
.\mvnw.cmd clean javafx:run

Linux/Mac  
./mvnw clean javafx:run

Or with Maven
mvn clean javafx:run
```

First Launch
- 30 products load automatically
- Browse without login
- Register to track orders


Key Features

Shopping:
- 30 products across 6 categories
- Search, filter, sort
- Ratings & reviews
- Wishlist

### Checkout
- 🛒 Smart shopping cart
- Shipping information (address, postal, city, country)
- Payment simulation
- Invoice export (PDF/text)

Order Tracking
- 6-stage status progression
- Visual progress timeline
- Delivery tracking
- Estimated delivery dates

User System
- Login & Registration
- Admin/Customer roles
- Order history


Project Structure:

data/
├── products.csv (30 products)
├── orders.csv (tracking) NEW
└── discounts.csv

src/main/
├── java/ci553/shoppingcenter/
│   ├── model/ (Order, Product, User)
│   ├── service/ (OrderTrackingService, Cart, etc.)
│   └── client/ (ShoppingCenterView)
└── resources/
    └── shopping-center-styles.css (enhanced)



Features Checklist:

Modern UI (dark + light mode)  
30 products, search, filter, sort  
Ratings, reviews, wishlist  
Purple, visible header buttons
Shipping info collection 
6-stage order tracking  
Visual progress timeline  
Admin controls  
Invoice export  


Demo Accounts

- Admin: admin / adminpass
- Customer: customer / custpass
- Or register your own account



All features are fully functional and tested.

See documentation for detailed guides.

