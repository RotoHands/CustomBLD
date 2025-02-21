import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { wingPositions } from './LetterScheme';
import { wingBufferOptions } from '../constants/Constants';


const WingSection = ({ formData, handleChange, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random');
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);
  const [selectedLetters, setSelectedLetters] = useState(
    wingPositions.reduce((acc, pos) => ({ ...acc, [pos]: true }), {})
  );
  const [wingParityType, setWingParityType] = useState('random');

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'wing_buffer',
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

  const handleSelectAll = () => {
    const newSelected = { ...selectedLetters };
    wingPositions.forEach(pos => {
      newSelected[pos] = true;
      handlePracticeLetterChange('wings', pos, true);
    });
    setSelectedLetters(newSelected);
  };

  const handleRemoveAll = () => {
    const newSelected = { ...selectedLetters };
    wingPositions.forEach(pos => {
      newSelected[pos] = false;
      handlePracticeLetterChange('wings', pos, false);
    });
    setSelectedLetters(newSelected);
  };

  const handleSingleCheck = (pos, checked) => {
    setSelectedLetters(prev => ({
      ...prev,
      [pos]: checked
    }));
    handlePracticeLetterChange('wings', pos, checked);
  };

  const handleWingParityChange = (value) => {
    handleChange({
      target: {
        name: 'wing_parity',
        value
      }
    });
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Wing Buffer</Form.Label>
        <Col sm="9">
          <div className="d-flex flex-wrap gap-3">
            {wingBufferOptions.map(buffer => (
              <Form.Check
                key={buffer}
                type="radio"
                id={`wing-buffer-${buffer}`}
                label={buffer}
                checked={formData.wing_buffer === buffer}
                onChange={() => handleBufferChange(buffer)}
                className="me-3"
              />
            ))}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Wing Length</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="wing-length-random"
              label="random"
              checked={lengthType === 'random'}
              onChange={() => handleTypeChange('wings_length', 'random', setLengthType)}
            />
            <Form.Check
              type="radio"
              id="wing-length-range"
              label="range"
              checked={lengthType === 'range'}
              onChange={() => handleTypeChange('wings_length', 'range', setLengthType)}
            />
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_length_min || 0}
                onChange={(e) => handleRangeChange('wings_length', 'min', e.target.value)}
                disabled={lengthType !== 'range'}
                style={{ width: '70px', opacity: lengthType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_length_max || 40}
                onChange={(e) => handleRangeChange('wings_length', 'max', e.target.value)}
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
              id="wing-cycle-breaks-random"
              label="random"
              checked={cycleBreaksType === 'random'}
              onChange={() => handleTypeChange('wings_cycle_breaks', 'random', setCycleBreaksType)}
            />
            <Form.Check
              type="radio"
              id="wing-cycle-breaks-range"
              label="range"
              checked={cycleBreaksType === 'range'}
              onChange={() => handleTypeChange('wings_cycle_breaks', 'range', setCycleBreaksType)}
            />
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_cycle_breaks_min || 0}
                onChange={(e) => handleRangeChange('wings_cycle_breaks', 'min', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_cycle_breaks_max || 10}
                onChange={(e) => handleRangeChange('wings_cycle_breaks', 'max', e.target.value)}
                disabled={cycleBreaksType !== 'range'}
                style={{ width: '70px', opacity: cycleBreaksType === 'range' ? 1 : 0.6 }}
              />
            </div>
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved Wings</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="wing-solved-random"
              label="random"
              checked={solvedType === 'random'}
              onChange={() => handleTypeChange('wings_solved', 'random', setSolvedType)}
            />
            <Form.Check
              type="radio"
              id="wing-solved-range"
              label="range"
              checked={solvedType === 'range'}
              onChange={() => handleTypeChange('wings_solved', 'range', setSolvedType)}
            />
            <div className="d-flex align-items-center gap-2 ms-3">
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_solved_min || 0}
                onChange={(e) => handleRangeChange('wings_solved', 'min', e.target.value)}
                disabled={solvedType !== 'range'}
                style={{ width: '70px', opacity: solvedType === 'range' ? 1 : 0.6 }}
              />
              <span>-</span>
              <Form.Control
                type="number"
                min="0"
                value={formData.wings_solved_max || 24}
                onChange={(e) => handleRangeChange('wings_solved', 'max', e.target.value)}
                disabled={solvedType !== 'range'}
                style={{ width: '70px', opacity: solvedType === 'range' ? 1 : 0.6 }}
              />
            </div>
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Wing Parity</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="wing-parity-random"
              label="random"
              checked={formData.wing_parity === 'random'}
              onChange={() => handleWingParityChange('random')}
            />
            <Form.Check
              type="radio"
              id="wing-parity-yes"
              label="yes"
              checked={formData.wing_parity === 'yes'}
              onChange={() => handleWingParityChange('yes')}
            />
            <Form.Check
              type="radio"
              id="wing-parity-no"
              label="no"
              checked={formData.wing_parity === 'no'}
              onChange={() => handleWingParityChange('no')}
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
                {wingPositions.map((pos) => {
                  const letter = formData.letterScheme?.wings?.[pos] || '';
                  return letter && (
                    <Form.Check
                      key={pos}
                      type="checkbox"
                      id={`wing-practice-${pos}`}
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

export default WingSection;