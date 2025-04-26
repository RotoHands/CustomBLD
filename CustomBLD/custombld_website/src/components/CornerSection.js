import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { cornerPositions } from './LetterScheme';
import { cornerBufferOptions } from '../constants/Constants';

const CornerSection = ({ formData, handleChange, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random');
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [cwTwistsType, setCwTwistsType] = useState('random');
  const [ccwTwistsType, setCcwTwistsType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);
  const [selectedLetters, setSelectedLetters] = useState(
    cornerPositions.reduce((acc, pos) => ({ ...acc, [pos]: true }), {})
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

  // Generic handler for range changes
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
        name: 'corner_parity',
        value
      }
    });
  };

  const handleSelectAll = () => {
    const newSelected = { ...selectedLetters };
    cornerPositions.forEach(pos => {
      newSelected[pos] = true;
      handlePracticeLetterChange('corners', pos, true);
    });
    setSelectedLetters(newSelected);
  };

  const handleRemoveAll = () => {
    const newSelected = { ...selectedLetters };
    cornerPositions.forEach(pos => {
      newSelected[pos] = false;
      handlePracticeLetterChange('corners', pos, false);
    });
    setSelectedLetters(newSelected);
  };

  const handleSingleCheck = (pos, checked) => {
    setSelectedLetters(prev => ({
      ...prev,
      [pos]: checked
    }));
    handlePracticeLetterChange('corners', pos, checked);
  };

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'corner_buffer',
        value: value
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

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Buffer</Form.Label>
        <Col sm="9">
          <div className="d-flex flex-wrap gap-3">
            {cornerBufferOptions.map(buffer => (
              <Form.Check
                key={buffer}
                type="radio"
                id={`corner-buffer-${buffer}`}
                label={buffer}
                checked={formData.corner_buffer === buffer}
                onChange={() => handleBufferChange(buffer)}
                className="me-3"
              />
            ))}
          </div>
        </Col>
      </Form.Group>
      
      {/* Corner Length */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Length</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="corner-length-random"
              label="random"
              checked={lengthType === 'random'}
              onChange={() => handleTypeChange('corner_length', 'random', setLengthType)}
            />
            <Form.Check
              type="radio"
              id="corner-length-range"
              label="range"
              checked={lengthType === 'range'}
              onChange={() => handleTypeChange('corner_length', 'range', setLengthType)}
            />
            {renderRangeControl('corner_length', lengthType, 16)}
          </div>
        </Col>
      </Form.Group>

      {/* Cycle Breaks */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="cycle-breaks-random"
              label="random"
              checked={cycleBreaksType === 'random'}
              onChange={() => handleTypeChange('corners_cycle_breaks', 'random', setCycleBreaksType)}
            />
            <Form.Check
              type="radio"
              id="cycle-breaks-range"
              label="range"
              checked={cycleBreaksType === 'range'}
              onChange={() => handleTypeChange('corners_cycle_breaks', 'range', setCycleBreaksType)}
            />
            {renderRangeControl('corners_cycle_breaks', cycleBreaksType, 7)}
          </div>
        </Col>
      </Form.Group>

      {/* Clockwise Twists */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Clockwise Twists</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="cw-twists-random"
              label="random"
              checked={cwTwistsType === 'random'}
              onChange={() => handleTypeChange('corners_cw_twists', 'random', setCwTwistsType)}
            />
            <Form.Check
              type="radio"
              id="cw-twists-range"
              label="range"
              checked={cwTwistsType === 'range'}
              onChange={() => handleTypeChange('corners_cw_twists', 'range', setCwTwistsType)}
            />
            {renderRangeControl('corners_cw_twists', cwTwistsType, 7)}
          </div>
        </Col>
      </Form.Group>

      {/* Counter-Clockwise Twists */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Counter-Clockwise Twists</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="ccw-twists-random"
              label="random"
              checked={ccwTwistsType === 'random'}
              onChange={() => handleTypeChange('corners_ccw_twists', 'random', setCcwTwistsType)}
            />
            <Form.Check
              type="radio"
              id="ccw-twists-range"
              label="range"
              checked={ccwTwistsType === 'range'}
              onChange={() => handleTypeChange('corners_ccw_twists', 'range', setCcwTwistsType)}
            />
            {renderRangeControl('corners_ccw_twists', ccwTwistsType, 7)}
          </div>
        </Col>
      </Form.Group>

      {/* Solved Corners */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved Corners</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="corners-solved-random"
              label="random"
              checked={solvedType === 'random'}
              onChange={() => handleTypeChange('corners_solved', 'random', setSolvedType)}
            />
            <Form.Check
              type="radio"
              id="corners-solved-range"
              label="range"
              checked={solvedType === 'range'}
              onChange={() => handleTypeChange('corners_solved', 'range', setSolvedType)}
            />
            {renderRangeControl('corners_solved', solvedType, 8)}
          </div>
        </Col>
      </Form.Group>

      {/* Corner Parity */}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Parity</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="corner-parity-random"
              label="random"
              checked={formData.corner_parity === 'random'}
              onChange={() => handleParityChange('random')}
            />
            <Form.Check
              type="radio"
              id="corner-parity-yes"
              label="yes"
              checked={formData.corner_parity === 'yes'}
              onChange={() => handleParityChange('yes')}
            />
            <Form.Check
              type="radio"
              id="corner-parity-no"
              label="no"
              checked={formData.corner_parity === 'no'}
              onChange={() => handleParityChange('no')}
            />
          </div>
        </Col>
      </Form.Group>

      {/* Letters to Practice - Collapsible Section */}
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
                {cornerPositions.map((pos) => {
                  const letter = formData.letterScheme?.corners?.[pos] || '';
                  return letter && (
                    <Form.Check
                      key={pos}
                      type="checkbox"
                      id={`corner-practice-${pos}`}
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

export default CornerSection;