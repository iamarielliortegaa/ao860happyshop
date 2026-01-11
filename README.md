# 🛍️ Shopping Center - Modern E-Commerce Application v2.0

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Status](https://img.shields.io/badge/Status-Production%20Ready-green.svg)]()

> A complete, modern e-commerce platform with **order tracking**, **shipping management**, and a beautiful dual-theme UI.

---

## ✨ **What's New in v2.0**

### 🎨 Enhanced UI
- ✅ **Purple, visible, labeled header buttons** with emojis
- ✅ **Dark and Light mode** theme toggle
- ✅ Modern product cards with hover effects

### 📦 Order Tracking System ✨ **NEW**
- ✅ **6-stage delivery tracking** (Order Placed → Delivered)
- ✅ **Visual progress timeline** with status indicators
- ✅ **Required shipping information** collection
- ✅ **Real-time status updates** with admin controls

---

## 🚀 Quick Start

### Run Application
```bash
# Windows
.\mvnw.cmd clean javafx:run

# Linux/Mac  
./mvnw clean javafx:run

# Or with Maven
mvn clean javafx:run
```

### First Launch
- 30 products load automatically
- Browse without login
- Register to track orders

---

## 🎯 **Key Features**

### Shopping
- 🛍️ **30 products** across 6 categories
- 🔍 Search, filter, sort
- ⭐ Ratings & reviews
- ♥️ Wishlist

### Checkout
- 🛒 Smart shopping cart
- 📦 **Shipping information** (address, postal, city, country) ✨ NEW
- 💳 Payment simulation
- 📄 Invoice export (PDF/text)

### Order Tracking ✨ **NEW**
- 📋 **6-stage status** progression
- 🎯 Visual progress timeline
- 📍 Delivery tracking
- 📅 Estimated delivery dates

### User System
- 🔐 Login & Registration
- 👥 Admin/Customer roles
- 📊 Order history

---

## 📖 **Documentation**

| Document | Description |
|----------|-------------|
| **[QUICK_START.md](QUICK_START.md)** | Setup guide |
| **[FINAL_IMPLEMENTATION_SUMMARY.md](FINAL_IMPLEMENTATION_SUMMARY.md)** | Complete features |
| **[ORDER_TRACKING_IMPLEMENTATION.md](ORDER_TRACKING_IMPLEMENTATION.md)** | Tracking system |
| **[ORDER_TRACKING_FLOW.md](ORDER_TRACKING_FLOW.md)** | Flow diagrams |

---

## 🧪 **Test Order Tracking**

1. Add products to cart
2. Proceed to checkout
3. **Enter shipping info:** ✨
   - Address: "123 Main Street, Apt 4B"
   - City: "London"
   - Postal Code: "SW1A 1AA"
   - Country: "United Kingdom"
4. Complete payment
5. **Track in Profile** → My Orders ✨
6. (Admin) Advance status through 6 stages

---

## 📊 **Project Structure**

```
data/
├── products.csv (30 products)
├── orders.csv (tracking) ✨ NEW
└── discounts.csv

src/main/
├── java/ci553/shoppingcenter/
│   ├── model/ (Order ✨, Product, User)
│   ├── service/ (OrderTrackingService ✨, Cart, etc.)
│   └── client/ (ShoppingCenterView)
└── resources/
    └── shopping-center-styles.css (enhanced ✨)
```

---

## 🎨 **Visual Preview**

### Header Buttons ✨
```
[👤 Profile] [🔔 Notifications] [🔑 Login] [🛒 Cart (3)]
[📦 Orders] [🌙 Dark]
```

### Order Tracking ✨
```
Order #ORD-123456789    [🟣 Processing]
────────────────────────────────────────
📍 123 Main St...London, SW1A 1AA
📅 Estimated: January 16, 2026

[●]━[●]━[○]━[○]━[○]━[○]
Order Process Ship Transit Out Deliver
```

---

## 📋 **Features Checklist**

✅ Modern UI (dark + light mode)  
✅ 30 products, search, filter, sort  
✅ Ratings, reviews, wishlist  
✅ **Purple, visible header buttons** ✨  
✅ **Shipping info collection** ✨  
✅ **6-stage order tracking** ✨  
✅ **Visual progress timeline** ✨  
✅ Admin controls  
✅ Invoice export  

---

## 🎁 **Demo Accounts**

- **Admin:** admin / adminpass
- **Customer:** customer / custpass
- **Or register** your own account

---

## 📞 **Support**

All features are fully functional and tested.

See documentation for detailed guides.

---

## 🚀 **Get Started!**

```bash
.\mvnw.cmd clean javafx:run
```

**Everything works out of the box!** ✨

---

*Version 2.0 - Production Ready - January 11, 2026*
