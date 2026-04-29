# routes/describe.py
from flask import Blueprint, request, jsonify
from datetime import datetime
from services.groq_service import call_groq

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():

    # Step 1: Get the data sent by user
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

    # Step 3: Load prompt and call Groq
    user_input = data["text"]
    ai_output = call_groq(user_input)

    # Step 4: Return structured JSON with generated_at
    return jsonify({
        "success": True,
        "input": user_input,
        "output": ai_output,
        "generated_at": datetime.utcnow().isoformat()
    }), 200