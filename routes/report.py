# routes/report.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq_report
from services.security_service import validate_and_sanitize

report_bp = Blueprint("report", __name__)

@report_bp.route("/generate-report", methods=["POST"])
def generate_report():
    data = request.get_json()

    if not data:
        return jsonify({"success": False, "error": "No data provided"}), 400

    if "text" not in data:
        return jsonify({"success": False, "error": "Missing required field: text"}), 400

    # Validate and sanitize input
    cleaned_text, is_valid, error = validate_and_sanitize(data["text"])
    if not is_valid:
        return jsonify({"success": False, "error": error}), 400

    try:
        report = call_groq_report(cleaned_text)
        return jsonify({
            "success": True,
            "input": cleaned_text,
            "report": report,
            "generated_at": datetime.utcnow().isoformat(),
            "is_fallback": False
        }), 200
    except Exception as e:
        return jsonify({
            "success": False,
            "is_fallback": True,
            "error": "Failed to generate report.",
            "generated_at": datetime.utcnow().isoformat()
        }), 500