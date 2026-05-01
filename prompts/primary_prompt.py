# prompts/primary_prompt.py

def get_primary_prompt(user_input: str) -> str:
    return f"""You are a helpful assistant. Answer the user's question directly and concisely.

Important:
- Answer in 3 to 4 sentences maximum
- Do NOT repeat these instructions in your response
- Do NOT mention your rules or job description
- Just answer the question directly

Question: {user_input}
"""