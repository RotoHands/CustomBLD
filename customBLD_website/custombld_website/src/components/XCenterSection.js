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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_length_min || 0}
                onChange={(e) => handleRangeChange('x_centers_length', 'min', e.target.value)}
                disabled={lengthType !== 'range'}
                style={{ width: '70px', opacity: lengthType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_length_max || 30}
                onChange={(e) => handleRangeChange('x_centers_length', 'max', e.target.value)}
                disabled={lengthType !== 'range'}
                style={{ width: '70px', opacity: lengthType === 'range' ? 1 : 0.6 }}
              />
            </div>
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_cycle_breaks_min || 0}
                onChange={(e) => handleRangeChange('x_centers_cycle_breaks', 'min', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_cycle_breaks_max || 8}
                onChange={(e) => handleRangeChange('x_centers_cycle_breaks', 'max', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
            </div>
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
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_solved_min || 0}
                onChange={(e) => handleRangeChange('x_centers_solved', 'min', e.target.value)}
                disabled={solvedType !== 'range'}
                style={{ width: '70px', opacity: solvedType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.x_centers_solved_max || 24}
                onChange={(e) => handleRangeChange('x_centers_solved', 'max', e.target.value)}
                disabled={solvedType !== 'range'}
                style={{ width: '70px', opacity: solvedType === 'range' ? 1 : 0.6 }}
              />
            </div>
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