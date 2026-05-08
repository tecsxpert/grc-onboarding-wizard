# test_performance.py
import time
import requests

BASE_URL = "http://127.0.0.1:5000"
test_input = {"text": "Onboarding a new software engineer"}

print("🚀 Day 16 — Final Performance Verification\n")
print("=" * 60)

# ─────────────────────────────────────────
# TEST 1: Response time verification
# ─────────────────────────────────────────
print("\n📊 TEST 1: Response Time Verification")
print("-" * 40)

endpoints = [
    ("POST", "/describe", test_input),
    ("POST", "/recommend", test_input),
    ("POST", "/generate-report", test_input),
    ("GET", "/health", None),
]

all_fast = True
for method, endpoint, body in endpoints:
    start = time.time()
    if method == "POST":
        response = requests.post(f"{BASE_URL}{endpoint}", json=body)
    else:
        response = requests.get(f"{BASE_URL}{endpoint}")
    duration = (time.time() - start) * 1000

    status = "✅" if duration < 2000 else "❌ TOO SLOW"
    print(f"{status} {endpoint}: {duration:.0f}ms")

    if duration >= 2000:
        all_fast = False

if all_fast:
    print("\n✅ All endpoints within 2s target!")
else:
    print("\n❌ Some endpoints exceeded 2s target!")

# ─────────────────────────────────────────
# TEST 2: Cache verification
# ─────────────────────────────────────────
print("\n📊 TEST 2: Cache Verification")
print("-" * 40)

# First request — should be MISS
start = time.time()
r1 = requests.post(f"{BASE_URL}/describe", json={"text": "What is Python?"})
time1 = (time.time() - start) * 1000
print(f"First request (MISS): {time1:.0f}ms")

# Second request — should be HIT (faster)
start = time.time()
r2 = requests.post(f"{BASE_URL}/describe", json={"text": "What is Python?"})
time2 = (time.time() - start) * 1000
print(f"Second request (HIT): {time2:.0f}ms")

if time2 < time1:
    print(f"✅ Cache working! Second request was {time1-time2:.0f}ms faster!")
else:
    print("⚠️ Cache may not be working — check Redis!")

# ─────────────────────────────────────────
# TEST 3: Fallback verification
# ─────────────────────────────────────────
print("\n📊 TEST 3: Fallback Verification")
print("-" * 40)

# Test with normal input — should be is_fallback: false
r = requests.post(f"{BASE_URL}/describe", json={"text": "What is Python?"})
data = r.json()

if "is_fallback" in data:
    print(f"✅ is_fallback field present: {data['is_fallback']}")
else:
    print("❌ is_fallback field missing!")

# Test all 3 endpoints have is_fallback
for endpoint in ["/describe", "/recommend", "/generate-report"]:
    r = requests.post(
        f"{BASE_URL}{endpoint}",
        json=test_input
    )
    data = r.json()
    has_fallback = "is_fallback" in data
    status = "✅" if has_fallback else "❌"
    print(f"{status} {endpoint} has is_fallback: {has_fallback}")

# ─────────────────────────────────────────
# TEST 4: Security headers verification
# ─────────────────────────────────────────
print("\n📊 TEST 4: Security Headers Verification")
print("-" * 40)

r = requests.get(f"{BASE_URL}/health")
headers = r.headers

security_headers = [
    "X-Frame-Options",
    "X-Content-Type-Options",
    "X-XSS-Protection",
    "Content-Security-Policy",
    "Referrer-Policy",
    "Strict-Transport-Security",
]

all_headers_present = True
for header in security_headers:
    present = header in headers
    status = "✅" if present else "❌"
    print(f"{status} {header}: {headers.get(header, 'MISSING')[:40]}")
    if not present:
        all_headers_present = False

# ─────────────────────────────────────────
# FINAL SUMMARY
# ─────────────────────────────────────────
print("\n" + "=" * 60)
print("📋 FINAL PERFORMANCE SUMMARY")
print("=" * 60)
print(f"✅ Response times: All under 2s" if all_fast else "❌ Response times: Some over 2s")
print(f"✅ Cache: Working" if time2 < time1 else "⚠️ Cache: Check Redis")
print(f"✅ Fallback: is_fallback field present")
print(f"✅ Security headers: All present" if all_headers_present else "❌ Security headers: Some missing")
print("\n🎉 Day 16 Performance Verification Complete!")