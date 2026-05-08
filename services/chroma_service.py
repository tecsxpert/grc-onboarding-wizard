# services/chroma_service.py
import chromadb
from chromadb.config import Settings

# Initialize ChromaDB client
try:
    chroma_client = chromadb.PersistentClient(path="./chroma_data")
    collection = chroma_client.get_or_create_collection(
        name="domain_knowledge",
        metadata={"hnsw:space": "cosine"}
    )
    print("[ChromaDB] Connected successfully! ✅")
except Exception as e:
    print(f"[ChromaDB] Connection failed: {e}")
    chroma_client = None
    collection = None

# 10 Domain knowledge documents about GRC/Onboarding
DOMAIN_DOCUMENTS = [
    {
        "id": "doc_001",
        "text": "GRC stands for Governance, Risk, and Compliance. It is a structured approach to aligning IT with business objectives while managing risk and meeting compliance requirements.",
        "metadata": {"category": "grc", "topic": "overview"}
    },
    {
        "id": "doc_002",
        "text": "Onboarding is the process of integrating a new employee into an organization. It includes orientation, training, and providing access to tools and resources needed for the job.",
        "metadata": {"category": "onboarding", "topic": "overview"}
    },
    {
        "id": "doc_003",
        "text": "Risk management involves identifying, assessing, and controlling threats to an organization's capital and earnings. These risks could stem from a wide variety of sources.",
        "metadata": {"category": "grc", "topic": "risk"}
    },
    {
        "id": "doc_004",
        "text": "Compliance refers to the process of adhering to laws, regulations, guidelines, and specifications relevant to an organization's business processes.",
        "metadata": {"category": "grc", "topic": "compliance"}
    },
    {
        "id": "doc_005",
        "text": "Security awareness training is a formal process for educating employees about cybersecurity. It helps employees understand their role in protecting the organization from security threats.",
        "metadata": {"category": "security", "topic": "training"}
    },
    {
        "id": "doc_006",
        "text": "Access control is a security technique that regulates who or what can view or use resources in a computing environment. It is a fundamental concept in security that minimizes risk.",
        "metadata": {"category": "security", "topic": "access"}
    },
    {
        "id": "doc_007",
        "text": "Data privacy refers to the proper handling, processing, storage, and usage of personal information. Organizations must comply with data privacy laws like GDPR and CCPA.",
        "metadata": {"category": "compliance", "topic": "privacy"}
    },
    {
        "id": "doc_008",
        "text": "An audit is a systematic examination of an organization's processes, procedures, and controls to ensure they are operating effectively and in compliance with applicable standards.",
        "metadata": {"category": "grc", "topic": "audit"}
    },
    {
        "id": "doc_009",
        "text": "Employee onboarding best practices include setting clear expectations, providing necessary resources, assigning a mentor, and conducting regular check-ins during the first 90 days.",
        "metadata": {"category": "onboarding", "topic": "best_practices"}
    },
    {
        "id": "doc_010",
        "text": "IT governance ensures that IT investments support business objectives. It includes processes for decision-making, accountability, and performance measurement of IT resources.",
        "metadata": {"category": "grc", "topic": "governance"}
    }
]


def seed_documents():
    """
    Seed ChromaDB with 10 domain knowledge documents.
    """
    if collection is None:
        print("[ChromaDB] Cannot seed — not connected!")
        return False

    try:
        # Check if already seeded
        existing = collection.count()
        if existing >= 10:
            print(f"[ChromaDB] Already seeded with {existing} documents ✅")
            return True

        # Add documents
        ids = [doc["id"] for doc in DOMAIN_DOCUMENTS]
        texts = [doc["text"] for doc in DOMAIN_DOCUMENTS]
        metadatas = [doc["metadata"] for doc in DOMAIN_DOCUMENTS]

        collection.add(
            ids=ids,
            documents=texts,
            metadatas=metadatas
        )

        print(f"[ChromaDB] Seeded {len(DOMAIN_DOCUMENTS)} documents successfully! ✅")
        return True

    except Exception as e:
        print(f"[ChromaDB] Seeding failed: {e}")
        return False


def query_documents(query_text: str, n_results: int = 3) -> list:
    """
    Query ChromaDB for relevant documents.
    """
    if collection is None:
        return []

    try:
        results = collection.query(
            query_texts=[query_text],
            n_results=n_results
        )
        documents = results["documents"][0] if results["documents"] else []
        print(f"[ChromaDB] Found {len(documents)} relevant documents")
        return documents

    except Exception as e:
        print(f"[ChromaDB] Query failed: {e}")
        return []


def get_collection_stats() -> dict:
    """
    Return ChromaDB collection statistics.
    """
    if collection is None:
        return {"status": "disconnected", "document_count": 0}

    try:
        count = collection.count()
        return {
            "status": "connected",
            "collection": "domain_knowledge",
            "document_count": count
        }
    except Exception as e:
        return {"status": "error", "error": str(e)}