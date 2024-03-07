import Order from './Order';
import Product from './Product';

interface OrderItem {
    orderItemId?: number;
    quantity: number;
    subtotal: number;
    orderId?: number;
    product: Product;
}

export default OrderItem;