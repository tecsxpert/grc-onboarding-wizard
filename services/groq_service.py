# services/groq_service.py
import os
import json
import time
from groq import Groq
from dotenv import load_dotenv
from prompts.primary_prompt import get_primary_prompt
from prompts.recommend_prompt import get_recommend_prompt
from prompts.report_prompt import get_report_prompt
from services.health_service import record_response_time
from services.cache_service import get_from_cache, set_in_cache, make_cache_key

load_dotenv()

# Connect to Groq
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

# Fallback templates when Groq fails
FALLBACK_DESCRIBE = {
    "output": "We are currently unable to generate a description. Please try again later.",
    "is_fallback": True
}

FALLBACK_RECOMMEND = {
    "recommendations": [
        {
            "action_type": "retry",
            "description": "AI service is temporarily unavailable. Please try again later.",
            "priority": "high"
        },
        {
            "action_type": "contact",
            "description": "Contact support if the issue persists.",
            "priority": "medium"
        },
        {
            "action_type": "wait",
            "description": "Wait a few minutes and retry your request.",
            "priority": "low"
        }
    ],
    "is_fallback": True
}

FALLBACK_REPORT = {
    "report": {
        "title": "Report Temporarily Unavailable",
        "summary": "The AI service is temporarily unavailable. Please try again later.",
        "overview": "We apologize for the inconvenience. Our AI service is currently experiencing issues.",
        "key_items": [
            "AI service temporarily unavailable",
            "Please try again in a few minutes",
            "Contact support if issue persists",
            "Your data has been saved safely"
        ],
        "recommendations": [
            {
                "action_type": "retry",
                "description": "Try generating the report again in a few minutes.",
                "priority": "high"
            },
            {
                "action_type": "contact",
                "description": "Contact support if the issue persists.",
                "priority": "medium"
            },
            {
                "action_type": "save",
                "description": "Save your work and try again later.",
                "priority": "low"
            }
        ]
    },
    "is_fallback": True
}


def call_groq(user_input: str) -> dict:
    # Check cache first
    cache_key = make_cache_key("describe", user_input)
    cached = get_from_cache(cache_key)
    if cached:
        return {"output": cached, "is_fallback": False}

    try:
        start = time.time()
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            max_tokens=500,        # Optimised for speed
            temperature=0.3,       # Lower = faster + more consistent
            messages=[
                {"role": "user", "content": get_primary_prompt(user_input)}
            ]
        )
        duration = (time.time() - start) * 1000
        record_response_time(duration)
        print(f"[Groq] /describe response time: {duration:.0f}ms")

        result = response.choices[0].message.content
        set_in_cache(cache_key, result)
        return {"output": result, "is_fallback": False}

    except Exception as e:
        print(f"[Groq] /describe error: {e}")
        return FALLBACK_DESCRIBE


def call_groq_recommend(user_input: str) -> dict:
    # Check cache first
    cache_key = make_cache_key("recommend", user_input)
    cached = get_from_cache(cache_key)
    if cached:
        return {"recommendations": cached, "is_fallback": False}

    try:
        start = time.time()
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            max_tokens=600,        # Optimised for speed
            temperature=0.3,
            messages=[
                {"role": "user", "content": get_recommend_prompt(user_input)}
            ]
        )
        duration = (time.time() - start) * 1000
        record_response_time(duration)
        print(f"[Groq] /recommend response time: {duration:.0f}ms")

        raw = response.choices[0].message.content.strip()
        if raw.startswith("```"):
            raw = raw.split("```")[1]
            if raw.startswith("json"):
                raw = raw[4:]

        result = json.loads(raw)
        set_in_cache(cache_key, result)
        return {"recommendations": result, "is_fallback": False}

    except Exception as e:
        print(f"[Groq] /recommend error: {e}")
        return FALLBACK_RECOMMEND


def call_groq_report(user_input: str) -> dict:
    # Check cache first
    cache_key = make_cache_key("report", user_input)
    cached = get_from_cache(cache_key)
    if cached:
        return {"report": cached, "is_fallback": False}

    try:
        start = time.time()
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            max_tokens=1000,       # Optimised for speed
            temperature=0.3,
            messages=[
                {"role": "user", "content": get_report_prompt(user_input)}
            ]
        )
        duration = (time.time() - start) * 1000
        record_response_time(duration)
        print(f"[Groq] /generate-report response time: {duration:.0f}ms")

        raw = response.choices[0].message.content.strip()
        if raw.startswith("```"):
            raw = raw.split("```")[1]
            if raw.startswith("json"):
                raw = raw[4:]

        result = json.loads(raw.strip())
        set_in_cache(cache_key, result)
        return {"report": result, "is_fallback": False}

    except Exception as e:
        print(f"[Groq] /generate-report error: {e}")
        return FALLBACK_REPORT