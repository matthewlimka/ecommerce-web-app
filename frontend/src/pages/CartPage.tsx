import "../styles/CartPage.css";
import Navbar from "../components/Navbar";
import { useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import CartItemCard from "../components/CartItemCard";
import Cart from "../models/Cart";
import CartItem from "../models/CartItem";

const CartPage: React.FC = () => {
    const { jwt } = useAuth();
    const { cart, getCart, createCheckoutOrder } = useAPI();
    const navigate = useNavigate();

    useEffect(() => {
        if (jwt !== null) {
            getCart(jwt);
        }
    }, [jwt]);

    const handleCheckout = (event: any) => {
        event.preventDefault();
        createCheckoutOrder(jwt!);
        navigate('/checkout');
    }

    return (
        <div className="page">
            <Navbar />
            <div className="cart-page-content">
                <h1>My Cart</h1>
                <div className="cards-container">
                    <div className="cart-item-cards">
                        {cart?.cartItems.length !== 0 ? (
                            cart?.cartItems.map((cartItem) => {
                                return (
                                    <CartItemCard
                                        cartItemId={cartItem.cartItemId}
                                        quantity={cartItem.quantity}
                                        subtotal={cartItem.subtotal}
                                        cartId={cartItem.cartId}
                                        product={cartItem.product}
                                    />
                                );
                            })
                        ) : (
                            <div className="empty-cart-content">
                                <h2>Your cart is empty</h2>
                                <Link to="/home">Browse our products</Link>
                            </div>
                        )}
                    </div>
                </div>
                {cart?.cartItems.length !== 0 && (
                    <div className="cart-page-summary-section">
                        <h2>Summary</h2>
                        <h3>Total: ${cart?.totalAmount}</h3>
                        <button onClick={handleCheckout} className="cart-page-action-button">Checkout</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CartPage;
