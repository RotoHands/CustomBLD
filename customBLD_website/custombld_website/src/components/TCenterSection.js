import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { tCenterBufferOptions, parityOptions } from '../constants/Constants';
import { tCenterPositions } from './LetterScheme';

const TCenterSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange }) => {
  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">T-Center Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="tcenter_buffer" value={formData.tcenter_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {tCenterBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('tcenter_length', 0, 30, 'T-Center Length')}
      {renderNumberSelect('tcenters_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('tcenters_solved', 0, 24, 'Solved T-Centers')}
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">T-Center Parity</Form.Label>
        <Col sm="9">
          <Form.Select name="tcenter_parity" value={formData.tcenter_parity} onChange={handleChange}>
            {parityOptions.map(option => <option key={option}>{option}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>

      <Form.Group className="mb-3 mt-4">
        <Form.Label>Letters to Practice</Form.Label>
        <div className="practice-letters p-3 border rounded bg-light">
          <div className="d-flex flex-wrap gap-2">
            {tCenterPositions.map((pos, index) => {
              const letter = formData.letterScheme?.base?.[pos] || String.fromCharCode(65 + index);
              return (
                <Form.Check
                  key={pos}
                  type="checkbox"
                  id={`tcenter-practice-${pos}`}
                  label={`${letter} (${pos})`}
                  defaultChecked={true}
                  onChange={(e) => handlePracticeLetterChange('tCenters', pos, e.target.checked)}
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

export default TCenterSection;