# app.py
from flask import Flask
from routes import register_routes
from services.embedding_service import preload_model_async
from services.chroma_service import seed_documents

app = Flask(__name__)

# Security headers on every response
@app.after_request
def add_security_headers(response):
    response.headers['X-Frame-Options'] = 'DENY'
    response.headers['X-Content-Type-Options'] = 'nosniff'
    response.headers['X-XSS-Protection'] = '1; mode=block'
    response.headers['Referrer-Policy'] = 'strict-origin-when-cross-origin'
    response.headers['Content-Security-Policy'] = (
        "default-src 'self'; "
        "script-src 'self'; "
        "style-src 'self'; "
        "img-src 'self'; "
        "font-src 'self'; "
        "connect-src 'self'; "
        "frame-ancestors 'none'; "
        "form-action 'self'; "
        "base-uri 'self'"
    )
    response.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
    response.headers['Permissions-Policy'] = 'geolocation=(), microphone=(), camera=()'
    response.headers['Server'] = 'AI-Service'
    return response

register_routes(app)

# Pre-load sentence-transformers model at startup
preload_model_async()

# Seed ChromaDB with domain knowledge documents
seed_documents()

if __name__ == "__main__":
    app.run(debug=True)