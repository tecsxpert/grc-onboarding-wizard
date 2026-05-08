# test_demo.py
import os
from dotenv import load_dotenv
from groq import Groq
from prompts.primary_prompt import get_primary_prompt
from prompts.recommend_prompt import get_recommend_prompt
from prompts.report_prompt import get_report_prompt
import json

load_dotenv()
client = Groq(api_key=os.getenv("GROQ_API_KEY"))

# 30 demo inputs covering all scenarios
demo_inputs = [
    # Onboarding scenarios
    "Onboarding a new software engineer",
    "Onboarding a new data scientist",
    "Onboarding a new product manager",
    "Onboarding a new DevOps engineer",
    "Onboarding a new security analyst",
    "Onboarding a new QA engineer",
    "Onboarding a new frontend developer",
    "Onboarding a new backend developer",
    "Onboarding a new UX designer",
    "Onboarding a new project manager",
    # GRC scenarios
    "Implement GDPR compliance for our organization",
    "Create a risk management framework",
    "Set up an IT audit process",
    "Implement access control policies",
    "Create a data privacy policy",
    "Set up security awareness training",
    "Implement IT governance framework",
    "Create compliance checklist for healthcare",
    "Set up vendor risk management",
    "Implement incident response plan",
    # Learning scenarios
    "Learn cloud computing from scratch",
    "Learn cybersecurity fundamentals",
    "Learn Python for data science",
    "Learn Docker and Kubernetes",
    "Learn machine learning basics",
    # Career scenarios
    "Transition from developer to tech lead",
    "Build skills for a DevOps role",
    "Prepare for a security certification",
    "Improve leadership skills for managers",
    "Build communication skills for engineers"
]

print("🚀 Running 30 demo records test...\n")
passed = 0
failed = 0

for i, input_text in enumerate(demo_inputs, 1):
    try:
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            max_tokens=300,
            temperature=0.3,
            messages=[
                {"role": "user", "content": get_primary_prompt(input_text)}
            ]
        )
        output = response.choices[0].message.content
        print(f"✅ Test {i:02d}: {input_text[:50]}...")
        passed += 1
    except Exception as e:
        print(f"❌ Test {i:02d}: FAILED — {e}")
        failed += 1

print(f"\n📊 Results: {passed}/30 passed, {failed}/30 failed")
if failed == 0:
    print("🎉 All 30 demo records are demo-ready!")
else:
    print("⚠️ Some records failed — check above!")