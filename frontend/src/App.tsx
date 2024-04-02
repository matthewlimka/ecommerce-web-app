import React from 'react';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { APIProvider } from './contexts/APIContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import OAuthCallbackPage from './pages/OAuthCallbackPage';
import ProductPage from './pages/ProductPage';
import ResultPage from './pages/ResultPage';
import CartPage from './pages/CartPage';
import AccountContainer from './components/AccountContainer';
import CheckoutPage from './pages/CheckoutPage';
import CheckoutSuccessPage from './pages/CheckoutSuccessPage';

function App() {
  return (
    <AuthProvider>
      <APIProvider>
        <div className="App">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/home" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<RegistrationPage />} />
            <Route path="/oauth/callback" element={<OAuthCallbackPage />} />
            <Route path="/product/:productId" element={<ProductPage />} />
            <Route path="/result" element={<ResultPage />} />
            <Route path="/cart/:userId" element={<CartPage />} />
            <Route path="/account/profile/:userId" element={<AccountContainer />} />
            <Route path="/account/payment/:userId" element={<AccountContainer />} />
            <Route path="/account/address/:userId" element={<AccountContainer />} />
            <Route path="/account/orders/:userId" element={<AccountContainer />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/checkout/success" element={<CheckoutSuccessPage />} />
          </Routes>
        </div>
      </APIProvider>
    </AuthProvider>
  );
}

export default App;