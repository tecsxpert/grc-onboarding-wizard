# routes/items.py
import uuid
from flask import Blueprint, request, jsonify
from services.item_service import create_item, get_item

items_bp = Blueprint("items", __name__)

@items_bp.route("/items", methods=["POST"])
def create():
    """
    Create a new item and trigger async AI enrichment.
    """

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

    # Step 2: Generate unique ID for item
    item_id = str(uuid.uuid4())

    # Step 3: Create item (AI runs in background)
    item = create_item(item_id, data["text"])

    # Step 4: Return immediately without waiting for AI
    return jsonify({
        "success": True,
        "message": "Item created. AI enrichment is processing in background.",
        "item": item
    }), 201


@items_bp.route("/items/<item_id>", methods=["GET"])
def get(item_id):
    """
    Get item by ID — check if AI has finished.
    """
    item = get_item(item_id)

    # Handle null gracefully
    if item is None:
        return jsonify({
            "success": False,
            "error": f"Item not found: {item_id}"
        }), 404

    return jsonify({
        "success": True,
        "item": item
    }), 200