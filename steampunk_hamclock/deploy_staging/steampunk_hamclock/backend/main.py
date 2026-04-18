from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
import os
import uvicorn
from contextlib import asynccontextmanager
from gps_service import GPSService

gps_service = GPSService()

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup logic
    print("Steampunk HamClock Backend Starting...")
    gps_service.start()
    yield
    # Shutdown logic
    print("Steampunk HamClock Backend Shutting Down...")
    gps_service.stop()

app = FastAPI(lifespan=lifespan)

# CORS configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/api/status")
async def get_status():
    return {"status": "ok", "app": "steampunk_hamclock"}

@app.get("/api/time")
async def get_time():
    import datetime
    now = datetime.datetime.now()
    return {
        "hour": now.hour,
        "minute": now.minute,
        "second": now.second,
        "microsecond": now.microsecond
    }

@app.get("/api/location")
async def get_location():
    return gps_service.get_data()

# Serve frontend if it exists
frontend_dist = os.path.join(os.path.dirname(__file__), "..", "frontend", "dist")
if os.path.isdir(frontend_dist):
    app.mount("/", StaticFiles(directory=frontend_dist, html=True), name="static")

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
