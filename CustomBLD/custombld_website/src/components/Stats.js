import React, { useState } from 'react';
import { Badge, Spinner, Alert, Button, Form } from 'react-bootstrap';

const Stats = ({ stats, statsLoading, statsError, fetchStats, isMobile }) => {
  const [selectedBuffers, setSelectedBuffers] = useState({});
  const colors = {
    corners: '#B5EAD7',
    edges: '#FF9AA2',
    wings: '#FFDAC1',
    tcenters: '#9AA2FF',
    xcenters: '#B3E2FF'
  };

  // Function to handle color changes
  const handleColorChange = (pieceType, color) => {
    colors[pieceType] = color;
  };

  // Function to format relative time
  const formatRelativeTime = (seconds) => {
    if (seconds < 60) return `${Math.round(seconds)} seconds ago`;
    if (seconds < 3600) return `${Math.round(seconds / 60)} minutes ago`;
    if (seconds < 86400) return `${Math.round(seconds / 3600)} hours ago`;
    return `${Math.round(seconds / 86400)} days ago`;
  };

  // Function to convert letter buffer to position
  const letterToPosition = (letter, pieceType) => {
    // Buffer mapping for converting letter notation to piece positions
    const reverseBufferMap = {
      edges: {
        "C": "UF",
        "I": "FU",
        "U": "DF",
        "B": "UR"
      },
      corners: {
        "C": "UFR",
        "A": "UBL",
        "D": "UFL",
        "P": "RDF"
      },
      wings: {
        "C": "UFr",
        "U": "DFr",
        "I": "FUr"
      },
      xcenters: {
        "C": "Ufr",
        "A": "Ubl",
        "B": "Ubr",
        "D": "Ufl"
      },
      tcenters: {
        "C": "Uf",
        "A": "Ub",
        "B": "Ur",
        "D": "Ul"
      }
    };

    // Check if it's already a position name (e.g., "UF" instead of "C")
    if (letter.length > 1 && !letter.match(/^[A-Z]$/i)) {
      return letter;
    }
    
    // Get the correct mapping for the piece type
    const mapping = reverseBufferMap[pieceType];
    if (!mapping) return letter;
    
    return mapping[letter] || letter;
  };

  // Function to format cube type display
  const formatCubeType = (type) => {
    switch (type) {
      case '3bld': return '3x3 BLD';
      case '3bld_corners': return '3x3 Corners Only';
      case '3bld_edges': return '3x3 Edges Only';
      case '4bld': return '4x4 BLD';
      case '4bld_wings': return '4x4 Wings Only';
      case '4bld_centers': return '4x4 Centers Only';
      case '5bld': return '5x5 BLD';
      case '5bld_edges_corners': return '5x5 Edges & Corners';
      default: return type;
    }
  };

  // Function to get sort order for scramble types
  const getScrambleTypeOrder = (type) => {
    const order = {
      '3bld': 1,
      '3bld_corners': 2,
      '3bld_edges': 3,
      '4bld': 4,
      '4bld_wings': 5,
      '4bld_centers': 6,
      '5bld': 7,
      '5bld_edges_corners': 8
    };
    return order[type] || 999; // Default to end for unknown types
  };

  // Function to format percentage for mobile (shorter)
  const formatPercentage = (count, total) => {
    return isMobile 
      ? `${((count / total) * 100).toFixed(1)}%`
      : `${((count / total) * 100).toFixed(2)}%`;
  };

  // Function to handle buffer click
  const handleBufferClick = (pieceType, buffer) => {
    setSelectedBuffers(prev => {
      const newState = { ...prev };
      if (newState[pieceType] === buffer) {
        delete newState[pieceType];
      } else {
        newState[pieceType] = buffer;
      }
      return newState;
    });
  };

  // Function to check if a combination matches selected buffers
  const matchesSelectedBuffers = (buffers, pieceTypes) => {
    // Only check filters that are relevant to this scramble type
    return Object.entries(selectedBuffers).every(([type, buffer]) => {
      // Skip filters for piece types that don't exist in this scramble type
      if (!pieceTypes.includes(type)) return true;
      
      const index = pieceTypes.indexOf(type);
      return index !== -1 && buffers[index] === buffer;
    });
  };

  // Function to get piece types for a scramble type
  const getPieceTypesForScrambleType = (type) => {
    switch (type) {
      case '3bld':
        return ['corners', 'edges'];
      case '3bld_corners':
        return ['corners'];
      case '3bld_edges':
        return ['edges'];
      case '4bld':
        return ['corners', 'wings', 'xcenters'];
      case '4bld_wings':
        return ['wings'];
      case '4bld_centers':
        return ['xcenters'];
      case '5bld':
        return ['corners', 'edges', 'wings', 'xcenters', 'tcenters'];
      case '5bld_edges_corners':
        return ['corners', 'edges'];
      default:
        return [];
    }
  };

  // Function to render active filters for a specific scramble type
  const renderActiveFilters = (scrambleType) => {
    const pieceTypes = getPieceTypesForScrambleType(scrambleType);
    const relevantFilters = Object.entries(selectedBuffers)
      .filter(([type]) => pieceTypes.includes(type));

    if (relevantFilters.length === 0) return null;

    const activeFilters = relevantFilters.map(([type, buffer]) => (
      <span 
        key={type} 
        className="badge me-2" 
        style={{ 
          fontSize: '0.9rem', 
          fontWeight: 'normal', 
          backgroundColor: colors[type], 
          color: '#333',
          cursor: 'pointer'
        }}
        onClick={() => handleBufferClick(type, buffer)}
        title="Click to remove filter"
      >
        <strong>{type.charAt(0).toUpperCase() + type.slice(1)}:</strong> {letterToPosition(buffer, type)}
      </span>
    ));

    return (
      <div className="d-inline-block ms-2">
        <small className="text-muted me-2">Active filters:</small>
        {activeFilters}
      </div>
    );
  };

  // Function to render a clickable buffer badge
  const renderBufferBadge = (pieceType, buffer, isSelected) => (
    <span 
      className="badge me-2" 
      style={{ 
        fontSize: '1rem', 
        fontWeight: 'normal', 
        backgroundColor: colors[pieceType], 
        color: '#333',
        cursor: 'pointer',
        opacity: 1,
        transition: 'opacity 0.2s'
      }}
      onClick={() => handleBufferClick(pieceType, buffer)}
      title="Click to filter"
    >
      <strong>{pieceType.charAt(0).toUpperCase() + pieceType.slice(1)}:</strong> {letterToPosition(buffer, pieceType)}
    </span>
  );

  if (statsLoading) {
    return (
      <div className="text-center py-5" style={{ fontFamily: 'Rubik, sans-serif' }}>
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
        <p className="mt-3">Loading scramble statistics...</p>
      </div>
    );
  }

  if (statsError) {
    return (
      <Alert variant="danger" style={{ fontFamily: 'Rubik, sans-serif' }}>
        <Alert.Heading>Error Loading Statistics</Alert.Heading>
        <p>{statsError}</p>
        <button className="btn btn-danger" onClick={fetchStats}>Try Again</button>
      </Alert>
    );
  }

  if (!stats) {
    return <p style={{ fontFamily: 'Rubik, sans-serif' }}>No statistics available.</p>;
  }

  // Log available buffer combinations keys for debugging
  if (stats.buffer_combinations) {
    console.log("Available buffer combination keys:", Object.keys(stats.buffer_combinations));
  }

  return (
    <div style={{ fontFamily: 'Rubik, sans-serif' }}>
      <div className="mb-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5>
            <i className="fas fa-database me-2"></i>
            Total Scrambles: <Badge bg="primary">{stats.total_scrambles.toLocaleString()}</Badge>
          </h5>
        </div>
        
        {stats.timestamp && (
          <div className="text-muted small mb-3">
            Last updated: {new Date(stats.timestamp * 1000).toLocaleString()}
          </div>
        )}
        
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
              {stats.scramble_types
                .sort((a, b) => getScrambleTypeOrder(a.type) - getScrambleTypeOrder(b.type))
                .map((item) => (
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

      {/* Buffer Combinations Across Puzzle Types */}
      {stats.buffer_combinations && (
        <div className="stats-card mt-4" style={{ 
          borderRadius: '12px',
          boxShadow: '0 4px 12px rgba(0,0,0,0.08)'
        }}>
          <h5 className="stats-header" style={{ 
            fontWeight: '600', 
            fontSize: '1.25rem',
            color: '#333',
            borderBottom: '2px solid #e0e0e0',
            paddingBottom: '12px'
          }}>
            <i className="fas fa-layer-group me-2"></i>
            Buffers Combinations
            <small className="text-muted ms-2" style={{ fontSize: '0.9rem', fontWeight: 'normal' }}>
              (Press the buffers to apply filters)
            </small>
          </h5>
          
          {/* 3x3 Complete */}
          {stats.buffer_combinations['3bld'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>3x3 BLD</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('3bld')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld']
                      .filter(item => {
                        const [edgeBuffer, cornerBuffer] = item.combo.split('-');
                        return matchesSelectedBuffers([cornerBuffer, edgeBuffer], ['corners', 'edges']);
                      })
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => {
                        const [edgeBuffer, cornerBuffer] = item.combo.split('-');
                        return (
                          <tr key={`3bld-combo-${index}`}>
                            <td>
                              {renderBufferBadge('corners', cornerBuffer, selectedBuffers.corners === cornerBuffer)}
                              {renderBufferBadge('edges', edgeBuffer, selectedBuffers.edges === edgeBuffer)}
                            </td>
                            <td className="text-end">{item.count.toLocaleString()}</td>
                          </tr>
                        );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 3x3 Corners Only */}
          {stats.buffer_combinations['3bld_corners'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>3x3 Corners Only</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('3bld_corners')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld_corners']
                      .filter(item => !selectedBuffers.corners || item.combo === selectedBuffers.corners)
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => (
                        <tr key={`3bld-corners-${index}`}>
                          <td>
                            {renderBufferBadge('corners', item.combo, selectedBuffers.corners === item.combo)}
                          </td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 3x3 Edges Only */}
          {stats.buffer_combinations['3bld_edges'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>3x3 Edges Only</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('3bld_edges')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld_edges']
                      .filter(item => !selectedBuffers.edges || item.combo === selectedBuffers.edges)
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => (
                        <tr key={`3bld-edges-${index}`}>
                          <td>
                            {renderBufferBadge('edges', item.combo, selectedBuffers.edges === item.combo)}
                          </td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 4x4 Complete */}
          {stats.buffer_combinations['4bld'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>4x4 BLD</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('4bld')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld']
                      .filter(item => {
                        const [cornerBuffer, wingBuffer, xcenterBuffer] = item.combo.split('-');
                        return matchesSelectedBuffers([cornerBuffer, wingBuffer, xcenterBuffer], ['corners', 'wings', 'xcenters']);
                      })
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => {
                        const [cornerBuffer, wingBuffer, xcenterBuffer] = item.combo.split('-');
                        return (
                          <tr key={`4bld-combo-${index}`}>
                            <td>
                              <div className="d-flex flex-wrap gap-2">
                                {renderBufferBadge('corners', cornerBuffer, selectedBuffers.corners === cornerBuffer)}
                                {renderBufferBadge('wings', wingBuffer, selectedBuffers.wings === wingBuffer)}
                                {renderBufferBadge('xcenters', xcenterBuffer, selectedBuffers.xcenters === xcenterBuffer)}
                              </div>
                            </td>
                            <td className="text-end">{item.count.toLocaleString()}</td>
                          </tr>
                        );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 4x4 Wings Only */}
          {stats.buffer_combinations['4bld_wings'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>4x4 Wings Only</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('4bld_wings')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld_wings']
                      .filter(item => !selectedBuffers.wings || item.combo === selectedBuffers.wings)
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => (
                        <tr key={`4bld-wings-${index}`}>
                          <td>
                            {renderBufferBadge('wings', item.combo, selectedBuffers.wings === item.combo)}
                          </td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 4x4 Centers Only */}
          {stats.buffer_combinations['4bld_centers'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>4x4 Centers Only</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('4bld_centers')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld_centers']
                      .filter(item => !selectedBuffers.xcenters || item.combo === selectedBuffers.xcenters)
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => (
                        <tr key={`4bld-centers-${index}`}>
                          <td>
                            {renderBufferBadge('xcenters', item.combo, selectedBuffers.xcenters === item.combo)}
                          </td>
                          <td className="text-end">{item.count.toLocaleString()}</td>
                        </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 5x5 Complete */}
          {stats.buffer_combinations['5bld'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>5x5 BLD</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('5bld')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['5bld']
                      .filter(item => {
                        const buffers = item.combo.split('-');
                        return matchesSelectedBuffers(buffers, ['corners', 'edges', 'wings', 'xcenters', 'tcenters']);
                      })
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => {
                        const buffers = item.combo.split('-');
                        return (
                          <tr key={`5bld-combo-${index}`}>
                            <td>
                              <div className="d-flex flex-wrap gap-2">
                                {renderBufferBadge('corners', buffers[0], selectedBuffers.corners === buffers[0])}
                                {renderBufferBadge('edges', buffers[1], selectedBuffers.edges === buffers[1])}
                                {renderBufferBadge('wings', buffers[2], selectedBuffers.wings === buffers[2])}
                                {renderBufferBadge('xcenters', buffers[3], selectedBuffers.xcenters === buffers[3])}
                                {renderBufferBadge('tcenters', buffers[4], selectedBuffers.tcenters === buffers[4])}
                              </div>
                            </td>
                            <td className="text-end">{item.count.toLocaleString()}</td>
                          </tr>
                        );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* 5x5 Edges and Corners */}
          {stats.buffer_combinations['5bld_edges_corners'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>5x5 Edges & Corners</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>
                        <div className="d-flex align-items-center">
                          <span>Buffers</span>
                          {renderActiveFilters('5bld_edges_corners')}
                        </div>
                      </th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['5bld_edges_corners']
                      .filter(item => {
                        const [cornerBuffer, edgeBuffer] = item.combo.split('-');
                        return matchesSelectedBuffers([cornerBuffer, edgeBuffer], ['corners', 'edges']);
                      })
                      .slice(0, isMobile ? 5 : 15)
                      .map((item, index) => {
                        const [cornerBuffer, edgeBuffer] = item.combo.split('-');
                        return (
                          <tr key={`5bld-edges-corners-${index}`}>
                            <td>
                              <div className="d-flex flex-wrap gap-2">
                                {renderBufferBadge('corners', cornerBuffer, selectedBuffers.corners === cornerBuffer)}
                                {renderBufferBadge('edges', edgeBuffer, selectedBuffers.edges === edgeBuffer)}
                              </div>
                            </td>
                            <td className="text-end">{item.count.toLocaleString()}</td>
                          </tr>
                        );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Stats; 