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
          {renderNumberSelect('edge', 'length', 0, 18, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          {renderNumberSelect('edge_cycle', 'breaks', 0, 10, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Flipped Edges</Form.Label>
        <Col sm="9">
          {renderNumberSelect('edges', 'flipped', 0, 12, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved Edges</Form.Label>
        <Col sm="9">
          {renderNumberSelect('edges', 'solved', 0, 12, 1)}
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