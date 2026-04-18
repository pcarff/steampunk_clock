#!/bin/bash

# Steampunk HamClock Pi Setup Script
echo "Installing Steampunk HamClock dependencies..."
sudo apt-get update
sudo apt-get install -y python3-venv python3-pip chromium-browser curl

# Create directory
mkdir -p ~/Workspaced/steampunk_hamclock
cd ~/Workspaced/steampunk_hamclock

# Setup Backend
cd backend
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
cd ..

# Make launch script executable
chmod +x launch_steampunk.sh

# Install Desktop Icon
mkdir -p ~/Desktop
cp steampunk_clock.desktop ~/Desktop/
chmod +x ~/Desktop/steampunk_clock.desktop

echo "Setup Complete!"
echo "You can now double-click the Steampunk HamClock icon on your desktop."
