# prompts/report_prompt.py

def get_report_prompt(user_input: str) -> str:
    return f"""You are a professional report generator.

Based on the user's input, generate a structured onboarding report.

You must respond ONLY with a valid JSON object. No extra text, no markdown, no explanation.

The JSON must have exactly these fields:
- "title": a short professional report title (string)
- "summary": a 2-3 sentence executive summary (string)
- "overview": a detailed paragraph overview (string)
- "key_items": an array of exactly 4 key points, each as a string
- "recommendations": an array of exactly 3 objects, each with:
    - "action_type": short category word
    - "description": clear recommendation in 1-2 sentences
    - "priority": exactly "high", "medium", or "low"

Example format:
{{
    "title": "Onboarding Report: Software Engineer",
    "summary": "This report provides an overview...",
    "overview": "The onboarding process for...",
    "key_items": [
        "Complete environment setup",
        "Review codebase structure",
        "Meet with team leads",
        "Complete security training"
    ],
    "recommendations": [
        {{
            "action_type": "learn",
            "description": "Complete the internal documentation review.",
            "priority": "high"
        }},
        {{
            "action_type": "meet",
            "description": "Schedule 1:1 with your team lead in first week.",
            "priority": "medium"
        }},
        {{
            "action_type": "setup",
            "description": "Configure local development environment.",
            "priority": "low"
        }}
    ]
}}

User Input: {user_input}

Respond ONLY with the JSON object:
"""