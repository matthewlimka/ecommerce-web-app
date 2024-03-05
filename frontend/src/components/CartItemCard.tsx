import "../styles/CartItemCard.css";
import CartItem from "../models/CartItem";

const CartItemCard = ({ quantity, cartId, product }: CartItem) => {
    return (
        <div className="cartItemCard">
            <div className="cartItemImage">
                <img src="https://via.placeholder.com/150" alt="CartItem" />
            </div>
            <h3>{product.productName}</h3>
            <div className="cartItemDetails">
                <p>Qty: {quantity}</p>
            </div>
        </div>
    );
};

export default CartItemCard;