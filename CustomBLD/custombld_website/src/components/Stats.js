import React from 'react';
import { Badge, Spinner, Alert } from 'react-bootstrap';

const Stats = ({ stats, statsLoading, statsError, fetchStats, isMobile }) => {
  // Function to convert letter buffer to position
  const letterToPosition = (letter, pieceType) => {
    // Buffer mapping for converting letter notation to piece positions
    const reverseBufferMap = {
      edges: {
        "A": "UB", "B": "UR", "C": "UF", "D": "UL",
        "E": "LU", "F": "LF", "G": "LD", "H": "LB",
        "I": "FU", "J": "FR", "K": "FD", "L": "FL",
        "M": "RU", "N": "RB", "O": "RD", "P": "RF",
        "Q": "BU", "R": "BL", "S": "BD", "T": "BR",
        "U": "DF", "V": "DR", "W": "DB", "X": "DL"
      },
      corners: {
        "A": "UBL", "B": "UBR", "C": "UFR", "D": "UFL",
        "E": "LUB", "F": "LFU", "G": "LDF", "H": "LBD",
        "I": "FUL", "J": "FRU", "K": "FDR", "L": "FLD",
        "M": "RUF", "N": "RBU", "O": "RDB", "P": "RFD",
        "Q": "BUR", "R": "BLU", "S": "BDL", "T": "BRD",
        "U": "DFL", "V": "DRF", "W": "DBR", "X": "DLB"
      },
      wings: {
        "A": "UBl", "B": "URb", "C": "UFr", "D": "ULf",
        "E": "LUf", "F": "LFd", "G": "LDb", "H": "LBu",
        "I": "FUr", "J": "FRd", "K": "FDl", "L": "FLu",
        "M": "RUb", "N": "RBd", "O": "RDf", "P": "RFu",
        "Q": "BUl", "R": "BLd", "S": "BDr", "T": "BRu",
        "U": "DFr", "V": "DRb", "W": "DBl", "X": "DLf"
      },
      xcenters: {
        "A": "Ubl", "B": "Urb", "C": "Ufr", "D": "Ulf",
        "E": "Lub", "F": "Lfu", "G": "Ldf", "H": "Lbd",
        "I": "Ful", "J": "Fru", "K": "Fdr", "L": "Fld",
        "M": "Ruf", "N": "Rbu", "O": "Rdb", "P": "Rfd",
        "Q": "Bur", "R": "Blu", "S": "Bdl", "T": "Brd",
        "U": "Dfl", "V": "Drf", "W": "Dbr", "X": "Dlb"
      },
      tcenters: {
        "A": "Ub", "B": "Ur", "C": "Uf", "D": "Ul",
        "E": "Lu", "F": "Lf", "G": "Ld", "H": "Lb",
        "I": "Fu", "J": "Fr", "K": "Fd", "L": "Fl",
        "M": "Ru", "N": "Rb", "O": "Rd", "P": "Rf",
        "Q": "Bu", "R": "Bl", "S": "Bd", "T": "Br",
        "U": "Df", "V": "Dr", "W": "Db", "X": "Dl"
      }
    };

    // Check if it's already a position name (e.g., "UF" instead of "C")
    if (letter.length > 1 && !letter.match(/^[A-X]$/i)) {
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
                        <td>{letterToPosition(item.buffer, 'edges')}</td>
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
                        <td>{letterToPosition(item.buffer, 'corners')}</td>
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
                        <td>{letterToPosition(item.buffer, 'wings')}</td>
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
                        <td>{letterToPosition(item.buffer, 'xcenters')}</td>
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
        <div className="row g-3">
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
                      <td>{letterToPosition(item.buffer, 'tcenters')}</td>
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
      
      {/* Buffer Combinations Across Puzzle Types */}
      {stats.buffer_combinations && (
        <div className="stats-card mt-4">
          <h5 className="stats-header">
            <i className="fas fa-layer-group me-2"></i>
            Buffer Combinations
          </h5>
          
          {/* 3x3 Edge-Corner Combinations */}
          <div className="mb-4">
            <h6 className="mt-3">3x3 Edge-Corner Combinations</h6>
            <div className="table-responsive">
              <table className="table table-sm table-striped table-bordered">
                <thead>
                  <tr>
                    <th>Combination</th>
                    <th className="text-end">Count</th>
                  </tr>
                </thead>
                <tbody>
                  {stats.buffer_combinations['3x3'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                    const [edgeBuffer, cornerBuffer] = item.combo.split('-');
                    return (
                      <tr key={`3x3-combo-${index}`}>
                        <td>
                          <span className="badge bg-light text-dark me-1">E: {letterToPosition(edgeBuffer, 'edges')}</span>
                          <span className="badge bg-info text-dark">C: {letterToPosition(cornerBuffer, 'corners')}</span>
                        </td>
                        <td className="text-end">{item.count.toLocaleString()}</td>
                      </tr>
                    );
                  })}
                  {isMobile && stats.buffer_combinations['3x3'].length > 5 && (
                    <tr>
                      <td colSpan="2" className="text-center">
                        <button 
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => alert("Switch to landscape view to see more combinations")}
                        >
                          See more...
                        </button>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
          
          {/* 4x4 Complete Buffer Combinations */}
          <div className="mb-4">
            <h6 className="mt-3">4x4 Buffer Combinations</h6>
            <div className="table-responsive">
              <table className="table table-sm table-striped table-bordered">
                <thead>
                  <tr>
                    <th>Combination</th>
                    <th className="text-end">Count</th>
                  </tr>
                </thead>
                <tbody>
                  {stats.buffer_combinations['4x4'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                    const [cornerBuffer, wingBuffer, xcenterBuffer] = item.combo.split('-');
                    return (
                      <tr key={`4x4-combo-${index}`}>
                        <td>
                          <div className="d-flex flex-wrap gap-1">
                            <span className="badge bg-info text-dark">C: {letterToPosition(cornerBuffer, 'corners')}</span>
                            <span className="badge bg-light text-dark">W: {letterToPosition(wingBuffer, 'wings')}</span>
                            <span className="badge bg-warning text-dark">X: {letterToPosition(xcenterBuffer, 'xcenters')}</span>
                          </div>
                        </td>
                        <td className="text-end">{item.count.toLocaleString()}</td>
                      </tr>
                    );
                  })}
                  {isMobile && stats.buffer_combinations['4x4'].length > 5 && (
                    <tr>
                      <td colSpan="2" className="text-center">
                        <button 
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => alert("Switch to landscape view to see more combinations")}
                        >
                          See more...
                        </button>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
          
          {/* 5x5 Buffer Combinations */}
          <div>
            <h6 className="mt-3">5x5 Buffer Combinations</h6>
            <div className="table-responsive">
              <table className="table table-sm table-striped table-bordered">
                <thead>
                  <tr>
                    <th>Combination</th>
                    <th className="text-end">Count</th>
                  </tr>
                </thead>
                <tbody>
                  {stats.buffer_combinations['5x5'].slice(0, isMobile ? 5 : 15).map((item, index) => {
                    const [cornerBuffer, tcenterBuffer] = item.combo.split('-');
                    return (
                      <tr key={`5x5-combo-${index}`}>
                        <td>
                          <span className="badge bg-info text-dark me-1">C: {letterToPosition(cornerBuffer, 'corners')}</span>
                          <span className="badge bg-success text-dark">T: {letterToPosition(tcenterBuffer, 'tcenters')}</span>
                        </td>
                        <td className="text-end">{item.count.toLocaleString()}</td>
                      </tr>
                    );
                  })}
                  {isMobile && stats.buffer_combinations['5x5'].length > 5 && (
                    <tr>
                      <td colSpan="2" className="text-center">
                        <button 
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => alert("Switch to landscape view to see more combinations")}
                        >
                          See more...
                        </button>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Stats; 