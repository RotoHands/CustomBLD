import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { tCenterPositions } from './LetterScheme';
import { tCenterBufferOptions } from '../constants/Constants';


const TCenterSection = ({ formData, handleChange, handlePracticeLetterChange, renderNumberSelect }) => {
  const [lengthType, setLengthType] = useState('random');
  const [cycleBreaksType, setCycleBreaksType] = useState('random');
  const [solvedType, setSolvedType] = useState('random');
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);
  const [selectedLetters, setSelectedLetters] = useState(
    tCenterPositions.reduce((acc, pos) => ({ ...acc, [pos]: true }), {})
  );

  const handleBufferChange = (value) => {
    handleChange({
      target: {
        name: 'tcenter_buffer',
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
        name: 'tcenter_parity',
        value
      }
    });
  };

  const handleSelectAll = () => {
    const newSelected = { ...selectedLetters };
    tCenterPositions.forEach(pos => {
      newSelected[pos] = true;
      handlePracticeLetterChange('tCenters', pos, true);
    });
    setSelectedLetters(newSelected);
  };

  const handleRemoveAll = () => {
    const newSelected = { ...selectedLetters };
    tCenterPositions.forEach(pos => {
      newSelected[pos] = false;
      handlePracticeLetterChange('tCenters', pos, false);
    });
    setSelectedLetters(newSelected);
  };

  const handleSingleCheck = (pos, checked) => {
    setSelectedLetters(prev => ({
      ...prev,
      [pos]: checked
    }));
    handlePracticeLetterChange('tCenters', pos, checked);
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">T-Center Buffer</Form.Label>
        <Col sm="9">
          <div className="d-flex flex-wrap gap-3">
            {tCenterBufferOptions.map(buffer => (
              <Form.Check
                key={buffer}
                type="radio"
                id={`tcenter-buffer-${buffer}`}
                label={buffer}
                checked={formData.tcenter_buffer === buffer}
                onChange={() => handleBufferChange(buffer)}
                className="me-3"
              />
            ))}
          </div>
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">T-Center Length</Form.Label>
        <Col sm="9">
          {renderNumberSelect('t_centers', 'length', 0, 30, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          {renderNumberSelect('t_centers_cycle', 'breaks', 0, 8, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved T-Centers</Form.Label>
        <Col sm="9">
          {renderNumberSelect('t_centers', 'solved', 0, 24, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">T-Center Parity</Form.Label>
        <Col sm="9">
          <div className="d-flex align-items-center gap-3">
            <Form.Check
              type="radio"
              id="tcenter-parity-random"
              label="random"
              checked={formData.tcenter_parity === 'random'}
              onChange={() => handleParityChange('random')}
            />
            <Form.Check
              type="radio"
              id="tcenter-parity-yes"
              label="yes"
              checked={formData.tcenter_parity === 'yes'}
              onChange={() => handleParityChange('yes')}
            />
            <Form.Check
              type="radio"
              id="tcenter-parity-no"
              label="no"
              checked={formData.tcenter_parity === 'no'}
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
                {tCenterPositions.map((pos) => {
                  const letter = formData.letterScheme?.tCenters?.[pos] || '';
                  return letter && (
                    <Form.Check
                      key={pos}
                      type="checkbox"
                      id={`tcenter-practice-${pos}`}
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

export default TCenterSection;