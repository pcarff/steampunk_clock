#!/bin/bash

# Launch Steampunk HamClock in Kiosk Mode
APP_DIR="/home/pcarff/Workspaced/steampunk_hamclock"
cd "$APP_DIR"

echo "Starting Steampunk HamClock Backend..."
source backend/venv/bin/activate
python3 backend/main.py &
BACKEND_PID=$!

# Wait for backend to be ready
echo "Waiting for backend to initialize..."
until curl -s http://localhost:8000/api/status > /dev/null; do
  sleep 1
done

echo "Launching Chromium in Kiosk Mode..."
chromium-browser --kiosk --app=http://localhost:8000 --noerrdialogs --disable-infobars --check-for-update-interval=31536000 &
BROWSER_PID=$!

# Handle exit
trap "kill $BACKEND_PID $BROWSER_PID; exit" INT TERM
wait
