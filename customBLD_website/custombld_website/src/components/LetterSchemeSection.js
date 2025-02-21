import React, { useState, useEffect } from 'react';
import { Form, Row, Col, Table, Button } from 'react-bootstrap';
import { basePositions, cornerPositions, edgePositions, wingPositions, xCenterPositions, tCenterPositions } from './LetterScheme';
import './LetterSchemeSection.css';

const LetterSchemeSection = ({ formData, handleLetterChange }) => {
  const [hasChanges, setHasChanges] = useState(false);

  const handleLocalChange = (piece, pos, value) => {
    handleLetterChange(piece, pos, value);
    setHasChanges(true);
  };

  const saveSettings = () => {
    try {
      localStorage.setItem('letterScheme', JSON.stringify(formData.letterScheme));
      setHasChanges(false);
      alert('Letter scheme saved successfully!');
    } catch (error) {
      alert('Error saving settings: ' + error.message);
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
    alert('Base letter scheme applied to all pieces');
  };

  const [showCustomScheme, setShowCustomScheme] = useState({
    corners: false,
    edges: false,
    wings: false,
    xCenters: false,
    tCenters: false
  });

  useEffect(() => {
    const savedScheme = localStorage.getItem('letterScheme');
    if (savedScheme) {
      const parsed = JSON.parse(savedScheme);
      Object.entries(parsed).forEach(([piece, letters]) => {
        Object.entries(letters).forEach(([pos, letter]) => {
          handleLetterChange(piece, pos, letter);
        });
      });
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

      <div className="d-flex justify-content-between mt-4 mb-4">
        <div>
          <Button 
            variant="primary" 
            onClick={saveSettings}
            disabled={!hasChanges}
            className="me-2"
          >
            Save Letter Scheme
          </Button>
          <Button 
            variant="success" 
            onClick={applyToAllPieces}
          >
            Apply Letter Scheme to All
          </Button>
        </div>
        <Button 
          variant="outline-danger" 
          onClick={resetSettings}
        >
          Reset to Default
        </Button>
      </div>
        
      {hasChanges && (
        <div className="alert alert-warning">
          You have unsaved changes
        </div>
      )}
    </div>
  );
};

export default LetterSchemeSection;