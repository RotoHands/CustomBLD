import React, { useState } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { xCenterBufferOptions, parityOptions } from '../constants/Constants';
import { xCenterPositions } from './LetterScheme';

const XCenterSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  const [showPracticeLetters, setShowPracticeLetters] = useState(false);

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="xcenter_buffer" value={formData.xcenter_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {xCenterBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('xcenter_length', 0, 30, 'X-Center Length')}
      {renderNumberSelect('xcenters_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('xcenters_solved', 0, 24, 'Solved X-Centers')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">X-Center Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="xcenter_parity" value={formData.xcenter_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>

      <Form.Group className="mb-3 mt-4">
        <div 
          className="collapsible-header d-flex align-items-center"
          onClick={() => setShowPracticeLetters(!showPracticeLetters)}
          style={{ color: '#6c757d' }}  // Grey color when collapsed
        >
          <i className={`fa-solid fa-caret-right`} style={{ color: '#6c757d' }}></i>
          <Form.Label className="mb-0 ms-2">Letters to Practice</Form.Label>
        </div>
      </Form.Group>
      {showPracticeLetters && (
        <Form.Group className="mb-3 mt-4">
          <Form.Label>Letters to Practice</Form.Label>
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
                    defaultChecked={true}
                    onChange={(e) => handlePracticeLetterChange('xCenters', pos, e.target.checked)}
                    className="me-3"
                  />
                );
              })}
            </div>
          </div>
        </Form.Group>
      )}
    </>
  );
};

export default XCenterSection;