import "../styles/ItemCard.css";
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
    };

    const increaseQuantity = (event: any) => {
        event.preventDefault();
        updateCartItem(jwt!, cartItemId!, quantity + 1);
    };

    return (
        <div className="cart-item-card">
            <div className="cart-item-image">
                <img src="https://via.placeholder.com/150" alt="CartItem" />
            </div>
            <h3 className="cart-item-product-name">{product.productName}</h3>
            <div className="cart-item-right-section">
                <div className="cart-item-card-headers">
                    <h4 className="cart-item-price-header">Unit Price</h4>
                    <h4 className="cart-item-quantity-header">Amount</h4>
                    <h4 className="cart-item-subtotal-header">Item Subtotal</h4>
                </div>
                <div className="cart-item-card-values">
                    <h4 className="cart-item-price">${product.price.toFixed(2)}</h4>
                    <div className="cart-item-quantity">
                        <button onClick={decreaseQuantity} className="cart-item-card-quantity-button">-</button>
                        <p>{quantity}</p>
                        <button onClick={increaseQuantity} className="cart-item-card-quantity-button">+</button>
                    </div>
                    <h4 className="cart-item-subtotal">${subtotal.toFixed(2)}</h4>
                </div>
            </div>
        </div>
    );
};

export default CartItemCard;