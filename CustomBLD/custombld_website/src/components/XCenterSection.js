import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { xCenterBufferOptions } from '../constants/Constants';
import { xCenterPositions } from './LetterScheme';

const XCenterSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
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
            ))}
          </div>
        </Col>
      </Form.Group>
      
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Centers Length</Form.Label>
        <Col sm="9">
          {renderNumberSelect('x_centers', 'length', 0, 24, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Cycle Breaks</Form.Label>
        <Col sm="9">
          {renderNumberSelect('x_centers_cycle', 'breaks', 0, 8, 1)}
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Solved X-Centers</Form.Label>
        <Col sm="9">
          {renderNumberSelect('x_centers', 'solved', 0, 24, 1)}
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