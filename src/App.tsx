import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Navbar from './components/Navbar';
import AllKitas from './components/Allkitas'; // Denumire schimbată
import AboutVoucher from './components/Aboutvoucher'; // Denumire schimbată
import Logo from './components/Logo';
import LogIn from './components/Login'; // Denumire schimbată
import MyAccount from './components/MyAccount'; // Denumire schimbată

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/all-kitas" element={<AllKitas />} /> {/* Denumire schimbată */}
        <Route path="/about-voucher" element={<AboutVoucher />} /> {/* Denumire schimbată */}
        <Route path="/logo" element={<Logo />} />
        <Route path="/log-in" element={<LogIn />} /> {/* Denumire schimbată */}
        <Route path="/my-account" element={<MyAccount />} /> {/* Denumire schimbată */}
      </Routes>
    </Router>
  );
}

export default App;
