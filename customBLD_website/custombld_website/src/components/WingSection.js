import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { wingBufferOptions, parityOptions } from '../constants/Constants';
import { wingPositions } from './LetterScheme';

const WingSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Wing Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="wing_buffer" value={formData.wing_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {wingBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('wings_length', 0, 40, 'Wing Length')}
      {renderNumberSelect('wings_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('wings_solved', 0, 24, 'Solved Wings')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Wing Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="wing_parity" value={formData.wing_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>

      <Form.Group className="mb-3 mt-4">
        <Form.Label>Letters to Practice</Form.Label>
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
                  defaultChecked={true}
                  onChange={(e) => handlePracticeLetterChange('wings', pos, e.target.checked)}
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

export default WingSection;