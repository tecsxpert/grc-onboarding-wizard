# routes/health.py
from flask import Blueprint, jsonify
from services.health_service import get_health_status
from services.embedding_service import get_model_status
from services.chroma_service import get_collection_stats

health_bp = Blueprint("health", __name__)

@health_bp.route("/health", methods=["GET"])
def health():
    status = get_health_status()
    status["embedding_model"] = get_model_status()
    status["chromadb"] = get_collection_stats()
    return jsonify(status), 200