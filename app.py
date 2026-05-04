# app.py
from flask import Flask
from routes import register_routes

app = Flask(__name__)

# Add security headers to every response
@app.after_request
def add_security_headers(response):
    # Prevents clickjacking attacks
    response.headers['X-Frame-Options'] = 'DENY'
    # Prevents MIME type sniffing
    response.headers['X-Content-Type-Options'] = 'nosniff'
    # Enables XSS protection in browsers
    response.headers['X-XSS-Protection'] = '1; mode=block'
    # Controls how much referrer info is shared
    response.headers['Referrer-Policy'] = 'strict-origin-when-cross-origin'
    # Restricts which resources can be loaded
    response.headers['Content-Security-Policy'] = "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'; font-src 'self'; connect-src 'self'; frame-ancestors 'none'; form-action 'self'; base-uri 'self'"
    # Forces HTTPS connections
    response.headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains'
    # Restricts browser features
    response.headers['Permissions-Policy'] = 'geolocation=(), microphone=(), camera=()'
    # Hide server version information  ← ADD THIS LINE
    response.headers['Server'] = 'WebServer'
    return response

register_routes(app)

if __name__ == "__main__":
    app.run(debug=True)