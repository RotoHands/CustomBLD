import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { xCenterPositions } from './LetterScheme';
import { xCenterBufferOptions } from '../constants/Constants';

const XCenterSection = ({ formData, handleChange, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random');
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);
  const [selectedLetters, setSelectedLetters] = useState(
    xCenterPositions.reduce((acc, pos) => ({ ...acc, [pos]: true }), {})
  );

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
        name: 'xcenter_buffer',
        value: value
      }
    });
  };

  const handleTypeChange = (field, type, setter) => {
    setter(type);
    handleChange({
      target: {
        name: `${field}_type`,
        value: type
      }
    });
  };

  const handleRangeChange = (field, type, value) => {
    handleChange({
      target: {
        name: `${field}_${type}`,
        value: Math.max(0, parseInt(value) || 0)
      }
    });
  };

  const handleParityChange = (value) => {
    handleChange({
      target: {
        name: 'xcenter_parity',
        value
      }
    });
  };

  const handleSelectAll = () => {
    const newSelected = { ...selectedLetters };
    xCenterPositions.forEach(pos => {
      newSelected[pos] = true;
      handlePracticeLetterChange('xCenters', pos, true);
    });
    setSelectedLetters(newSelected);
  };

  const handleRemoveAll = () => {
    const newSelected = { ...selectedLetters };
    xCenterPositions.forEach(pos => {
      newSelected[pos] = false;
      handlePracticeLetterChange('xCenters', pos, false);
    });
    setSelectedLetters(newSelected);
  };

  const handleSingleCheck = (pos, checked) => {
    setSelectedLetters(prev => ({
      ...prev,
      [pos]: checked
    }));
    handlePracticeLetterChange('xCenters', pos, checked);
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Buffer</Form.Label>
        <Col sm="9">
          <div className="d-flex flex-wrap gap-3">
            {xCenterBufferOptions.map(buffer => (

              <Form.Check
                key={buffer}
                type="radio"
                id={`xcenter-buffer-${buffer}`}
                label={buffer}
                checked={formData.xcenter_buffer === buffer}
                onChange={() => handleBufferChange(buffer)}
                className="me-3"
              />
            ))
            }
            
          </div>
        </Col>
        
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Length</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="xcenter-length-random"
              label="random"
              checked={lengthType === 'random'}
              onChange={() => handleTypeChange('x_centers_length', 'random', setLengthType)}
            />
            <Form.Check
              type="radio"
              id="xcenter-length-range"
              label="range"
              checked={lengthType === 'range'}
              onChange={() => handleTypeChange('x_centers_length', 'range', setLengthType)}
            />
            {renderRangeControl('x_centers_length', lengthType, 30)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="xcenter-cycle-breaks-random"
              label="random"
              checked={cycleBreaksType === 'random'}
              onChange={() => handleTypeChange('x_centers_cycle_breaks', 'random', setCycleBreaksType)}
            />
            <Form.Check
              type="radio"
              id="xcenter-cycle-breaks-range"
              label="range"
              checked={cycleBreaksType === 'range'}
              onChange={() => handleTypeChange('x_centers_cycle_breaks', 'range', setCycleBreaksType)}
            />
            {renderRangeControl('x_centers_cycle_breaks', cycleBreaksType, 8)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved X-Centers</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="xcenter-solved-random"
              label="random"
              checked={solvedType === 'random'}
              onChange={() => handleTypeChange('x_centers_solved', 'random', setSolvedType)}
            />
            <Form.Check
              type="radio"
              id="xcenter-solved-range"
              label="range"
              checked={solvedType === 'range'}
              onChange={() => handleTypeChange('x_centers_solved', 'range', setSolvedType)}
            />
            {renderRangeControl('x_centers_solved', solvedType, 24)}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Parity</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="xcenter-parity-random"
              label="random"
              checked={formData.xcenter_parity === 'random'}
              onChange={() => handleParityChange('random')}
            />
            <Form.Check
              type="radio"
              id="xcenter-parity-yes"
              label="yes"
              checked={formData.xcenter_parity === 'yes'}
              onChange={() => handleParityChange('yes')}
            />
            <Form.Check
              type="radio"
              id="xcenter-parity-no"
              label="no"
              checked={formData.xcenter_parity === 'no'}
              onChange={() => handleParityChange('no')}
            />
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
            <div className="practice-letters p-3 border rounded bg-light">
              <div className="d-flex flex-wrap gap-2">
                {xCenterPositions.map((pos) => {

                  const letter = formData.letterScheme?.xCenters?.[pos] || '';
                  return letter && (
                    <Form.Check
                      key={pos}
                      type="checkbox"
                      id={`xcenter-practice-${pos}`}
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

export default XCenterSection;