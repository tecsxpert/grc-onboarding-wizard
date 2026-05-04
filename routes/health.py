# routes/health.py
from flask import Blueprint, jsonify
from services.health_service import get_health_status

health_bp = Blueprint("health", __name__)

@health_bp.route("/health", methods=["GET"])
def health():
    """
    Returns health status of the AI service.
    """
    status = get_health_status()
    return jsonify(status), 200