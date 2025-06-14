import React, { useState, useEffect } from 'react';
import { Card, Button, Collapse, Alert, Pagination, Dropdown } from 'react-bootstrap';
import { CSVLink } from 'react-csv';

const ScrambleResults = ({ results, isMobile }) => {
  const [showSolutions, setShowSolutions] = useState({});
  const [copySuccess, setCopySuccess] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [scramblesPerPage, setScramblesPerPage] = useState(15);
  
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
        <div className="solution-card mt-3">
          <div className="p-3 solution-container">
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
    const maxVisiblePages = isMobile ? 5 : 10;
    let startPage = 1;
    let endPage = totalPages;

    if (totalPages > maxVisiblePages) {
      // Calculate start and end page numbers
      const halfVisible = Math.floor(maxVisiblePages / 2);
      startPage = Math.max(currentPage - halfVisible, 1);
      endPage = startPage + maxVisiblePages - 1;

      if (endPage > totalPages) {
        endPage = totalPages;
        startPage = Math.max(endPage - maxVisiblePages + 1, 1);
      }
    }
    
    // Add first page and ellipsis if needed
    if (startPage > 1) {
      pages.push(
        <Pagination.Item 
          key={1}
          active={1 === currentPage}
          onClick={() => handlePageChange(1)}
        >
          1
        </Pagination.Item>
      );
      if (startPage > 2) {
        pages.push(<Pagination.Ellipsis key="ellipsis1" />);
      }
    }

    // Add page numbers
    for (let number = startPage; number <= endPage; number++) {
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

    // Add last page and ellipsis if needed
    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        pages.push(<Pagination.Ellipsis key="ellipsis2" />);
      }
      pages.push(
        <Pagination.Item 
          key={totalPages}
          active={totalPages === currentPage}
          onClick={() => handlePageChange(totalPages)}
        >
          {totalPages}
        </Pagination.Item>
      );
    }
    
    return (
      <div className="pagination-container">
        <Pagination className="justify-content-center mt-3 mb-0 flex-wrap">
          <Pagination.First
            onClick={() => handlePageChange(1)}
            disabled={currentPage === 1}
          />
          <Pagination.Prev 
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          />
          {pages}
          <Pagination.Next 
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
          />
          <Pagination.Last
            onClick={() => handlePageChange(totalPages)}
            disabled={currentPage === totalPages}
          />
        </Pagination>
      </div>
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
      {/* Action buttons - now in a fixed position on mobile */}
      

      {/* Results */}
      <div className="mt-3 scramble-list-container">
        <div className="scramble-header p-3 pb-0 d-flex flex-column flex-md-row justify-content-between align-items-md-center">
          <h4 className="mb-3 mb-md-0 scramble-header-label" style={{fontSize: '1.6rem', fontWeight: 500}}>Scrambles ({results.length})</h4>
          <div className="d-flex flex-row flex-wrap gap-2 align-items-center justify-content-md-end">
            <Button 
              variant="outline-primary" 
              size={isMobile ? "sm" : undefined}
              className={isMobile ? "btn-xs" : ""}
              onClick={() => toggleAllSolutions(true)}
            >
              Expand All
            </Button>
            <Button 
              variant="outline-primary" 
              size={isMobile ? "sm" : undefined}
              className={isMobile ? "btn-xs" : ""}
              onClick={() => toggleAllSolutions(false)}
            >
              Collapse All
            </Button>
            <Button 
              variant="outline-primary" 
              size={isMobile ? "sm" : undefined}
              className={isMobile ? "btn-xs" : ""}
              onClick={copyScramblesToClipboard}
            >
              Copy Scrambles
            </Button>
            <CSVLink 
              data={prepareCsvData()} 
              filename="scrambles.csv"
              className={`btn btn-outline-success ${isMobile ? 'btn-sm btn-xs' : ''}`}
              style={{ textDecoration: 'none' }}
            >
              Download CSV
            </CSVLink>
            <div className="d-flex align-items-center ms-1">
              <span className="me-1 small">Show:</span>
              <Dropdown>
                <Dropdown.Toggle 
                  variant="outline-secondary" 
                  size={isMobile ? "sm" : undefined}
                  className={isMobile ? "btn-xs px-2" : "px-2"}
                >
                  {scramblesPerPage}
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  {[5, 15, 50, 100, 1000].map(size => (
                    <Dropdown.Item 
                      key={size} 
                      onClick={() => handlePageSizeChange(size)}
                    >
                      {size} per page
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
            </div>
          </div>
        </div>
        {currentScrambles.map((result, index) => (
          <div key={result.id} className="scramble-row-card">
            <div className="p-3 d-flex flex-column flex-md-row justify-content-between align-items-start">
              {/* Show solution button at the top on mobile */}
              {isMobile && (
                <Button
                  variant="outline-primary"
                  size="sm"
                  className="mb-2 w-100 btn-xs"
                  onClick={() => toggleSolution(result.id)}
                >
                  {showSolutions[result.id] ? 'Hide Solution' : 'Show Solution'}
                </Button>
              )}
              <div className="flex-grow-1">
                <div className="d-flex align-items-center mb-2">
                  <span className="scramble-number-lg me-2">{indexOfFirstScramble + index + 1})</span>
                  <div className="scramble-text">{result.scramble}</div>
                </div>
              </div>
              {/* Show solution button on the right for desktop */}
              {!isMobile && (
                <Button
                  variant="outline-primary"
                  onClick={() => toggleSolution(result.id)}
                  className="ms-4"
                >
                  {showSolutions[result.id] ? 'Hide Solution' : 'Show Solution'}
                </Button>
              )}
            </div>
            {/* Solution section */}
            {renderSolution(result)}
            {index !== currentScrambles.length - 1 && (
              <div className="scramble-divider"></div>
            )}
          </div>
        ))}
      </div>

      {/* Pagination */}
      <div className="d-flex justify-content-center mt-3">
        {renderPagination()}
      </div>

      {/* Copy success alert */}
      {copySuccess && (
        <Alert 
          variant="success" 
          className="position-fixed bottom-0 end-0 m-3" 
          style={{ zIndex: 1000 }}
          onClose={() => setCopySuccess(false)} 
          dismissible
        >
          Scrambles copied to clipboard!
        </Alert>
      )}

      {/* Add custom styling */}
      <style jsx>{`
        .scramble-text {
          font-size: ${isMobile ? '1.1rem' : '1.25rem'};
          font-family: 'Rubik', monospace, 'Rubik', 'Arial', sans-serif;
          white-space: pre-wrap;
          word-break: keep-all;
          word-wrap: break-word;
          letter-spacing: 0.5px;
          color: #000000;
          text-align: justify;
          text-justify: inter-word;
        }
        
        .scramble-header-label {
          color: #000;
        }
        
        .scramble-number-lg {
          font-size: ${isMobile ? '1.2rem' : '1.3rem'};
          font-weight: bold;
          color: #000000;
          font-family: 'Rubik', monospace;
        }
        
        .smaller-text {
          font-size: 0.8rem;
          padding: 0.25rem 0.5rem;
        }
        
        .scramble-row-card {
          background: #fff;
          border-radius: 0;
          box-shadow: none;
          margin: 0;
          border: none;
        }
        
        .scramble-number {
          font-size: 1.2rem;
          font-weight: bold;
          color: #222;
          font-family: 'Rubik', monospace;
        }
        
        .solution-card {
          background: #f8f9fa;
          border-radius: 0.5rem;
          box-shadow: 0 2px 8px rgba(0,0,0,0.06);
          border: 1px solid #e3e6ea;
          margin-top: 0.5rem;
        }
        
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

        .show-dropdown {
          min-width: 45px;
          font-size: 0.8rem;
        }

        .btn-xs {
          font-size: 0.92rem !important;
          padding: 0.32rem 0.7rem !important;
          line-height: 1.3 !important;
        }

        .scramble-list-container {
          border: 1.5px solid #e3e6ea;
          border-radius: 12px;
          background: #f8f9fa;
          overflow: hidden;
        }
        .scramble-divider {
          border-bottom: 1px solid #e3e6ea;
          margin: 0 18px;
        }

        .pagination-container {
          overflow-x: auto;
          padding: 0.5rem 0;
          margin: -0.5rem 0;
        }

        .pagination {
          margin-bottom: 0;
          white-space: nowrap;
          gap: 0.25rem;
        }

        .page-link {
          min-width: 40px;
          text-align: center;
          border-radius: 4px !important;
        }

        @media (max-width: 768px) {
          .page-link {
            min-width: 35px;
            padding: 0.25rem 0.5rem;
            font-size: 0.9rem;
          }
          
          .pagination {
            gap: 0.15rem;
          }
        }
      `}</style>
    </div>
  );
};

export default ScrambleResults;