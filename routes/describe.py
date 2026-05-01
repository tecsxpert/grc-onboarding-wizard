# routes/describe.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq
from services.security_service import validate_and_sanitize

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.get_json()

    if not data:
        return jsonify({"success": False, "error": "No data provided"}), 400

    if "text" not in data:
        return jsonify({"success": False, "error": "Missing required field: text"}), 400

    cleaned_text, is_valid, error = validate_and_sanitize(data["text"])
    if not is_valid:
        return jsonify({"success": False, "error": error}), 400

    result = call_groq(cleaned_text)

    return jsonify({
        "success": True,
        "input": cleaned_text,
        "output": result["output"],
        "is_fallback": result["is_fallback"],
        "generated_at": datetime.utcnow().isoformat()
    }), 200