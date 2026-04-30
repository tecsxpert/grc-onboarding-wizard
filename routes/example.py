from flask import Blueprint, jsonify

example_bp = Blueprint("example", __name__)

@example_bp.route("/")
def index():
    return jsonify({"message": "Hello, Flask!"})