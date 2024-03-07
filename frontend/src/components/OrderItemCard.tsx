import OrderItem from "../models/OrderItem";

const OrderItemCard = ({ orderItemId, quantity, subtotal, orderId, product }: OrderItem) => {

    return (
        <div className="order-item-card">
            <div className="order-item-image">
                <img src="https://via.placeholder.com/150" alt="OrderItem" />
            </div>
            <h3>{product.productName}</h3>
            <h4>Amount: {quantity}</h4>
            <h4>Unit Price: ${product.price.toFixed(2)}</h4>
            <h4>Item Subtotal: ${subtotal.toFixed(2)}</h4>
        </div>
    );
}

export default OrderItemCard;