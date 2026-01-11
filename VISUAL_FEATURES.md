# 🎨 Visual Features Showcase

## Shopping Center Application - UI/UX Highlights

This document showcases the visual design and user experience features of the Shopping Center application.

---

## 🌓 Dual Theme Support

### Dark Mode (Default)
- **Background**: Deep purple gradient (#1a0f30 → #0e071a)
- **Sidebar**: Dark purple (#170b2b)
- **Text**: Light colors (#eae6f7, #bfb6dd)
- **Accents**: Purple (#7c4dff), Pink (#ff8db8), Green (#5ce6b8)
- **Cards**: Dark gradient with glowing borders

### Light Mode
- **Background**: Light gradient (#f5f7fa → #dce4f0)
- **Sidebar**: White (#ffffff → #f8f9fb)
- **Text**: Dark colors (#1a202c, #2d3748)
- **Accents**: Purple (#6b46c1), Pink (#ed64a6), Teal (#38b2ac)
- **Cards**: White with subtle shadows

**Toggle**: Click "Dark" or "Light" button in header - instant theme switch!

---

## 🏠 Home Page Dashboard

### Layout
```
┌─────────────────────────────────────────────┐
│  Welcome to Shopping Center!                │
│  Your one-stop shop for everything you need │
├─────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │    30    │  │    6     │  │    0     │  │
│  │ Products │  │Categories│  │   Cart   │  │
│  └──────────┘  └──────────┘  └──────────┘  │
├─────────────────────────────────────────────┤
│           Quick Actions                      │
│  ┌────────────────┐  ┌────────────────┐    │
│  │ Browse Products│  │   View Cart    │    │
│  └────────────────┘  └────────────────┘    │
│  ┌────────────────┐  ┌────────────────┐    │
│  │   My Profile   │  │  Order Tracker │    │
│  └────────────────┘  └────────────────┘    │
└─────────────────────────────────────────────┘
```

### Features
- **Large welcome header** with emoji icon
- **3 stat cards** showing live counts (purple, green, pink)
- **4 action buttons** with emoji icons (🛍️ 🛒 👤 📦)
- **Responsive layout** adapts to window size

---

## 🛍️ Shop Page - Product Grid

### Header Bar
```
┌─────────────────────────────────────────────────────────┐
│ 🔎 [Search...]  [Category▼]  [$min] [$max] [Sort▼] [Apply] │
└─────────────────────────────────────────────────────────┘
```

**Search Filters:**
- Text search (name/category)
- Category dropdown (All, Electronics, Clothing, etc.)
- Min/Max price range
- Sort: Featured, Price ↑, Price ↓, Popularity

### Product Cards (Grid Layout)
```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  [Image]     │  │  [Image]     │  │  [Image]     │
│              │  │  OUT OF STOCK│  │              │
│ Product Name │  │ Product Name │  │ Product Name │
│ $79.99       │  │ $199.99      │  │ $12.99       │
│ Electronics  │  │ Electronics  │  │ Electronics  │
│ Stock: 45    │  │ Stock: 0     │  │ Stock: 150   │
│ ★★★★★ (4.7)  │  │ ★★★★☆ (4.9)  │  │ ★★★★★ (4.8)  │
│ Qty: [▼ 1]   │  │ Qty: [▼ 1]   │  │ Qty: [▼ 1]   │
│ [Add to Cart]│  │ [Add to Cart]│  │ [Add to Cart]│
│ [♡][Review]  │  │ [♡][Review]  │  │ [♡][Review]  │
└──────────────┘  └──────────────┘  └──────────────┘
```

### Card Features
- **Product image**: 250x180px, preserves ratio
- **Product name**: Bold, wraps if long
- **Price**: Large, purple ($79.99)
- **Category pill**: Rounded badge
- **Stock indicator**: Shows quantity or "Out of stock"
- **Star ratings**: Gold stars (★★★★★) with average
- **Quantity spinner**: 1 to stock limit
- **Add to Cart button**: Primary style (gradient purple)
- **Wishlist heart**: ♡ → ♥ on click
- **Review button**: Opens review dialog
- **Out of stock badge**: Red overlay on image (when stock = 0)

### Hover Effects
- Card border glows purple
- Slight shadow increase
- Smooth transition

---

## 🛒 Shopping Cart Dialog

### Layout
```
┌─────────────────────────────────────────┐
│  Shopping Cart                          │
├─────────────────────────────────────────┤
│ ┌─────────────────────────────────────┐ │
│ │ [img] Wireless Headphones           │ │
│ │       $79.99 each                    │ │
│ │       Available: 45                  │ │
│ │       [▼ 2] [Update] $159.98 [Remove]│ │
│ └─────────────────────────────────────┘ │
│ ┌─────────────────────────────────────┐ │
│ │ [img] Smart Watch                   │ │
│ │       $199.99 each                   │ │
│ │       Available: 30                  │ │
│ │       [▼ 1] [Update] $199.99 [Remove]│ │
│ └─────────────────────────────────────┘ │
├─────────────────────────────────────────┤
│ Subtotal: $359.97                       │
│ Discount: [code____] [Apply] ___        │
│ [Proceed to Payment]                    │
└─────────────────────────────────────────┘
```

### Features
- **Product cards** with images (80x60px)
- **Quantity spinners** with Update button
- **Stock availability** shown per item
- **Line totals** calculated in real-time
- **Discount code** field with Apply button
- **Validation**: Can't exceed stock, must be positive
- **Remove button** per item
- **Proceed to Payment** button (primary style)

### Discount Feedback
- **Valid code**: Green text "Applied 10% discount"
- **Invalid code**: Red text "Invalid code"
- **Updated subtotal**: Shows original and discounted price

---

## 👤 Login / Register Dialog

### Tabbed Interface
```
┌─────────────────────────────────────┐
│ [ Login ] [ Register ]              │
├─────────────────────────────────────┤
│  Login to Your Account              │
│                                     │
│  Username:                          │
│  [________________]                 │
│                                     │
│  Password:                          │
│  [****************]                 │
│                                     │
│  [        Login        ]            │
│                                     │
│  ❌ Invalid credentials             │
└─────────────────────────────────────┘
```

### Login Tab
- Username field (required)
- Password field (required)
- Login button (full width, primary)
- Error message (red text)
- Enter key submits

### Register Tab
- Username field (min 3 chars)
- Display name field (optional)
- Password field (min 6 chars)
- Confirm password field (must match)
- Register button (full width, primary)
- Success message (green) or error (red)
- Auto-switch to Login on success

### Validation Messages
- "Username is required"
- "Username must be at least 3 characters"
- "Password is required"
- "Password must be at least 6 characters"
- "Passwords do not match"
- "Username already exists"
- ✅ "Registration successful!" (green)

---

## ⚙️ Settings / Admin Page

### Sections

#### 1. Role Switcher
```
Current role: [Guest ▼] [Switch Role]
```
Options: Guest, Customer, Admin

#### 2. Change Password
```
Current password: [****************]
New password:     [****************]
Confirm:          [****************]
[Change Password]
```

#### 3. Product Management (Scrollable)
```
Products
┌─────────────────────────────────────┐
│ p001: Wireless Headphones - $79.99  │
│ [Edit] [Remove]                     │
├─────────────────────────────────────┤
│ p002: Smart Watch - $199.99         │
│ [Edit] [Remove]                     │
├─────────────────────────────────────┤
│ ...more products...                 │
└─────────────────────────────────────┘

Add New Product:
[id___] [name_______] [$____] [image__] [Choose] 
[Add Product]
✅ Product added successfully!
```

#### 4. Discount Management
```
Discount codes
SAVE10 - 10% [Remove]
SAVE20 - 20% [Remove]

[code___] [percent] [Add Discount]
```

### Edit Product Dialog
- Product ID (read-only)
- Name field
- Price field (numeric)
- Category field
- Stock field (numeric)
- Image filename field
- [Choose Image] button with file picker
- [Save] button
- Validation messages

---

## 📦 Order Tracker (Admin Only)

### Layout
```
┌─────────────────────────────────────┐
│  Order Tracker                      │
├─────────────────────────────────────┤
│  [Refresh]                          │
│                                     │
│  Notifications                      │
│  ┌───────────────────────────────┐ │
│  │ [Notification text area...]   │ │
│  └───────────────────────────────┘ │
│                                     │
│  Progressing orders                 │
│  order_123.txt [View] [Promote]     │
│  order_124.txt [View] [Promote]     │
│                                     │
│  Ordered orders                     │
│  order_120.txt [View] [Collected]   │
│  order_121.txt [View] [Collected]   │
└─────────────────────────────────────┘
```

### Features
- Refresh button updates all orders
- Notification area shows system messages
- Progressing orders can be promoted to Ordered
- Ordered orders can be marked as Collected
- View button shows order receipt

---

## 🎨 Color Palette

### Dark Mode
| Element | Color | Hex |
|---------|-------|-----|
| Background | Deep Purple | #1a0f30 → #0e071a |
| Sidebar | Dark Purple | #170b2b |
| Text Primary | Light Gray | #eae6f7 |
| Text Secondary | Muted | #bfb6dd |
| Accent Primary | Purple | #7c4dff |
| Accent Success | Green | #5ce6b8 |
| Accent Error | Pink | #ff5a8a |
| Star Rating | Gold | #ffd700 |

### Light Mode
| Element | Color | Hex |
|---------|-------|-----|
| Background | Light Blue-Gray | #f5f7fa → #dce4f0 |
| Sidebar | White | #ffffff |
| Text Primary | Dark Gray | #1a202c |
| Text Secondary | Muted | #718096 |
| Accent Primary | Purple | #6b46c1 |
| Accent Success | Teal | #38b2ac |
| Accent Error | Red | #f56565 |
| Star Rating | Gold | #ffd700 |

---

## 🔤 Typography

### Font Sizes
- **Page Title**: 32px, bold (Home page welcome)
- **Section Title**: 24px, bold (form-title class)
- **Subsection**: 20px, bold (Settings sections)
- **Price (large)**: 18px, bold (product cards)
- **Normal**: 14-16px (body text, labels)
- **Small**: 11-12px (stock info, badges)

### Font Weights
- **Bold**: Titles, prices, important info
- **Semi-bold**: Navigation, form labels
- **Regular**: Body text, descriptions

---

## 📐 Spacing & Layout

### Padding
- **Pages**: 16-24px around content
- **Cards**: 16-20px internal
- **Buttons**: 12-16px vertical, 24-32px horizontal
- **Forms**: 10-12px between fields

### Margins
- **Between sections**: 16-24px
- **Between elements**: 8-12px
- **Stat cards**: 16px gap

### Card Dimensions
- **Product card**: 280px width, auto height
- **Stat card**: 200px width, auto height
- **Images**: 250x180px (products), 80x60px (cart)

---

## 🎯 Interactive Elements

### Buttons

#### Primary Button
- Purple gradient background
- White text, bold
- Rounded corners (10px)
- Hover: Darker purple
- Uses: Add to Cart, Login, Register, Proceed to Payment

#### Secondary Button
- Transparent/light background
- Colored border
- Hover: Slight background
- Uses: Update, Remove, Edit, Review

#### Icon Buttons
- Heart (♡ → ♥)
- Notification bell (🔔)
- Emojis in quick actions (🛍️ 🛒 👤 📦)

### Form Fields
- Rounded corners (10px)
- Border: Subtle in normal state
- Border: Purple/blue on focus
- Placeholder text: Muted color
- Validation: Border turns red on error

### Dropdowns (ComboBox)
- Category selector
- Role switcher
- Sort selector
- Rounded styling matches text fields

### Spinners
- Quantity selectors
- Min/Max values enforced
- Editable or arrow buttons
- Rounded styling

---

## ✨ Animations & Transitions

### Toast Notifications
- Fade in from bottom-right
- Display for 1.6 seconds
- Fade out automatically
- Semi-transparent dark background
- White text

### Theme Toggle
- Instant switch (no animation)
- All colors update simultaneously
- Smooth visual transition

### Card Hover
- Border color change (→ purple)
- Shadow intensity increase
- Smooth CSS transition

### Button Hover
- Background color change
- Slight scale or shadow (optional)
- Cursor changes to pointer

---

## 📱 Responsive Design

### Product Grid
- **FlowPane** layout automatically wraps
- **Card width**: Fixed at 280px
- **Gaps**: 16px horizontal and vertical
- **Adapts**: Based on window width
  - Wide window: 4-5 cards per row
  - Medium: 3 cards per row
  - Narrow: 2 cards per row

### Sidebar
- Fixed width: 280px
- Scrollable if content exceeds height

### Content Area
- **ScrollPane** for long content
- Fits to width automatically
- Vertical scroll as needed

---

## 🎭 Visual Hierarchy

### Information Priority (High → Low)

1. **Page Title**: Largest, boldest (32px)
2. **Product Price**: Large, colored (18px purple)
3. **Product Name**: Bold, prominent (15px)
4. **Action Buttons**: Bright colors, clear labels
5. **Stats/Counts**: Large numbers with small labels
6. **Category/Stock**: Small pills and badges
7. **Ratings**: Visual stars with number
8. **Secondary Info**: Muted text color

### Visual Weight
- **Primary actions**: Bright gradient buttons
- **Destructive actions**: Red text or border (Remove, Delete)
- **Success messages**: Green text
- **Error messages**: Red text
- **Muted info**: Gray text

---

## 🏆 Accessibility Features

### Contrast
- High contrast in both themes
- Text readable on all backgrounds
- Buttons clearly distinguishable

### Labels
- All form fields labeled
- Tooltips on icon buttons
- Clear button text (no icons-only)

### Feedback
- Visual feedback on all actions
- Toast messages for success
- Alert dialogs for errors
- Color + text (not color alone)

### Navigation
- Keyboard shortcuts shown (H, P, S)
- Tab navigation through forms
- Enter key submits forms
- Esc closes dialogs

---

## 🎬 User Flow Examples

### Guest Shopping Flow
1. Launch app → See 30 products in grid
2. Browse → Use search/filters
3. Select product → Choose quantity
4. Add to cart → See toast "Added to cart"
5. Click Cart button → See cart dialog with items
6. Update quantity → Click Update
7. Apply discount code → See discount applied
8. Proceed to Payment → Enter fake card info
9. Complete → See receipt, export PDF

### User Registration Flow
1. Click "Login / Register"
2. Go to Register tab
3. Enter username (validated: min 3 chars)
4. Enter password (validated: min 6 chars)
5. Confirm password (validated: must match)
6. Click Register → See success message
7. Auto-switch to Login tab
8. Login with new credentials
9. See "Welcome back!" toast

### Admin Product Management Flow
1. Go to Settings
2. Switch Role → Select "Admin"
3. Scroll to Products section
4. Click Edit on a product
5. Update price/stock/name
6. Click Save → See update confirmation
7. Or add new product at bottom
8. Fill all required fields
9. Click Add Product → See success message
10. Product appears in Shop grid immediately

---

## 🌟 Polish & Details

### Micro-interactions
- Button hover effects
- Card hover glows
- Spinner increment/decrement
- Toast slide-in
- Theme instant switch

### Icons & Emojis
- 🔎 Search
- 🛍️ Browse Products
- 🛒 Cart
- 👤 Profile
- ⚙️ Settings
- 📦 Orders
- 🔔 Notifications
- ★ Ratings (filled)
- ☆ Ratings (empty)
- ♡ Wishlist (empty)
- ♥ Wishlist (filled)

### Badges & Pills
- Category pills (rounded, subtle background)
- Out of stock badge (red, overlay on image)
- Active nav pill (purple, in sidebar)
- Discount applied (green text)

### Shadows & Depth
- Cards: Subtle shadow increases on hover
- Dialogs: Overlay background
- Buttons: Slight depth with gradient
- Product images: Clean, no shadow

---

**This comprehensive visual design creates a cohesive, professional, and delightful shopping experience!** 🎨✨

