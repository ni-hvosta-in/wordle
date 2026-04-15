# 🚀 Wordle App

## 📖 About

This project is a Wordle-like game designed to help improve English vocabulary.
Players guess a hidden word while receiving feedback after each attempt.

The game includes **difficulty levels based on English proficiency**, allowing users to practice words appropriate to their level.

---

## 🎮 Game Rules

* You need to guess a hidden English word within a limited number of attempts
* After each guess, you receive feedback:

  * 🟩 correct letter in the correct position
  * 🟨 correct letter in the wrong position
  * ⬜ letter is not in the word
* The number of attempts is limited (default: 6)

---

## 🎯 Difficulty Levels

The game uses English proficiency levels based on the CEFR standard:

* **A1 (Beginner)** — very basic everyday words
* **A2 (Elementary)** — simple vocabulary used in common situations
* **B1 (Intermediate)** — more complex and descriptive words
* **B2 (Upper-Intermediate)** — advanced vocabulary with less frequent usage

This system helps players gradually improve their English skills by practicing words appropriate to their level.


---

## 📦 Stack

* Backend: Java + Spring Boot
* Frontend: React + TypeScript
* Database: PostgreSQL
* Docker

---

## 🔧 Development (recommended)

### 1. Run database

```bash
docker-compose up postgres
```

Database will be available at:

```
localhost:5433
```

---

### 2. Run backend

Start from IDE (IntelliJ) or:

```bash
./gradlew bootRun
```

---

### 3. Run frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend will be available at:

```
http://localhost:5173
```

---

## 📦 Run with Docker

```bash
docker-compose up --build
```

---

### 🌐 Access

* Frontend: http://localhost:3000
* Backend: http://localhost:8080

---

## ⚙️ Environment variables

Backend uses:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
WORD_GAME_SEED
```

---

## 🧠 Notes

* The game is intended as a tool for learning and practicing English vocabulary
* For development it's recommended to run backend locally (faster)
* Docker is used for full application run
