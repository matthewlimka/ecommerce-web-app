import { createContext, useContext, useState } from 'react';

interface AuthContextType {
    jwt: string | null;
    login: (jwt: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    jwt: null,
    login: () => { },
    logout: () => { }
})

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

    const [jwt, setJwt] = useState<string | null>(null);

    const login = (jwt: string) => {
        setJwt(jwt);
    }

    const logout = () => {
        setJwt(null);
    }

    return (
        <AuthContext.Provider value={{ jwt, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);