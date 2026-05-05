# GRC Onboarding Wizard
Tool-81 — Onboarding Wizard

#  **Overview**

Tool-81 is an AI-powered onboarding management system designed to help teams create, manage, and analyze onboarding workflows efficiently.

It integrates:

Spring Boot backend (Java)
React frontend
Flask AI microservice
PostgreSQL + Redis
Groq AI (LLaMA model)

The system allows users to:

Create onboarding records
Get AI-generated descriptions & recommendations
Generate reports
Track progress via dashboard

# **Architecture Diagram**

+----------------------+
|     React Frontend   |
|     (Port 80)        |
+----------+-----------+
|
v
+----------------------+
|   Spring Boot API    |
|     (Port 8080)      |
+----------+-----------+
|
-----------------------------------------
|                                       |
v                                       v
+-------------------+                +-------------------+
|   PostgreSQL DB   |                |      Redis        |
|     (Port 5432)   |                |   (Cache Layer)   |
+-------------------+                +-------------------+

                           |
                           v
                +----------------------+
                |   Flask AI Service   |
                |     (Port 5000)      |
                +----------+-----------+
                           |
                           v
                     Groq API (LLM)


Prerequisites

Make sure you install:

Tool	            Version
Java	              17
Maven	              3+
Node.js	              18+
Python	              3.11
Docker	              Latest
Git 	              Latest


# Setup Instructions 

# 1. Clone Repository

   git clone <your-repo-url>
   cd onboarding-wizard

# 2. Create .env file

   cp .env.example .env

# 3. Start Full Project (Docker)

   docker-compose up --build

# 4. Access Applications

   Service	URL
   Frontend	http://localhost
   Backend API	http://localhost:8080
   Swagger UI	http://localhost:8080/swagger-ui.html
   AI Service	http://localhost:5000/health

# 5. Stop Services

   docker-compose down
    Environment Variables (.env)
   Variable	        Description	                Example
   DB_URL	        PostgreSQL URL	            jdbc:postgresql://postgres:5432/tooldb
   DB_USER	DB      username	                postgres
   DB_PASSWORD	    DB password	                postgres
   REDIS_HOST	    Redis host:	                redis
   REDIS_PORT	    Redis port	                6379
   JWT_SECRET	    JWT signing key	            secret123
   GROQ_API_KEY	    Groq API key	            gsk_xxx
   MAIL_USER	    Email username	            example@gmail.com
   MAIL_PASSWORD	Email password:	            password



your-project-folder/
|-- backend/ <- Spring Boot project
| |-- src/main/java/com/internship/tool/
| | |-- controller/ <- REST endpoints
| | |-- service/ <- Business logic
| | |-- repository/ <- DB queries
| | |-- entity/ <- JPA table models
| | |-- config/ <- Security, Redis, Mail
| | +-- exception/ <- Custom exceptions
| |-- src/main/resources/
| | |-- db/migration/ <- V1__init.sql, V2__...
| | +-- application.yml <- All config (env variable references)
| +-- pom.xml
|
|-- ai-service/ <- Flask Python microservice
| |-- routes/ <- endpoint files
| |-- services/ <- groq_client.py
| |-- prompts/ <- prompt template text files
| |-- app.py
| |-- Dockerfile
| +-- requirements.txt
|
|-- frontend/ <- React + Vite frontend
| |-- src/
| | |-- components/
| | |-- pages/
| | |-- services/
| | +-- App.jsx
| +-- package.json
|
|-- docker-compose.yml
|-- .env.example
+-- README.md




# **Testing**

Backend:

mvn test

Frontend:

npm install
npm run dev


# **Docker**

Run everything:

docker-compose up --build

Reset DB:

docker-compose down -v
docker-compose up --build