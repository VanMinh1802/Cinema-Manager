# 🎬 CineMe Manager - Cinema Management System

**CineMe Manager** is a comprehensive, professional desktop application designed to streamline the operations of modern movie theaters. Built with Java Swing and MySQL, it offers a robust solution for ticket booking, staff management, scheduling, and revenue analytics.

![Cinema Manager Banner](https://placeholder-banner-image-url.com) *(Optional: Replace with actual screenshot)*

## 🚀 Key Features

### 🌟 Core Operations
*   **Smart Ticket Booking:** Interactive seat map with real-time status (Available, Booked, Selected, VIP). Integrated checkout with concessions.
*   **Dual-Database System:** Seamlessly switch between **Cloud (Aiven)** and **Local (MySQL)** databases with hot-swap support (F12) to ensure continuous operation even during internet outages.
*   **Concessions Management:** Manage food & beverage menus, create combos, and track inventory.

### 👥 Management Modules
*   **Movie Scheduling:** Intuitive **Drag & Drop** interface for scheduling movie showtimes across multiple theaters. Avoids conflicts automatically.
*   **Staff & Shifts:** Manage employee profiles, accounts, and shift schedules with weekly planners and payroll estimation.
*   **Room & Seat Layout:** Visual editor to configure theater layouts, seat types (Standard, VIP, Double), and pricing.
*   **Loyalty Program:** Automated membership tiering (Silver, Gold, Diamond) and point accumulation system.

### 📊 Analytics & Reporting
*   **Real-time Dashboard:** Monitor KPI metrics like Daily Revenue, Ticket Sales, and Occupancy rates instantly.
*   **Traffic Analysis:** Visualize peak hours and customer traffic trends.
*   **Revenue Reports:** Detailed daily/monthly revenue breakdowns customizable by date range.

## 🛠️ Technology Stack
*   **Language:** Java (JDK 8+)
*   **GUI Framework:** Java Swing (Custom UI Components)
*   **Database:** MySQL (Cloud & Local)
*   **IDE:** NetBeans
*   **External Libraries:** MySQL JDBC Driver, JCalendar (Date handling)

## ⚙️ Installation & Setup

### Prerequisites
*   Java Development Kit (JDK) 8 or higher.
*   MySQL Server (for Local mode).
*   NetBeans IDE (recommended for editing).

### Setup Steps
1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/VanMinh1802/Cinema-Manager.git
    ```
2.  **Database Configuration:**
    *   The application supports a local MySQL instance. Import the `sql/Cinema_db.sql` file into your local MySQL server.
    *   (Optional) Configure connection strings in `src/com/cinema/db/DBConnection.java` or `database.properties` if used.
3.  **Run the Application:**
    *   Navigate to the `CinemaManager_Release` directory.
    *   Run `RunApp.bat` (Windows).

## 📖 Usage Guide

*   **Login:**
    *   **Admin:** `admin` / `123456` (Full access)
    *   **Staff:** `staff` / `123456` (Limited access: Booking, Concessions)
*   **Hot-Keys:**
    *   `F12`: Toggle between Local and Cloud database modes at login.

## 🤝 Contribution
Contributions are welcome! Please fork the repository and submit a Pull Request.

## 📞 Contact
**Project Lead:** Nguyen Van Minh  
**Email:** nguyenminh180220@gmail.com  
**GitHub:** [VanMinh1802](https://github.com/VanMinh1802)
