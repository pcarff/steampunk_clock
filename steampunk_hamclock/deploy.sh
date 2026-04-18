#!/bin/bash

PI_USER="pcarff"
PI_IP="10.23.1.25"
REMOTE="${PI_USER}@${PI_IP}"
TARGET_DIR="/home/${PI_USER}/Workspaces/steampunk_hamclock"

echo "Preparing smart deployment for Pi at $REMOTE..."

# 1. Build Frontend
echo "Building frontend..."
cd frontend
npm run build
cd ..

# 2. Ensure target directory exists on Pi
ssh ${REMOTE} "mkdir -p $TARGET_DIR"

# 3. Smart Copy using rsync (only changes)
echo "Syncing files..."

# Sync Backend
rsync -avz --progress \
    --exclude 'venv' \
    --exclude '__pycache__' \
    --exclude '.pytest_cache' \
    backend/ ${REMOTE}:${TARGET_DIR}/backend/

# Sync Frontend Build
rsync -avz --progress \
    frontend/dist/ ${REMOTE}:${TARGET_DIR}/frontend/

# Sync Configuration Scripts
rsync -avz --progress \
    pi_dist/launch_steampunk.sh \
    pi_dist/steampunk_clock.desktop \
    pi_dist/setup_pi.sh \
    pi_dist/xbindkeysrc \
    ${REMOTE}:${TARGET_DIR}/

echo "Running setup on Pi (if needed)..."
ssh -t ${REMOTE} "cd $TARGET_DIR && bash setup_pi.sh"

echo "Smart deployment finished!"
echo ""
echo "TIP: If you want to avoid entering your password multiple times,"
echo "run this command once: ssh-copy-id $REMOTE"
