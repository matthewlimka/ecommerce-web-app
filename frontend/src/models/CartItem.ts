import Cart from './Cart';
import Product from './Product';

interface CartItem {
    cartItemId: number;
    quantity: number;
    cart: Cart;
    product: Product;
}

export default CartItem;