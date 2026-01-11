# Shopping Center GUI - Modern Dark Purple Theme

## Overview

This project now includes a comprehensive, modern Shopping Center interface with a beautiful dark purple theme. The new GUI provides an enhanced user experience with unified navigation and professional styling.

## 🎨 New Features

### 1. **Unified Shopping Center Interface**
- Modern sidebar navigation with Home, Shop, Profile, and Settings
- Sleek dark purple gradient background
- Professional header with search functionality
- User profile display
- Interactive navigation with keyboard shortcuts

### 2. **Customer Authentication Module**
- **Registration**: Create new customer accounts
- **Login**: Secure customer login
- **Forgot Password**: Password recovery functionality
- Beautiful form styling with consistent theme

### 3. **Admin Authentication**
- Dedicated admin login interface
- Administrator-only access controls
- Secure authentication system

### 4. **Shopping Cart**
- Visual shopping cart interface
- Real-time cart updates
- Product list display
- Total price calculation
- Clear cart and checkout functionality

### 5. **Shipping Management**
- Delivery address input
- Multiple delivery method options:
  - Standard Delivery (3-5 days) - Free
  - Express Delivery (1-2 days) - £5.99
  - Next Day Delivery - £9.99
  - Click & Collect - Free
- Special delivery instructions
- Status indicators

### 6. **Order Tracking (Sendings)**
- Real-time order status tracking
- Visual timeline of delivery progress:
  - Order Placed
  - Processing
  - Shipped
  - In Transit
  - Out for Delivery
  - Delivered
- Order details display
- Contact support integration

## 🚀 How to Launch

Run the `Main.java` class. The application will automatically launch:

1. **Shopping Center** - Main unified interface
2. **Customer Authentication** - Login/Register window
3. **Admin Authentication** - Admin login window
4. **Shopping Cart** - Cart management
5. **Shipping** - Delivery details
6. **Order Tracking** - Sendings/delivery status
7. **Original Clients** - Customer, Picker, Warehouse, etc.

## 🎨 Design Theme

### Color Palette
- **Primary Background**: `#130923` (Dark Purple)
- **Sidebar**: `#170b2b` to `#120a24` (Gradient)
- **Card Background**: `#1c0f34` to `#160b2b` (Gradient)
- **Accent Primary**: `#7c4dff` (Vibrant Purple)
- **Accent Secondary**: `#a07cff` (Light Purple)
- **Text Primary**: `#eae6f7` (Light Lavender)
- **Text Secondary**: `#bfb6dd` (Dimmed Lavender)
- **Success**: `#5ce6b8` (Mint Green)
- **Danger**: `#ff5a8a` (Pink)
- **Highlight**: `#5c2cf3` (Deep Purple)

### Typography
- Clean sans-serif fonts
- Bold headings for emphasis
- Readable body text
- Monospace for code/data areas

### Components
- **Rounded corners** (10px - 18px radius)
- **Soft shadows** for depth
- **Gradient backgrounds** for visual interest
- **Smooth transitions** for interactivity
- **Status indicators** with colored dots
- **Chip badges** for tags and labels

## 📁 File Structure

```
src/main/java/ci553/happyshop/client/shoppingcenter/
├── ShoppingCenterView.java      # Main unified interface
├── CustomerAuthView.java        # Customer authentication
├── AdminAuthView.java           # Admin authentication
├── CartView.java                # Shopping cart
├── ShippingView.java            # Shipping management
└── SendingsView.java            # Order tracking

src/main/resources/
└── shopping-center-styles.css   # Complete theme stylesheet
```

## 🔧 Integration

The new Shopping Center GUI integrates with your existing backend:

- **DatabaseRW**: Database access for products and orders
- **OrderHub**: Order management and notifications
- **Product Models**: Product catalog integration
- **Customer/Picker/Warehouse**: Original client compatibility

## 💡 Usage Examples

### Shopping Center Main View
```java
ShoppingCenterView shoppingCenter = new ShoppingCenterView();
shoppingCenter.start(new Stage());
```

### Customer Authentication
```java
CustomerAuthView customerAuth = new CustomerAuthView();
customerAuth.start(new Stage());
```

### Shopping Cart
```java
CartView cart = new CartView();
cart.start(new Stage());
// Add products
cart.addProduct(product);
```

## 🎯 Navigation Flow

```
Shopping Center (Main)
├── Home
├── Shop (Active by default)
│   ├── Customer Authentication Node
│   ├── Admin Authentication Node
│   ├── Add to Cart Node
│   ├── Shipping Node
│   └── Sendings Node
├── Profile
└── Settings
```

## ✨ Interactive Elements

### Node Cards
- Visual representation of system modules
- Clickable chips for different actions
- Color-coded status indicators:
  - **Purple** - Standard actions
  - **Blue** - Login actions
  - **Green** - Delivery/success
  - **Pink** - Danger/critical

### Navigation
- Hover effects on all interactive elements
- Active state highlighting
- Keyboard shortcuts (H, P, S)
- Smooth page transitions

## 🔒 Security Features

- Separate authentication for customers and admins
- Password field masking
- Secure login forms
- Admin-only access controls

## 📱 Responsive Design

- Fixed sidebar (280px width)
- Flexible content area
- Scrollable sections for long content
- Centered forms and cards
- Maximum widths for readability

## 🛠️ Customization

To customize the theme, edit `shopping-center-styles.css`:

1. **Colors**: Modify CSS variables (if using `:root`)
2. **Fonts**: Change font families in typography classes
3. **Spacing**: Adjust padding and margins
4. **Borders**: Modify radius values
5. **Effects**: Customize shadows and transitions

## 📝 Notes

- All new views use the same dark purple theme
- CSS classes are consistent across all components
- Forms include validation and error handling
- Status indicators update dynamically
- Cart totals calculate automatically
- Order tracking shows real-time progress

## 🚧 Future Enhancements

- [ ] Connect authentication to database
- [ ] Implement actual checkout process
- [ ] Add payment gateway integration
- [ ] Real-time order updates
- [ ] Email notifications
- [ ] Product search functionality
- [ ] User profile management
- [ ] Settings configuration
- [ ] Multi-language support
- [ ] Dark/light theme toggle

## 📄 License

University of Brighton - HappyShop Project

---

**Developed by**: Arielli Ortega  
**Course**: CI553  
**Year**: 2025  
**Theme**: Modern Dark Purple Shopping Center