import Product from './Product';

interface CartItem {
    cartItemId?: number;
    quantity: number;
    cartId: number;
    product: Product;
}

export default CartItem;