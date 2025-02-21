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

  const renderRangeControl = (fieldName, type, defaultMax = 18) => (
    <div className="d-flex align-items-center gap-2 ms-3">
      <Form.Control
        type="number"
        min="0"
        placeholder="Min"
        value={formData[`${fieldName}_min`] || 0}
        onChange={(e) => handleChange({
          target: {
            name: `${fieldName}_min`,
            value: Math.max(0, parseInt(e.target.value) || 0)
          }
        })}
        disabled={type !== 'range'}
        style={{ width: '70px', opacity: type === 'range' ? 1 : 0.6 }}
      />
      <span>-</span>
      <Form.Control
        type="number"
        min="0"
        placeholder="Max"
        value={formData[`${fieldName}_max`] || defaultMax}
        onChange={(e) => handleChange({
          target: {
            name: `${fieldName}_max`,
            value: Math.max(0, parseInt(e.target.value) || 0)
          }
        })}
        disabled={type !== 'range'}
        style={{ width: '70px', opacity: type === 'range' ? 1 : 0.6 }}
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
              onChange={() => handleTypeChange('edges_cycle_breaks', 'random', setCycleBreaksType)}
              className="me-3"
            />
            <Form.Check
              type="radio"
              id="cycle-breaks-range"
              label="range"
              checked={cycleBreaksType === 'range'}
              onChange={() => handleTypeChange('edges_cycle_breaks', 'range', setCycleBreaksType)}
            />
            {renderRangeControl('edges_cycle_breaks', cycleBreaksType, 10)}
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