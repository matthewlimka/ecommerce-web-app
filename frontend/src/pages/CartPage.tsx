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
                <h1 className="cart-page-header">My Cart</h1>
                {cart?.cartItems.length !== 0 ? (
                    <div className="cart-page-items-section">
                        <h2 className="cart-page-items-section-header">Products In Cart</h2>
                        {cart?.cartItems.map((cartItem) => (
                            <CartItemCard
                                cartItemId={cartItem.cartItemId}
                                quantity={cartItem.quantity}
                                subtotal={cartItem.subtotal}
                                cartId={cartItem.cartId}
                                product={cartItem.product}
                            />
                        ))}
                    </div>
                ) : (
                    <div className="cart-page-empty-cart">
                        <h1 className="cart-page-empty-cart-header">Your cart is empty</h1>
                        <Link to="/home">Browse our products</Link>
                    </div>
                )}
                {cart?.cartItems.length !== 0 && (
                    <div className="cart-page-summary-section">
                        <h2 className="cart-page-summary-header">Summary</h2>
                        <h3 className="cart-page-summary-total">Total ({cart?.cartItems.length} item{cart?.cartItems.length === 1 ? '' : 's'}): ${cart?.totalAmount.toFixed(2)}</h3>
                        <button onClick={handleCheckout} className="cart-page-action-button">Checkout</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CartPage;