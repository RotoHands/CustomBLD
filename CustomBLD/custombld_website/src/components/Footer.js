import React from 'react';
import { Container } from 'react-bootstrap';
import './Footer.css';

function Footer() {
  return (
    <footer className="bg-primary text-white py-4 mt-4">
      <Container>
        <div className="d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center">
            <a href="https://rotohands.com" target="_blank" rel="noopener noreferrer">
              <img 
                src="/rotohands_logo.png" 
                alt="Roto Hands" 
                className="footer-logo me-4"
              />
            </a>
            <a 
              href="https://www.paypal.com/donate?hosted_button_id=X9X9VZEAYK3DJ" 
              target="_blank" 
              rel="noopener noreferrer" 
              className="btn"
              style={{ 
                fontFamily: 'Rubik, sans-serif',
                fontWeight: '600',
                color: 'white',
                backgroundColor: 'var(--bs-primary)',
                border: '2px solid white',
                padding: '0.3rem 0.8rem',
                fontSize: '1.1rem'
              }}
            >
              Support :)
            </a>
          </div>
          <div className="contact-section me-4 ">
            <h5 className="mb-2">Feel free to contact me!</h5>
            <div className="d-flex flex-column gap-2">
              <a href="mailto:contact@custombld.net" className="text-white text-decoration-none">
                <i className="fas fa-envelope me-2"></i>
                contact@custombld.net
              </a>
              <a href="https://discord.gg/rotohands" target="_blank" rel="noopener noreferrer" className="text-white text-decoration-none">
                <i className="fab fa-discord me-2"></i>
                Rotohands Discord
              </a>
            </div>
          </div>
          <div className="social-grid">
            <div className="d-flex gap-3 mb-3">
              <a 
                href="#" 
                className="text-white"
              >
                <i className="fas fa-question-circle fa-2x"></i>
              </a>
              <a href="https://github.com/rotohands" target="_blank" rel="noopener noreferrer" className="text-white">
                <i className="fab fa-github fa-2x"></i>
              </a>
            </div>
            <div className="d-flex gap-3">
              <a href="https://instagram.com/roto_hands" target="_blank" rel="noopener noreferrer" className="text-white">
                <i className="fab fa-instagram fa-2x"></i>
              </a>
              <a href="https://youtube.com/channel/UCVGKCZFamCuYXiln9w3Cnxw" target="_blank" rel="noopener noreferrer" className="text-white">
                <i className="fab fa-youtube fa-2x"></i>
              </a>
            </div>
          </div>
        </div>
      </Container>
    </footer>
  );
}

export default Footer; 