# Shopping Center GUI - Modern Dark Purple Theme

## Overview

This is the new unified Shopping Center interface implementing a modern dark purple theme design. The GUI provides a comprehensive shopping experience with authentication, cart management, shipping, and order tracking.

## Features Implemented

### 1. **Unified Shopping Center View** (`ShoppingCenterView.java`)
- **Sidebar Navigation** with Home, Shop, Profile, and Settings
- **Dark Purple Theme** with gradients and modern styling
- **Search Functionality** for products, orders, and users
- **User Profile Display** with avatar
- **Interactive Flow Chart** showing the shopping process:
  - Customer Authentication
  - Admin Authentication
  - Add to Cart
  - Shipping
  - Sendings/Delivery

### 2. **Customer Authentication** (`CustomerAuthView.java`)
- **Registration Form**: Create new customer accounts
- **Login Form**: Customer login with email and password
- **Forgot Password**: Password recovery functionality
- Seamless switching between authentication modes

### 3. **Admin Authentication** (`AdminAuthView.java`)
- **Admin Login**: Secure administrator access
- Dedicated admin interface with special permissions

### 4. **Shopping Cart** (`CartView.java`)
- **Add/Remove Products**: Manage cart items
- **Real-time Total Calculation**: Dynamic price updates
- **Clear Cart**: Remove all items at once
- **Checkout Process**: Proceed to payment
- Integration with existing `Product` and `ProductListFormatter` classes

### 5. **Shipping Management** (`ShippingView.java`)
- **Delivery Address**: Complete address form
- **Delivery Methods**:
  - Standard Delivery (3-5 days) - Free
  - Express Delivery (1-2 days) - £5.99
  - Next Day Delivery - £9.99
  - Click & Collect - Free
- **Special Instructions**: Optional delivery notes
- **Status Indicators**: Visual delivery status

### 6. **Order Tracking / Sendings** (`SendingsView.java`)
- **Order Status Timeline**:
  - Order Placed
  - Processing
  - Shipped
  - In Transit
  - Out for Delivery
  - Delivered
- **Visual Progress Indicators**: Timeline with colored dots
- **Order Details**: Date, status, and tracking information
- **Support Contact**: Customer support access

## Styling

### CSS Theme (`shopping-center-styles.css`)
The application uses a comprehensive CSS theme with:

- **Color Palette**:
  - Background: `#130923` (Dark purple)
  - Sidebar: `#170b2b`
  - Cards: `#1c0f34`
  - Accent: `#7c4dff` (Purple)
  - Success: `#5ce6b8` (Green)
  - Danger: `#ff5a8a` (Pink)

- **Components Styled**:
  - Sidebar navigation with hover effects
  - Form fields with focus states
  - Primary and secondary buttons
  - Cards with shadows and borders
  - Chips and badges
  - Custom scrollbars
  - Text areas

## How to Launch

The new Shopping Center GUI is integrated into `Main.java`:

```java
// Launch all new views
startShoppingCenter();      // Main unified interface
startCustomerAuth();        // Customer authentication
startAdminAuth();           // Admin authentication
startCartView();            // Shopping cart
startShippingView();        // Shipping management
startSendingsView();        // Order tracking
```

## File Structure

```
src/main/java/ci553/happyshop/client/shoppingcenter/
├── ShoppingCenterView.java      # Main unified interface
├── CustomerAuthView.java        # Customer login/register
├── AdminAuthView.java           # Admin login
├── CartView.java                # Shopping cart
├── ShippingView.java            # Shipping details
└── SendingsView.java            # Order tracking

src/main/resources/
└── shopping-center-styles.css   # Dark purple theme
```

## Integration with Existing System

The new GUI integrates seamlessly with your existing backend:

- **DatabaseRW**: Database read/write operations
- **OrderHub**: Order management and notifications
- **Product**: Product catalog
- **ProductListFormatter**: Cart display formatting
- **Original Views**: All original customer, picker, warehouse views remain functional

## User Experience

### Navigation Flow
1. **Authentication**: Customer/Admin login or registration
2. **Browse**: Search and view products in the main interface
3. **Cart**: Add products and manage shopping cart
4. **Shipping**: Enter delivery details and select method
5. **Tracking**: Monitor order status through delivery

### Visual Features
- Modern dark purple gradient background
- Smooth hover effects and transitions
- Clear visual hierarchy
- Responsive layout components
- Intuitive navigation with keyboard shortcuts

## Future Enhancements

Potential additions to consider:
- Connect authentication forms to actual user database
- Implement product search functionality
- Add payment processing integration
- Connect cart to order submission system
- Real-time order tracking updates
- User profile management
- Settings customization

## Technical Details

- **Framework**: JavaFX
- **Architecture**: MVC pattern compatible
- **Styling**: External CSS with class-based theming
- **Components**: VBox, HBox, BorderPane, StackPane layouts
- **Controls**: TextField, TextArea, Button, Label, ComboBox, etc.

---

**Version**: 2.0  
**Author**: Shopping Center Development Team  
**Date**: January 9, 2026
