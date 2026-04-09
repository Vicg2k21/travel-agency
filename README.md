# Travel Agency Lodging System

## 📌 Overview
This is a Java-based desktop application developed using Apache NetBeans. The system allows users to manage lodging reservations, including account login, registration, and guest access. Users can browse and manage lodging options, select booking dates, and generate reports of their reservations.

## 🚀 Features

### 🔐 User Management
- User login with existing account
- New user registration
- Option to continue as a guest

### 🏡 Lodging Management
- View available lodges (houses or hotels)
- Add new lodges
- Remove existing lodges
- Attach images to lodges

### 📅 Booking System
- Select start and end dates for lodging
- Manage reservation details

### 📊 Reporting
- Generate a report of selected lodging and booking details

## 🛠 Technologies Used
- Java
- Apache NetBeans IDE
- Object-Oriented Programming (OOP)
- File handling (TXT-based data storage)
- MySQL Workbench (database for lodging and structured data)

## 🗄 Data Management
This application uses a combination of database and file-based storage:

- MySQL database is used to manage structured data such as lodging information.
- TXT files are used to store user-related data such as customers and employees.

This approach demonstrates flexibility in handling different types of data storage.

## 📸 Screenshot

![Application Screenshot](https://raw.githubusercontent.com/Vicg2k21/Vicg2k21/main/Images/travel.png)

## 🎥 Demo

![Demo](https://raw.githubusercontent.com/Vicg2k21/Vicg2k21/main/Images/travel-demo.gif)

## ▶️ How to Run
1. Open the project in Apache NetBeans
2. Build the project
3. Run the application
4. Login, register, or continue as a guest to begin using the system

## 📂 Project Structure
- `src/` → Source code
- `DatabaseCreation/` → MySQL database scripts and setup
- `*.txt` files → Data storage for customers and employees
