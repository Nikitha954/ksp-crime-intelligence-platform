import os
import re
import json
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, Dict, Any

app = FastAPI(title="KSP Crime Intelligence AI Microservice")

# Enable CORS for Spring Boot & React UI calls
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class QueryRequest(BaseModel):
    message: str

VALID_DISTRICTS = [
    "Bengaluru Urban", "Bengaluru Rural", "Mysuru", "Mangaluru (Dakshina Kannada)",
    "Belagavi", "Hubballi-Dharwad", "Tumakuru", "Ballari", "Shivamogga", "Kalaburagi"
]

VALID_CRIMES = [
    "Theft (MVT & General)", "Burglary & House Breaking", "Robbery", "Dacoity",
    "Grievous Hurt & Assault", "Kidnapping & Abduction", "Attempt to Murder",
    "Online Financial Fraud / UPI Scam", "Identity Theft & Phishing",
    "Cheating & Criminal Breach of Trust", "Forgery & Document Fraud",
    "NDPS (Narcotics)", "Arms Act Violation"
]

SYSTEM_PROMPT = f"""You convert natural language crime analytics questions into structured JSON filter objects for the Karnataka State Police dataset.

Valid district enum values: {json.dumps(VALID_DISTRICTS)}
Valid crimeType enum values: {json.dumps(VALID_CRIMES)}
Valid status enum values: ["Under Investigation", "Chargesheeted", "Closed / Final Report"]
Valid intent values: ["list", "count", "trend", "hotspot", "network"]

Return ONLY a valid raw JSON object matching this exact shape (no free text, no markdown backticks):
{{
  "crimeType": "matched enum string or null",
  "district": "matched district string or null",
  "dateFrom": "YYYY-MM-DD or null",
  "dateTo": "YYYY-MM-DD or null",
  "status": "matched status string or null",
  "intent": "list|count|trend|hotspot|network"
}}
"""

def extract_json_str(text: str) -> str:
    cleaned = text.strip()
    if cleaned.startswith("```"):
        cleaned = re.sub(r"^```(?:json)?", "", cleaned, flags=re.IGNORECASE)
        cleaned = re.sub(r"```$", "", cleaned).strip()
    match = re.search(r"\{.*\}", cleaned, re.DOTALL)
    if match:
        return match.group(0)
    return cleaned

@app.post("/api/parse-query")
async def parse_query(request: QueryRequest) -> Dict[str, Any]:
    user_msg = request.message.strip()
    print(f"Received Query to Parse: '{user_msg}'")

    groq_api_key = os.environ.get("GROQ_API_KEY")
    
    if groq_api_key:
        try:
            from langchain_groq import ChatGroq
            llm = ChatGroq(
                temperature=0,
                groq_api_key=groq_api_key,
                model_name="llama-3.1-8b-instant"
            )
            prompt_content = f"{SYSTEM_PROMPT}\nUser Question: {user_msg}"
            response = llm.invoke(prompt_content)
            raw_text = response.content
            json_str = extract_json_str(raw_text)
            parsed = json.loads(json_str)
            return parsed
        except Exception as e:
            print(f"LLM Parsing Warning: {e}. Falling back to heuristic rule extractor.")

    # Rule-based fallback extractor if LLM API Key is unconfigured or call fails
    district_found = None
    for d in VALID_DISTRICTS:
        if d.lower() in user_msg.lower() or d.split()[0].lower() in user_msg.lower():
            district_found = d
            break

    crime_found = None
    for c in VALID_CRIMES:
        short_c = c.split("(")[0].strip().lower()
        if short_c in user_msg.lower() or "theft" in user_msg.lower() or "kalla" in user_msg.lower():
            crime_found = "Theft (MVT & General)"
            break
        elif "fraud" in user_msg.lower() or "upi" in user_msg.lower() or "cyber" in user_msg.lower():
            crime_found = "Online Financial Fraud / UPI Scam"
            break
        elif "burglary" in user_msg.lower():
            crime_found = "Burglary & House Breaking"
            break

    intent = "list"
    if "how many" in user_msg.lower() or "count" in user_msg.lower() or "total" in user_msg.lower():
        intent = "count"
    elif "hotspot" in user_msg.lower() or "area" in user_msg.lower() or "emerging" in user_msg.lower():
        intent = "hotspot"
    elif "network" in user_msg.lower() or "accused" in user_msg.lower() or "gang" in user_msg.lower():
        intent = "network"

    return {
        "crimeType": crime_found,
        "district": district_found,
        "dateFrom": None,
        "dateTo": None,
        "status": None,
        "intent": intent
    }

@app.get("/health")
def health_check():
    return {"status": "healthy", "service": "KSP Crime Intelligence AI Microservice"}

if __name__ == "__main__":
    import uvicorn
    port = int(os.environ.get("X_ZOHO_CATALYST_LISTEN_PORT", os.environ.get("PORT", 8000)))
    uvicorn.run(app, host="0.0.0.0", port=port)