import "../styles/CartPage.css";
import Navbar from "../components/Navbar";
import { useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import CartItemCard from "../components/CartItemCard";
import Cart from "../models/Cart";
import CartItem from "../models/CartItem";

const CartPage: React.FC = () => {
    const { jwt } = useAuth();
    const { cart, getCart } = useAPI();

    useEffect(() => {
        if (jwt !== null) {
            getCart(jwt);
        }
    }, [jwt]);

    return (
        <div className="page">
            <Navbar />
            <div className="cart-page-content">
                <h1>My Cart</h1>
                <div className="cards-container">
                    <div className="cart-item-cards">
                        {cart?.cartItems.length !== 0 ? (
                            cart?.cartItems.map((cartItem, index) => {
                                return (
                                    <CartItemCard
                                        cartItemId={cartItem.cartItemId}
                                        quantity={cartItem.quantity}
                                        cartId={cartItem.cartId}
                                        product={cartItem.product}
                                    />
                                );
                            })
                        ) : (
                            <h2>Your cart is empty</h2>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CartPage;
