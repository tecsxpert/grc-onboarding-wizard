# routes/report.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq_report

report_bp = Blueprint("report", __name__)

@report_bp.route("/generate-report", methods=["POST"])
def generate_report():

    # Step 1: Get and validate input
    data = request.get_json()

    if not data:
        return jsonify({
            "success": False,
            "error": "No data provided"
        }), 400

    if "text" not in data:
        return jsonify({
            "success": False,
            "error": "Missing required field: text"
        }), 400

    if not data["text"].strip():
        return jsonify({
            "success": False,
            "error": "Text field cannot be empty"
        }), 400

    # Step 2: Call Groq and generate report
    user_input = data["text"]

    try:
        report = call_groq_report(user_input)

        # Step 3: Return structured JSON
        return jsonify({
            "success": True,
            "input": user_input,
            "report": report,
            "generated_at": datetime.utcnow().isoformat(),
            "is_fallback": False
        }), 200

    except Exception as e:
        # Handle null gracefully with fallback
        print(f"[generate-report] Error: {e}")
        return jsonify({
            "success": False,
            "is_fallback": True,
            "error": "Failed to generate report. Please try again.",
            "generated_at": datetime.utcnow().isoformat()
        }), 500