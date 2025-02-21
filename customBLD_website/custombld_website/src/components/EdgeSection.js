import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { edgeBufferOptions } from '../constants/Constants';
import { basePositions, edgePositions } from './LetterScheme';

const EdgeSection = ({ formData, handleChange, renderNumberSelect, handlePracticeLetterChange, setHasChanges }) => {
  const handleLocalChange = (piece, pos, value) => {
    handlePracticeLetterChange(piece, pos, value);
    setHasChanges(true);  // Track changes
  };

  return (
    <>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm="3">Edge Buffer</Form.Label>
        <Col sm="9">
          <Form.Select name="edge_buffer" value={formData.edge_buffer} onChange={handleChange}>
            <option value="">Select Buffer</option>
            {edgeBufferOptions.map(buffer => <option key={buffer}>{buffer}</option>)}
          </Form.Select>
        </Col>
      </Form.Group>
      {renderNumberSelect('edge_length', 0, 20, 'Edge Length')}
      {renderNumberSelect('edges_cycle_breaks', 0, 10, 'Cycle Breaks')}
      {renderNumberSelect('edges_flipped', 0, 12, 'Flipped Edges')}
      {renderNumberSelect('edges_solved', 0, 12, 'Solved Edges')}

      <Form.Group className="mb-3 mt-4">
        <Form.Label>Letters to Practice</Form.Label>
        <div className="practice-letters p-3 border rounded bg-light">
          <div className="d-flex flex-wrap gap-2">
            {edgePositions.map((pos, index) => {
              // Get corresponding base position
              const basePos = basePositions[index];
              const letter = formData.letterScheme?.base?.[basePos] || '';
              
              // Only show checkbox if there's a letter defined
              return letter && (
                <Form.Check
                  key={pos}
                  type="checkbox"
                  id={`edge-practice-${pos}`}
                  label={`${letter} (${pos})`}
                  defaultChecked={true}
                  onChange={(e) => handleLocalChange('edges', pos, e.target.checked)}
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

export default EdgeSection;