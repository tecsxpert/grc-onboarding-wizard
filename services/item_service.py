# services/item_service.py
import threading
from datetime import datetime
from services.ai_client import AiServiceClient
from prompts.primary_prompt import get_primary_prompt
from prompts.recommend_prompt import get_recommend_prompt

# Initialize AI client
ai_client = AiServiceClient()

# Temporary in-memory storage (like a fake database)
items_store = {}

def create_item(item_id: str, text: str) -> dict:
    """
    Create a new item and trigger async AI enrichment.
    """

    # Step 1: Create basic item immediately
    item = {
        "id": item_id,
        "text": text,
        "status": "processing",      # AI is working
        "description": None,         # Will be filled by AI
        "recommendations": None,     # Will be filled by AI
        "created_at": datetime.utcnow().isoformat(),
        "enriched_at": None          # Will be set after AI completes
    }

    # Step 2: Save item to store immediately
    items_store[item_id] = item

    # Step 3: Trigger AI enrichment in background (Async)
    thread = threading.Thread(
        target=_enrich_item_async,
        args=(item_id, text)
    )
    thread.daemon = True
    thread.start()

    return item


def _enrich_item_async(item_id: str, text: str):
    """
    Runs in background thread.
    Calls AI and attaches result to item.
    """
    print(f"[Async] Starting AI enrichment for item: {item_id}")

    # Step 1: Call AI for description
    description = ai_client.complete(get_primary_prompt(text))

    # Step 2: Call AI for recommendations
    recommendations = ai_client.complete_json(get_recommend_prompt(text))

    # Step 3: Handle null gracefully
    # If AI fails, use fallback values instead of crashing
    if description is None:
        print(f"[Async] Description failed for item: {item_id}")
        description = "Description could not be generated at this time."

    if recommendations is None:
        print(f"[Async] Recommendations failed for item: {item_id}")
        recommendations = [
            {
                "action_type": "retry",
                "description": "Could not generate recommendations. Please try again.",
                "priority": "high"
            }
        ]

    # Step 4: Attach AI results to item
    items_store[item_id]["description"] = description
    items_store[item_id]["recommendations"] = recommendations
    items_store[item_id]["status"] = "completed"
    items_store[item_id]["enriched_at"] = datetime.utcnow().isoformat()

    print(f"[Async] AI enrichment completed for item: {item_id}")


def get_item(item_id: str) -> dict | None:
    """
    Get an item by ID.
    Returns None if not found.
    """
    return items_store.get(item_id, None)