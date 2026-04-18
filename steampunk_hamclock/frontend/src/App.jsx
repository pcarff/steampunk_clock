import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Radio, Wind, Sun, MapPin, Navigation, Globe } from 'lucide-react';

const Digit = ({ value }) => {
  return (
    <div className="nixie-digit">
      <AnimatePresence mode="popLayout">
        <motion.img
          key={value}
          src={`/digits/${value}.jpg`}
          alt={value}
          initial={{ opacity: 0, scale: 1.1, filter: 'brightness(2)' }}
          animate={{ opacity: 1, scale: 1, filter: 'brightness(1)' }}
          exit={{ opacity: 0, scale: 0.9, filter: 'brightness(0.5)' }}
          transition={{ duration: 0.3, ease: "easeOut" }}
          className="nixie-image"
        />
      </AnimatePresence>
      <div className="glow-overlay"></div>
    </div>
  );
};

const InfoPanel = ({ title, icon: Icon, value, label }) => (
  <div className="brass-panel" style={{ padding: '20px', minWidth: '180px' }}>
    <div className="screw screw-tl" style={{ width: '10px', height: '10px' }}></div>
    <div className="screw screw-tr" style={{ width: '10px', height: '10px' }}></div>
    <div className="screw screw-bl" style={{ width: '10px', height: '10px' }}></div>
    <div className="screw screw-br" style={{ width: '10px', height: '10px' }}></div>
    <div style={{ display: 'flex', alignItems: 'center', gap: '10px', color: '#1a0f00', marginBottom: '12px', textShadow: '0.5px 0.5px 0 rgba(255,255,255,0.2)' }}>
      <Icon size={20} />
      <span style={{ fontSize: '0.9rem', fontWeight: '800', textTransform: 'uppercase', letterSpacing: '1px' }}>{title}</span>
    </div>
    <div style={{ 
      fontSize: '1.8rem', 
      color: '#ffdd00', 
      textShadow: '0 0 12px rgba(0,0,0,0.8), 0 0 6px rgba(255,150,0,0.5)', 
      fontFamily: 'Orbitron',
      fontWeight: 'bold'
    }}>
      {value}
    </div>
    <div style={{ fontSize: '0.9rem', color: '#2a1a0a', marginTop: '6px', fontWeight: '600' }}>{label}</div>
  </div>
);

const Clock = () => {
  const [time, setTime] = useState(new Date());
  const [location, setLocation] = useState(null);

  useEffect(() => {
    const timer = setInterval(() => {
      setTime(new Date());
    }, 1000);

    // Fetch location
    fetch('http://localhost:8000/api/location')
      .then(res => res.json())
      .then(data => setLocation(data))
      .catch(err => console.error("Location fetch failed", err));

    return () => clearInterval(timer);
  }, []);

  const format = (num) => num.toString().padStart(2, '0');
  
  const hours = format(time.getHours());
  const minutes = format(time.getMinutes());
  const seconds = format(time.getSeconds());

  return (
    <div className="brass-panel" style={{ padding: '50px 70px' }}>
      <div className="screw screw-tl"></div>
      <div className="screw screw-tr"></div>
      <div className="screw screw-bl"></div>
      <div className="screw screw-br"></div>
      
      <div className="nixie-container">
        <Digit value={hours[0]} />
        <Digit value={hours[1]} />
        
        <div className="divider">
          <div className="dot"></div>
          <div className="dot"></div>
        </div>
        
        <Digit value={minutes[0]} />
        <Digit value={minutes[1]} />
        
        <div className="divider">
          <div className="dot"></div>
          <div className="dot"></div>
        </div>
        
        <Digit value={seconds[0]} />
        <Digit value={seconds[1]} />
      </div>
      
      <div style={{ 
        marginTop: '25px', 
        textAlign: 'center', 
        fontFamily: "'Orbitron', sans-serif",
        color: '#1a0f00',
        textShadow: '0.5px 0.5px 0 rgba(255,255,255,0.3)',
        fontWeight: '900',
        fontSize: '1.5rem',
        textTransform: 'uppercase',
        letterSpacing: '5px',
        borderTop: '2px solid rgba(0,0,0,0.15)',
        paddingTop: '18px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '8px'
      }}>
        <div>Steampunk HamClock</div>
        {location && (
          <div style={{ 
            fontSize: '1.4rem', 
            letterSpacing: '2px', 
            color: '#ffdd00', 
            textShadow: '0 0 10px rgba(0,0,0,0.8), 0 0 5px rgba(255,150,0,0.5)',
            fontFamily: 'Orbitron',
            fontWeight: 'bold',
            marginTop: '10px',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
              {location.city ? `${location.city.toUpperCase()}, ${location.country?.toUpperCase() || ''}` : location.status.toUpperCase()}
            </div>
            
            <div style={{ 
              fontSize: '1.1rem', 
              opacity: 0.9, 
              marginTop: '6px', 
              textAlign: 'center', 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center', 
              gap: '12px',
              color: (location.source === 'GPS' && location.fix) ? '#ffdd00' : '#ffaa00',
              animation: (location.gps_detected && !location.fix) ? 'blink 1s infinite' : 'none'
            }}>
              {(location.source === 'GPS' || location.gps_detected) ? <Navigation size={20} /> : <Globe size={20} />}
              <span>[{location.lat.toFixed(2)}, {location.lon.toFixed(2)}]</span>
            </div>
            {location.gps_detected && !location.fix && (
              <div style={{ 
                fontSize: '0.9rem', 
                marginTop: '8px', 
                color: '#ff6600', 
                fontWeight: 'bold',
                letterSpacing: '1px',
                background: 'rgba(0,0,0,0.2)',
                padding: '4px 12px',
                borderRadius: '4px'
              }}>
                SEARCHING SATS: {location.sats || location.sats_in_view || 0}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

function App() {
  return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column',
      justifyContent: 'center', 
      alignItems: 'center', 
      gap: '30px',
      height: '100vh', 
      width: '100vw',
      background: 'radial-gradient(circle at center, #2c1e12 0%, #000 100%)',
      padding: '15px'
    }}>
      <div className="steampunk-bg"></div>
      
      <Clock />
      
      <div style={{ display: 'flex', gap: '25px' }}>
        <InfoPanel title="Solar" icon={Sun} value="SFI 142" label="Flux Index" />
        <InfoPanel title="Radio" icon={Radio} value="20m" label="Band Open" />
        <InfoPanel title="Weather" icon={Wind} value="12 kts" label="Wind Speed" />
        <InfoPanel title="Location" icon={MapPin} value="CN87" label="Maidenhead" />
      </div>
    </div>
  );
}

export default App;
