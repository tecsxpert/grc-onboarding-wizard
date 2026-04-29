# services/ai_client.py
import os
import json
from groq import Groq
from dotenv import load_dotenv

load_dotenv()

class AiServiceClient:
    """
    A dedicated client that handles all AI calls cleanly.
    All AI communication goes through this class.
    """

    def __init__(self):
        self.client = Groq(api_key=os.getenv("GROQ_API_KEY"))
        self.model = "llama-3.3-70b-versatile"

    def complete(self, prompt: str) -> str | None:
        """
        Send a prompt and get a text response.
        Returns None if something goes wrong.
        """
        try:
            response = self.client.chat.completions.create(
                model=self.model,
                messages=[
                    {"role": "user", "content": prompt}
                ]
            )
            return response.choices[0].message.content.strip()

        except Exception as e:
            print(f"[AiServiceClient] Error during completion: {e}")
            return None

    def complete_json(self, prompt: str) -> list | dict | None:
        """
        Send a prompt and get a JSON response.
        Returns None if something goes wrong.
        """
        try:
            raw = self.complete(prompt)

            if raw is None:
                return None

            # Clean markdown if present
            if raw.startswith("```"):
                raw = raw.split("```")[1]
                if raw.startswith("json"):
                    raw = raw[4:]

            return json.loads(raw.strip())

        except Exception as e:
            print(f"[AiServiceClient] Error parsing JSON: {e}")
            return None