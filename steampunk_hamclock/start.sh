#!/bin/bash

# Steampunk HamClock Start Script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$DIR"

echo "Starting Steampunk HamClock..."

# Start Backend
echo "Starting Backend on http://localhost:8000"
cd backend
if [ ! -d "venv" ]; then
    python3 -m venv venv
    source venv/bin/activate
    pip install -r requirements.txt
else
    source venv/bin/activate
fi
python3 main.py &
BACKEND_PID=$!

# Start Frontend
echo "Starting Frontend..."
cd ../frontend
npm run dev &
FRONTEND_PID=$!

function cleanup {
    echo "Shutting down..."
    kill $BACKEND_PID
    kill $FRONTEND_PID
}

trap cleanup INT TERM

echo "Both services are running."
echo "Frontend: http://localhost:5173"
echo "Backend API: http://localhost:8000"
echo "Press [CTRL+C] to stop."

wait
