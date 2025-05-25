import React from 'react';
import { Container } from 'react-bootstrap';
import './Footer.css';

function Footer() {
  return (
    <footer className="bg-primary text-white py-4 mt-4">
      <Container>
        <div className="d-flex justify-content-between align-items-center">
          <img 
            src="/rotohands_logo.png" 
            alt="Roto Hands" 
            className="footer-logo me-4"
          />
          <div className="contact-section me-4">
            <h5 className="mb-2">Feel free to contact me!</h5>
            <div className="d-flex flex-column gap-2">
              <a href="mailto:rotohands@gmail.com" className="text-white text-decoration-none">
                <i className="fas fa-envelope me-2"></i>
                rotohands@gmail.com
              </a>
              <a href="https://discord.gg/rotohands" target="_blank" rel="noopener noreferrer" className="text-white text-decoration-none">
                <i className="fab fa-discord me-2"></i>
                Discord
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