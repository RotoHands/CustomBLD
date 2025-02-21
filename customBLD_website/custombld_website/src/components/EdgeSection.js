import React, { useState } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { edgeBufferOptions } from '../constants/Constants';
import { edgePositions } from './LetterScheme';

const EdgeSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  const [lengthType, setLengthType] = useState('random'); // 'random' or 'range'
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [flippedType, setFlippedType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);

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

  // Generic handler for range values
  const handleRangeChange = (field, type, value) => {
    handleChange({
      target: {
        name: `${field}_${type}`,
        value: Math.max(0, parseInt(value) || 0)
      }
    });
  };

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'edge_buffer',
        value: value
      }
    });
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
                defaultChecked={buffer === 'UF'}
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
            
            {lengthType === 'range' && (
              <div className="d-flex align-items-center gap-2 ms-3">
                <Form.Control
                  type="number"
                  min="0"
                  placeholder="Min"
                  value={formData.edge_length_min || 0}
                  onChange={(e) => handleRangeChange('edge_length', 'min', e.target.value)}
                  style={{ width: '70px' }}
                />
                <span>-</span>
                <Form.Control
                  type="number"
                  min="0"
                  placeholder="Max"
                  value={formData.edge_length_max || 18}
                  onChange={(e) => handleRangeChange('edge_length', 'max', e.target.value)}
                  style={{ width: '70px' }}
                />
              </div>
            )}
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
            {cycleBreaksType === 'range' && (
              <div className="d-flex align-items-center gap-2 ms-3">
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_cycle_breaks_min || 0}
                  onChange={(e) => handleRangeChange('edges_cycle_breaks', 'min', e.target.value)}
                  style={{ width: '70px' }}
                />
                <span>-</span>
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_cycle_breaks_max || 10}
                  onChange={(e) => handleRangeChange('edges_cycle_breaks', 'max', e.target.value)}
                  style={{ width: '70px' }}
                />
              </div>
            )}
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
            {flippedType === 'range' && (
              <div className="d-flex align-items-center gap-2 ms-3">
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_flipped_min || 0}
                  onChange={(e) => handleRangeChange('edges_flipped', 'min', e.target.value)}
                  style={{ width: '70px' }}
                />
                <span>-</span>
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_flipped_max || 12}
                  onChange={(e) => handleRangeChange('edges_flipped', 'max', e.target.value)}
                  style={{ width: '70px' }}
                />
              </div>
            )}
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
            {solvedType === 'range' && (
              <div className="d-flex align-items-center gap-2 ms-3">
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_solved_min || 0}
                  onChange={(e) => handleRangeChange('edges_solved', 'min', e.target.value)}
                  style={{ width: '70px' }}
                />
                <span>-</span>
                <Form.Control
                  type="number"
                  min="0"
                  value={formData.edges_solved_max || 12}
                  onChange={(e) => handleRangeChange('edges_solved', 'max', e.target.value)}
                  style={{ width: '70px' }}
                />
              </div>
            )}
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
                    defaultChecked={true}
                    onChange={(e) => handlePracticeLetterChange('edges', pos, e.target.checked)}
                    className="me-3"
                  />
                );
              })}
            </div>
          </div>
        )}
      </Form.Group>
    </>
  );
};

export default EdgeSection;