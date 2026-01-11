# 📚 Shopping Center - Complete Documentation

**Version:** 2.0  
**Date:** January 11, 2026  
**Status:** Production Ready  

---

## 📋 TABLE OF CONTENTS

1. [Overview & Quick Start](#overview-quick-start)
2. [Login Credentials & Discount Codes](#login-credentials-discount-codes)
3. [Features & Capabilities](#features-capabilities)
4. [Installation & Setup](#installation-setup)
5. [User Guide](#user-guide)
6. [Admin Guide](#admin-guide)
7. [Testing Documentation](#testing-documentation)
8. [Technical Documentation](#technical-documentation)
9. [Troubleshooting](#troubleshooting)

---

# 1. OVERVIEW & QUICK START

## 🛍️ About Shopping Center

A complete, modern e-commerce platform built with JavaFX featuring:
- ✅ 30+ products across 6 categories
- ✅ Order tracking with 6-stage delivery system
- ✅ Dual theme (Dark/Light mode)
- ✅ Admin warehouse management
- ✅ CSV data persistence
- ✅ Complete shopping workflow

## 🚀 Quick Start

### Run the Application:
```bash
# Windows
.\mvnw.cmd clean javafx:run

# Linux/Mac
./mvnw clean javafx:run

# Or with Maven
mvn clean javafx:run
```

### First Time Setup:
1. Launch the application
2. Default accounts are auto-created
3. Browse products or login
4. Start shopping!

---

# 2. LOGIN CREDENTIALS & DISCOUNT CODES

## 🔐 Default Login Credentials

### **👤 CUSTOMER ACCOUNT**
```
Username: customer
Password: custpass
Role: Customer
```

**Access:**
- Browse and search products
- Add to cart and wishlist
- Place orders
- Track order status
- Manage profile and settings

---

### **👑 ADMINISTRATOR ACCOUNT**
```
Username: admin
Password: adminpass
Role: Administrator
```

**Access:**
- Everything customer can do, PLUS:
- Access Warehouse tab
- Add/Edit/Delete products
- Manage inventory
- View all user orders
- Admin dashboard features

---

### **🌐 GUEST ACCESS**
```
No login required
```

**Access:**
- Browse products only
- View product details
- Search and filter
- ❌ Cannot add to cart (must login)

---

## 🎟️ DISCOUNT CODES

### **Available Codes:**

| Code | Discount | Description |
|------|----------|-------------|
| **BLACKFRIDAY** | 50% OFF | Black Friday Special - Best savings! |
| **SAVE10** | 10% OFF | Standard discount for regular shopping |
| **WELCOME5** | 5% OFF | Welcome discount for new customers |

### **How to Apply Discount:**
1. Add items to cart
2. Proceed to checkout
3. Enter discount code in the "Discount Code" field
4. Click "Apply"
5. Discount applied to total!

**Example:**
- Cart Total: $100.00
- Code: `BLACKFRIDAY`
- Discount: -$50.00 (50%)
- **Final Total: $50.00** ✅

---

## 🎯 Quick Reference Card

```
╔═══════════════════════════════════════════╗
║   SHOPPING CENTER - QUICK REFERENCE       ║
╠═══════════════════════════════════════════╣
║                                           ║
║  👤 CUSTOMER LOGIN:                       ║
║     Username: customer                    ║
║     Password: custpass                    ║
║                                           ║
║  👑 ADMIN LOGIN:                          ║
║     Username: admin                       ║
║     Password: adminpass                   ║
║                                           ║
║  🎟️ DISCOUNT CODES:                       ║
║     BLACKFRIDAY  →  50% OFF               ║
║     SAVE10       →  10% OFF               ║
║     WELCOME5     →   5% OFF               ║
║                                           ║
╚═══════════════════════════════════════════╝
```

---

# 3. FEATURES & CAPABILITIES

## 🎨 User Interface Features

### **Modern Design:**
- ✅ Responsive purple theme throughout
- ✅ Dark mode and Light mode toggle
- ✅ Clean, professional card-based layouts
- ✅ Smooth animations and hover effects
- ✅ Consistent spacing and typography
- ✅ No horizontal scrolling on any page

### **Navigation:**
- ✅ Sidebar navigation (Home, Shop, Profile, Settings, Warehouse)
- ✅ Clean header with essential buttons only
- ✅ Cart button with item count
- ✅ Theme toggle
- ✅ Login/Logout

---

## 🛍️ Shopping Features

### **Product Browsing:**
- ✅ 30+ products across 6 categories
- ✅ Product search by name
- ✅ Filter by category
- ✅ Filter by price range (min/max)
- ✅ Sort by: Featured, Price (Low-High, High-Low), Popularity
- ✅ Product images for all items
- ✅ Product ratings (1-5 stars)
- ✅ Product reviews and comments

### **Shopping Cart:**
- ✅ Add products with quantity selector
- ✅ Update quantities in cart
- ✅ Remove items from cart
- ✅ Cart persists across sessions (CSV storage)
- ✅ Real-time cart total calculation
- ✅ Multi-user cart isolation

### **Wishlist:**
- ✅ Save products for later
- ✅ Visual product cards in wishlist
- ✅ Quick add to cart from wishlist
- ✅ Remove from wishlist
- ✅ Persistent storage

---

## 📦 Order Management

### **Checkout Process:**
1. Review cart items
2. Enter shipping information:
   - Full delivery address
   - City
   - Postal/ZIP code
   - Country (dropdown with global countries)
3. Apply discount code (optional)
4. Enter payment information:
   - Card type selection (Visa, MasterCard, Amex, Discover)
   - Card number
   - Expiration date (auto-formats with /)
   - CVV
   - Cardholder name
5. Review order summary
6. Complete purchase

### **Order Tracking (6-Stage System):**
1. **Order Placed** - Order confirmed
2. **Processing** - Payment verified, preparing order
3. **Shipped** - Order dispatched from warehouse
4. **In Transit** - En route to destination
5. **Out for Delivery** - Local delivery in progress
6. **Delivered** - Order received by customer

**Features:**
- ✅ Visual progress timeline
- ✅ Real-time status updates
- ✅ Estimated delivery dates
- ✅ Unique tracking numbers
- ✅ Delivery address display
- ✅ Order history in Profile

---

## 👤 User Management

### **Account Features:**
- ✅ User registration with validation
- ✅ Secure login system
- ✅ Password change with strength indicator
- ✅ Show/hide password toggle
- ✅ Profile picture upload
- ✅ Update username and email
- ✅ Role-based access control (Admin/Customer)

### **Security Features:**
- ✅ Username uniqueness enforcement
- ✅ Password validation
- ✅ Session management
- ✅ Two-factor authentication (2FA) setup
- ✅ Active sessions list
- ✅ Login history tracking
- ✅ Security alerts for suspicious activity

---

## 👑 Admin Features

### **Warehouse Management:**
- ✅ Standalone Warehouse dashboard
- ✅ Add new products with full details
- ✅ Edit existing products (name, price, stock, category, image)
- ✅ Delete products with confirmation
- ✅ Upload product images
- ✅ Stock quantity management
- ✅ Low-stock alerts
- ✅ Inventory search and filters
- ✅ Real-time stock tracking

### **Order Management:**
- ✅ View all customer orders
- ✅ Advance order status through delivery stages
- ✅ Order filtering and search
- ✅ Invoice generation and export

---

## 📊 Data Persistence

### **CSV Storage System:**
All data persists across application restarts:

- ✅ **users.csv** - User accounts and passwords
- ✅ **carts.csv** - Shopping cart contents
- ✅ **wishlists.csv** - User wishlists
- ✅ **products.csv** - Product catalog
- ✅ **discounts.csv** - Discount codes
- ✅ **order_tracking.csv** - Order tracking data
- ✅ **activity_log.csv** - User actions and logs
- ✅ **reviews.csv** - Product reviews

**Benefits:**
- ✅ Data survives application restart
- ✅ Human-readable format
- ✅ Easy backup (copy data/ folder)
- ✅ Import/export capable
- ✅ Database migration ready

---

# 4. INSTALLATION & SETUP

## 📋 Requirements

### **System Requirements:**
- **Java:** JDK 21 or higher
- **Maven:** 3.8+ (or use included Maven wrapper)
- **OS:** Windows, macOS, or Linux
- **RAM:** 2GB minimum
- **Disk Space:** 500MB

### **Dependencies (Auto-installed by Maven):**
- JavaFX 21.0.7
- JUnit 5.10.2
- Apache Derby 10.16.1.1
- Apache PDFBox 3.0.0

---

## 🔧 Installation Steps

### **1. Clone or Download Repository:**
```bash
git clone https://github.com/iamarielliortegaa/ao860happyshop.git
cd ao860happyshop-modern-ui
```

### **2. Build Project:**
```bash
# Using Maven wrapper (recommended)
.\mvnw.cmd clean install    # Windows
./mvnw clean install        # Linux/Mac

# Or using system Maven
mvn clean install
```

### **3. Run Application:**
```bash
# Using Maven wrapper
.\mvnw.cmd clean javafx:run    # Windows
./mvnw clean javafx:run        # Linux/Mac

# Or using system Maven
mvn clean javafx:run
```

### **4. First Launch:**
- Application creates default accounts automatically
- Sample products load from `data/products.csv`
- Ready to use immediately!

---

## 📂 Project Structure

```
ao860happyshop-modern-ui/
├── src/
│   ├── main/
│   │   ├── java/ci553/shoppingcenter/
│   │   │   ├── client/          # UI components
│   │   │   ├── model/           # Data models
│   │   │   ├── service/         # Business logic
│   │   │   ├── storage/         # Data persistence
│   │   │   └── utility/         # Helper classes
│   │   └── resources/
│   │       ├── shopping-center-styles.css
│   │       └── images/
│   └── test/
│       └── java/                # Unit & integration tests
├── data/                        # CSV data files
│   ├── users.csv
│   ├── products.csv
│   ├── carts.csv
│   ├── discounts.csv
│   └── ...
├── images/                      # Product images
├── orders/                      # Order data
├── pom.xml                      # Maven configuration
└── *.md                         # Documentation
```

---

# 5. USER GUIDE

## 🛍️ Shopping Workflow

### **Step 1: Browse Products**
1. Launch application (opens on Home Dashboard)
2. Click **"Shop"** tab in sidebar
3. Browse 30+ products
4. Use search box to find specific products
5. Filter by category or price range
6. Sort by price or popularity

### **Step 2: Add to Cart**
1. Find product you like
2. Adjust quantity (use spinner or type number)
3. Click **"Add to Cart"** button
4. See cart count update in header
5. Continue shopping or proceed to checkout

### **Step 3: Manage Cart**
1. Click **Cart** button (🛒) in header
2. Review items in cart
3. Update quantities if needed
4. Remove unwanted items
5. Apply discount code (BLACKFRIDAY, SAVE10, WELCOME5)
6. See updated total

### **Step 4: Checkout**
1. Click **"Proceed to Checkout"**
2. **Enter Shipping Information:**
   - Full delivery address
   - City
   - Postal/ZIP code
   - Country (select from dropdown)
3. **Apply Discount Code** (optional)
4. **Enter Payment Details:**
   - Select card type
   - Card number
   - Expiration (auto-formats: MM/YY)
   - CVV
   - Cardholder name
5. Review order summary
6. Click **"Complete Purchase"**

### **Step 5: Track Order**
1. Go to **Profile** tab → **My Orders**
2. See all your orders with:
   - Order ID
   - Tracking number
   - Delivery address
   - Current status
   - Estimated delivery
3. Watch progress through 6 stages:
   - Order Placed → Processing → Shipped → In Transit → Out for Delivery → Delivered

---

## ❤️ Using Wishlist

### **Add to Wishlist:**
1. Find product you like
2. Click **♥ Add to Wishlist** button
3. Product saved for later

### **View Wishlist:**
1. Go to **Profile** tab → **Wishlist**
2. See all saved products with images
3. Click **"Add to Cart"** to purchase
4. Click **"Remove"** to remove from wishlist

---

## 👤 Profile Management

### **Update Profile:**
1. Go to **Profile** tab → **Profile & Account**
2. Click **"Choose File"** to upload profile picture
3. Update username or email
4. Click **"Save Changes"**

### **Change Password:**
1. Go to **Settings** tab → **Security**
2. Enter current password
3. Enter new password
4. See strength indicator
5. Click **"Change Password"**

---

# 6. ADMIN GUIDE

## 👑 Admin Access

### **Login as Admin:**
```
Username: admin
Password: adminpass
```

### **Admin Features Available:**
- ✅ Warehouse tab (sidebar)
- ✅ Admin Orders button (header)
- ✅ All customer features

---

## 🏭 Warehouse Management

### **Access Warehouse:**
1. Login as admin
2. Click **"Warehouse"** in sidebar
3. See inventory dashboard

### **Add New Product:**
1. In Warehouse tab, click **"Add Product"**
2. Fill in details:
   - Product ID (unique)
   - Product Name
   - Price
   - Stock Quantity
   - Category
   - Description
3. Click **"Choose Image"** to upload product image
4. Click **"Add Product"**
5. Product appears immediately

### **Edit Existing Product:**
1. Find product in Warehouse list
2. Click **"Edit"** button
3. Modify any field
4. Click **"Save Changes"**
5. Updates persist to CSV

### **Delete Product:**
1. Find product in Warehouse list
2. Click **"Delete"** button
3. Confirm deletion
4. Product removed from system

### **Manage Stock:**
1. See current stock levels
2. Get low-stock alerts (< 10 items)
3. Update quantities via Edit function
4. Track inventory in real-time

---

## 📦 Order Management

### **View All Orders:**
1. Click **"Admin Orders"** button in header
2. See all customer orders
3. Filter by status or search

### **Advance Order Status:**
1. Find order in list
2. Click **"Advance Status"** button
3. Order moves to next delivery stage:
   - Order Placed → Processing
   - Processing → Shipped
   - Shipped → In Transit
   - In Transit → Out for Delivery
   - Out for Delivery → Delivered
4. Customer sees updated status in their profile

---

# 7. TESTING DOCUMENTATION

## 🧪 Unit Testing

### **Test Coverage:**
- **Total Tests:** 64
- **Unit Tests:** 57 (89%)
- **Integration Tests:** 7 (11%)
- **Coverage:** All service layer components

### **Test Classes:**

#### **1. UserServiceTest (15 tests)**
- User registration (new, duplicate, validation)
- Login (success, failure, credentials)
- Password change (valid, invalid)
- Admin role verification
- Default account checks

#### **2. CartServiceTest (17 tests)**
- Add to cart (single, multiple, duplicate)
- Update quantities (positive, zero, negative)
- Remove items
- Clear cart
- Multi-user isolation
- Guest cart handling

#### **3. WishlistServiceEnhancedTest (13 tests)**
- Add to wishlist
- Remove from wishlist
- Duplicate prevention
- Multi-user wishlists
- Guest wishlist
- Data persistence

#### **4. ActionLoggerTest (12 tests)**
- Log creation
- CSV format verification
- Multiple log entries
- Special character handling
- Read operations

#### **5. ShoppingCenterIntegrationTest (7 tests)**
- Complete shopping flow (register → login → shop → checkout)
- Multi-user shopping isolation
- Admin vs Customer permissions
- Data persistence verification
- Order workflow

---

## 🚀 Running Tests

### **Run All Tests:**
```bash
mvn test
```

### **Run Specific Test Class:**
```bash
mvn test -Dtest=UserServiceTest
mvn test -Dtest=CartServiceTest
mvn test -Dtest=ShoppingCenterIntegrationTest
```

### **Run Test Suite:**
```bash
mvn test -Dtest=AllTestsSuite
```

### **Test Results:**
- All tests use JUnit 5
- @DisplayName annotations for clarity
- Proper setup/teardown with @BeforeEach/@AfterEach
- Covers positive, negative, and edge cases

---

# 8. TECHNICAL DOCUMENTATION

## 🏗️ Architecture

### **Design Patterns:**
- **Service Layer Pattern** - Business logic separation
- **Repository Pattern** - Data access abstraction
- **Observer Pattern** - UI updates
- **Factory Pattern** - Component creation
- **MVC Pattern** - Model-View-Controller separation

### **Technology Stack:**
- **Language:** Java 21
- **UI Framework:** JavaFX 21
- **Build Tool:** Maven 3.8+
- **Testing:** JUnit 5.10.2
- **Database:** Apache Derby (legacy) + CSV storage
- **PDF Export:** Apache PDFBox 3.0

---

## 📊 Data Models

### **User Model:**
```java
- id: String (username)
- name: String (display name)
- password: String (plain text - demo only)
- admin: boolean (role)
```

### **Product Model:**
```java
- id: String
- name: String
- price: double
- stockQuantity: int
- category: String
- imageFilename: String
- rating: double
- reviews: List<Review>
```

### **Order Model:**
```java
- orderId: String
- username: String
- items: Map<Product, Integer>
- total: double
- status: OrderStatus (enum)
- shippingAddress: String
- trackingNumber: String
- timestamp: LocalDateTime
```

---

## 🔧 Service Layer

### **UserService:**
- Registration, login, authentication
- Password management
- Role verification
- CSV persistence

### **ProductService:**
- Product CRUD operations
- Search and filtering
- Category management
- Stock tracking

### **CartService:**
- Add/remove/update cart items
- Multi-user cart isolation
- Persistent storage

### **OrderTrackingService:**
- 6-stage order tracking
- Status updates
- Delivery estimation
- Tracking number generation

### **InvoiceService:**
- PDF invoice generation
- Text invoice export
- Order summaries

---

## 📁 CSV File Formats

### **users.csv:**
```csv
username,displayName,password,admin
admin,Administrator,adminpass,true
customer,Demo Customer,custpass,false
```

### **products.csv:**
```csv
productId,name,price,category,stock,imageFilename
p001,Wireless Headphones,79.99,Electronics,50,0001.jpg
p002,Cotton T-Shirt,24.99,Clothing,100,0002.jpg
```

### **discounts.csv:**
```csv
code,percentage
BLACKFRIDAY,50
SAVE10,10
WELCOME5,5
```

### **carts.csv:**
```csv
username,productId,quantity
customer,p001,2
admin,p003,1
```

---

# 9. TROUBLESHOOTING

## ❓ Common Issues

### **Application Won't Start**

**Issue:** Java version mismatch
**Solution:**
```bash
# Check Java version
java -version
# Should be Java 21+
# Download from: https://www.oracle.com/java/technologies/downloads/
```

**Issue:** JavaFX not found
**Solution:**
```bash
# Use Maven wrapper (includes JavaFX)
.\mvnw.cmd clean javafx:run
```

---

### **Login Issues**

**Problem:** Can't login with default credentials
**Solution:**
1. Verify credentials:
   - Customer: `customer` / `custpass`
   - Admin: `admin` / `adminpass`
2. Check `data/users.csv` exists
3. Delete `data/users.csv` to reset to defaults
4. Restart application

---

### **Products Not Loading**

**Problem:** No products displayed
**Solution:**
1. Check `data/products.csv` exists
2. Verify product images in `images/` folder
3. Check console for errors
4. Reset data by copying from `data_backup/` (if available)

---

### **Cart Not Persisting**

**Problem:** Cart clears on restart
**Solution:**
1. Check `data/carts.csv` exists
2. Ensure write permissions on `data/` folder
3. Verify CartService is calling `persist()`
4. Check console for IOException errors

---

### **Discount Code Not Working**

**Problem:** Discount code rejected
**Solution:**
1. Use exact codes (case-sensitive):
   - BLACKFRIDAY
   - SAVE10
   - WELCOME5
2. Check `data/discounts.csv` exists
3. Apply code BEFORE final checkout

---

### **Order Tracking Not Showing**

**Problem:** Orders don't appear in Profile
**Solution:**
1. Ensure you completed checkout
2. Check you're logged in
3. Go to Profile → My Orders tab
4. Verify order was saved to `data/order_tracking.csv`

---

### **IDE Shows "Cannot Resolve" Errors**

**Problem:** IntelliJ shows red underlines
**Solution:**
1. File → Invalidate Caches / Restart
2. Maven → Reload Project
3. Build → Rebuild Project
4. Wait for background indexing to complete

**Note:** These are IDE cache issues, not real compilation errors.

---

## 📞 Getting Help

### **Resources:**
- **Documentation:** Check all .md files in project root
- **GitHub Issues:** Report bugs at repository
- **Logs:** Check console output for error messages
- **Data Files:** Inspect CSV files in `data/` folder

### **Debug Mode:**
```bash
# Run with verbose logging
mvn clean javafx:run -X
```

---

## 🎊 SUMMARY

### **Quick Stats:**
- ✅ 30+ products in 6 categories
- ✅ 64 comprehensive unit tests
- ✅ 6-stage order tracking system
- ✅ 2 user roles (Admin, Customer)
- ✅ 3 discount codes (up to 50% off)
- ✅ 100% data persistence via CSV
- ✅ Modern responsive UI
- ✅ Production-ready quality

### **Ready to Use:**
1. **Login:** customer / custpass
2. **Discount:** BLACKFRIDAY for 50% off
3. **Admin:** admin / adminpass
4. **Start Shopping!** 🛍️

---

**Shopping Center - Complete Documentation**  
**Version:** 2.0  
**Last Updated:** January 11, 2026  
**Status:** ✅ Production Ready

---

*For additional details, see individual markdown files in the project root directory.*

