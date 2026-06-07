readme_content = """# 🍔 CraveHub - Full-Stack Food Delivery Web Application

![Java](https://img.shields.io/badge/Java-17-orange.svg?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.3-brightgreen.svg?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg?style=for-the-badge&logo=mysql)
![Razorpay](https://img.shields.io/badge/Payment-Razorpay-blue.svg?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Security-OAuth2-green.svg?style=for-the-badge)

Welcome to **CraveHub**! This is an enterprise-grade, full-stack **Online Food Delivery System** and **Restaurant Management Dashboard** that I built using **Java Spring Boot**, **Hibernate**, and **MySQL**. 

The main goal behind this project was to move beyond basic CRUD applications and build a secure, real-world platform that can handle live payments, data analytics, and robust role-based user authentication. It completely digitizes the food ordering process, bridging the gap between customers and restaurant owners.

> **Keywords / Topics:** `Spring Boot E-commerce Project`, `Java Full-Stack Application`, `Online Food Delivery System`, `Restaurant Management System`, `Razorpay API Integration`, `Google OAuth2 SSO`, `Spring Security Role-Based Access Control (RBAC)`.

---

## ✨ Key Features & Project UI

### 1. Interactive UI & Custom Chatbot
A modern, responsive frontend built with Glassmorphism design principles. It includes **FoodieBot**, a custom JavaScript-powered chatbot for instant customer support and menu navigation.
<br>
<img src="Images/Home Page.png" alt="Home Page" width="800"/>

### 2. Advanced Authentication (OAuth2, OTP & SMTP Registration)
Secure, multi-layered onboarding system powered by **Spring Security**. 
* **Users:** Quick login via **Google OAuth 2.0 (SSO)**. New users can securely register using traditional email and password, which requires real-time **JavaMailSender SMTP Email OTP** verification to prevent fake accounts.
* **Admins:** Isolated, secure portal for restaurant managers.
<br>
<img src="Images/Register Page.png" alt="Register Page" width="400"/> <img src="Images/Email Verification.png" alt="Email Verification" width="390"/>
<br>
<img src="Images/Login Page.png" alt="Login Page" width="400"/> <img src="Images/CraveHub Emai.jpg" alt="Email Inbox OTP" width="390"/>

### 3. Dynamic Digital Menu & Smart Search
Real-time product catalog fetched dynamically from the **MySQL Database**. Features a keyword-based **Smart Search Engine** for instant food filtering and an intuitive shopping cart with automated bill calculation.
<br>
<img src="Images/Menu.png" alt="Menu" width="800"/>
<br>
<img src="Images/Food Order.png" alt="Food Order" width="800"/>

### 4. Real-World Payment Integration (Razorpay API)
Fully functional checkout system integrated directly with the **Razorpay Payment Gateway**. The backend cryptographically verifies payment signatures (HMAC SHA256) before generating unique order IDs, ensuring secure, fraud-free transactions.
<br>
<img src="Images/Razorpay Page.png" alt="Razorpay Checkout" width="800"/>
<br>
<img src="Images/Payment Progess.png" alt="Payment Processing" width="400"/> <img src="Images/Payment Successful.png" alt="Payment Success" width="400"/>

### 5. Live Order Tracking
Customers can track their order lifecycle in real-time on a dedicated dashboard. Statuses update dynamically (Placed ➔ Preparing ➔ Out for Delivery ➔ Delivered) as the admin processes the order.
<br>
<img src="Images/Order Tracking.png" alt="Order Tracking" width="800"/>

### 6. Admin Business Intelligence Dashboard
A comprehensive control panel for the restaurant owner.
* **Data Analytics:** Interactive visual charts (built with **Chart.js**) tracking daily revenue, top-selling items, and total orders.
* **Inventory & Moderation:** Complete CRUD control over the menu and advanced community moderation tools (ability to instantly **Block/Unblock** users).
<br>
<img src="Images/Admin Dashboard Analytis.png" alt="Admin Analytics" width="800"/>
<br>
<img src="Images/Order Dashboard.png" alt="Order Dashboard" width="800"/>
<br>
<img src="Images/Inventory Management.png" alt="Inventory Management" width="800"/>

---

## 🛠️ Tech Stack & Architecture

* **Backend:** Java 17, Spring Boot 3.1.3, Spring Data JPA (Hibernate), Spring Security
* **Database:** MySQL 8.0 (ACID compliant for transaction integrity)
* **Frontend:** HTML5, CSS3, Vanilla JavaScript, Thymeleaf (Server-Side Rendering)
* **APIs & Tools:** Razorpay API, Google OAuth 2.0 API, JavaMail API, Apache Maven, Chart.js

---

## 🚀 How to Run the Project Locally

Follow these exact steps to set up and run CraveHub on your local machine:

### Step 1: Clone the Repository
Open your terminal or command prompt and clone the project using Git:
```bash
git clone [https://github.com/Priyanshu7204/CraveHub.git](https://github.com/Priyanshu7204/CraveHub.git)
cd CraveHub
