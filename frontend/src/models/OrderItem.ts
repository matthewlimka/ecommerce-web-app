import Order from './Order';
import Product from './Product';

interface OrderItem {
    orderItemId: number;
    quantity: number;
    subtotal: number;
    order: Order;
    price: number;
    product: Product;
}

export default OrderItem;