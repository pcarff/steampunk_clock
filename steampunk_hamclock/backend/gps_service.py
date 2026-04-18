import random
import requests
import serial
import pynmea2
import threading
import time

class GPSService:
    def __init__(self):
        self.data = {
            "lat": 0.0,
            "lon": 0.0,
            "alt": 0.0,
            "fix": False,
            "source": "None",
            "status": "Initializing",
            "sats": 0
        }
        self.running = False
        self.thread = None
        self.connected_port = None
        self.last_baud = 9600
        self.ports = [
            '/dev/ttyACM0', '/dev/ttyACM1',
            '/dev/ttyUSB0', '/dev/ttyUSB1', '/dev/ttyUSB2', 
            '/dev/ttyAMA0', '/dev/serial0',
            '/dev/cu.usbserial', '/dev/cu.usbmodem'
        ]

    def start(self):
        if self.running:
            return
        self.running = True
        self.thread = threading.Thread(target=self._read_loop, daemon=True)
        self.thread.start()

    def stop(self):
        self.running = False
        if self.thread:
            self.thread.join(timeout=1.0)

    def _read_loop(self):
        while self.running:
            found_port = False
            # Try last known good port/baud first if available
            scan_ports = [self.connected_port] + self.ports if self.connected_port else self.ports
            scan_bauds = [self.last_baud] + [9600, 4800, 115200]
            
            for port in scan_ports:
                if not port: continue
                if found_port: break
                for baud in scan_bauds:
                    if found_port: break
                    try:
                        with serial.Serial(port, baud, timeout=1) as ser:
                            # Verify valid NMEA data
                            valid_data = False
                            for _ in range(10): # Give it more chances to see a $
                                line = ser.readline().decode('ascii', errors='replace')
                                if line.startswith('$'):
                                    valid_data = True
                                    break
                            
                            if valid_data:
                                self.connected_port = port
                                self.last_baud = baud
                                found_port = True
                                print(f"GPS locked to {port} at {baud} baud")
                                
                                timeout_count = 0
                                while self.running:
                                    line = ser.readline().decode('ascii', errors='replace')
                                    if not line:
                                        timeout_count += 1
                                        if timeout_count > 10: # 10 seconds of silence
                                            print(f"GPS lost signal on {port}")
                                            break
                                        continue
                                    
                                    timeout_count = 0
                                    if line.startswith('$GPRMC') or line.startswith('$GPGGA'):
                                        try:
                                            msg = pynmea2.parse(line)
                                            num_sats = getattr(msg, 'num_sats', 0)
                                            if hasattr(msg, 'latitude') and msg.latitude:
                                                self.data = {
                                                    "lat": msg.latitude,
                                                    "lon": msg.longitude,
                                                    "alt": getattr(msg, 'altitude', 0.0),
                                                    "fix": True,
                                                    "source": "GPS",
                                                    "status": "GPS Fix",
                                                    "port": port,
                                                    "sats": num_sats
                                                }
                                            else:
                                                self.data["status"] = "GPS Searching..."
                                                self.data["source"] = "GPS"
                                                self.data["fix"] = False
                                                self.data["sats"] = num_sats
                                        except: pass
                                    elif line.startswith('$GPGSV'):
                                        try:
                                            msg = pynmea2.parse(line)
                                            self.data["sats_in_view"] = getattr(msg, 'num_sv_in_view', 0)
                                        except: pass
                                
                    except (serial.SerialException, OSError):
                        continue
            
            if not found_port:
                self.connected_port = None
                self.data["source"] = "None"
                self.data["fix"] = False
            
            time.sleep(2)

    def get_data(self):
        if self.data["fix"] and self.data["source"] == "GPS":
            return self.data
        
        net_loc = self.get_network_location()
        
        if self.connected_port:
            return {
                **net_loc,
                "gps_detected": True,
                "gps_status": self.data["status"],
                "sats": self.data.get("sats", 0),
                "sats_in_view": self.data.get("sats_in_view", 0),
                "is_fallback": True
            }
        
        return net_loc

    def get_network_location(self):
        try:
            response = requests.get("http://ip-api.com/json/", timeout=5)
            if response.status_code == 200:
                net_data = response.json()
                return {
                    "lat": net_data.get('lat', 0.0),
                    "lon": net_data.get('lon', 0.0),
                    "city": net_data.get('city', 'Unknown'),
                    "region": net_data.get('regionName', 'Unknown'),
                    "country": net_data.get('country', 'Unknown'),
                    "source": "Network",
                    "status": "Network Mode",
                    "fix": True
                }
        except Exception:
            pass
        
        return {
            "lat": 0.0,
            "lon": 0.0,
            "city": "Unknown",
            "source": "Network",
            "status": "Offline",
            "fix": False
        }
