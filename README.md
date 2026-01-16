ao860happyshop is a Java-based desktop shopping management system developed for the CI553 Object-Oriented Development and Testing module at the University of Brighton.

The application supports both administrative and customer workflows and is built using Java and JavaFX, following a layered MVC architecture. The codebase is intentionally structured to be clean, readable, and easy to navigate, with a strong focus on maintainability and separation of concerns.

Getting Started / How to Run
Prerequisites

Java JDK 17 (or compatible version)

IntelliJ IDEA (recommended) or another Java IDE

JavaFX properly configured (bundled or added as a dependency)

Running the Application

Clone the repository:

git clone https://github.com/iamarielliortegaa/ao860happyshop.git


Open the project in IntelliJ IDEA.

Ensure JavaFX is correctly configured:

VM options may be required, for example:

--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml


Locate the main application class and run it from the IDE.

The application will launch as a desktop JavaFX interface.

Data Storage

Application data is stored locally using CSV files.

If data files are missing or empty, they are automatically recreated on startup.

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

