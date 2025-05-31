# Midas 💰
Project repository for the **JPMC Advanced Software Engineering Virtual Experience** on Forage.

![Certificate](assets/completion_certificate.png)
![Certificate Link](https://forage-uploads-prod.s3.amazonaws.com/completion-certificates/Sj7temL583QAYpHXD/E6McHJDKsQYh79moz_Sj7temL583QAYpHXD_uJLPADKzu3bmQkzxa_1748684124952_completion_certificate.pdf)

---

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-0066CC?style=for-the-badge&logo=databricks&logoColor=white)
![REST API](https://img.shields.io/badge/REST%20API-005571?style=for-the-badge&logo=rest&logoColor=white)

---

## 📄 About

This project simulates a real-world financial backend system:
- Consumes transactions via **Kafka**
- Calculates incentives via an external **Incentive API**
- Persists data using **Spring Data JPA**
- Serves user balances via **REST endpoints**
- Uses **H2 in-memory database** for testing

It’s part of the virtual internship simulation offered by **J.P. Morgan & Co. on Forage** to gain hands-on experience with backend microservices, APIs, and messaging systems.
Enroll in program: (https://www.theforage.com/simulations/jpmorgan/advanced-software-engineering-r0fm)
---

## 📸 Certificate

> Located at `assets/jpmc-advanced-software-cert.png`

---

## 📦 Run Locally

```bash
# Run incentive API
./gradlew :incentive-api:bootRun

# Run midas core
./gradlew :midas-core:bootRun
