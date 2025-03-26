import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { edgeBufferOptions } from '../constants/Constants';
import { edgePositions } from './LetterScheme';

const EdgeSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random'); // 'random' or 'range'
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [flippedType, setFlippedType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);
  const [selectedLetters, setSelectedLetters] = useState(
    edgePositions.reduce((acc, pos) => ({ ...acc, [pos]: true }), {})
  );

  // Generic handler for type changes
  const handleTypeChange = (field, type, setter) => {
    setter(type);
    handleChange({
      target: {
        name: `${field}_type`,
        value: type
      }
    });
  };

  const renderRangeControl = (fieldName, type, defaultMax) => (
    <div className="d-flex align-items-center gap-2 ms-3">
      <Form.Control
        type="number"
        min="0"
        placeholder="0"
        value={formData[`${fieldName}_min`] === 0 ? "0" : (formData[`${fieldName}_min`] !== undefined ? formData[`${fieldName}_min`] : "0")}
        onChange={(e) => {
          // Get the raw value directly from the input
          const rawValue = e.target.value;
          
          // Handle selection and replacement - this is key for replacing 0 with 1
          if (e.target.selectionStart === e.target.selectionEnd && e.target.selectionStart === 0) {
            // If cursor is at the beginning and user types, replace the entire value
            const parsed = parseInt(rawValue, 10);
            if (!isNaN(parsed)) {
              const currentMax = formData[`${fieldName}_max`] || defaultMax;
              
              // If the new min is greater than current max, update max too
              if (parsed > currentMax) {
                // First update min
                handleChange({
                  target: {
                    name: `${fieldName}_min`,
                    value: parsed,
                    type: 'number'
                  }
                });
                
                // Then update max to match the new min
                handleChange({
                  target: {
                    name: `${fieldName}_max`,
                    value: parsed,
                    type: 'number'
                  }
                });
              } else {
                // Otherwise just update min
                handleChange({
                  target: {
                    name: `${fieldName}_min`,
                    value: parsed,
                    type: 'number'
                  }
                });
              }
              return;
            }
          }
          
          // Normal handling for other cases
          let processedValue;
          
          if (rawValue === '') {
            processedValue = 0; // Default to 0 when empty
          } else {
            // Parse as number, ensuring we handle 0 correctly
            const parsed = parseInt(rawValue, 10);
            processedValue = isNaN(parsed) ? 0 : parsed;
          }
          
          // Check if we need to update the max value too
          const currentMax = formData[`${fieldName}_max`] || defaultMax;
          if (processedValue > currentMax) {
            // First update min
            handleChange({
              target: {
                name: `${fieldName}_min`,
                value: processedValue,
                type: 'number'
              }
            });
            
            // Then update max to match the new min
            handleChange({
              target: {
                name: `${fieldName}_max`,
                value: processedValue,
                type: 'number'
              }
            });
          } else {
            // Just update min if no max adjustment needed
            handleChange({
              target: {
                name: `${fieldName}_min`,
                value: processedValue,
                type: 'number'
              }
            });
          }
        }}
        disabled={type !== 'range'}
        style={{ width: '70px', opacity: type === 'range' ? 1 : 0.6 }}
        onFocus={(e) => {
          // Select all text when the input is focused
          e.target.select();
        }}
      />
      <span>-</span>
      <Form.Control
        type="number"
        min="0"
        placeholder={defaultMax.toString()}
        value={formData[`${fieldName}_max`] === 0 ? "0" : (formData[`${fieldName}_max`] || defaultMax)}
        onChange={(e) => {
          // Get the raw value directly from the input
          const rawValue = e.target.value;
          
          // Handle selection and replacement - this is key for replacing 0 with 1
          if (e.target.selectionStart === e.target.selectionEnd && e.target.selectionStart === 0) {
            // If cursor is at the beginning and user types, replace the entire value
            const parsed = parseInt(rawValue, 10);
            if (!isNaN(parsed)) {
              const currentMin = formData[`${fieldName}_min`] || 0;
              
              // If the new max is less than current min (but not 0), update min too
              if (parsed !== 0 && parsed < currentMin) {
                // Update min to match the new max
                handleChange({
                  target: {
                    name: `${fieldName}_min`,
                    value: parsed,
                    type: 'number'
                  }
                });
              }
              
              // Always update max
              handleChange({
                target: {
                  name: `${fieldName}_max`,
                  value: parsed,
                  type: 'number'
                }
              });
              return;
            }
          }
          
          // Special handling for backspace/delete when the value is "1"
          if (rawValue === '' && e.nativeEvent.inputType === 'deleteContentBackward') {
            console.log("Backspace detected on value, setting to 0");
            handleChange({
              target: {
                name: `${fieldName}_max`,
                value: 0,
                type: 'number'
              }
            });
            return;
          }
          
          // Normal handling for other cases
          let processedValue;
          
          if (rawValue === '') {
            processedValue = 0; // Default to 0 when empty
          } else {
            // Parse as number, ensuring we handle 0 correctly
            const parsed = parseInt(rawValue, 10);
            processedValue = isNaN(parsed) ? 0 : parsed;
          }
          
          // Check if the new max is less than min (but not 0)
          const currentMin = formData[`${fieldName}_min`] || 0;
          if (processedValue !== 0 && processedValue < currentMin) {
            // First update max
            handleChange({
              target: {
                name: `${fieldName}_max`,
                value: processedValue,
                type: 'number'
              }
            });
            
            // Then update min to match the new max
            handleChange({
              target: {
                name: `${fieldName}_min`,
                value: processedValue,
                type: 'number'
              }
            });
          } else {
            // Otherwise just update max
            handleChange({
              target: {
                name: `${fieldName}_max`,
                value: processedValue,
                type: 'number'
              }
            });
          }
        }}
        disabled={type !== 'range'}
        style={{ width: '70px', opacity: type === 'range' ? 1 : 0.6 }}
        onFocus={(e) => {
          // Select all text when the input is focused
          e.target.select();
        }}
      />
    </div>
  );

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'edge_buffer',
        value: value
      }
    });
  };

  const handleSelectAll = () => {
    const newSelected = { ...selectedLetters };
    edgePositions.forEach(pos => {
      newSelected[pos] = true;
      handlePracticeLetterChange('edges', pos, true);
    });
    setSelectedLetters(newSelected);
  };

  const handleRemoveAll = () => {
    const newSelected = { ...selectedLetters };
    edgePositions.forEach(pos => {
      newSelected[pos] = false;
      handlePracticeLetterChange('edges', pos, false);
    });
    setSelectedLetters(newSelected);
  };

  const handleSingleCheck = (pos, checked) => {
    setSelectedLetters(prev => ({
      ...prev,
      [pos]: checked
    }));
    handlePracticeLetterChange('edges', pos, checked);
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Edge Buffer</Form.Label>
        <Col sm="9">
          <div className="d-flex flex-wrap gap-3">
            {edgeBufferOptions.map(buffer => (
              <Form.Check
                key={buffer}
                type="radio"
                id={`edge-buffer-${buffer}`}
                label={buffer}
                checked={formData.edge_buffer === buffer}
                onChange={() => handleBufferChange(buffer)}
                className="me-3"
              />
            ))}
          </div>
        </Col>
      </Form.Group>
      
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Edge Length</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="edge-length-random"
              label="random"
              checked={lengthType === 'random'}
              onChange={() => handleTypeChange('edge_length', 'random', setLengthType)}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="edge-length-range"
              label="range"
              checked={lengthType === 'range'}
              onChange={() => handleTypeChange('edge_length', 'range', setLengthType)}
            />
            {renderRangeControl('edge_length', lengthType, 18)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="cycle-breaks-random"
              label="random"
              checked={cycleBreaksType === 'random'}
              onChange={() => handleTypeChange('edge_cycle_breaks', 'random', setCycleBreaksType)}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="cycle-breaks-range"
              label="range"
              checked={cycleBreaksType === 'range'}
              onChange={() => handleTypeChange('edge_cycle_breaks', 'range', setCycleBreaksType)}
            />
            {renderRangeControl('edge_cycle_breaks', cycleBreaksType, 10)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Flipped Edges</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="flipped-edges-random"
              label="random"
              checked={flippedType === 'random'}
              onChange={() => handleTypeChange('edges_flipped', 'random', setFlippedType)}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="flipped-edges-range"
              label="range"
              checked={flippedType === 'range'}
              onChange={() => handleTypeChange('edges_flipped', 'range', setFlippedType)}
            />
            {renderRangeControl('edges_flipped', flippedType, 12)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved Edges</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="solved-edges-random"
              label="random"
              checked={solvedType === 'random'}
              onChange={() => handleTypeChange('edges_solved', 'random', setSolvedType)}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="solved-edges-range"
              label="range"
              checked={solvedType === 'range'}
              onChange={() => handleTypeChange('edges_solved', 'range', setSolvedType)}
            />
            {renderRangeControl('edges_solved', solvedType, 12)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group className="mb-3">
        <div 
          className="collapsible-header d-flex align-items-center"
          onClick={() => setShowPracticeLetters(!showPracticeLetters)}
        >
          <i className={`fa-solid ${showPracticeLetters ? 'fa-caret-down' : 'fa-caret-right'}`}></i>
          <Form.Label className="mb-0 ms-2">Letters to Practice</Form.Label>
        </div>
        
        {showPracticeLetters && (
          <>
            <div className="d-flex gap-2 mb-2 mt-2">
              <Button
                variant="outline-primary"
                size="sm"
                onClick={handleSelectAll}
              >
                Select All
              </Button>
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={handleRemoveAll}
              >
                Remove All
              </Button>
            </div>
            <div className="practice-letters p-3 border rounded bg-light mt-2">
              <div className="d-flex flex-wrap gap-2">
                {edgePositions.map((pos) => {
                  const letter = formData.letterScheme?.edges?.[pos] || '';
                  return letter && (
                    <Form.Check
                      key={pos}
                      type="checkbox"
                      id={`edge-practice-${pos}`}
                      label={`${letter} (${pos})`}
                      checked={selectedLetters[pos]}
                      onChange={(e) => handleSingleCheck(pos, e.target.checked)}
                      className="me-3"
                    />
                  );
                })}
              </div>
            </div>
          </>
        )}
      </Form.Group>
    </>
  );
};

export default EdgeSection;