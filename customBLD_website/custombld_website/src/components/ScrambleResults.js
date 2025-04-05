import React, { useState } from 'react';
import { Card, Button, Collapse, Alert, Pagination } from 'react-bootstrap';
import { CSVLink } from 'react-csv';

const ScrambleResults = ({ results }) => {
  const [showSolutions, setShowSolutions] = useState({});
  const [copySuccess, setCopySuccess] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const scramblesPerPage = 10;
  
  if (!results || results.length === 0) {
    return null;
  }

  // Calculate total pages
  const totalPages = Math.ceil(results.length / scramblesPerPage);
  
  // Get current page items
  const indexOfLastScramble = currentPage * scramblesPerPage;
  const indexOfFirstScramble = indexOfLastScramble - scramblesPerPage;
  const currentScrambles = results.slice(indexOfFirstScramble, indexOfLastScramble);

  // Prepare data for CSV download - include all columns
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

  // Function to render solution with all stats as text
  const renderSolution = (result) => {
    // Add this inside your renderSolution function to debug the values
    console.log('Edges solved value:', {
      value: result.edges_solved,
      type: typeof result.edges_solved,
      isZero: result.edges_solved === 0,
      isStringZero: result.edges_solved === '0',
      parsed: Number(result.edges_solved),
      shouldShow: shouldShowStat(result.edges_solved)
    });
    
    return (
      <Collapse in={showSolutions[result.id]}>
        <div className="mt-2">
          <div className="p-3 bg-light rounded solution-container">
            {/* Edges section */}
            {result.edges && (
              <>
                <div className="mb-1">
                  <strong>Edges:</strong> <code>{result.edges}</code>
                  {result.edges_flipped && !isExplicitlyZero(result.edges_flipped) && (
                    <span className="ms-2">Flips: <code>{result.flips}</code></span>
                  )}
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.edge_length) && <div>Length: {result.edge_length}</div>}
                  {shouldShowStat(result.edges_cycle_breaks) && <div>Cycle breaks: {result.edges_cycle_breaks}</div>}
                  {shouldShowStat(result.edges_solved) && <div>Solved edges: {result.edges_solved}</div>}
                  {shouldShowStat(result.edge_parity) && <div>Parity: {result.edge_parity}</div>}
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
                        <span>Twist CW: <code>{result.twist_clockwise}</code></span>
                      )}
                      {result.twist_clockwise && !isExplicitlyZero(result.twist_clockwise) && 
                       result.twist_counterclockwise && !isExplicitlyZero(result.twist_counterclockwise) && (
                        <span> | </span>
                      )}
                      {result.twist_counterclockwise && !isExplicitlyZero(result.twist_counterclockwise) && (
                        <span>Twist CCW: <code>{result.twist_counterclockwise}</code></span>
                      )}
                    </span>
                  ) : null}
                </div>
                
                {/* Show numeric stats at the end of the solution block */}
                <div className="mb-2 ms-3 small stats-section">
                  {shouldShowStat(result.corner_length) && <div>Length: {result.corner_length}</div>}
                  {shouldShowStat(result.corners_cycle_breaks) && <div>Cycle breaks: {result.corners_cycle_breaks}</div>}
                  {shouldShowStat(result.corners_solved) && <div>Solved corners: {result.corners_solved}</div>}
                  {shouldShowStat(result.corner_parity) && <div>Parity: {result.corner_parity}</div>}
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
                  {result.wings_length && <div>Length: {result.wings_length}</div>}
                  {result.wings_cycle_breaks && !isExplicitlyZero(result.wings_cycle_breaks) && <div>Cycle breaks: {result.wings_cycle_breaks}</div>}
                  {result.wings_solved && !isExplicitlyZero(result.wings_solved) && <div>Solved wings: {result.wings_solved}</div>}
                  {result.wing_parity && <div>Parity: {result.wing_parity}</div>}
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
                  {result.xcenter_length && <div>Length: {result.xcenter_length}</div>}
                  {result.xcenters_cycle_breaks && !isExplicitlyZero(result.xcenters_cycle_breaks) && <div>Cycle breaks: {result.xcenters_cycle_breaks}</div>}
                  {result.xcenters_solved && !isExplicitlyZero(result.xcenters_solved) && <div>Solved X-centers: {result.xcenters_solved}</div>}
                  {result.xcenter_parity && <div>Parity: {result.xcenter_parity}</div>}
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
                  {result.tcenter_length && <div>Length: {result.tcenter_length}</div>}
                  {result.tcenters_cycle_breaks && !isExplicitlyZero(result.tcenters_cycle_breaks) && <div>Cycle breaks: {result.tcenters_cycle_breaks}</div>}
                  {result.tcenters_solved && !isExplicitlyZero(result.tcenters_solved) && <div>Solved T-centers: {result.tcenters_solved}</div>}
                  {result.tcenter_parity && <div>Parity: {result.tcenter_parity}</div>}
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

  return (
    <Card className="my-4 shadow-sm">
      <Card.Header className="bg-primary text-white d-flex justify-content-between align-items-center">
        <h3 className="mb-0">Generated Scrambles ({results.length})</h3>
        <div>
          <Button 
            variant="light" 
            className="me-2"
            onClick={copyScramblesToClipboard}
          >
            Copy Scrambles
          </Button>
          <CSVLink 
            data={prepareCsvData()} 
            filename={"bld-scrambles.csv"}
            className="btn btn-light"
          >
            Download CSV
          </CSVLink>
        </div>
      </Card.Header>
      <Card.Body>
        {copySuccess && (
          <Alert variant="success" className="mb-3">
            Scrambles copied to clipboard!
          </Alert>
        )}
        
        {currentScrambles.map((result, index) => (
          <Card key={result.id || (indexOfFirstScramble + index)} className="mb-3">
            <Card.Header className="d-flex justify-content-between align-items-center">
              <span>Scramble #{indexOfFirstScramble + index + 1}</span>
              <Button 
                variant="outline-primary" 
                size="sm" 
                onClick={() => toggleSolution(result.id)}
              >
                {showSolutions[result.id] ? 'Hide Solution' : 'Show Solution'}
              </Button>
            </Card.Header>
            <Card.Body>
              <div className="mb-3 p-2 bg-light rounded">
                <code className="scramble-text">{result.scramble}</code>
              </div>
              
              {renderSolution(result)}
            </Card.Body>
          </Card>
        ))}
        
        {totalPages > 1 && renderPagination()}
      </Card.Body>
      
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
      `}</style>
    </Card>
  );
};

export default ScrambleResults;