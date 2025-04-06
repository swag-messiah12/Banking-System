# JavaFX Banking System

A simple, modern banking management system built with **JavaFX** and **SQLite**. This project supports **admin**, **employee**, and **customer** roles, and includes all essential features for a small-scale banking app.

# Features

# Admin
- View all users with live search
- Add employees
- Reset any user’s password
- Delete users
- See roles and balances

# Employee
- Assist customers:
  - View customer balances
  - Deposit/Withdraw on behalf of customers
  - Reset customer passwords
  - View customer transaction history
  - Open/Close accounts

# Customer
- Register/login
- View balance
- Deposit / Withdraw money
- View personal transaction history
- Change own password

# Technologies Used

- Java 21
- JavaFX 24
- SQLite (embedded)
- IntelliJ IDEA
- Git & GitHub

# Setup Instructions

1. Clone the project:
   git clone https://github.com/swag-messiah12/Banking-System.git

2. Open in IntelliJ:
   - Open the project folder
   - Make sure JavaFX SDK is configured

3. Add VM options under Run > Edit Configurations:
   --module-path "path/to/javafx-sdk-24/lib" --add-modules javafx.controls,javafx.fxml

4. Run `MainApp.java`

#  Authors

- Tanveer Singh *(Swag Messiah)*  
- Yahya Fazal  
- Tabish Hassan  
- Mian Ahmed

# License

This project is open source and free to use under the MIT License.
