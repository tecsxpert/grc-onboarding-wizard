# Demo Day Script — AI Developer 1

## Opening (30 seconds)
"Good morning/afternoon. I am Tejas, AI Developer 1.
Our tool is the GRC Onboarding Wizard.
The problem: Organizations struggle to onboard new 
employees efficiently.
Our solution: An AI-powered microservice that generates 
personalized onboarding content instantly."

## Architecture (30 seconds)
"Our AI service runs on Flask at port 5000.
It uses Groq API with LLaMA-3.3-70b model.
We have Redis caching for 15 minute TTL.
ChromaDB stores 10 domain knowledge documents.
All endpoints are secured with OWASP ZAP verified headers."

## Live Demo — /describe (20 seconds)
Input: "Onboarding a new software engineer"
"This endpoint generates a concise description.
Watch the AI respond in under 2 seconds."

## Live Demo — /recommend (20 seconds)
Input: "Onboarding a new software engineer"
"This endpoint generates 3 actionable recommendations
each with action_type, description and priority."

## Live Demo — /generate-report (20 seconds)
Input: "Onboarding a new software engineer"
"This endpoint generates a full structured report
with title, summary, overview, key items and recommendations."

## Health Endpoint (10 seconds)
"Our /health endpoint shows:
- Model: llama-3.3-70b-versatile
- ChromaDB: connected with 10 documents
- Embeddings: loaded
- Average response time"

## Security (10 seconds)
"We ran OWASP ZAP scan — zero Critical/High findings.
All responses include security headers:
X-Frame-Options, CSP, HSTS and more."

## Closing
"Our AI service is fully containerized with Docker,
cached with Redis, and secured with industry
standard practices. Thank you!"