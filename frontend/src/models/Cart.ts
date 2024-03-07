import User from './User';
import CartItem from './CartItem';

interface Cart {
    cartId: number;
    totalAmount: number;
    user: User;
    cartItems: CartItem[];
}

export default Cart;