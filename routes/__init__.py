# routes/__init__.py
def register_routes(app):
    from .example import example_bp
    from .describe import describe_bp
    from .recommend import recommend_bp
    from .items import items_bp
    from .report import report_bp
    from .health import health_bp

    app.register_blueprint(example_bp)
    app.register_blueprint(describe_bp)
    app.register_blueprint(recommend_bp)
    app.register_blueprint(items_bp)
    app.register_blueprint(report_bp)
    app.register_blueprint(health_bp)