import { createContext, useContext, useState } from 'react';
import axios from 'axios';

type APIContextType = {
    user: User | null;
    getUser: (jwt: string) => void;
    products: string[];
    getProducts: () => void;
    cart: string[];
    getCart: (jwt: string) => void;
    orders: string[];
    getOrders: (jwt: string) => void;
    queryResults: string[];
    getQueryResults: (query: string) => void;
}

interface User {
    username: string;
    email: string;
    firstName?: string;
    lastName?: string;
}

const APIContext = createContext<APIContextType>({
    user: null,
    getUser: (jwt: string) => { },
    products: [],
    getProducts: () => { },
    cart: [],
    getCart: (jwt: string) => { },
    orders: [],
    getOrders: (jwt: string) => { },
    queryResults: [],
    getQueryResults: (query: string) => { }
})

export const APIProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

    const API = 'http://localhost:9001';
    const [user, setUser] = useState<User | null>(null);
    const [products, setProducts] = useState<string[]>([]);
    const [cart, setCart] = useState<string[]>([]);
    const [orders, setOrders] = useState<string[]>([]);
    const [queryResults, setQueryResults] = useState<string[]>([]);

    const getUser = async (jwt: string) => {
        try {
            const response = await axios.get(`${API}/user`, { headers: { Authorization: `Bearer ${jwt}` } })
            setUser(response.data)
        } catch (error) {
            console.log(error)
        }
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
            user, getUser,
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