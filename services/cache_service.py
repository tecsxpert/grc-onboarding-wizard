# services/cache_service.py
import redis
import hashlib
import json
import os

# Connect to Redis
try:
    redis_client = redis.Redis(
        host="localhost",
        port=6379,
        db=0,
        decode_responses=True
    )
    redis_client.ping()
    REDIS_AVAILABLE = True
    print("[Cache] Redis connected successfully!")

except Exception as e:
    print(f"[Cache] Redis not available: {e}")
    REDIS_AVAILABLE = False
    redis_client = None

# 15 minutes TTL (as per PDF requirement)
CACHE_TTL = 900

def make_cache_key(prefix: str, text: str) -> str:
    """
    Create SHA256 cache key from input text.
    """
    hash_value = hashlib.sha256(text.encode()).hexdigest()
    return f"{prefix}:{hash_value}"

def get_from_cache(key: str):
    """
    Get value from Redis cache.
    Returns None if not found or Redis unavailable.
    """
    if not REDIS_AVAILABLE:
        return None
    try:
        value = redis_client.get(key)
        if value:
            print(f"[Cache] HIT for key: {key[:20]}...")
            return json.loads(value)
        print(f"[Cache] MISS for key: {key[:20]}...")
        return None
    except Exception as e:
        print(f"[Cache] Get error: {e}")
        return None

def set_in_cache(key: str, value, ttl: int = CACHE_TTL):
    """
    Store value in Redis cache with TTL.
    """
    if not REDIS_AVAILABLE:
        return
    try:
        redis_client.setex(key, ttl, json.dumps(value))
        print(f"[Cache] SET for key: {key[:20]}... TTL: {ttl}s")
    except Exception as e:
        print(f"[Cache] Set error: {e}")