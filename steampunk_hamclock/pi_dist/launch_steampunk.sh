#!/bin/bash

# Launch Steampunk HamClock in Kiosk Mode
LOGFILE="/home/pcarff/steampunk_launch.log"
exec > >(tee -a "$LOGFILE") 2>&1

echo "--- Launching Steampunk HamClock at $(date) ---"
APP_DIR="/home/pcarff/Workspaces/steampunk_hamclock"
cd "$APP_DIR" || { echo "Failed to cd to $APP_DIR"; exit 1; }

echo "Starting Steampunk HamClock Backend..."
if [ -d "backend/venv" ]; then
    source backend/venv/bin/activate
else
    echo "ERROR: Virtual environment not found in $APP_DIR/backend/venv"
    exit 1
fi

# Kill any existing instances
pkill -f "python3 backend/main.py"
pkill -f "chromium"

python3 backend/main.py &
BACKEND_PID=$!
echo "Backend started with PID $BACKEND_PID"

# Wait for backend to be ready
echo "Waiting for backend to initialize..."
for i in {1..30}; do
  if curl -s http://localhost:8000/api/status > /dev/null; then
    echo "Backend is UP!"
    break
  fi
  echo "Still waiting ($i/30)..."
  sleep 1
done

echo "Launching Chromium in Kiosk Mode..."
# Use --display=:0 to ensure it opens on the physical screen
export DISPLAY=:0

# Detect browser command
if command -v chromium &> /dev/null; then
    BROWSER_CMD="chromium"
else
    BROWSER_CMD="chromium-browser"
fi

echo "Using browser: $BROWSER_CMD"
# Added flags to suppress Google API/Sync errors and background noise
$BROWSER_CMD --kiosk --app=http://localhost:8000 \
    --noerrdialogs --disable-infobars \
    --check-for-update-interval=31536000 \
    --disable-features=TranslateUI,Translate,GlobalMediaControls,InProductHelp \
    --overscroll-history-navigation=0 \
    --disable-sync \
    --no-first-run \
    --disable-background-networking \
    --log-level=3 &
BROWSER_PID=$!

echo "Browser launched with PID $BROWSER_PID"

# Handle exit
trap "kill $BACKEND_PID $BROWSER_PID; exit" INT TERM
wait
