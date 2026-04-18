#!/bin/bash

# Steampunk HamClock Pi Setup Script
echo "Installing Steampunk HamClock dependencies..."

# Ignore errors from broken repositories (like NodeSource)
sudo apt-get update || true

echo "Installing required packages..."
# Try to install chromium and other deps, ignoring failures from other repos
sudo apt-get install -y python3-venv python3-pip curl xbindkeys chromium || \
sudo apt-get install -y python3-venv python3-pip curl xbindkeys chromium-browser || \
echo "Warning: Some packages failed to install. Please check manually."

# Create directory
mkdir -p ~/Workspaces/steampunk_hamclock
cd ~/Workspaces/steampunk_hamclock

# Setup Backend
echo "Setting up Python virtual environment..."
if [ ! -d "backend/venv" ]; then
    python3 -m venv backend/venv
fi

source backend/venv/bin/activate
pip install -r backend/requirements.txt

# Make launch script executable
chmod +x launch_steampunk.sh

# Install Desktop Icon
echo "Installing Desktop shortcut..."
mkdir -p ~/Desktop
cp steampunk_clock.desktop ~/Desktop/
chmod +x ~/Desktop/steampunk_clock.desktop

# Configure reTerminal F1 Button
echo "Configuring reTerminal buttons..."
cp xbindkeysrc ~/.xbindkeysrc
# Ensure xbindkeys starts with the desktop
mkdir -p ~/.config/autostart
echo "[Desktop Entry]
Type=Application
Exec=xbindkeys
Name=xbindkeys
" > ~/.config/autostart/xbindkeys.desktop

echo "Setup Complete!"
echo "You can now double-click the Steampunk HamClock icon on your desktop."
