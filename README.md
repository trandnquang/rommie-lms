# 🏠 Roomie LMS - Backend Server (Java Spring Boot)

## 📋 Overview
Module này chịu trách nhiệm xử lý Logic nghiệp vụ (Business Logic) và quản lý dữ liệu cho hệ thống Roomie LMS. 
Dự án sử dụng **PostgreSQL** làm cơ sở dữ liệu chính, được triển khai qua **Docker**.

---

## 🛠 Prerequisites (Điều kiện tiên quyết)
Trước khi bắt đầu, hãy đảm bảo máy bạn đã cài đặt:
* **Docker & Docker Compose**
* **Java 17+ (JDK)**
* **IntelliJ IDEA** (Recommended)
* **PostgreSQL Client** (DBeaver hoặc pgAdmin) để kiểm tra dữ liệu.

---

## 🚀 Setup & Installation (Hướng dẫn cài đặt)

### 1. Khởi động Database (Docker)
Di chuyển vào thư mục `roomie-server` và chạy lệnh sau để khởi động PostgreSQL container ở chế độ chạy ngầm (detached mode):

```bash
cd roomie-server
docker-compose up -d