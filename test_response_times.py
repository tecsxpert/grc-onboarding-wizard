# test_response_times.py
import time
import requests

BASE_URL = "http://127.0.0.1:5000"

test_input = {"text": "Onboarding a new software engineer to the team"}

endpoints = [
    ("POST", "/describe", test_input),
    ("POST", "/recommend", test_input),
    ("POST", "/generate-report", test_input),
    ("GET", "/health", None),
]

print("🚀 AI Dry Run — Response Time Test\n")
print("-" * 50)

all_passed = True

for method, endpoint, body in endpoints:
    try:
        start = time.time()

        if method == "POST":
            response = requests.post(
                f"{BASE_URL}{endpoint}",
                json=body
            )
        else:
            response = requests.get(f"{BASE_URL}{endpoint}")

        duration = (time.time() - start) * 1000

        status = "✅" if response.status_code == 200 else "❌"
        speed = "⚡ FAST" if duration < 2000 else "⚠️ SLOW"

        print(f"{status} {method} {endpoint}")
        print(f"   Status: {response.status_code}")
        print(f"   Time: {duration:.0f}ms {speed}")
        print()

        if response.status_code != 200:
            all_passed = False

    except Exception as e:
        print(f"❌ {method} {endpoint} — FAILED: {e}\n")
        all_passed = False

print("-" * 50)
if all_passed:
    print("🎉 All endpoints passed! Ready for Demo Day!")
else:
    print("⚠️ Some endpoints failed — fix before Demo Day!")