import "../styles/ItemCard.css";
import OrderItem from "../models/OrderItem";

const OrderItemCard = ({ orderItemId, quantity, subtotal, orderId, product }: OrderItem) => {

    return (
        <div className="order-item-card">
            <div className="order-item-image">
                <img src="https://via.placeholder.com/150" alt="OrderItem" />
            </div>
            <h3 className="order-item-product-name">{product.productName}</h3>
            <div className="order-item-right-section">
                <div className="order-item-card-headers">
                    <h4 className="order-item-price-header">Unit Price</h4>
                    <h4 className="order-item-quantity-header">Amount</h4>
                    <h4 className="order-item-subtotal-header">Item Subtotal</h4>
                </div>
                <div className="order-item-card-values">
                    <h4 className="order-item-price">${product.price.toFixed(2)}</h4>
                    <h4 className="order-item-quantity">{quantity}</h4>
                    <h4 className="order-item-subtotal">${subtotal.toFixed(2)}</h4>
                </div>
            </div>
        </div>
    );
}

export default OrderItemCard;