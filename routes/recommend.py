# routes/recommend.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq_recommend

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():

    # Step 1: Get data sent by user
    data = request.get_json()

    # Step 2: Validate input
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

    # Step 3: Call Groq and get recommendations
    user_input = data["text"]

    try:
        recommendations = call_groq_recommend(user_input)

        # Step 4: Return structured JSON
        return jsonify({
            "success": True,
            "input": user_input,
            "recommendations": recommendations,
            "generated_at": datetime.utcnow().isoformat()
        }), 200

    except Exception as e:
        return jsonify({
            "success": False,
            "error": f"Failed to generate recommendations: {str(e)}"
        }), 500