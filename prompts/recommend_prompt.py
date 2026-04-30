# prompts/recommend_prompt.py

def get_recommend_prompt(user_input: str) -> str:
    return f"""You are a helpful recommendation assistant.

Based on the user's input, provide exactly 3 recommendations.

You must respond ONLY with a valid JSON array. No extra text, no explanation, no markdown.

Each recommendation must have exactly these fields:
- "action_type": a short category word like "learn", "practice", "read", "watch", "build", "join", "resource"
- "description": a clear and helpful recommendation in 1-2 sentences
- "priority": must be exactly one of "high", "medium", or "low"

Example format:
[
    {{
        "action_type": "learn",
        "description": "Start with the basics before moving to advanced topics.",
        "priority": "high"
    }},
    {{
        "action_type": "practice",
        "description": "Practice daily to build strong habits.",
        "priority": "medium"
    }},
    {{
        "action_type": "resource",
        "description": "Use online platforms to find quality resources.",
        "priority": "low"
    }}
]

User Input: {user_input}

Respond ONLY with the JSON array:
"""