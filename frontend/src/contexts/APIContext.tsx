import { createContext, useContext, useState } from 'react';
import User from '../models/User';
import Product from '../models/Product';
import Cart from '../models/Cart';
import Order from '../models/Order';
import axios from 'axios';

type APIContextType = {
    user: User | null;
    getUser: (jwt: string) => void;
    logoutUser: () => void;
    products: Product[];
    getProducts: () => void;
    cart: Cart | null;
    getCart: (jwt: string) => void;
    orders: Order[];
    getOrders: (jwt: string) => void;
    queryResults: Product[];
    getQueryResults: (query: string) => void;
}

const APIContext = createContext<APIContextType>({
    user: null,
    getUser: (jwt: string) => { },
    logoutUser: () => { },
    products: [],
    getProducts: () => { },
    cart: null,
    getCart: (jwt: string) => { },
    orders: [],
    getOrders: (jwt: string) => { },
    queryResults: [],
    getQueryResults: (query: string) => { }
})

export const APIProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

    const API = 'http://localhost:9001/api/v1';
    const [user, setUser] = useState<User | null>(null);
    const [products, setProducts] = useState<Product[]>([]);
    const [cart, setCart] = useState<Cart | null>(null);
    const [orders, setOrders] = useState<Order[]>([]);
    const [queryResults, setQueryResults] = useState<Product[]>([]);

    const getUser = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/user`, { headers: { Authorization: `Bearer ${jwt}` } })
            setUser(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    const logoutUser = () => {
        setUser(null)
    }

    const getProducts = async () => {
        try {
            const response = await axios.get(`${API}/products`)
            setProducts(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    const getCart = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/cart`, { headers: { Authorization: `Bearer ${jwt}` } })
            setCart(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    const getOrders = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/orders`, { headers: { Authorization: `Bearer ${jwt}` } })
            setOrders(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    const getQueryResults = async (query: string) => {
        try {
            const response = await axios.get(`${API}/search?q=${query}`)
            setQueryResults(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <APIContext.Provider value={{
            user, getUser, logoutUser,
            products, getProducts,
            cart, getCart,
            orders, getOrders,
            queryResults, getQueryResults
        }}>
            {children}
        </APIContext.Provider>
    )
}

export const useAPI = () => useContext(APIContext);