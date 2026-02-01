# 🎬 Cinema Manager

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

> **Cinema Manager** is a comprehensive desktop application designed to streamline cinema operations, from ticket booking and seat selection to staff scheduling and revenue reporting. Built with Java Swing and MySQL.

![Dashboard Preview](doc/images/dashboard-preview.png)
*(Note: Screenshots are placeholders. Please look at the actual application run)*

## 📚 Table of Contents
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Installation](#-installation)
- [Project Documentation](#-project-documentation)
- [Team](#-team)

## ✨ Key Features
- **Dual-Mode Database:** Works seamlessly both Online (Cloud) and Offline (Localhost), ensuring business continuity.
- **Interactive Seat Map:** Visual seat selection with real-time status updates (Available, Booked, Selected) using Custom Graphics2D.
- **Smart Scheduling:** Drag-and-drop interface for scheduling movies, with automatic conflict detection.
- **Revenue Analytics:** Detailed reports and charts for sales performance, exportable to Excel.
- **Role-Based Access:** Secure login for Admins (Full Access) and Staff (Sales Only).

## 🛠 Technology Stack
- **Language:** Java JDK 17
- **GUI Framework:** Java Swing (with FlatLaf for modern UI)
- **Database:** MySQL 8.0
- **Build Tool:** Maven
- **Libraries:** Apache POI (Excel Export), BCrypt (Password Hashing), JCalendar.

## 🚀 Installation

### Prerequisites
- Java Development Kit (JDK) 17 or higher.
- MySQL Server 8.0+.

### Setup Support
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/VanMinh1802/Cinema-Manager.git
    cd Cinema-Manager
    ```
2.  **Database Setup:**
    - Open MySQL Workbench or phpMyAdmin.
    - Create a database named `cinema_db`.
    - Import the script `DB/cinema_db.sql`.
3.  **Run the Application:**
    - Double-click `RunApp.bat` (Windows).
    - Or run via command line:
      ```bash
      java -jar dist/CinemaManager.jar
      ```

## 📄 Project Documentation
For detailed information, please refer to the following documents:

- **[🔍 Functional Requirements](FUNCTIONAL_REQUIREMENTS.md)**: Detailed breakdown of system functions.
- **[🏗 System Design](SYSTEM_DESIGN.md)**: MVC Architecture, Database Schema, and Data Flow.
- **[💾 Database Schema](DATABASE_SCHEMA.md)**: Table structures and relationships.
- **[⚙️ Technologies Used](TECHNOLOGIES.md)**: Deep dive into the tech stack.
- **[✅ Detailed Functions](DETAILED_FUNCTIONS.md)**: Step-by-step description of each feature.

## 👥 Team
| Member | Role | ID |
| :--- | :--- | :--- |
| **[Phạm Mỹ Hạ]** | Team Leader & UI/UX | [24210208] |
| **[Nguyễn Văn Minh]** | Backend & Database | [24210232] |
| **[Nguyễn Hồng Phúc]** | Admin Features (Movies/Schedule) | [24210239] |
| **[Lê Uy Võ]** | Reports & Documentation | [24210280] |

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Developed by Group [Số Nhóm] - UIT*
