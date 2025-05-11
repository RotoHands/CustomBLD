import React, { useState } from 'react';
import { Badge, Spinner, Alert, Button, Form } from 'react-bootstrap';

const Stats = ({ stats, statsLoading, statsError, fetchStats, isMobile }) => {
  const [isRefreshing, setIsRefreshing] = useState(false);
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

  // Function to handle manual refresh
  const handleRefresh = async () => {
    setIsRefreshing(true);
    try {
      // Call the fetchStats function with refresh=true
      await fetchStats(true);
    } finally {
      setIsRefreshing(false);
    }
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
          <Button 
            variant="outline-primary" 
            size="sm"
            onClick={handleRefresh}
            disabled={isRefreshing}
          >
            {isRefreshing ? (
              <>
                <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                <span className="ms-1">Refreshing...</span>
              </>
            ) : (
              <>
                <i className="fas fa-sync-alt me-1"></i> Refresh Stats
              </>
            )}
          </Button>
        </div>
        
        {/* Cache info */}
        {stats.timestamp && (
          <div className="text-muted small mb-3">
            <div className="d-flex justify-content-between">
              <span>
                Last updated: {new Date(stats.timestamp * 1000).toLocaleString()}
                {stats.query_time && ` (Query time: ${stats.query_time.toFixed(2)}s)`}
              </span>
              {stats.cache_age && (
                <span className="ms-2">
                  <Badge bg="info">{formatRelativeTime(stats.cache_age)}</Badge>
                  {stats.cache_expires_in > 0 && (
                    <Badge bg="secondary" className="ms-1">
                      Refreshes in {formatRelativeTime(stats.cache_expires_in)}
                    </Badge>
                  )}
                </span>
              )}
            </div>
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
          </h5>
          
          {/* 3x3 Complete */}
          {stats.buffer_combinations['3bld'] && (
            <div className="mb-3">
              <h6 className="mt-2" style={{ fontWeight: '500', color: '#333' }}>3x3 BLD</h6>
              <div className="table-responsive">
                <table className="table table-sm table-striped table-bordered">
                  <thead>
                    <tr>
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                      const [edgeBuffer, cornerBuffer] = item.combo.split('-');
                      return (
                        <tr key={`3bld-combo-${index}`}>
                          <td>
                            <span className="badge me-2" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.corners, color: '#333' }}><strong>Corners:</strong> {letterToPosition(cornerBuffer, 'corners')}</span>
                            <span className="badge " style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.edges, color: '#333' }}><strong>Edges:</strong> {letterToPosition(edgeBuffer, 'edges')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld_corners'].slice(0, isMobile ? 5 : 15).map((item, index) => (
                      <tr key={`3bld-corners-${index}`}>
                        <td>
                          <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.corners, color: '#333' }}><strong>Corners:</strong> {letterToPosition(item.combo, 'corners')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['3bld_edges'].slice(0, isMobile ? 5 : 15).map((item, index) => (
                      <tr key={`3bld-edges-${index}`}>
                        <td>
                          <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.edges, color: '#333' }}><strong>Edges:</strong> {letterToPosition(item.combo, 'edges')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                      const [cornerBuffer, wingBuffer, xcenterBuffer] = item.combo.split('-');
                      return (
                        <tr key={`4bld-combo-${index}`}>
                          <td>
                            <div className="d-flex flex-wrap gap-2">
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.corners, color: '#333' }}><strong>Corners:</strong> {letterToPosition(cornerBuffer, 'corners')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.wings, color: '#333' }}><strong>Wings:</strong> {letterToPosition(wingBuffer, 'wings')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.xcenters, color: '#333' }}><strong>X-Centers:</strong> {letterToPosition(xcenterBuffer, 'xcenters')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld_wings'].slice(0, isMobile ? 5 : 15).map((item, index) => (
                      <tr key={`4bld-wings-${index}`}>
                        <td>
                          <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.wings, color: '#333' }}><strong>Wings:</strong> {letterToPosition(item.combo, 'wings')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['4bld_centers'].slice(0, isMobile ? 5 : 15).map((item, index) => (
                      <tr key={`4bld-centers-${index}`}>
                        <td>
                          <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.xcenters, color: '#333' }}><strong>X-Centers:</strong> {letterToPosition(item.combo, 'xcenters')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['5bld'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                      const buffers = item.combo.split('-');
                      return (
                        <tr key={`5bld-combo-${index}`}>
                          <td>
                            <div className="d-flex flex-wrap gap-2">
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.corners, color: '#333' }}><strong>Corners:</strong> {letterToPosition(buffers[0], 'corners')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.edges, color: '#333' }}><strong>Edges:</strong> {letterToPosition(buffers[1], 'edges')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.wings, color: '#333' }}><strong>Wings:</strong> {letterToPosition(buffers[2], 'wings')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.xcenters, color: '#333' }}><strong>X-Centers:</strong> {letterToPosition(buffers[3], 'xcenters')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.tcenters, color: '#333' }}><strong>T-Centers:</strong> {letterToPosition(buffers[4], 'tcenters')}</span>
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
                      <th style={{ fontWeight: 'normal' }}>Buffers</th>
                      <th className="text-end">Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.buffer_combinations['5bld_edges_corners'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                      const [cornerBuffer, edgeBuffer] = item.combo.split('-');
                      return (
                        <tr key={`5bld-edges-corners-${index}`}>
                          <td>
                            <div className="d-flex flex-wrap gap-2">
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.corners, color: '#333' }}><strong>Corners:</strong> {letterToPosition(cornerBuffer, 'corners')}</span>
                              <span className="badge" style={{ fontSize: '1rem', fontWeight: 'normal', backgroundColor: colors.edges, color: '#333' }}><strong>Edges:</strong> {letterToPosition(edgeBuffer, 'edges')}</span>
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