# services/security_service.py
import re

def sanitize_input(text: str) -> str:
    """
    Remove HTML tags and dangerous characters
    from user input.
    """
    # Remove HTML tags
    clean = re.sub(r'<[^>]+>', '', text)
    # Remove script tags specifically
    clean = re.sub(r'<script.*?>.*?</script>', '', clean, flags=re.DOTALL)
    # Strip leading/trailing whitespace
    clean = clean.strip()
    return clean

def detect_prompt_injection(text: str) -> bool:
    """
    Detect common prompt injection patterns.
    Returns True if injection detected.
    """
    injection_patterns = [
        r'ignore previous instructions',
        r'ignore all instructions',
        r'disregard your instructions',
        r'you are now',
        r'act as',
        r'pretend you are',
        r'forget everything',
        r'new instructions',
        r'system prompt',
        r'jailbreak',
    ]

    text_lower = text.lower()
    for pattern in injection_patterns:
        if re.search(pattern, text_lower):
            return True
    return False

def validate_and_sanitize(text: str) -> tuple[str, bool, str]:
    """
    Full validation pipeline.
    Returns: (cleaned_text, is_valid, error_message)
    """
    # Check for prompt injection
    if detect_prompt_injection(text):
        return "", False, "Invalid input detected"

    # Sanitize HTML
    cleaned = sanitize_input(text)

    # Check length
    if len(cleaned) < 3:
        return "", False, "Input too short"

    if len(cleaned) > 2000:
        return "", False, "Input too long (max 2000 characters)"

    return cleaned, True, ""