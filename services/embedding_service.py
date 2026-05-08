# services/embedding_service.py
import threading

# Global model variable
embedding_model = None
model_loading = False
model_loaded = False

def load_model():
    """
    Load sentence-transformer model in background.
    """
    global embedding_model, model_loading, model_loaded

    try:
        print("[Embeddings] Loading sentence-transformers model...")
        model_loading = True

        from sentence_transformers import SentenceTransformer
        embedding_model = SentenceTransformer('all-MiniLM-L6-v2')

        model_loaded = True
        model_loading = False
        print("[Embeddings] Model loaded successfully! ✅")

    except Exception as e:
        model_loading = False
        model_loaded = False
        print(f"[Embeddings] Failed to load model: {e}")


def preload_model_async():
    """
    Pre-load model in background thread at startup.
    """
    thread = threading.Thread(target=load_model)
    thread.daemon = True
    thread.start()
    print("[Embeddings] Model pre-loading started in background...")


def get_embedding(text: str) -> list | None:
    """
    Get embedding for a text.
    Returns None if model not loaded.
    """
    global embedding_model, model_loaded

    if not model_loaded or embedding_model is None:
        print("[Embeddings] Model not loaded yet")
        return None

    try:
        embedding = embedding_model.encode(text)
        return embedding.tolist()
    except Exception as e:
        print(f"[Embeddings] Error getting embedding: {e}")
        return None


def get_model_status() -> dict:
    """
    Return current model status.
    """
    return {
        "model_name": "all-MiniLM-L6-v2",
        "model_loaded": model_loaded,
        "model_loading": model_loading
    }