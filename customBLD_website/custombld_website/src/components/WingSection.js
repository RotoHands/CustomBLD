import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { wingBufferOptions, parityOptions } from '../constants/Constants';

const WingSection = ({ formData, handleChange, renderNumberSelect }) => {
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
    </>
  );
};

export default WingSection;