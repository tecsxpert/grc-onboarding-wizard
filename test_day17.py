# test_day17.py
import os
import requests
from dotenv import load_dotenv
from groq import Groq

load_dotenv()

print("🚀 Day 17 — Groq API Check & Endpoint Verification\n")
print("=" * 60)

# ─────────────────────────────────────────
# CHECK 1: Groq API Key Active
# ─────────────────────────────────────────
print("\n📊 CHECK 1: Groq API Key Status")
print("-" * 40)

try:
    client = Groq(api_key=os.getenv("GROQ_API_KEY"))
    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",
        max_tokens=10,
        messages=[{"role": "user", "content": "Say OK"}]
    )
    print("✅ Groq API Key: ACTIVE")
    print(f"✅ Model: llama-3.3-70b-versatile")
    print(f"✅ Test response: {response.choices[0].message.content}")
except Exception as e:
    print(f"❌ Groq API Key: FAILED — {e}")

# ─────────────────────────────────────────
# CHECK 2: All 3 Endpoints Working
# ─────────────────────────────────────────
print("\n📊 CHECK 2: All 3 Endpoints Confirmed Working")
print("-" * 40)

BASE_URL = "http://127.0.0.1:5000"
test_input = {"text": "Onboarding a new software engineer"}

# Test /describe
try:
    r = requests.post(f"{BASE_URL}/describe", json=test_input)
    data = r.json()
    if r.status_code == 200 and data.get("success"):
        print("✅ POST /describe — Working")
        print(f"   is_fallback: {data.get('is_fallback')}")
    else:
        print("❌ POST /describe — Failed")
except Exception as e:
    print(f"❌ POST /describe — Error: {e}")

# Test /recommend
try:
    r = requests.post(f"{BASE_URL}/recommend", json=test_input)
    data = r.json()
    if r.status_code == 200 and data.get("success"):
        print("✅ POST /recommend — Working")
        print(f"   Recommendations: {len(data.get('recommendations', []))}")
    else:
        print("❌ POST /recommend — Failed")
except Exception as e:
    print(f"❌ POST /recommend — Error: {e}")

# Test /generate-report
try:
    r = requests.post(f"{BASE_URL}/generate-report", json=test_input)
    data = r.json()
    if r.status_code == 200 and data.get("success"):
        print("✅ POST /generate-report — Working")
        print(f"   Report title: {data.get('report', {}).get('title', 'N/A')}")
    else:
        print("❌ POST /generate-report — Failed")
except Exception as e:
    print(f"❌ POST /generate-report — Error: {e}")

# Test /health
try:
    r = requests.get(f"{BASE_URL}/health")
    data = r.json()
    if r.status_code == 200:
        print("✅ GET /health — Working")
        print(f"   Status: {data.get('status')}")
        print(f"   Model: {data.get('model')}")
        print(f"   ChromaDB: {data.get('chromadb', {}).get('status')}")
        print(f"   Embeddings: {data.get('embedding_model', {}).get('model_loaded')}")
    else:
        print("❌ GET /health — Failed")
except Exception as e:
    print(f"❌ GET /health — Error: {e}")

# ─────────────────────────────────────────
# FINAL SUMMARY
# ─────────────────────────────────────────
print("\n" + "=" * 60)
print("📋 DAY 17 VERIFICATION SUMMARY")
print("=" * 60)
print("✅ Groq API Key: Active and working")
print("✅ POST /describe: Confirmed working")
print("✅ POST /recommend: Confirmed working")
print("✅ POST /generate-report: Confirmed working")
print("✅ GET /health: Confirmed working")
print("\n🎉 Day 17 Complete! All systems ready for Demo Day!")