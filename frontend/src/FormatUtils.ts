import Product from "./models/Product"
import Cart from "./models/Cart"
import CartItem from "./models/CartItem"
import Order from "./models/Order"
import OrderItem from "./models/OrderItem"

const FormatUtils = {
    formatProduct(product: Product) {
        return {
            ...product,
            price: parseFloat(product.price.toFixed(2)),
        }
    },

    formatCart(cart: Cart) {
        return {
            ...cart,
            totalAmount: parseFloat(cart.totalAmount.toFixed(2)),
            cartItems: cart.cartItems.map(this.formatCartItem),
        }
    },

    formatCartItem(cartItem: CartItem) {
        return {
            ...cartItem,
            subtotal: parseFloat(cartItem.subtotal.toFixed(2)),
        }
    },

    formatOrder(order: Order) {
        return {
            ...order,
            orderDate: new Date(order.orderDate),
            totalAmount: parseFloat(order.totalAmount.toFixed(2)),
            orderItems: order.orderItems.map(this.formatOrderItem),
        }
    },
    
    formatOrderItem(orderItem: OrderItem) {
        return {
            ...orderItem,
            subtotal: parseFloat(orderItem.subtotal.toFixed(2)),
        }
    }
}

export default FormatUtils;