import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { cornerPositions } from './LetterScheme';
import { cornerBufferOptions } from '../constants/Constants';

const CornerSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
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

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'corner_buffer',
        value: value
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
      
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Length</Form.Label>
        <Col sm="9">
          {renderNumberSelect('corner', 'length', 0, 16, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          {renderNumberSelect('corners_cycle', 'breaks', 0, 7, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Clockwise Twists</Form.Label>
        <Col sm="9">
          {renderNumberSelect('corners_cw', 'twists', 0, 7, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Counter-Clockwise Twists</Form.Label>
        <Col sm="9">
          {renderNumberSelect('corners_ccw', 'twists', 0, 7, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved Corners</Form.Label>
        <Col sm="9">
          {renderNumberSelect('corners', 'solved', 0, 8, 1)}
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
            <div className="practice-letters p-3 border rounded bg-light mt-2">
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