# services/health_service.py
import time
from datetime import datetime

# Track when the service started
START_TIME = time.time()

# Track response times
response_times = []

def record_response_time(duration: float):
    """
    Record a response time in milliseconds.
    Keeps only last 100 records.
    """
    response_times.append(duration)
    if len(response_times) > 100:
        response_times.pop(0)

def get_avg_response_time() -> float:
    """
    Calculate average response time.
    """
    if not response_times:
        return 0.0
    return round(sum(response_times) / len(response_times), 2)

def get_uptime() -> str:
    """
    Calculate how long the service has been running.
    """
    uptime_seconds = int(time.time() - START_TIME)
    hours = uptime_seconds // 3600
    minutes = (uptime_seconds % 3600) // 60
    seconds = uptime_seconds % 60
    return f"{hours}h {minutes}m {seconds}s"

def get_health_status() -> dict:
    """
    Return full health status of AI service.
    """
    return {
        "status": "healthy",
        "model": "llama-3.3-70b-versatile",
        "avg_response_time_ms": get_avg_response_time(),
        "uptime": get_uptime(),
        "total_requests": len(response_times),
        "checked_at": datetime.utcnow().isoformat()
    }