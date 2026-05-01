# routes/recommend.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq_recommend
from services.security_service import validate_and_sanitize

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
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
        recommendations = call_groq_recommend(cleaned_text)
        return jsonify({
            "success": True,
            "input": cleaned_text,
            "recommendations": recommendations,
            "generated_at": datetime.utcnow().isoformat()
        }), 200
    except Exception as e:
        return jsonify({"success": False, "error": f"Failed: {str(e)}"}), 500