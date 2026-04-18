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
        self.ports = [
            '/dev/ttyUSB0', '/dev/ttyUSB1', '/dev/ttyUSB2', 
            '/dev/ttyACM0', '/dev/ttyACM1', 
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
            for port in self.ports:
                for baud in [9600, 4800, 115200]:
                    try:
                        with serial.Serial(port, baud, timeout=1) as ser:
                            # Read a few lines to check for valid NMEA
                            for _ in range(5):
                                line = ser.readline().decode('ascii', errors='replace')
                                if line.startswith('$'):
                                    self.connected_port = port
                                    found_port = True
                                    print(f"GPS hardware detected on {port} at {baud} baud")
                                    
                                    while self.running:
                                        line = ser.readline().decode('ascii', errors='replace')
                                        if not line: break
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
                                                        "status": "Fix acquired",
                                                        "port": port,
                                                        "sats": num_sats
                                                    }
                                                else:
                                                    self.data["status"] = "Waiting for Fix"
                                                    self.data["source"] = "GPS"
                                                    self.data["fix"] = False
                                                    self.data["sats"] = num_sats
                                            except: pass
                                        elif line.startswith('$GPGSV'):
                                            # Satellites in view
                                            try:
                                                msg = pynmea2.parse(line)
                                                self.data["sats_in_view"] = getattr(msg, 'num_sv_in_view', 0)
                                            except: pass
                                    break # Exit baud loop if port closed or stopped
                    except:
                        continue
                if found_port: break
                            
                except (serial.SerialException, OSError):
                    self.connected_port = None
                    continue
            
            if not found_port:
                self.connected_port = None
            
            time.sleep(2)

    def get_data(self):
        # PRIORITY 1: GPS with a Fix
        if self.data["fix"] and self.data["source"] == "GPS":
            return self.data
        
        # PRIORITY 2: GPS detected but no Fix
        if self.connected_port:
            return {
                **self.data,
                "lat": 0.0,
                "lon": 0.0,
                "source": "GPS",
                "status": "Searching for Satellites...",
                "port": self.connected_port
            }
        
        # PRIORITY 3: Network Fallback
        return self.get_network_location()

    def get_network_location(self):
        try:
            response = requests.get("http://ip-api.com/json/", timeout=5)
            if response.status_code == 200:
                net_data = response.json()
                return {
                    "lat": net_data.get( 'lat', 0.0),
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
