import React, { useState, useEffect } from 'react';
import { Form, Button, Card, Accordion } from 'react-bootstrap';
import EdgeSection from './components/EdgeSection';
import CornerSection from './components/CornerSection';
import WingSection from './components/WingSection';
import XCenterSection from './components/XCenterSection';
import TCenterSection from './components/TCenterSection';
import LetterSchemeSection from './components/LetterSchemeSection';
import { scrambleTypes } from './constants/Constants';
import { defaultLetterScheme } from './components/LetterScheme';

const QueryForm = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    scramble_type: '',
    edge_buffer: '',
    edge_length: 'random',
    edges_cycle_breaks: 'random',
    edges_flipped: 'random',
    edges_solved: 'random',
    first_edges: '',
    corner_buffer: '',
    corner_length: 'random',
    corners_cycle_breaks: 'random',
    twist_clockwise: 'random',
    twist_counterclockwise: 'random',
    corner_parity: 'random',
    first_corners: '',
    wing_buffer: '',
    wings_length: 'random',
    wings_cycle_breaks: 'random',
    wings_solved: 'random',
    wing_parity: 'random',
    first_wings: '',
    xcenter_buffer: '',
    xcenter_length: 'random',
    xcenters_cycle_breaks: 'random',
    xcenters_solved: 'random',
    xcenter_parity: 'random',
    first_xcenters: '',
    tcenter_buffer: '',
    tcenter_length: 'random',
    tcenters_cycle_breaks: 'random',
    tcenters_solved: 'random',
    tcenter_parity: 'random',
    first_tcenters: '',
    letterScheme: defaultLetterScheme
  });
  const [hasChanges, setHasChanges] = useState(false);

  useEffect(() => {
    const savedForm = localStorage.getItem('scrambleForm');
    if (savedForm) {
      setFormData(JSON.parse(savedForm));
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setHasChanges(true);
  };

  const handleLetterChange = (piece, position, value) => {
    setFormData(prev => ({
      ...prev,
      letterScheme: {
        ...prev.letterScheme,
        [piece]: {
          ...prev.letterScheme[piece],
          [position]: value
        }
      }
    }));
  };

  const saveSettings = () => {
    try {
      localStorage.setItem('scrambleForm', JSON.stringify(formData));
      setHasChanges(false);
      alert('Settings saved successfully!');
    } catch (error) {
      alert('Error saving settings: ' + error.message);
    }
  };

  const resetSettings = () => {
    if (window.confirm('Are you sure you want to reset all settings to default?')) {
      localStorage.removeItem('scrambleForm');
      setFormData({
        scramble_type: '',
        edge_buffer: '',
        edge_length: 'random',
        edges_cycle_breaks: 'random',
        edges_flipped: 'random',
        edges_solved: 'random',
        first_edges: '',
        corner_buffer: '',
        corner_length: 'random',
        corners_cycle_breaks: 'random',
        twist_clockwise: 'random',
        twist_counterclockwise: 'random',
        corner_parity: 'random',
        first_corners: '',
        wing_buffer: '',
        wings_length: 'random',
        wings_cycle_breaks: 'random',
        wings_solved: 'random',
        wing_parity: 'random',
        first_wings: '',
        xcenter_buffer: '',
        xcenter_length: 'random',
        xcenters_cycle_breaks: 'random',
        xcenters_solved: 'random',
        xcenter_parity: 'random',
        first_xcenters: '',
        tcenter_buffer: '',
        tcenter_length: 'random',
        tcenters_cycle_breaks: 'random',
        tcenters_solved: 'random',
        tcenter_parity: 'random',
        first_tcenters: '',
        letterScheme: defaultLetterScheme
      });
      setHasChanges(false);
    }
  };

  const renderNumberSelect = (name, min, max, label) => (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Select name={name} value={formData[name]} onChange={handleChange}>
        <option value="random">random</option>
        {[...Array(max - min + 1)].map((_, i) => (
          <option key={i + min} value={i + min}>{i + min}</option>
        ))}
      </Form.Select>
    </Form.Group>
  );

  return (
    <Card className="p-4 my-4 shadow-sm bg-light">
      <Card.Body>
        <h2 className="text-primary text-center mb-4">BLD Scramble Generator</h2>
        
        <Form onSubmit={(e) => {
          e.preventDefault();
          onSubmit(formData);
        }}>
          <Accordion defaultActiveKey={['0','1','2','3','4','5','6']} alwaysOpen>
            <Accordion.Item eventKey="0">
              <Accordion.Header>Scramble Type</Accordion.Header>
              <Accordion.Body>
                <Form.Group className="mb-3">
                  <Form.Select 
                    name="scramble_type"
                    value={formData.scramble_type}
                    onChange={handleChange}
                  >
                    <option value="">Select Scramble Type</option>
                    {scrambleTypes.map(type => (
                      <option key={type} value={type}>{type}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="1">
              <Accordion.Header>Edges</Accordion.Header>
              <Accordion.Body>
                <EdgeSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="2">
              <Accordion.Header>Corners</Accordion.Header>
              <Accordion.Body>
                <CornerSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="3">
              <Accordion.Header>Wings</Accordion.Header>
              <Accordion.Body>
                <WingSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="4">
              <Accordion.Header>X-Centers</Accordion.Header>
              <Accordion.Body>
                <XCenterSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="5">
              <Accordion.Header>T-Centers</Accordion.Header>
              <Accordion.Body>
                <TCenterSection 
                  formData={formData} 
                  handleChange={handleChange}
                  renderNumberSelect={renderNumberSelect}
                />
              </Accordion.Body>
            </Accordion.Item>

            <Accordion.Item eventKey="6">
              <Accordion.Header>Letter Scheme</Accordion.Header>
              <Accordion.Body>
                <LetterSchemeSection 
                  formData={formData}
                  handleLetterChange={handleLetterChange}
                />
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>

          <div className="d-flex justify-content-between mt-4">
            <Button 
              variant="primary" 
              onClick={saveSettings}
              disabled={!hasChanges}
              type="button"
            >
              Save Settings
            </Button>
            <Button 
              variant="outline-danger" 
              onClick={resetSettings}
              type="button"
            >
              Reset All
            </Button>
          </div>

          {hasChanges && (
            <div className="alert alert-warning mt-3">
              You have unsaved changes
            </div>
          )}

          <div className="d-grid gap-2 mt-4">
            <Button variant="primary" size="lg" type="submit">
              Generate Scramble
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default QueryForm;