import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Tabs, Tab, Spinner, Alert, Badge, Modal, Button } from 'react-bootstrap';
import QueryForm from './QueryForm';
import ScrambleResults from './components/ScrambleResults';
import Stats from './components/Stats';
import Footer from './components/Footer';
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
  const [showHelp, setShowHelp] = useState(false);

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

  const fetchStats = async (refresh = false) => {
    try {
      setStatsLoading(true);
      setStatsError(null);
      
      // Add refresh parameter to URL if needed
      const refreshParam = refresh ? '?refresh=true' : '';
      
      // URLs to try in order
      const urls = [
        `/scramble-stats${refreshParam}`,
        // `http://server:5000/scramble-stats${refreshParam}`,
        // `http://localhost:5000/scramble-stats${refreshParam}`
      ];
      
      let succeeded = false;
      let lastError = null;
      
      // Try each URL in sequence
      for (const url of urls) {
        try {
          console.log(`Attempting to fetch stats from: ${url}`);
          const response = await fetch(url, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          });
          
          if (response.ok) {
            const data = await response.json();
            setStats(data);
            setStatsLoading(false);
            succeeded = true;
            console.log(`Successfully fetched stats from: ${url}`);
            
            // If this was a refresh, show a message
            if (refresh) {
              // If we have a notification system, we could use it here
              console.log('Statistics refreshed successfully');
            }
            
            break; // Exit the loop on success
          } else {
            const errorText = await response.text();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
          }
        } catch (error) {
          console.warn(`Failed to fetch from ${url}:`, error);
          lastError = error;
        }
      }
      
      if (!succeeded) {
        throw lastError || new Error('Failed to fetch statistics from all available endpoints');
      }
    } catch (err) {
      console.error('Error fetching stats:', err);
      setStatsError(`Failed to load statistics: ${err.message}`);
      setStatsLoading(false);
    }
  };

  return (
    <Container fluid className="px-0">
      <header className="bg-primary text-white py-3 mb-3">
        <Container>
          <div className="d-flex justify-content-between align-items-center">
            <h1 className="mb-0" style={{ 
              fontSize: isMobile ? '1.75rem' : '2.5rem',
              fontFamily: 'Rubik, sans-serif',
              fontWeight: '600'
            }}>Custom BLD Trainer</h1>
            <div className="social-icons d-flex align-items-center">
              <div className="social-grid me-3">
                <div className="d-flex gap-2 mb-2">
                  <a 
                    href="#" 
                    onClick={(e) => {
                      e.preventDefault();
                      setShowHelp(true);
                    }}
                    className="text-white"
                  >
                    <i className="fas fa-question-circle fa-2x"></i>
                  </a>
                  <a href="https://github.com/rotohands" target="_blank" rel="noopener noreferrer" className="text-white">
                    <i className="fab fa-github fa-2x"></i>
                  </a>
                </div>
                <div className="d-flex gap-2">
                  <a href="https://instagram.com/roto_hands" target="_blank" rel="noopener noreferrer" className="text-white">
                    <i className="fab fa-instagram fa-2x"></i>
                  </a>
                  <a href="https://youtube.com/channel/UCVGKCZFamCuYXiln9w3Cnxw" target="_blank" rel="noopener noreferrer" className="text-white">
                    <i className="fab fa-youtube fa-2x"></i>
                  </a>
                </div>
              </div>
              <img 
                src="/rotohands_logo.png" 
                alt="Roto Hands" 
                style={{ 
                  width: '65px', 
                  height: '65px',
                  filter: 'invert(1)'
                }} 
              />
            </div>
          </div>
        </Container>
      </header>

      {/* Help Modal */}
      <Modal show={showHelp} onHide={() => setShowHelp(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Resources</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="resources-list">
            <ul className="list-unstyled">
              <li className="mb-3">
                <a href="https://youtube.com/channel/UCVGKCZFamCuYXiln9w3Cnxw" target="_blank" rel="noopener noreferrer" className="text-decoration-none">
                  <i className="fab fa-youtube me-2"></i>
                  Tutorial Videos
                </a>
              </li>
              <li className="mb-3">
                <a href="https://github.com/rotohands" target="_blank" rel="noopener noreferrer" className="text-decoration-none">
                  <i className="fab fa-github me-2"></i>
                  GitHub Repository
                </a>
              </li>
              <li>
                <a href="mailto:rotohands@gmail.com" className="text-decoration-none">
                  <i className="fas fa-envelope me-2"></i>
                  Contact me at rotohands@gmail.com
                </a>
              </li>
            </ul>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowHelp(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      <Container className={isMobile ? "px-2 py-2" : "py-4"} style={{ maxWidth: isMobile ? '100%' : '1400px' }}>
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
          <small></small>
        </div>
      )}

      <Footer />
    </Container>
  );
}

export default App;