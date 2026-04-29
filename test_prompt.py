# test_prompt.py
import os
from dotenv import load_dotenv
from groq import Groq
from prompts.primary_prompt import get_primary_prompt

# Load API key from .env file
load_dotenv()

# Connect to Groq
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

# Your 5 real test inputs
test_inputs = [
    "What is Python?",
    "What is Flask?",
    "What is an API?",
    "What is a prompt template?",
    "What is a virtual environment?",
]

# Test each input
for i, input_text in enumerate(test_inputs, 1):
    print(f"\n--- Test {i} ---")
    print(f"Input: {input_text}")

    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",   # Free and fast model
        messages=[
            {"role": "user", "content": get_primary_prompt(input_text)}
        ]
    )

    print(f"Output: {response.choices[0].message.content}")
    print("-" * 40)