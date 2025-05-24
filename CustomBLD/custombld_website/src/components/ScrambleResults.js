import React, { useState, useEffect } from 'react';
import { Card, Button, Collapse, Alert, Pagination, Dropdown } from 'react-bootstrap';
import { CSVLink } from 'react-csv';

const ScrambleResults = ({ results }) => {
  const [showSolutions, setShowSolutions] = useState({});
  const [copySuccess, setCopySuccess] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [scramblesPerPage, setScramblesPerPage] = useState(10);
  
  // Add debug logging
  useEffect(() => {
    console.log('Results received:', results);
  }, [results]);
  
  if (!results || results.length === 0) {
    console.log('No results to display');
    return null;
  }

  // Calculate total pages
  const totalPages = Math.ceil(results.length / scramblesPerPage);
  
  // Get current page items
  const indexOfLastScramble = currentPage * scramblesPerPage;
  const indexOfFirstScramble = indexOfLastScramble - scramblesPerPage;
  const currentScrambles = results.slice(indexOfFirstScramble, indexOfLastScramble);

  console.log('Current page items:', currentScrambles);

  // Modify the CSV data preparation function
  const prepareCsvData = () => {
    // Create a comprehensive header with all possible fields
    const headers = [
      'ID', 'Scramble Type', 'Scramble', 'Solution',
      // Edge data
      'Edge Buffer', 'Edges', 'Edge Length', 'Edge Cycle Breaks', 
      'Edges Flipped', 'Edges Solved', 'Flips', 'First Edges', 'Edge Parity',
      // Corner data
      'Corner Buffer', 'Corners', 'Corner Length', 'Corner Cycle Breaks', 
      'CW Twists', 'CCW Twists', 'Corners Twisted', 'Corners Solved', 
      'Corner Parity', 'First Corners',
      // Wing data
      'Wing Buffer', 'Wings', 'Wing Length', 'Wing Cycle Breaks', 
      'Wings Solved', 'Wing Parity', 'First Wings',
      // X-Center data
      'X-Center Buffer', 'X-Centers', 'X-Center Length', 'X-Center Cycle Breaks', 
      'X-Centers Solved', 'X-Center Parity', 'First X-Centers',
      // T-Center data
      'T-Center Buffer', 'T-Centers', 'T-Center Length', 'T-Center Cycle Breaks', 
      'T-Centers Solved', 'T-Center Parity', 'First T-Centers'
    ];
    
    const csvData = [headers];
    
    results.forEach(result => {
      const row = [
        result.id,
        result.scramble_type,
        result.scramble,
        result.solution,
        // Edge data
        result.edge_buffer,
        result.edges,
        result.edge_length,
        result.edges_cycle_breaks,
        result.edges_flipped,
        result.edges_solved,
        result.flips,
        result.first_edges,
        result.edge_parity,
        // Corner data
        result.corner_buffer,
        result.corners,
        result.corner_length,
        result.corners_cycle_breaks,
        result.twist_clockwise,
        result.twist_counterclockwise,
        result.corners_twisted,
        result.corners_solved,
        result.corner_parity,
        result.first_corners,
        // Wing data
        result.wing_buffer,
        result.wings,
        result.wings_length,
        result.wings_cycle_breaks,
        result.wings_solved,
        result.wing_parity,
        result.first_wings,
        // X-Center data
        result.xcenter_buffer,
        result.xcenters,
        result.xcenter_length,
        result.xcenters_cycle_breaks,
        result.xcenters_solved,
        result.xcenter_parity,
        result.first_xcenters,
        // T-Center data
        result.tcenter_buffer,
        result.tcenters,
        result.tcenter_length,
        result.tcenters_cycle_breaks,
        result.tcenters_solved,
        result.tcenter_parity,
        result.first_tcenters
      ];
      
      csvData.push(row);
    });
    
    return csvData;
  };

  // Function to toggle showing solution for a specific scramble
  const toggleSolution = (id) => {
    setShowSolutions(prev => ({
      ...prev,
      [id]: !prev[id]
    }));
  };

  // Enhanced function to check if a value is exactly zero (handles various formats)
  const isExplicitlyZero = (value) => {
    // Convert to string and trim to handle whitespace
    if (value === null || value === undefined) return false;
    
    // Handle numeric values directly
    if (typeof value === 'number') return value === 0;
    
    // Handle string values by converting to number
    const stringValue = String(value).trim();
    return stringValue === '0' || stringValue === '' || Number(stringValue) === 0;
  };

  // Or use a simpler approach with a function that directly checks if a value should be shown
  const shouldShowStat = (value) => {
    if (!value && value !== 0) return false; // Handles null, undefined, empty strings
    if (value === 0 || value === '0') return false; // Handles numeric and string zeros
    return true; // Show all other values
  };

  // Function to format parity value
  const formatParity = (value) => {
    if (value === true || value === 'true') return 'Yes';
    if (value === false || value === 'false') return 'No';
    return value;
  };

  // Update the renderSolution function to show solution as the rotation
  const renderSolution = (result) => {
    return (
      <Collapse in={showSolutions[result.id]}>
        <div className="mt-2">
          <div className="p-3 bg-light rounded solution-container">
            {/* Rotation information at the beginning */}
            {result.solution && (
              <div className="mb-1">
                <strong>Rotations:</strong> <code>{result.solution}</code>
              </div>
            )}
            
            {/* Edges section */}
            {result.edges && (
              <>
                <div className="mb-1">
                  <strong>Edges:</strong> <code>{result.edges}</code>
                  {shouldShowStat(result.edges_flipped) && (
                    <span className="ms-2">Flips: <code>{result.flips}</code></span>
                  )}
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.edge_length) && <div>Length: {result.edge_length}</div>}
                  {shouldShowStat(result.edges_cycle_breaks) && <div>Cycle breaks: {result.edges_cycle_breaks}</div>}
                  {shouldShowStat(result.edges_solved) && <div>Solved edges: {result.edges_solved}</div>}
                  {shouldShowStat(result.edge_parity) && <div>Parity: {formatParity(result.edge_parity)}</div>}
                </div>
              </>
            )}
            
            {/* Corners section */}
            {result.corners && (
              <>
                <div className="mb-1">
                  <strong>Corners:</strong> <code>{result.corners}</code>
                  
                  {/* Show twist information on the same line */}
                  {(result.twist_clockwise && !isExplicitlyZero(result.twist_clockwise)) || 
                   (result.twist_counterclockwise && !isExplicitlyZero(result.twist_counterclockwise)) ? (
                    <span className="ms-2">
                      {result.twist_clockwise && !isExplicitlyZero(result.twist_clockwise) && (
                        <span>Twist Cw: <code>{result.twist_clockwise}</code></span>
                      )}
                      {result.twist_clockwise && !isExplicitlyZero(result.twist_clockwise) && 
                       result.twist_counterclockwise && !isExplicitlyZero(result.twist_counterclockwise) && (
                        <span> | </span>
                      )}
                      {result.twist_counterclockwise && !isExplicitlyZero(result.twist_counterclockwise) && (
                        <span>Twist Ccw: <code>{result.twist_counterclockwise}</code></span>
                      )}
                    </span>
                  ) : null}
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.corner_length) && <div>Length: {result.corner_length}</div>}
                  {shouldShowStat(result.corners_cycle_breaks) && <div>Cycle breaks: {result.corners_cycle_breaks}</div>}
                  {shouldShowStat(result.corners_solved) && <div>Solved corners: {result.corners_solved}</div>}
                  {shouldShowStat(result.corner_parity) && <div>Parity: {formatParity(result.corner_parity)}</div>}
                </div>
              </>
            )}
            
            {/* Wings section */}
            {result.wings && (
              <>
                <div className="mb-1">
                  <strong>Wings:</strong> <code>{result.wings}</code>
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.wings_length) && <div>Length: {result.wings_length}</div>}
                  {shouldShowStat(result.wings_cycle_breaks) && <div>Cycle breaks: {result.wings_cycle_breaks}</div>}
                  {shouldShowStat(result.wings_solved) && <div>Solved wings: {result.wings_solved}</div>}
                  {shouldShowStat(result.wing_parity) && <div>Parity: {formatParity(result.wing_parity)}</div>}
                </div>
              </>
            )}
            
            {/* X-Centers section */}
            {result.xcenters && (
              <>
                <div className="mb-1">
                  <strong>X-Centers:</strong> <code>{result.xcenters}</code>
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.xcenter_length) && <div>Length: {result.xcenter_length}</div>}
                  {shouldShowStat(result.xcenters_cycle_breaks) && <div>Cycle breaks: {result.xcenters_cycle_breaks}</div>}
                  {shouldShowStat(result.xcenters_solved) && <div>Solved X-centers: {result.xcenters_solved}</div>}
                  {shouldShowStat(result.xcenter_parity) && <div>Parity: {formatParity(result.xcenter_parity)}</div>}
                </div>
              </>
            )}
            
            {/* T-Centers section */}
            {result.tcenters && (
              <>
                <div className="mb-1">
                  <strong>T-Centers:</strong> <code>{result.tcenters}</code>
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.tcenter_length) && <div>Length: {result.tcenter_length}</div>}
                  {shouldShowStat(result.tcenters_cycle_breaks) && <div>Cycle breaks: {result.tcenters_cycle_breaks}</div>}
                  {shouldShowStat(result.tcenters_solved) && <div>Solved T-centers: {result.tcenters_solved}</div>}
                  {shouldShowStat(result.tcenter_parity) && <div>Parity: {formatParity(result.tcenter_parity)}</div>}
                </div>
              </>
            )}
          </div>
        </div>
      </Collapse>
    );
  };

  // Function to copy all scrambles to clipboard (with one newline between them)
  const copyScramblesToClipboard = () => {
    const scrambles = results.map(result => result.scramble).join('\n');
    
    navigator.clipboard.writeText(scrambles)
      .then(() => {
        setCopySuccess(true);
        setTimeout(() => setCopySuccess(false), 3000);
      })
      .catch(err => {
        console.error('Failed to copy scrambles:', err);
      });
  };

  // Handle pagination
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // Render pagination controls
  const renderPagination = () => {
    const pages = [];
    
    for (let number = 1; number <= totalPages; number++) {
      pages.push(
        <Pagination.Item 
          key={number}
          active={number === currentPage}
          onClick={() => handlePageChange(number)}
        >
          {number}
        </Pagination.Item>
      );
    }
    
    return (
      <Pagination className="justify-content-center mt-3 mb-0">
        <Pagination.Prev 
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        />
        {pages}
        <Pagination.Next 
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
        />
      </Pagination>
    );
  };

  // Function to expand/collapse all solutions
  const toggleAllSolutions = (expand) => {
    const newState = {};
    currentScrambles.forEach(result => {
      newState[result.id] = expand;
    });
    setShowSolutions(newState);
  };

  // Function to handle page size change
  const handlePageSizeChange = (size) => {
    setScramblesPerPage(size);
    setCurrentPage(1); // Reset to first page when changing page size
  };

  return (
    <div className="mt-4">
      <Card>
        <Card.Header className="d-flex justify-content-between align-items-center">
          <h3 className="mb-0">Scrambles ({results.length})</h3>
          <div className="d-flex align-items-center">
            <Dropdown className="me-2">
              <Dropdown.Toggle variant="outline-secondary" id="page-size-dropdown">
                {scramblesPerPage === results.length ? 'All' : `${scramblesPerPage} per page`}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item onClick={() => handlePageSizeChange(10)}>10 per page</Dropdown.Item>
                <Dropdown.Item onClick={() => handlePageSizeChange(20)}>20 per page</Dropdown.Item>
                <Dropdown.Item onClick={() => handlePageSizeChange(50)}>50 per page</Dropdown.Item>
                <Dropdown.Item onClick={() => handlePageSizeChange(100)}>100 per page</Dropdown.Item>
                <Dropdown.Item onClick={() => handlePageSizeChange(results.length)}>Show all</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
            <Button
              variant="outline-primary"
              className="me-2"
              onClick={() => toggleAllSolutions(true)}
            >
              Expand All
            </Button>
            <Button
              variant="outline-primary"
              className="me-2"
              onClick={() => toggleAllSolutions(false)}
            >
              Collapse All
            </Button>
            <Button
              variant="outline-primary"
              className="me-2"
              onClick={copyScramblesToClipboard}
            >
              Copy Scrambles
            </Button>
            <CSVLink
              data={prepareCsvData()}
              filename="scrambles.csv"
              className="btn btn-outline-success"
            >
              Download CSV
            </CSVLink>
          </div>
        </Card.Header>
        <Card.Body>
          {copySuccess && (
            <Alert variant="success" onClose={() => setCopySuccess(false)} dismissible>
              Scrambles copied to clipboard!
            </Alert>
          )}
          
          {currentScrambles.map((result, index) => (
            <div key={result.id || index} className="mb-4 scramble-item">
              <div className="d-flex justify-content-between align-items-start">
                <div className="d-flex align-items-center">
                  <span className="me-3 scramble-number">{indexOfFirstScramble + index + 1})</span>
                  <div className="scramble-text">
                    {result.scramble}
                  </div>
                </div>
                <Button
                  variant="outline-secondary"
                  size="sm"
                  onClick={() => toggleSolution(result.id || index)}
                >
                  {showSolutions[result.id || index] ? 'Hide Solution' : 'Show Solution'}
                </Button>
              </div>
              
              {renderSolution(result)}
            </div>
          ))}
          
          {totalPages > 1 && renderPagination()}
        </Card.Body>
      </Card>
      
      {/* Add some custom styling for the solution section */}
      <style jsx>{`
        .solution-container {
          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .solution-container code {
          font-size: 1.1em;
          color: #0056b3;
        }
        
        .solution-container .small {
          color: #6c757d;
        }
        
        .stats-section {
          margin-top: 0.25rem;
          padding-top: 0.25rem;
          border-top: 1px dotted #dee2e6;
        }
        
        .rotation-info {
          padding: 0.5rem;
          background-color: #f8f9fa;
          border-left: 3px solid #007bff;
          margin-bottom: 1rem;
        }
        
        .rotation-info code {
          font-weight: bold;
          font-size: 1.2em;
        }

        .scramble-text {
          font-size: 1.4rem;
          font-family: 'Rubik', monospace;
          white-space: pre-wrap;
          word-break: break-all;
          letter-spacing: 0.5px;
        }

        .scramble-number {
          font-size: 1.6rem;
          font-weight: 600;
          color: #495057;
          font-family: 'Rubik', sans-serif;
        }

        .scramble-item {
          padding-bottom: 1.2rem;
          border-bottom: 1px solid #dee2e6;
        }

        .scramble-item:last-child {
          border-bottom: none;
        }
      `}</style>
    </div>
  );
};

export default ScrambleResults;