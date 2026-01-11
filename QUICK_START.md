# 🚀 Quick Start Guide - Shopping Center Application

## Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 21 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/

2. **Maven** (Optional - Maven wrapper included)
   - Download from: https://maven.apache.org/download.cgi

## Setup Instructions

### Step 1: Verify Java Installation

Open a terminal/command prompt and run:

```bash
java -version
```

You should see Java version 21 or higher.

### Step 2: Set JAVA_HOME (if not set)

**Windows:**
```powershell
# Check current JAVA_HOME
echo $env:JAVA_HOME

# Set JAVA_HOME (replace path with your Java installation)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
```

**Linux/Mac:**
```bash
# Check current JAVA_HOME
echo $JAVA_HOME

# Set JAVA_HOME (add to ~/.bashrc or ~/.zshrc)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
```

### Step 3: Navigate to Project Directory

```bash
cd "C:\Users\ariel\OneDrive\Documents\ao860happyshop-modern-ui"
```

### Step 4: Run the Application

**Option A: Using Maven Wrapper (Recommended)**

Windows:
```powershell
.\mvnw.cmd clean javafx:run
```

Linux/Mac:
```bash
./mvnw clean javafx:run
```

**Option B: Using Installed Maven**

```bash
mvn clean javafx:run
```

**Option C: Build and Run JAR**

```bash
# Build
mvn clean package

# Run
java -jar target/HappyShop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## First Launch

When you first run the application:

1. **Products Load Automatically**: 30 sample products across 6 categories
2. **No Login Required**: Browse products as a guest
3. **Create Account**: Click "Login / Register" → Register tab
4. **Test Admin Features**: Go to Settings → Role Switcher → Select "Admin"

## Quick Feature Tour

### 1. Browse Products (Shop Page)
- **Search**: Type product name in search box
- **Filter**: Select category from dropdown
- **Price Range**: Enter min/max price
- **Sort**: Choose sort order (Price ↑↓, Popularity)
- **Add to Cart**: Select quantity, click "Add to Cart"
- **Wishlist**: Click heart (♡) button
- **Reviews**: Click "Write Review"

### 2. Shopping Cart
- **View Cart**: Click "Cart" button in header
- **Update Quantity**: Use spinner, click "Update"
- **Apply Discount**: Enter code (create in Settings first)
- **Checkout**: Click "Proceed to Payment"
- **Export Invoice**: After checkout, click "Export PDF"

### 3. User Profile
- **Order History**: View past orders
- **Wishlist**: See favorited products
- **Guest Checkout**: Allowed without login

### 4. Admin Features (Settings Page)
- **Switch Role**: Settings → Role Switcher → "Admin"
- **Add Product**: Fill form at bottom of Products section
- **Edit Product**: Click "Edit" on any product
- **Delete Product**: Click "Remove" (with confirmation)
- **Manage Discounts**: Add discount codes with percentages

### 5. Theme Toggle
- Click **"Dark"** or **"Light"** button in header
- Entire UI switches themes instantly

## Sample Data

### Pre-loaded Products (30 total)

**Electronics** (5):
- Wireless Headphones - $79.99
- Smart Watch - $199.99
- USB-C Cable 2m - $12.99
- Laptop Stand - $45.50
- Mechanical Keyboard - $129.99

**Clothing** (5):
- Cotton T-Shirt - $19.99
- Denim Jeans - $49.99
- Running Shoes - $89.99
- Winter Jacket - $149.99
- Baseball Cap - $24.99

**Home & Garden** (5):
- Coffee Maker - $89.99
- Ceramic Mug Set - $29.99
- Indoor Plant Pot - $15.99
- LED Desk Lamp - $39.99
- Kitchen Knife Set - $79.99

**Books** (5):
- Programming Book - $45.99
- Mystery Novel - $12.99
- Cookbook Collection - $34.99
- Self-Help Guide - $22.99
- Children's Stories - $18.99

**Sports** (5):
- Yoga Mat - $29.99
- Dumbbells 10kg Set - $59.99
- Tennis Racket - $89.99
- Water Bottle 1L - $14.99
- Resistance Bands - $19.99

**Toys** (5):
- Building Blocks Set - $34.99
- Remote Control Car - $49.99
- Board Game Classic - $29.99
- Puzzle 1000 Pieces - $24.99
- Plush Teddy Bear - $19.99

### Create a Discount Code

1. Switch to Admin role (Settings → Role Switcher)
2. Scroll to "Discount codes" section
3. Enter code: `SAVE10`
4. Enter percent: `10`
5. Click "Add Discount"
6. Now use `SAVE10` in cart for 10% off!

## Testing Checklist

- [ ] Application launches successfully
- [ ] 30 products display in Shop page
- [ ] Search finds products by name
- [ ] Category filter works
- [ ] Price range filter works
- [ ] Sorting changes order
- [ ] Add to cart increases cart count
- [ ] Cart shows products with images
- [ ] Update quantity in cart works
- [ ] Discount code applies in cart
- [ ] Checkout completes successfully
- [ ] Receipt displays after checkout
- [ ] Login/Register with validation works
- [ ] Order history shows orders
- [ ] Wishlist saves favorites
- [ ] Admin can add product
- [ ] Admin can edit product
- [ ] Admin can delete product
- [ ] Theme toggle switches dark/light
- [ ] Out of stock products show badge
- [ ] Stock validation prevents over-ordering

## Troubleshooting

### "JAVA_HOME not found"
- Set JAVA_HOME environment variable (see Step 2)
- Restart terminal after setting

### "mvn: command not found"
- Use Maven wrapper: `./mvnw` or `.\mvnw.cmd`
- Or install Maven from: https://maven.apache.org/

### "No products showing"
- Check `data/products.csv` exists
- If missing, delete `data` folder and restart app
- Products will regenerate automatically

### "Images not loading"
- Images are in `images/` folder (0001.jpg - 0011.jpg)
- Fallback placeholder is used if image missing

### Application won't start
- Verify Java 21+ installed: `java -version`
- Check JavaFX is available (Maven handles this)
- Delete `target/` folder and rebuild

### Performance Issues
- Reduce product count in `data/products.csv`
- Close other applications
- Increase JVM heap: `-Xmx1024m`

## Keyboard Shortcuts

- **H**: Home page (shown in sidebar)
- **P**: Profile page
- **S**: Settings page
- **Enter**: Submit in login/register forms
- **Esc**: Close dialogs (standard JavaFX)

## File Locations

- **Products**: `data/products.csv`
- **Orders**: `orders/progressing/`, `orders/ordered/`, `orders/collected/`
- **Invoices**: `orders/invoices/`
- **Logs**: `data/action.log`, `data/notifications.log`
- **Users**: In-memory (lost on restart)
- **Cart**: In-memory (lost on restart)

## Next Steps

1. **Customize Products**: Edit `data/products.csv`
2. **Add Product Images**: Place in `images/` folder, reference in CSV
3. **Configure Discounts**: Add codes in Settings → Admin mode
4. **Test All Features**: Follow testing checklist above
5. **Read Full Documentation**: See `IMPLEMENTATION_SUMMARY.md`

## Support

If you encounter issues:

1. Check logs: `data/action.log`
2. Review error messages in UI (red text)
3. Verify Java and Maven are correctly installed
4. Ensure all required files exist in project directory

## Enjoy Shopping! 🛍️

The application is now ready to use with all features working:
- ✅ 30 products across 6 categories
- ✅ Dark and Light themes
- ✅ Full shopping cart with quantity updates
- ✅ Checkout with discount codes
- ✅ Admin product management
- ✅ User registration and login
- ✅ Order history and wishlist
- ✅ Product reviews and ratings

Happy shopping! 🎉

