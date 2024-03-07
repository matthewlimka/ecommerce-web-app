import "../styles/CartItemCard.css";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import CartItem from "../models/CartItem";

const CartItemCard = ({ cartItemId, quantity, subtotal, cartId, product }: CartItem) => {
    
    const { jwt } = useAuth();
    const { updateCartItem } = useAPI();

    const decreaseQuantity = (event: any) => {
        event.preventDefault();
        if (quantity > 1) {
            updateCartItem(jwt!, cartItemId!, quantity - 1);
        }
    }

    const increaseQuantity = (event: any) => {
        event.preventDefault();
        updateCartItem(jwt!, cartItemId!, quantity + 1);
    }

    return (
        <div className="cart-item-card">
            <div className="cart-item-image">
                <img src="https://via.placeholder.com/150" alt="CartItem" />
            </div>
            <h3>{product.productName}</h3>
            <div className="quantity">
                <button onClick={decreaseQuantity} className="cart-item-card-quantity-button">-</button>
                <p>Amount: {quantity}</p>
                <button onClick={increaseQuantity} className="cart-item-card-quantity-button">+</button>
            </div>
            <h4>Unit Price: ${product.price.toFixed(2)}</h4>
            <h4>Item Subtotal: ${subtotal.toFixed(2)}</h4>
        </div>
    );
};

export default CartItemCard;