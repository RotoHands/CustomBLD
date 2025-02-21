import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { cornerPositions } from './LetterScheme';
import { cornerBufferOptions } from '../constants/Constants';

const CornerSection = ({ formData, handleChange, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random');
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [cwTwistsType, setCwTwistsType] = useState('random');
  const [ccwTwistsType, setCcwTwistsType] = useState('random');
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.corner_length_min || 0}
                onChange={(e) => handleRangeChange('corner_length', 'min', e.target.value)}
                disabled={lengthType !== 'range'}
                style={{ width: '70px', opacity: lengthType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.corner_length_max || 16}
                onChange={(e) => handleRangeChange('corner_length', 'max', e.target.value)}
                disabled={lengthType !== 'range'}
                style={{ width: '70px', opacity: lengthType === 'range' ? 1 : 0.6 }}
              />
            </div>
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_cycle_breaks_min || 0}
                onChange={(e) => handleRangeChange('corners_cycle_breaks', 'min', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_cycle_breaks_max || 7}
                onChange={(e) => handleRangeChange('corners_cycle_breaks', 'max', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
            </div>
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_cw_twists_min || 0}
                onChange={(e) => handleRangeChange('corners_cw_twists', 'min', e.target.value)}
                disabled={cwTwistsType !== 'range'}
                style={{ width: '70px', opacity: cwTwistsType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_cw_twists_max || 7}
                onChange={(e) => handleRangeChange('corners_cw_twists', 'max', e.target.value)}
                disabled={cwTwistsType !== 'range'}
                style={{ width: '70px', opacity: cwTwistsType === 'range' ? 1 : 0.6 }}
              />
            </div>
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_ccw_twists_min || 0}
                onChange={(e) => handleRangeChange('corners_ccw_twists', 'min', e.target.value)}
                disabled={ccwTwistsType !== 'range'}
                style={{ width: '70px', opacity: ccwTwistsType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.corners_ccw_twists_max || 7}
                onChange={(e) => handleRangeChange('corners_ccw_twists', 'max', e.target.value)}
                disabled={ccwTwistsType !== 'range'}
                style={{ width: '70px', opacity: ccwTwistsType === 'range' ? 1 : 0.6 }}
              />
            </div>
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