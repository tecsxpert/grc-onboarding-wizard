# AI Service — GRC Onboarding Wizard

A Flask-based AI microservice that powers the GRC Onboarding Wizard.
It uses Groq API (LLaMA-3.3-70b) to generate descriptions, recommendations,
and onboarding reports.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Python 3.11+ | Programming language |
| Flask 3.x | Web framework |
| Groq API | AI model (LLaMA-3.3-70b-versatile) |
| Redis 7 | AI response caching (15 min TTL) |
| Docker | Redis container |

---

## Project Structure

ai-service/
├── app.py                  # Entry point
├── requirements.txt        # Dependencies
├── .env                    # Environment variables (never commit!)
├── .env.example            # Example environment variables
├── routes/
│   ├── init.py         # Route registration
│   ├── describe.py         # POST /describe
│   ├── recommend.py        # POST /recommend
│   ├── report.py           # POST /generate-report
│   ├── health.py           # GET /health
│   └── items.py            # POST /items, GET /items/<id>
├── services/
│   ├── groq_service.py     # Groq API calls + fallbacks
│   ├── cache_service.py    # Redis caching (SHA256 keys)
│   ├── health_service.py   # Uptime + response time tracking
│   ├── security_service.py # Input sanitization + injection detection
│   ├── ai_client.py        # AiServiceClient class
│   └── item_service.py     # Async item enrichment
└── prompts/
├── primary_prompt.py   # /describe prompt template
├── recommend_prompt.py # /recommend prompt template
└── report_prompt.py    # /generate-report prompt template

---

## Prerequisites

- Python 3.11 or higher
- Docker Desktop (for Redis)
- Groq API key (free at console.groq.com)

---

## Setup Instructions

### Step 1: Clone the repository
```bash
git clone https://github.com/tejasgowda46/grc-onboarding-wizard
cd grc-onboarding-wizard
```

### Step 2: Create virtual environment
```bash
python -m venv venv
venv\Scripts\activate   # Windows
source venv/bin/activate # Mac/Linux
```

### Step 3: Install dependencies
```bash
pip install -r requirements.txt
```

### Step 4: Create `.env` file
```bash
cp .env.example .env
```
Then edit `.env` and add your Groq API key.

### Step 5: Start Redis with Docker
```bash
docker run -d --name redis-ai -p 6379:6379 redis:7
```

### Step 6: Run the Flask app
```bash
python app.py
```

The service will start at: `http://localhost:5000`

---

## Environment Variables

| Variable | Required | Description |
|---|---|---|
| `GROQ_API_KEY` | ✅ Yes | Your Groq API key from console.groq.com |

Create a `.env` file in the project root:

GROQ_API_KEY=your-groq-api-key-here

---

## API Reference

### 1. POST `/describe`
Generates a concise description for the given input.

**Request:**
```json
{
    "text": "What is Python?"
}
```

**Response (Success):**
```json
{
    "success": true,
    "input": "What is Python?",
    "output": "Python is a high-level programming language known for its simplicity...",
    "is_fallback": false,
    "generated_at": "2026-05-01T10:30:00.123456"
}
```

**Response (Fallback):**
```json
{
    "success": true,
    "input": "What is Python?",
    "output": "We are currently unable to generate a description. Please try again later.",
    "is_fallback": true,
    "generated_at": "2026-05-01T10:30:00.123456"
}
```

---

### 2. POST `/recommend`
Generates exactly 3 recommendations for the given input.

**Request:**
```json
{
    "text": "I want to learn programming"
}
```

**Response (Success):**
```json
{
    "success": true,
    "input": "I want to learn programming",
    "recommendations": [
        {
            "action_type": "learn",
            "description": "Start with Python basics through free platforms.",
            "priority": "high"
        },
        {
            "action_type": "practice",
            "description": "Build small projects daily to strengthen skills.",
            "priority": "medium"
        },
        {
            "action_type": "resource",
            "description": "Join communities like GitHub and Stack Overflow.",
            "priority": "low"
        }
    ],
    "is_fallback": false,
    "generated_at": "2026-05-01T10:30:00.123456"
}
```

---

### 3. POST `/generate-report`
Generates a structured onboarding report for the given input.

**Request:**
```json
{
    "text": "Onboarding a new software engineer"
}
```

**Response (Success):**
```json
{
    "success": true,
    "input": "Onboarding a new software engineer",
    "report": {
        "title": "Onboarding Report: Software Engineer",
        "summary": "This report outlines the onboarding process...",
        "overview": "The onboarding process for a new software engineer...",
        "key_items": [
            "Complete environment setup",
            "Review codebase structure",
            "Meet with team leads",
            "Complete security training"
        ],
        "recommendations": [
            {
                "action_type": "learn",
                "description": "Review internal documentation.",
                "priority": "high"
            },
            {
                "action_type": "meet",
                "description": "Schedule 1:1 with team lead.",
                "priority": "medium"
            },
            {
                "action_type": "setup",
                "description": "Configure local development environment.",
                "priority": "low"
            }
        ]
    },
    "is_fallback": false,
    "generated_at": "2026-05-01T10:30:00.123456"
}
```

---

### 4. GET `/health`
Returns the health status of the AI service.

**Response:**
```json
{
    "status": "healthy",
    "model": "llama-3.3-70b-versatile",
    "avg_response_time_ms": 850.5,
    "uptime": "0h 30m 15s",
    "total_requests": 10,
    "checked_at": "2026-05-01T10:30:00.123456"
}
```

---

## Security Features

- Input sanitization — strips HTML tags
- Prompt injection detection
- Security headers on all responses
- Rate limiting via flask-limiter
- OWASP ZAP scan — zero Critical/High findings

---

## Caching

- Redis cache with SHA256 keys
- 15 minute TTL (900 seconds)
- Cache HIT returns instantly without calling Groq
- Cache MISS calls Groq and stores result

---

## Error Handling

All endpoints return fallback responses if Groq is unavailable:
- `is_fallback: true` indicates a fallback response
- `is_fallback: false` indicates a real AI response

---

## Author

- **GitHub:** tejasgowda46
- **Project:** GRC Onboarding Wizard — Capstone Project
- **Sprint:** 14 April – 9 May 2026