# Online Movie Ticket Booking Management System
### SIT Pune | DBMS + Java Mini Project
**Team:** Parwez Khan | Rudra Patel | Prachi Jagota | Prisha Gupta  
**Guide:** Dr. Jitendra Rajpurohit

---

## 📁 Folder Structure

```
MovieBookingSystem/
│
├── database/
│   └── schema.sql                     ← Run this in MySQL first
│
└── src/
    └── com/
        └── moviebooking/
            │
            ├── db/
            │   └── DBConnection.java          ← JDBC Singleton connection
            │
            ├── exception/
            │   ├── MovieBookingException.java  ← Base custom exception
            │   ├── DatabaseConnectionException.java
            │   ├── SeatNotAvailableException.java
            │   ├── InvalidPaymentException.java
            │   └── UserNotFoundException.java
            │
            ├── model/
            │   ├── User.java                   ← Parent class (Inheritance root)
            │   ├── Admin.java                  ← extends User
            │   ├── Customer.java               ← extends User
            │   ├── Movie.java
            │   ├── Theatre.java
            │   ├── Screen.java
            │   ├── Seat.java
            │   ├── Show.java
            │   ├── Booking.java
            │   ├── Payment.java                ← Parent (2nd Inheritance root)
            │   ├── CardPayment.java            ← extends Payment
            │   ├── UPIPayment.java             ← extends Payment
            │   ├── NetBankingPayment.java      ← extends Payment
            │   ├── Cancellation.java
            │   ├── Refund.java
            │   └── DigitalInvoice.java
            │
            ├── dao/
            │   ├── UserDAO.java                ← DML + DRL for User
            │   ├── MovieDAO.java               ← DML + DRL for Movie
            │   ├── ShowDAO.java                ← DML + DRL for Show
            │   ├── BookingDAO.java             ← DML + DRL for Booking
            │   ├── PaymentDAO.java             ← DML + DRL for Payment + Invoice
            │   ├── TheatreDAO.java             ← DML + DRL for Theatre + Seat
            │   └── CancellationDAO.java        ← DML + DRL for Cancel + Refund
            │
            ├── service/
            │   ├── Bookable.java               ← Interface (book, cancel, view)
            │   ├── Payable.java                ← Interface (processPayment)
            │   └── BookingService.java         ← Implements Bookable
            │
            └── ui/
                └── MainMenu.java               ← CLI Entry Point (main method)
```

---

## 🛠️ OOPs Concepts Demonstrated

| Concept        | Where Used |
|---------------|------------|
| **Inheritance** | `Admin extends User`, `Customer extends User`, `CardPayment extends Payment`, `UPIPayment extends Payment`, `NetBankingPayment extends Payment` |
| **Polymorphism** | `getDetails()` overridden in Admin & Customer; `getPaymentSummary()` overridden in all 3 payment subclasses |
| **Interface** | `Bookable` (book, cancel, viewBookingDetails), `Payable` (processPayment) |
| **Packages** | `com.moviebooking.db`, `.model`, `.dao`, `.service`, `.exception`, `.ui` |
| **Encapsulation** | All fields are `private` with public getters/setters |
| **Exception Handling** | 5 custom exceptions + specific catch blocks |

---

## ⚙️ Setup Instructions

### Step 1 – MySQL Setup
```sql
-- Open MySQL and run:
source /path/to/database/schema.sql;
```
Or copy-paste the contents of `schema.sql` into MySQL Workbench and execute.

### Step 2 – Add MySQL JDBC Driver
Download `mysql-connector-j-x.x.x.jar` from https://dev.mysql.com/downloads/connector/j/  
Add it to your project classpath.

**In Eclipse:**
Right-click project → Build Path → Add External JARs → select the .jar

**In VS Code:**  
Add to `.classpath` or `lib/` folder and reference in launch config.

### Step 3 – Update DB Credentials
Open `src/com/moviebooking/db/DBConnection.java` and update:
```java
private static final String USER     = "root";      // your MySQL username
private static final String PASSWORD = "root";      // your MySQL password
```

### Step 4 – Compile and Run
```bash
# From the MovieBookingSystem/ directory:
javac -cp ".;lib/mysql-connector-j.jar" -d out src/com/moviebooking/**/*.java

# Run:
java -cp ".;out;lib/mysql-connector-j.jar" com.moviebooking.ui.MainMenu
```
*(Use `:` instead of `;` on Mac/Linux)*

---

## 👤 Default Login Credentials (from sample data)

| Role     | Email                        | Password   |
|----------|------------------------------|------------|
| Admin    | admin@moviebooking.com       | admin123   |
| Customer | parwez@gmail.com             | parwez123  |
| Customer | prachi@gmail.com             | prachi123  |
| Customer | prisha@gmail.com             | prisha123  |

---

## 📊 Database Tables (13 Tables)
`User` · `Theatre` · `Screen` · `Seat` · `Movie` · `Shows` · `Booking` · `Payment` · `DigitalInvoice` · `Cancellation` · `Refund`

---

## ✅ Marks Checklist

- [x] Classes, variables, methods, access specifiers (private/public/protected)
- [x] Inheritance (2 hierarchies: User→Admin/Customer, Payment→Card/UPI/NetBanking)
- [x] Polymorphism (Method Overriding in all subclasses)
- [x] Interfaces (Bookable, Payable)
- [x] Packages (6 packages)
- [x] Specific Exception Handling (5 custom exceptions)
- [x] DML – INSERT, UPDATE, DELETE (in every DAO)
- [x] DRL – SELECT (multiple queries per DAO)
- [x] JDBC Database Connectivity
