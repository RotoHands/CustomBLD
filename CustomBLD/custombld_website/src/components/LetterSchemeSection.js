import React, { useState, useEffect } from 'react';
import { Form, Row, Col, Table, Button } from 'react-bootstrap';
import { basePositions, cornerPositions, edgePositions, wingPositions, xCenterPositions, tCenterPositions } from './LetterScheme';
import { toast } from 'react-toastify';

import './LetterSchemeSection.css';

const LetterSchemeSection = ({ formData, handleLetterChange }) => {
  const [hasChanges, setHasChanges] = useState(false);

  const handleLocalChange = (piece, pos, value) => {
    handleLetterChange(piece, pos, value);
    setHasChanges(true);
  };

  const saveSettings = () => {
    try {
      // Save with more visible console logging
      console.log("Saving letter scheme to localStorage:", formData.letterScheme);
      localStorage.setItem('letterScheme', JSON.stringify(formData.letterScheme));
      setHasChanges(false);
      toast.success('Letter scheme saved successfully!');
    } catch (error) {
      console.error("Error saving letter scheme:", error);
      toast.error('Error saving settings: ' + error.message);
    }
  };

  const applyToAllPieces = () => {
    // For each base position, map to corresponding position in other piece types by index
    basePositions.forEach((basePos, index) => {
      const letter = formData.letterScheme.base[basePos];
      if (letter) {
        handleLetterChange('corners', cornerPositions[index], letter);
        handleLetterChange('edges', edgePositions[index], letter);
        handleLetterChange('wings', wingPositions[index], letter);
        handleLetterChange('xCenters', xCenterPositions[index], letter);
        handleLetterChange('tCenters', tCenterPositions[index], letter);
      }
    });
    toast.success('Base letter scheme applied to all pieces');
  };

  const [showCustomScheme, setShowCustomScheme] = useState({
    corners: false,
    edges: false,
    wings: false,
    xCenters: false,
    tCenters: false
  });

  useEffect(() => {
    try {
      const savedScheme = localStorage.getItem('letterScheme');
      console.log("Retrieved saved scheme:", savedScheme);
      
      if (savedScheme) {
        const parsed = JSON.parse(savedScheme);
        Object.entries(parsed).forEach(([piece, letters]) => {
          Object.entries(letters).forEach(([pos, letter]) => {
            handleLetterChange(piece, pos, letter);
          });
        });
      } else {
        console.log("No saved scheme found, initializing with default");
        // Initialize with default letter scheme
        const defaultScheme = {
          "corners": {
            "UBL": "A", "UBR": "B", "UFR": "C", "UFL": "D",
            "LUB": "E", "LFU": "F", "LDF": "G", "LBD": "H",
            "FUL": "I", "FRU": "J", "FDR": "K", "FLD": "L",
            "RUF": "M", "RBU": "N", "RDB": "O", "RFD": "P",
            "BUR": "Q", "BLU": "R", "BDL": "S", "BRD": "T",
            "DFL": "U", "DRF": "V", "DBR": "W", "DLB": "X"
          },
          "edges": {
            "UB": "A", "UR": "B", "UF": "C", "UL": "D",
            "LU": "E", "LF": "F", "LD": "G", "LB": "H",
            "FU": "I", "FR": "J", "FD": "K", "FL": "L",
            "RU": "M", "RB": "N", "RD": "O", "RF": "P",
            "BU": "Q", "BL": "R", "BD": "S", "BR": "T",
            "DF": "U", "DR": "V", "DB": "W", "DL": "X"
          },
          "wings": {
            "UBr": "A", "URf": "B", "UFl": "C", "ULb": "D",
            "LUf": "E", "LFd": "F", "LDb": "G", "LBu": "H",
            "FUr": "I", "FRd": "J", "FDl": "K", "FLu": "L",
            "RUb": "M", "RBd": "N", "RDf": "O", "RFu": "P",
            "BUl": "Q", "BLd": "R", "BDr": "S", "BRu": "T",
            "DFr": "U", "DRb": "V", "DBl": "W", "DLf": "X"
          },
          "xCenters": {
            "Ubl": "A", "Urb": "B", "Ufr": "C", "Ulf": "D",
            "Lub": "E", "Lfu": "F", "Ldf": "G", "Lbd": "H",
            "Ful": "I", "Fru": "J", "Fdr": "K", "Fld": "L",
            "Ruf": "M", "Rbu": "N", "Rdb": "O", "Rfd": "P",
            "Bur": "Q", "Blu": "R", "Bdl": "S", "Brd": "T",
            "Dfl": "U", "Drf": "V", "Dbr": "W", "Dlb": "X"
          },
          "tCenters": {
            "Ub": "A", "Ur": "B", "Uf": "C", "Ul": "D",
            "Lu": "E", "Lf": "F", "Ld": "G", "Lb": "H",
            "Fu": "I", "Fr": "J", "Fd": "K", "Fl": "L",
            "Ru": "M", "Rb": "N", "Rd": "O", "Rf": "P",
            "Bu": "Q", "Bl": "R", "Bd": "S", "Br": "T",
            "Df": "U", "Dr": "V", "Db": "W", "Dl": "X"
          },
          "base": {
            "UBL": "A", "UBR": "B", "UFR": "C", "UFL": "D",
            "LUB": "E", "LFU": "F", "LDF": "G", "LBD": "H",
            "FUL": "I", "FRU": "J", "FDR": "K", "FLD": "L",
            "RUF": "M", "RBU": "N", "RDB": "O", "RFD": "P",
            "BUR": "Q", "BLU": "R", "BDL": "S", "BRD": "T",
            "DFL": "U", "DRF": "V", "DBR": "W", "DLB": "X"
          }
        };
        
        Object.entries(defaultScheme).forEach(([piece, letters]) => {
          Object.entries(letters).forEach(([pos, letter]) => {
            handleLetterChange(piece, pos, letter);
          });
        });
        
        // Save the default scheme
        localStorage.setItem('letterScheme', JSON.stringify(defaultScheme));
      }
    } catch (error) {
      console.error("Error loading letter scheme:", error);
      toast.error('Error loading letter scheme: ' + error.message);
    }
  }, []);

  const handleLetterChangeWithTracking = (piece, pos, value) => {
    handleLetterChange(piece, pos, value);
    setHasChanges(true);
  };

  const resetSettings = () => {
    if (window.confirm('Are you sure you want to reset to default letters?')) {
      localStorage.removeItem('letterScheme');
      basePositions.forEach((pos, index) => {
        handleLetterChange('base', pos, String.fromCharCode(65 + index));
      });
      setHasChanges(false);
    }
  };

  const renderBaseScheme = () => (
    <>
      <h5>Base Letter Scheme</h5>
      <Table striped bordered hover size="sm" className="mb-4">
        <tbody>
          {Array.from({ length: 6 }, (_, rowIndex) => (
            <tr key={rowIndex}>
              {Array.from({ length: 4 }, (_, colIndex) => {
                const index = rowIndex * 4 + colIndex;
                if (index < basePositions.length) {
                  const pos = basePositions[index];
                  return (
                    <td key={pos}>
                      <div className="small text-muted mb-1">{pos}</div>
                      <Form.Control
                        type="text"
                        value={formData.letterScheme?.base?.[pos] ?? ''}
                        onChange={(e) => handleLetterChangeWithTracking('base', pos, e.target.value)}
                        className="w-75 mx-auto text-center"
                        style={{ fontSize: '0.875rem', padding: '0.25rem' }}
                      />
                    </td>
                  );
                }
                return <td key={`empty-${rowIndex}-${colIndex}`}></td>;
              })}
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );

  const getPositionsForPiece = (piece) => {
    switch(piece) {
      case 'corners': return cornerPositions;
      case 'edges': return edgePositions;
      case 'wings': return wingPositions;
      case 'xCenters': return xCenterPositions;
      case 'tCenters': return tCenterPositions;
      default: return basePositions;
    }
  };

  const renderLetterInputs = (piece) => {
    const positions = getPositionsForPiece(piece);
    return (
      <Table striped bordered hover size="sm" className="mb-4">
        <tbody>
          {Array.from({ length: 6 }, (_, rowIndex) => (
            <tr key={rowIndex}>
              {Array.from({ length: 4 }, (_, colIndex) => {
                const index = rowIndex * 4 + colIndex;
                if (index < positions.length) {
                  const pos = positions[index];
                  return (
                    <td key={pos}>
                      <div className="small text-muted mb-1">{pos}</div>
                      <Form.Control
                        type="text"
                        value={formData.letterScheme?.[piece]?.[pos] ?? ''}
                        onChange={(e) => handleLetterChangeWithTracking(piece, pos, e.target.value)}
                        className="w-75 mx-auto text-center"
                        style={{ fontSize: '0.875rem', padding: '0.25rem' }}
                      />
                    </td>
                  );
                }
                return <td key={`empty-${rowIndex}-${colIndex}`}></td>;
              })}
            </tr>
          ))}
        </tbody>
      </Table>
    );
  };

  return (
    <div className="letter-scheme-section">
      {renderBaseScheme()}
      
      <Form.Check 
        type="checkbox"
        label="Customize Corner Letters"
        checked={showCustomScheme.corners}
        onChange={e => setShowCustomScheme({...showCustomScheme, corners: e.target.checked})}
        className="mb-2"
      />
      {showCustomScheme.corners && (
        <>
          <h6>Corner Letters</h6>
          {renderLetterInputs('corners')}
        </>
      )}

      <Form.Check 
        type="checkbox"
        label="Customize Edge Letters"
        checked={showCustomScheme.edges}
        onChange={e => setShowCustomScheme({...showCustomScheme, edges: e.target.checked})}
        className="mb-2"
      />
      {showCustomScheme.edges && (
        <>
          <h6>Edge Letters</h6>
          {renderLetterInputs('edges')}
        </>
      )}

      <Form.Check 
        type="checkbox"
        label="Customize Wing Letters"
        checked={showCustomScheme.wings}
        onChange={e => setShowCustomScheme({...showCustomScheme, wings: e.target.checked})}
        className="mb-2"
      />
      {showCustomScheme.wings && (
        <>
          <h6>Wing Letters</h6>
          {renderLetterInputs('wings')}
        </>
      )}

      <Form.Check 
        type="checkbox"
        label="Customize X-Center Letters"
        checked={showCustomScheme.xCenters}
        onChange={e => setShowCustomScheme({...showCustomScheme, xCenters: e.target.checked})}
        className="mb-2"
      />
      {showCustomScheme.xCenters && (
        <>
          <h6>X-Center Letters</h6>
          {renderLetterInputs('xCenters')}
        </>
      )}

      <Form.Check 
        type="checkbox"
        label="Customize T-Center Letters"
        checked={showCustomScheme.tCenters}
        onChange={e => setShowCustomScheme({...showCustomScheme, tCenters: e.target.checked})}
        className="mb-2"
      />
      {showCustomScheme.tCenters && (
        <>
          <h6>T-Center Letters</h6>
          {renderLetterInputs('tCenters')}
        </>
      )}

      <div className="d-flex flex-wrap gap-2 mt-4 mb-4">
        <Button 
          variant="primary" 
          onClick={saveSettings}
          className="flex-grow-1"
        >
          <i className="fas fa-save me-2"></i>
          Save Letter Scheme
        </Button>
        <Button 
          variant="success" 
          onClick={applyToAllPieces}
          className="flex-grow-1"
        >
          <i className="fas fa-copy me-2"></i>
          Apply to All Pieces
        </Button>
        <Button 
          variant="outline-danger" 
          onClick={resetSettings}
          className="flex-grow-1"
        >
          <i className="fas fa-undo me-2"></i>
          Reset to Default
        </Button>
      </div>
        
      {hasChanges && (
        <div className="alert alert-warning">
          <i className="fas fa-exclamation-triangle me-2"></i>
          You have unsaved changes
        </div>
      )}
    </div>
  );
};

export default LetterSchemeSection;