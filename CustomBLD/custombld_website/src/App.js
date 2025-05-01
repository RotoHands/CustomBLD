import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Tabs, Tab, Spinner, Alert, Badge } from 'react-bootstrap';
import QueryForm from './QueryForm';
import ScrambleResults from './components/ScrambleResults';
import Stats from './components/Stats';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [results, setResults] = useState([]);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState(null);
  const [statsLoading, setStatsLoading] = useState(true);
  const [statsError, setStatsError] = useState(null);
  const [activeTab, setActiveTab] = useState('search');
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    // Check for mobile screen size
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
    };

    window.addEventListener('resize', handleResize);
    
    // Apply pastel-blue-8 theme on component mount
    const theme = {
      primary: '#14539E',
      secondary: '#2B66A9',
      success: '#C1E1C1',
      danger: '#FFB6B6',
      warning: '#FFE5B4',
      info: '#B5EAD7',
      light: '#F8F9FA',
      dark: '#343A40',
      'accordion-header-bg': '#F0F4F8',
      'accordion-header-color': '#FFFFFF',
      'primary-rgb': '20, 83, 158',
      'header-color': '#FFFFFF',
      'body-bg': '#FFFFFF',
      'body-color': '#212529'
    };

    Object.entries(theme).forEach(([key, value]) => {
      document.documentElement.style.setProperty(`--bs-${key}`, value);
    });

    document.body.style.backgroundColor = theme['body-bg'];
    document.body.style.color = theme['body-color'];

    const headers = document.querySelectorAll('h1, h2, h3, h4, h5, h6');
    headers.forEach(header => {
      header.style.color = theme['header-color'];
    });

    // Fetch stats when component mounts
    fetchStats();

    // Clean up event listener
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  const handleFormSubmit = (results, errorMessage) => {
    if (errorMessage) {
      setError(errorMessage);
      setResults([]);
    } else {
      setError(null);
      setResults(results);
    }
  };

  const fetchStats = async () => {
    try {
      setStatsLoading(true);
      setStatsError(null);
      
      // Try with proxy first (defined in package.json)
      try {
        const response = await fetch('/scramble-stats', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (response.ok) {
          const data = await response.json();
          setStats(data);
          setStatsLoading(false);
          return;
        }
      } catch (proxyError) {
        console.warn('Proxy request failed, trying direct URL', proxyError);
      }
      
      // Try with Docker service name
      try {
        const serviceResponse = await fetch('http://server:5000/scramble-stats', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (serviceResponse.ok) {
          const data = await serviceResponse.json();
          setStats(data);
          setStatsLoading(false);
          return;
        }
      } catch (serviceError) {
        console.warn('Service name request failed, trying localhost', serviceError);
      }
      
      // Final fallback to localhost
      const localhostResponse = await fetch('http://localhost:5000/scramble-stats', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!localhostResponse.ok) {
        throw new Error(`HTTP error! Status: ${localhostResponse.status}`);
      }

      const data = await localhostResponse.json();
      setStats(data);
    } catch (err) {
      console.error('Error fetching stats:', err);
      setStatsError(`Failed to load statistics: ${err.message}`);
    } finally {
      setStatsLoading(false);
    }
  };

  return (
    <Container fluid className="px-0">
      <header className="bg-primary text-white py-3 mb-3">
        <Container>
          <h1 className="mb-0" style={{ fontSize: isMobile ? '1.75rem' : '2.5rem' }}>Custom BLD Trainer</h1>
        </Container>
      </header>

      <Container className={isMobile ? "px-2 py-2" : "py-4"}>
        <Tabs
          id="main-tabs"
          activeKey={activeTab}
          onSelect={(k) => setActiveTab(k)}
          className="mb-3"
        >
          <Tab eventKey="search" title={<span><i className="fas fa-search me-1"></i>{isMobile ? "" : " Search"}</span>}>
            <div className="py-2">
              <QueryForm onSubmit={handleFormSubmit} />
              
              {error && (
                <div className="alert alert-danger my-3" role="alert">
                  {error}
                </div>
              )}
              
              {results && results.length > 0 && (
                <ScrambleResults results={results} />
              )}
            </div>
          </Tab>
          <Tab eventKey="stats" title={<span><i className="fas fa-chart-bar me-1"></i>{isMobile ? "" : " Statistics"}</span>}>
            <div className="py-2">
              <h3 className="mb-3" style={{ fontSize: isMobile ? '1.5rem' : '1.75rem' }}>Database Statistics</h3>
              <Stats 
                stats={stats} 
                statsLoading={statsLoading} 
                statsError={statsError} 
                fetchStats={fetchStats} 
                isMobile={isMobile} 
              />
            </div>
          </Tab>
        </Tabs>
      </Container>
      
      {isMobile && (
        <div className="mobile-helper-footer bg-light text-center py-2 mt-4 shadow-sm">
          <small>Tip: Rotate to landscape for full tables</small>
        </div>
      )}
    </Container>
  );
}

export default App;