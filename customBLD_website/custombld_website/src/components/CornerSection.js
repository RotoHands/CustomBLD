import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { cornerBufferOptions, parityOptions } from '../constants/Constants';
import { cornerPositions } from './LetterScheme';

const CornerSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="corner_buffer" value={formData.corner_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {cornerBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('corner_length', 0, 20, 'Corner Length')}
      {renderNumberSelect('corners_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('twist_clockwise', 0, 8, 'Clockwise Twists')}
      {renderNumberSelect('twist_counterclockwise', 0, 8, 'Counter-Clockwise Twists')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Corner Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="corner_parity" value={formData.corner_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>

      <Form.Group className="mb-3 mt-4">
        <Form.Label>Letters to Practice</Form.Label>
        <div className="practice-letters p-3 border rounded bg-light">
          <div className="d-flex flex-wrap gap-2">
            {cornerPositions.map((pos, index) => {
              const letter = formData.letterScheme?.base?.[pos] || String.fromCharCode(65 + index);
              return (
                <Form.Check
                  key={pos}
                  type="checkbox"
                  id={`corner-practice-${pos}`}
                  label={`${letter} (${pos})`}
                  defaultChecked={true}
                  onChange={(e) => handlePracticeLetterChange('corners', pos, e.target.checked)}
                  className="me-3"
                />
              );
            })}
          </div>
        </div>
      </Form.Group>
    </>
  );
};

export default CornerSection;