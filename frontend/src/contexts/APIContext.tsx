import { createContext, useContext, useState } from "react";
import FormatUtils from "../FormatUtils";
import PaymentMethod from "../enums/PaymentMethod";
import User from "../models/User";
import Product from "../models/Product";
import Cart from "../models/Cart";
import CartItem from "../models/CartItem";
import Order from "../models/Order";
import OrderItem from "../models/OrderItem";
import OrderStatus from "../enums/OrderStatus";
import Payment from "../models/Payment";
import axios from "axios";

type APIContextType = {
    user: User | null;
    getUser: (jwt: string) => void;
    logoutUser: () => void;
    product: Product | null;
    products: Product[];
    getProduct: (productId: number) => void;
    getProducts: () => void;
    cart: Cart | null;
    getCart: (jwt: string) => void;
    addToCart: (jwt: string, product: Product, quantity: number, subtotal: number) => void;
    removeFromCart: (jwt: string, productId: number) => void;
    updateCartItem: (jwt: string, cartItemId: number, quantity: number) => void;
    clearCart: (jwt: string) => void;
    checkoutOrder: Order | null;
    placedOrder: Order | null;
    userOrders: Order[];
    getUserOrders: (jwt: string) => void;
    getCheckoutOrder: (jwt: string, orderId: number) => void;
    getPlacedOrder(jwt: string): void;
    createCheckoutOrder: (jwt: string) => void;
    placeOrder: (jwt: string, selectedPaymentMethod: PaymentMethod) => void;
    updateOrderItem: (jwt: string, orderItemId: number, quantity: number) => void;
    queryResults: Product[];
    getQueryResults: (query: string) => void;
};

const APIContext = createContext<APIContextType>({
    user: null,
    getUser: (jwt: string) => {},
    logoutUser: () => {},
    product: null,
    products: [],
    getProduct: (productId: number) => {},
    getProducts: () => {},
    cart: null,
    getCart: (jwt: string) => {},
    addToCart: (jwt: string, product: Product, quantity: number, subtotal: number) => {},
    removeFromCart: (jwt: string, productId: number) => {},
    updateCartItem: (jwt: string, cartItemId: number, quantity: number) => {},
    clearCart: (jwt: string) => {},
    checkoutOrder: null,
    placedOrder: null,
    userOrders: [],
    getUserOrders: (jwt: string) => {},
    getCheckoutOrder: (jwt: string, orderId: number) => {},
    getPlacedOrder: (jwt: string) => {},
    createCheckoutOrder: (jwt: string) => {},
    placeOrder: (jwt: string, selectedPaymentMethod: PaymentMethod) => {},
    updateOrderItem: (jwt: string, orderItemId: number, quantity: number) => {},
    queryResults: [],
    getQueryResults: (query: string) => {},
});

export const APIProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const API = "http://localhost:9001/api/v1";
    const [user, setUser] = useState<User | null>(null);
    const [product, setProduct] = useState<Product | null>(null);
    const [products, setProducts] = useState<Product[]>([]);
    const [cart, setCart] = useState<Cart | null>(null);
    const [checkoutOrder, setCheckoutOrder] = useState<Order | null>(null);
    const [placedOrder, setPlacedOrder] = useState<Order | null>(null);
    const [userOrders, setUserOrders] = useState<Order[]>([]);
    const [queryResults, setQueryResults] = useState<Product[]>([]);

    const getUser = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/user`, { headers: { Authorization: `Bearer ${jwt}` }});
            setUser(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    const logoutUser = () => {
        setUser(null);
    };

    const getProduct = async (productId: number) => {
        try {
            const response = await axios.get(`${API}/products/${productId}`);
            setProduct(FormatUtils.formatProduct(response.data));
        } catch (error) {
            console.log(error);
        }
    };

    const getProducts = async () => {
        try {
            const response = await axios.get(`${API}/products`);
            setProducts(
                response.data.map((product: Product) => FormatUtils.formatProduct(product))
            );
        } catch (error) {
            console.log(error);
        }
    };

    const getCart = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/cart`, { headers: { Authorization: `Bearer ${jwt}` } });
            setCart(FormatUtils.formatCart(response.data));
        } catch (error) {
            console.log(error);
        }
    };

    const addToCart = async (jwt: string, product: Product, quantity: number, subtotal: number) => {
        interface CartItemDTO {
            cartItemId?: number;
            quantity: number;
            subtotal: number;
            cartId: number;
            product: number;
        }
        let cartItemsToSave: CartItemDTO[] = [];

        try {
            // Check if the product is already in the cart 
            const cartItem = cart!.cartItems.find((item) => item.product.productId === product.productId);
            if (cartItem) {
                // Update the quantity of the cart item
                cartItem.quantity += quantity;
                cartItem.subtotal += subtotal;
            } else {
                // Create a new cart item
                const newCartItem: CartItem = {
                    quantity: quantity,
                    subtotal: parseFloat(subtotal.toFixed(2)),
                    cartId: cart!.cartId,
                    product: product
                };
                // Add the new cart item to the cart's cartItems array
                cart!.cartItems.push(newCartItem);
            }

            // Convert the cartItems to a DTO
            cartItemsToSave = cart!.cartItems.map((cartItem) => {
                return {
                    cartItemId: cartItem.cartItemId,
                    quantity: cartItem.quantity,
                    subtotal: parseFloat(cartItem.subtotal.toFixed(2)),
                    cartId: cartItem.cartId,
                    product: cartItem.product.productId
                }
            });
            console.log('Cart items to save');
            console.log(cartItemsToSave);
            
            cart!.totalAmount = cart!.cartItems.reduce((total, item) => total + item.subtotal, 0);
            cart!.totalAmount = parseFloat(cart!.totalAmount.toFixed(2));

            // Send the updated list of cartItems in the cart
            const response = await axios.patch(`${API}/cart/${cart!.cartId}`, { totalAmount: cart!.totalAmount, cartItems: cartItemsToSave }, { headers: { Authorization: `Bearer ${jwt}`, 'Content-Type': 'application/json' } });
            setCart(FormatUtils.formatCart(response.data));
        } catch (error) {
            console.log(error);
        }
    };
            
    const removeFromCart = async (jwt: string, productId: number) => {
        try {
            const index = cart!.cartItems.findIndex((item) => item.product.productId === productId);
            if (index !== -1) {
                cart!.cartItems.splice(index, 1);
            }
            const response = await axios.put(`${API}/cart/${cart!.cartId}`, { cartItems: cart!.cartItems }, { headers: { Authorization: `Bearer ${jwt}` } });
            setCart(FormatUtils.formatCart(response.data));
        } catch (error) {
            console.log(error);
        }
    };

    const updateCartItem = async (jwt: string, cartItemId: number, quantity: number) => {
        try {
            const response = await axios.patch(`${API}/cartItems/${cartItemId}`, { quantity: quantity }, { headers: { Authorization: `Bearer ${jwt}` } });
            console.log('Updated cart item');
            console.log(response.data);
            getCart(jwt!)
        } catch (error) {
            console.log(error);
        }
    }

    const clearCart = async (jwt: string) => {
        try {
            const response = await axios.patch(`${API}/cart/${cart!.cartId}`, { cartItems: [] }, { headers: { Authorization: `Bearer ${jwt}` } });
            setCart(FormatUtils.formatCart(response.data));
            console.log('Cleared cart');
            console.log(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    const getUserOrders = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/orders`, { headers: { Authorization: `Bearer ${jwt}` } });
                setUserOrders(
                    response.data.map((order: Order) => FormatUtils.formatOrder(order))
                );
            } catch (error) {
                console.log(error);
            }
    };

    const getCheckoutOrder = async (jwt: string, orderId: number) => {
        try {
            const response = await axios.get(`${API}/orders/${orderId}`, { headers: { Authorization: `Bearer ${jwt}` } });
            setCheckoutOrder(FormatUtils.formatOrder(response.data));
        } catch (error) {
            console.log(error);
        }
    };

    const createCheckoutOrder = async (jwt: string) => {
        const convertCartItemsToOrderItems = (cartItems: CartItem[]): OrderItem[] => {
            return cartItems.map((cartItem) => {
                return {
                    quantity: cartItem.quantity,
                    subtotal: cartItem.subtotal,
                    product: cartItem.product
                }
            });
        };
        
        let orderItems: OrderItem[] = convertCartItemsToOrderItems(cart!.cartItems);
        let order: Order = {
            orderDate: new Date(),
            totalAmount: orderItems.reduce((total, item) => total + item.subtotal, 0),
            orderStatus: OrderStatus.PENDING,
            orderItems
        };

        const response = await axios.post(`${API}/orders`, { orderDate: order.orderDate, totalAmount: order.totalAmount, orderStatus: order.orderStatus, orderItems: order.orderItems }, { headers: { Authorization: `Bearer ${jwt}` } });
        setCheckoutOrder(FormatUtils.formatOrder(response.data));
        console.log('Created checkout order');
        console.log(response.data);
    };

    const getPlacedOrder = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/orders/${checkoutOrder!.orderId}`, { headers: { Authorization: `Bearer ${jwt}` } });
            setPlacedOrder(FormatUtils.formatOrder(response.data));
        } catch (error) {
            console.log(error);
        }
    };

    const placeOrder = async (jwt: string, selectedPaymentMethod: PaymentMethod) => {
        try {
            console.log('Order before being placed');
            console.log(checkoutOrder);

            console.log('Selected payment method: ' + selectedPaymentMethod);
            let payment: Payment = {
                paymentMethod: selectedPaymentMethod,
                amount: checkoutOrder!.totalAmount,
                paymentDate: new Date()
            }
            checkoutOrder!.payment = payment;
            checkoutOrder!.orderStatus = OrderStatus.PROCESSING;

            const response = await axios.put(`${API}/orders/${checkoutOrder!.orderId}`, { orderDate: checkoutOrder!.orderDate, totalAmount: checkoutOrder!.totalAmount, orderStatus: checkoutOrder!.orderStatus, orderItems: checkoutOrder!.orderItems, payment: checkoutOrder!.payment }, { headers: { Authorization: `Bearer ${jwt}` } });
            setCheckoutOrder(FormatUtils.formatOrder(response.data));
            console.log('Order after being placed');
            console.log(response.data);
        } catch (error) {
            console.log(error);
        }
    }

    const updateOrderItem = async (jwt: string, orderItemId: number, quantity: number) => {
        try {
            const response = await axios.patch(`${API}/orderItems/${orderItemId}`, { quantity: quantity }, { headers: { Authorization: `Bearer ${jwt}` } });
            console.log('Updated order item');
            console.log(response.data);
            getCheckoutOrder(jwt!, checkoutOrder!.orderId!);
        } catch (error) {
            console.log(error);
        }
    }

    const getQueryResults = async (query: string) => {
        try {
            const response = await axios.get(`${API}/products?productName=${query}`);
                setQueryResults(
                    response.data.map((product: Product) => FormatUtils.formatProduct(product))
                );
            } catch (error) {
                console.log(error);
            }
    };

    return (
        <APIContext.Provider value={{
            user, getUser, logoutUser,
            product, products, getProduct, getProducts,
            cart, getCart, addToCart, removeFromCart, updateCartItem, clearCart,
            checkoutOrder, placedOrder, userOrders, getUserOrders, getCheckoutOrder, createCheckoutOrder, getPlacedOrder, placeOrder, updateOrderItem,
            queryResults, getQueryResults,
        }}>
            {children}
        </APIContext.Provider>
    )
};

export const useAPI = () => useContext(APIContext);