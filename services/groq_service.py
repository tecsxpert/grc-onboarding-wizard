# services/groq_service.py
import os
import json
from groq import Groq
from dotenv import load_dotenv
from prompts.primary_prompt import get_primary_prompt
from prompts.recommend_prompt import get_recommend_prompt

load_dotenv()

# Connect to Groq
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def call_groq(user_input: str) -> str:
    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",
        messages=[
            {"role": "user", "content": get_primary_prompt(user_input)}
        ]
    )
    return response.choices[0].message.content


def call_groq_recommend(user_input: str) -> list:
    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",
        messages=[
            {"role": "user", "content": get_recommend_prompt(user_input)}
        ]
    )

    raw = response.choices[0].message.content.strip()

    # Clean response and parse JSON
    if raw.startswith("```"):
        raw = raw.split("```")[1]
        if raw.startswith("json"):
            raw = raw[4:]

    recommendations = json.loads(raw)
    return recommendations