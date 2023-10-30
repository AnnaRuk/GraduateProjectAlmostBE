import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar: React.FC = () => {
  return (
    <nav className="navbar">
      <div className="left">
        <Link to="/logo">Logo</Link>
      </div>
      <div className="center">
        <ul>
          <li><Link to="/">All Kitas</Link></li>
          <li><Link to="/about-voucher">About Voucher</Link></li>
        </ul>
      </div>
      <div className="right">
        <ul>
          <li><Link to="/log-in">Login</Link></li>
          <li><Link to="/my-account">My Account</Link></li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
