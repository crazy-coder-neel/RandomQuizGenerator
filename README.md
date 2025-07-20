# 🎯 Random Quiz Generator

A **Java Servlet-based web application** that dynamically generates random quizzes from a **PostgreSQL database**. The project is deployed on an **Apache Tomcat** server, demonstrating the integration of core Java EE components, JDBC for database operations, and a simple yet functional frontend to provide an interactive quiz experience.

---

## 📚 Project Overview

This application allows users to take a randomly generated quiz consisting of multiple-choice questions. The backend logic is written using **Java Servlets**, which interact with a **PostgreSQL** database using **JDBC** to fetch and render questions dynamically. It's hosted on an **Apache Tomcat** server and is designed to be lightweight, fast, and modular for easy extension.

---

## 🚀 Features

- 🎲 Randomly generated quiz from the database each time
- 🖥️ Built with standard Java EE (Servlets, JSP)
- 🛠️ Hosted on Apache Tomcat server
- 📋 Retrieves quiz questions using SQL with `ORDER BY RANDOM()`
- ✅ Automatic answer checking and score display
- 💾 PostgreSQL backend with JDBC connectivity
- 🌍 Fully functional on local or deployed server

---

## 🧰 Technologies Used

| Component    | Technology     |
|--------------|----------------|
| Backend      | Java Servlets (J2EE) |
| Frontend     | HTML, CSS, JSP |
| Database     | PostgreSQL     |
| Server       | Apache Tomcat  |
| DB Connector | JDBC Driver (PostgreSQL JDBC) |
| Build Tool   |  Maven  |

---
⚙️ Setup Instructions
✅ Prerequisites

Java (JDK 8+)
Apache Tomcat (v9 or v10 recommended)
PostgreSQL
Any Java IDE (Eclipse / IntelliJ)
PostgreSQL JDBC Driver (add to lib/)

---
**Configure JDBC in servlet** :
String url = "jdbc:postgresql://localhost:5432/quizdb";
String user = "your_username";
String password = "your_password";
Connection conn = DriverManager.getConnection(url, user, password);
---
