import { createContext, useContext, useState } from "react";
import FormatUtils from "../FormatUtils";
import User from "../models/User";
import Product from "../models/Product";
import Cart from "../models/Cart";
import CartItem from "../models/CartItem";
import Order from "../models/Order";
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
    addToCart: (jwt: string, productId: number, quantity: number) => void;
    removeFromCart: (jwt: string, productId: number) => void;
    orders: Order[];
    getOrders: (jwt: string) => void;
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
    addToCart: (jwt: string, productId: number, quantity: number) => {},
    removeFromCart: (jwt: string, productId: number) => {},
    orders: [],
    getOrders: (jwt: string) => {},
    queryResults: [],
    getQueryResults: (query: string) => {},
});

export const APIProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const API = "http://localhost:9001/api/v1";
    const [user, setUser] = useState<User | null>(null);
    const [product, setProduct] = useState<Product | null>(null);
    const [products, setProducts] = useState<Product[]>([]);
    const [cart, setCart] = useState<Cart | null>(null);
    const [orders, setOrders] = useState<Order[]>([]);
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
            setCart(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    const addToCart = async (jwt: string, productId: number, quantity: number) => {
        getCart(jwt);
        console.log(cart);

        interface CartItemDTO {
            quantity: number;
            product: number;
        }
        let cartItemsToSave: CartItemDTO[] = [];

        if (cart !== null) {
            try {
                const cartItem = cart.cartItems.find((item) => item.product.productId === productId);
                if (cartItem) {
                    cartItem.quantity += quantity;
                    const newCartItemToSave: CartItemDTO = {
                        quantity: cartItem.quantity += quantity,
                        product: productId
                    }
                    cartItemsToSave.push(newCartItemToSave);
                } else {
                    await getProduct(productId);
                    if (product) {
                        const newCartItem: CartItem = {
                            quantity,
                            cartId: cart.cartId,
                            product
                        };
                        const newCartItemToSave: CartItemDTO = {
                            quantity,
                            product: productId
                        }
                        cart.cartItems.push(newCartItem);
                        cartItemsToSave.push(newCartItemToSave);
                    }
                }
                await axios.patch(`${API}/cart/${cart.cartId}`, JSON.stringify({ cartItems: cartItemsToSave }), { headers: { Authorization: `Bearer ${jwt}`, 'Content-Type': 'application/json' } });
            } catch (error) {
                console.log(error);
            }
        }
    };
            
    const removeFromCart = async (jwt: string, productId: number) => {
        getCart(jwt);

        if (cart !== null) {
            try {
                const index = cart.cartItems.findIndex((item) => item.product.productId === productId);
                if (index !== -1) {
                    cart.cartItems.splice(index, 1);
                }
                await axios.put(`${API}/cart/${cart.cartId}`, { cartItems: cart.cartItems }, { headers: { Authorization: `Bearer ${jwt}` } });
            } catch (error) {
                console.log(error);
            }
        }
    };

    const getOrders = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/orders`, { headers: { Authorization: `Bearer ${jwt}` } });
                setOrders(response.data);
            } catch (error) {
                console.log(error);
            }
    };

    const getQueryResults = async (query: string) => {
        try {
            const response = await axios.get(`${API}/products?productName=${query}`);
                setQueryResults(response.data);
            } catch (error) {
                console.log(error);
            }
    };

    return (
        <APIContext.Provider value={{
            user, getUser, logoutUser,
            product, products, getProduct, getProducts,
            cart, getCart, addToCart, removeFromCart,
            orders, getOrders,
            queryResults, getQueryResults,
        }}>
            {children}
        </APIContext.Provider>
    )
};

export const useAPI = () => useContext(APIContext);