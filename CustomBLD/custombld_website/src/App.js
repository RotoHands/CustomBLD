import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Tabs, Tab, Spinner, Alert, Badge } from 'react-bootstrap';
import QueryForm from './QueryForm';
import ScrambleResults from './components/ScrambleResults';
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

  const formatCubeType = (type) => {
    switch (type) {
      case '3bld': return '3x3 BLD';
      case '4bld': return '4x4 BLD';
      case '5bld': return '5x5 BLD';
      case '3bld_corners': return '3x3 Corners Only';
      case '3bld_edges': return '3x3 Edges Only';
      case '4bld_centers': return '4x4 Centers Only';
      case '4bld_wings': return '4x4 Wings Only';
      case '5bld_edges_corners': return '5x5 Edges/Corners';
      default: return type;
    }
  };

  // Function to format percentage for mobile (shorter)
  const formatPercentage = (count, total) => {
    return isMobile 
      ? `${((count / total) * 100).toFixed(1)}%`
      : `${((count / total) * 100).toFixed(2)}%`;
  };

  const StatsSection = () => {
    if (statsLoading) {
      return (
        <div className="text-center py-5">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
          <p className="mt-3">Loading scramble statistics...</p>
        </div>
      );
    }

    if (statsError) {
      return (
        <Alert variant="danger">
          <Alert.Heading>Error Loading Statistics</Alert.Heading>
          <p>{statsError}</p>
          <button className="btn btn-danger" onClick={fetchStats}>Try Again</button>
        </Alert>
      );
    }

    if (!stats) {
      return <p>No statistics available.</p>;
    }

    return (
      <div>
        <div className="mb-4">
          <h5>
            <i className="fas fa-database me-2"></i>
            Total Scrambles: <Badge bg="primary">{stats.total_scrambles.toLocaleString()}</Badge>
          </h5>
          <div className="table-responsive">
            <table className="table table-striped table-bordered table-sm">
              <thead>
                <tr>
                  <th>Scramble Type</th>
                  <th className="text-end">Count</th>
                  <th className="text-end">%</th>
                </tr>
              </thead>
              <tbody>
                {stats.scramble_types.map((item) => (
                  <tr key={item.db_type}>
                    <td>{formatCubeType(item.type)}</td>
                    <td className="text-end">{item.count.toLocaleString()}</td>
                    <td className="text-end">{formatPercentage(item.count, stats.total_scrambles)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* 3x3 and 4x4 will be in separate accordions on mobile */}
        <div className="stats-container mb-4">
          {/* 3x3 Buffer Stats */}
          <div className="stats-card mb-4">
            <h5 className="stats-header">
              <i className="fas fa-cube me-2"></i>
              3x3 Buffers
            </h5>
            <div className="row g-3">
              <div className="col-md-6">
                <h6>Edge Buffers</h6>
                <div className="table-responsive">
                  <table className="table table-sm table-striped table-bordered">
                    <thead>
                      <tr>
                        <th>Buffer</th>
                        <th className="text-end">Count</th>
                      </tr>
                    </thead>
                    <tbody>
                      {stats.buffer_stats.edges.slice(0, isMobile ? 5 : undefined).map((item) => (
                        <tr key={`edge-${item.buffer}`}>
                          <td>{item.buffer}</td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                      ))}
                      {isMobile && stats.buffer_stats.edges.length > 5 && (
                        <tr>
                          <td colSpan="2" className="text-center">
                            <button 
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => alert("Switch to landscape view to see all buffers")}
                            >
                              See {stats.buffer_stats.edges.length - 5} more...
                            </button>
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
              <div className="col-md-6">
                <h6>Corner Buffers</h6>
                <div className="table-responsive">
                  <table className="table table-sm table-striped table-bordered">
                    <thead>
                      <tr>
                        <th>Buffer</th>
                        <th className="text-end">Count</th>
                      </tr>
                    </thead>
                    <tbody>
                      {stats.buffer_stats.corners.slice(0, isMobile ? 5 : undefined).map((item) => (
                        <tr key={`corner-${item.buffer}`}>
                          <td>{item.buffer}</td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                      ))}
                      {isMobile && stats.buffer_stats.corners.length > 5 && (
                        <tr>
                          <td colSpan="2" className="text-center">
                            <button 
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => alert("Switch to landscape view to see all buffers")}
                            >
                              See {stats.buffer_stats.corners.length - 5} more...
                            </button>
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
          
          {/* 4x4 Buffer Stats */}
          <div className="stats-card mb-4">
            <h5 className="stats-header">
              <i className="fas fa-cubes me-2"></i>
              4x4 Buffers
            </h5>
            <div className="row g-3">
              <div className="col-md-6">
                <h6>Wing Buffers</h6>
                <div className="table-responsive">
                  <table className="table table-sm table-striped table-bordered">
                    <thead>
                      <tr>
                        <th>Buffer</th>
                        <th className="text-end">Count</th>
                      </tr>
                    </thead>
                    <tbody>
                      {stats.buffer_stats.wings.slice(0, isMobile ? 5 : undefined).map((item) => (
                        <tr key={`wing-${item.buffer}`}>
                          <td>{item.buffer}</td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                      ))}
                      {isMobile && stats.buffer_stats.wings.length > 5 && (
                        <tr>
                          <td colSpan="2" className="text-center">
                            <button 
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => alert("Switch to landscape view to see all buffers")}
                            >
                              See {stats.buffer_stats.wings.length - 5} more...
                            </button>
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
              <div className="col-md-6">
                <h6>X-Center Buffers</h6>
                <div className="table-responsive">
                  <table className="table table-sm table-striped table-bordered">
                    <thead>
                      <tr>
                        <th>Buffer</th>
                        <th className="text-end">Count</th>
                      </tr>
                    </thead>
                    <tbody>
                      {stats.buffer_stats.xcenters.slice(0, isMobile ? 5 : undefined).map((item) => (
                        <tr key={`xcenter-${item.buffer}`}>
                          <td>{item.buffer}</td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                      ))}
                      {isMobile && stats.buffer_stats.xcenters.length > 5 && (
                        <tr>
                          <td colSpan="2" className="text-center">
                            <button 
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => alert("Switch to landscape view to see all buffers")}
                            >
                              See {stats.buffer_stats.xcenters.length - 5} more...
                            </button>
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* 5x5 Buffer Stats */}
        <div className="stats-card">
          <h5 className="stats-header">
            <i className="fas fa-cube me-2"></i>
            5x5 Buffers
          </h5>
          <div className="row">
            <div className="col-md-6">
              <h6>T-Center Buffers</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th>Buffer</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_stats.tcenters.slice(0, isMobile ? 5 : undefined).map((item) => (
                      <tr key={`tcenter-${item.buffer}`}>
                        <td>{item.buffer}</td>
                        <td className="text-end">{item.count.toLocaleString()}</td>
                      </tr>
                    ))}
                    {isMobile && stats.buffer_stats.tcenters.length > 5 && (
                      <tr>
                        <td colSpan="2" className="text-center">
                          <button 
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => alert("Switch to landscape view to see all buffers")}
                          >
                            See {stats.buffer_stats.tcenters.length - 5} more...
                          </button>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
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
              <StatsSection />
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