import serial
import time

def test_gps():
    ports = [
        '/dev/ttyUSB0', '/dev/ttyUSB1', '/dev/ttyUSB2', 
        '/dev/ttyACM0', '/dev/ttyACM1', 
        '/dev/ttyAMA0', '/dev/serial0'
    ]
    bauds = [9600, 4800, 115200]

    print("--- Steampunk HamClock GPS Tester ---")
    print("Searching for GPS data on all common ports...")

    for port in ports:
        for baud in bauds:
            print(f"Checking {port} at {baud} baud...", end='\r')
            try:
                # Use a short timeout for scanning
                with serial.Serial(port, baud, timeout=2) as ser:
                    # Read a few lines
                    for _ in range(10):
                        line = ser.readline().decode('ascii', errors='replace').strip()
                        if line.startswith('$'):
                            print(f"\n[SUCCESS] Found data on {port} at {baud} baud!")
                            print(f"Latest NMEA sentence: {line}")
                            
                            # Analyze common sentences
                            if '$GPRMC' in line or '$GPGGA' in line:
                                print(">> Valid Location sentence detected.")
                            if '$GPGSV' in line:
                                print(">> Satellite info detected.")
                            
                            print("\nContinuous stream (Ctrl+C to stop):")
                            while True:
                                print(ser.readline().decode('ascii', errors='replace').strip())
                            return
            except (serial.SerialException, OSError):
                continue
    
    print("\n[FAILED] No GPS data found on any common port.")
    print("\nPossible issues:")
    print("1. Permission denied: run with 'sudo' or check 'groups'.")
    print("2. Hardware not detected: check 'lsusb' and 'dmesg | grep tty'.")
    print("3. Wrong device: check if your GPS is on a different /dev/tty* port.")

if __name__ == "__main__":
    try:
        test_gps()
    except KeyboardInterrupt:
        print("\nTest stopped by user.")
