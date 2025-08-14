# EaseMind (Panic Detection & Intervention)

A mobile-first system that detects potential panic/anxiety from user text (SMS/email content or manual survey) and returns a personalized, calming intervention.

**Backend:** Java 17 · Spring Boot · WebClient · OpenRouter.ai (GPT-4o / Claude)  
**Frontend:** React Native (WIP in this repo)

---

## 🎥 Demo

▶️ **Watch the demo:** [Demo_Video.mp4](Demo_Video.mp4)

---

## 🧭 Overview

- **Inputs:** user-provided text from **SMS**, **email**, or **manual survey** (frontend WIP).  
- **Detection:** backend builds a structured prompt with context (mood, history, optional heart rate) and calls an **LLM via OpenRouter** to score urgency and compose an intervention.  
- **Output:** JSON with a numeric `panicScore` (0–1) and a short `calmingText` (breathing/grounding/check-in style).

> Wearables (Google Fit / Apple HealthKit) and push/TTS delivery are planned.

---

## 🏗 Architecture (simple)

1. **Controller** accepts a single POST with any combination of `sms`, `email`, `mood/survey`, optional `heartRate`, and `historyCount`.
2. **Chain of Responsibility** picks the **first non-empty** text source in order: **SMS → Email → Manual**.
3. **Prompt Builder (Builder)** composes a consistent system/user prompt with the context.
4. **LLM Proxy (Proxy)** calls OpenRouter with your chosen model (e.g., `openai/gpt-4o-mini`, `anthropic/claude-3-haiku`).
5. **Response Mapper** returns `{ panicScore, calmingText }`.

---

## 🔌 API

**Endpoint**
```
POST /api/panic-intervention
Content-Type: application/json
```

**Request (example)**
```json
{
  "sms": "Having trouble catching my breath.",
  "email": "optional@email.com",
  "wearables": { "hr": 95 },
  "mood": "anxious",
  "heartRate": 95,
  "historyCount": 2
}
```

**Response (example)**
```json
{
  "panicScore": 0.78,
  "calmingText": "Try 4-7-8 breathing: inhale 4s, hold 7s, exhale 8s. Notice five things you can see."
}
```

---

## 🚀 Quick Start

> **Prereqs:** Java **17**, Gradle (wrapper included), Internet access, **OpenRouter API key**.

```bash
# 1) Clone
git clone https://github.com/<your-username>/emotional-calming-app.git
cd emotional-calming-app/backend

# 2) Configure env vars (recommended)
export OPENROUTER_API_KEY="sk-or-xxxxxxxx"
export OPENROUTER_MODEL="openai/gpt-4o-mini"   # or a Claude model (see below)

# 3) Run backend
./gradlew bootRun

# 4) Test
curl -i -X POST http://localhost:8080/api/panic-intervention   -H "Content-Type: application/json"   -d '{ "sms":"I feel uneasy", "mood":"nervous", "historyCount":1 }'
```

---

## ⚙️ Configuration

**Environment variables (preferred)**

```bash
OPENROUTER_API_KEY=sk-or-xxxxxxxx

# Choose a model (availability/credits may vary)
OPENROUTER_MODEL=openai/gpt-4o-mini
# or:
# OPENROUTER_MODEL=anthropic/claude-3-haiku
# OPENROUTER_MODEL=anthropic/claude-3-5-sonnet
```

**application.properties (optional, avoid committing secrets)**
```properties
# only if you don’t use env vars
openrouter.api.key=sk-or-xxxxxxxx
openrouter.model=openai/gpt-4o-mini
```

---

## 🧪 Test It (curl recipes)

**1) Minimal**
```bash
curl -i -X POST http://localhost:8080/api/panic-intervention   -H "Content-Type: application/json"   -d '{ "sms":"Feeling stressed about a presentation", "historyCount":0 }'
```

**2) Moderate stress + HR**
```bash
curl -i -X POST http://localhost:8080/api/panic-intervention   -H "Content-Type: application/json"   -d '{ "email":"Work is overwhelming today", "heartRate":105, "historyCount":2 }'
```

**3) Extreme long message (load)**
```bash
curl -i -X POST http://localhost:8080/api/panic-intervention   -H "Content-Type: application/json"   -d '{
    "sms":"'"$(printf 'Stress! %.0s' {1..200})"'",
    "mood":"stressed",
    "heartRate":130,
    "historyCount":3
  }'
```

**4) Calm/neutral**
```bash
curl -i -X POST http://localhost:8080/api/panic-intervention   -H "Content-Type: application/json"   -d '{ "mood":"okay, just checking in", "historyCount":0 }'
```

---

## 🔄 Switching Models

```bash
# GPT-4o mini (fast/cheap)
export OPENROUTER_MODEL="openai/gpt-4o-mini"

# Claude (concise/grounded)
export OPENROUTER_MODEL="anthropic/claude-3-haiku"

# Higher-end Claude (may require credits)
export OPENROUTER_MODEL="anthropic/claude-3-5-sonnet"
```

Restart the app after changing the model.

---

## 🧰 Troubleshooting

- **401 Unauthorized (OpenRouter)**  
  Key missing/invalid. Re-export `OPENROUTER_API_KEY` and restart.
- **402 Payment Required**  
  Selected model needs credits/plan. Switch to a free/cheaper model (e.g., `openai/gpt-4o-mini`, `anthropic/claude-3-haiku`) or add credits.
- **DNS/Network errors**  
  Check connectivity/VPN/proxy; backend must reach `openrouter.ai`.
- **Version mismatch**  
  Use **Java 17**. Verify with `java -version`. The Gradle wrapper handles Gradle versioning.

---

## 🧱 Tech & Patterns

- **Tech:** Java 17, Spring Boot, Spring Web (WebClient), Gradle, OpenRouter.ai (GPT-4o/Claude)  
- **Patterns:**  
  - **Chain of Responsibility:** SMS → Email → Manual selection.  
  - **Builder:** structured prompt assembly with context (mood/history/HR).  
  - **Proxy:** LLM client behind a swappable interface.  
  - **Facade/Mediator:** central service orchestrates detection, prompt, LLM call, and response.

---

## 🗺️ Roadmap

- React Native UI (chatbot + breathing/grounding views), push notifications, text-to-speech  
- Wearables via Google Fit / Apple HealthKit  
- Strategy selection based on history/preferences  
- Safety guardrails and escalation paths

---

## 📁 Repo Structure

```
emotional-calming-app/
└─ backend/
   ├─ src/main/java/com/example/panic/
   │  ├─ controller/         # /api/panic-intervention
   │  ├─ service/            # detection pipeline, LLM client, orchestration
   │  ├─ prompt/             # prompt builder & models
   │  └─ config/             # WebClient + OpenRouter config
   ├─ src/main/resources/
   │  └─ application.properties
   ├─ build.gradle
   └─ gradle.properties
```

---

> ⚠️ **Disclaimer:** This is for research/education. It is **not** a medical device and is not a substitute for professional care.

_🫶 Stay calm and keep shipping._
