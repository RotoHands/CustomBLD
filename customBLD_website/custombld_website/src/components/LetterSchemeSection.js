import React, { useState, useEffect } from 'react';
import { Form, Table, Button } from 'react-bootstrap';
import { 
  basePositions,
  cornerPositions,
  edgePositions,
  wingPositions,
  xCenterPositions,
  tCenterPositions 
} from './LetterScheme';
import './LetterSchemeSection.css';

const LetterSchemeSection = ({ 
  formData, 
  handleLetterChange, 
  showCustomScheme, 
  setShowCustomScheme 
}) => {
  const [hasChanges, setHasChanges] = useState(false);

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

  const saveSettings = () => {
    try {
      localStorage.setItem('letterScheme', JSON.stringify(formData.letterScheme));
      setHasChanges(false);
      alert('Settings saved successfully!');
    } catch (error) {
      alert('Error saving settings: ' + error.message);
    }
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

  const handleCustomSchemeToggle = (piece, enabled) => {
    setShowCustomScheme(prev => ({
      ...prev,
      [piece]: enabled
    }));

    // If disabling custom scheme, reset to base scheme
    if (!enabled) {
      Object.entries(formData.letterScheme.base).forEach(([pos, value]) => {
        handleLetterChange(piece, pos, value);
      });
    }
  };

  const getPositionsForPiece = (piece) => {
    switch(piece) {
      case 'base': return basePositions;
      case 'corners': return cornerPositions;
      case 'edges': return edgePositions;
      case 'wings': return wingPositions;
      case 'xCenters': return xCenterPositions;
      case 'tCenters': return tCenterPositions;
      default: return [];
    }
  };

  const renderLetterInputs = (piece) => {
    const positions = getPositionsForPiece(piece);
    const isBase = piece === 'base';
    
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
                        maxLength={isBase ? undefined : 1}
                        value={formData.letterScheme[piece]?.[pos] ?? ''}
                        onChange={(e) => handleLetterChange(
                          piece, 
                          pos, 
                          isBase ? e.target.value : e.target.value.toUpperCase()
                        )}
                        className={`mx-auto text-center ${isBase ? 'w-75' : 'w-50'}`}
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
      <h5>Base Letter Scheme</h5>
      {renderLetterInputs('base')}
      
      {Object.entries(showCustomScheme).map(([piece, isCustom]) => (
        <div key={piece}>
          <Form.Check 
            type="checkbox"
            label={`Use custom ${piece} letter scheme`}
            checked={isCustom}
            onChange={(e) => handleCustomSchemeToggle(piece, e.target.checked)}
            className="mb-3"
          />
          {isCustom && renderLetterInputs(piece)}
        </div>
      ))}

      <div className="d-flex justify-content-between mt-4 mb-4">
        <Button 
          variant="primary" 
          onClick={saveSettings}
          disabled={!hasChanges}
        >
          Save Letter Scheme
        </Button>
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