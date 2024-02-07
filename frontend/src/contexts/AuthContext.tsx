import { createContext, useContext, useState } from 'react';

interface AuthContextType {
    jwt: string | null;
    code: string | null;
    login: (jwt: string) => void;
    logout: () => void;
    updateCode: (code: string | null) => void;
}

const AuthContext = createContext<AuthContextType>({
    jwt: null,
    code: null,
    login: () => { },
    logout: () => { },
    updateCode: () => { }
})

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

    const [jwt, setJwt] = useState<string | null>(null);
    const [code, setCode] = useState<string | null>(null);

    const login = (jwt: string) => {
        setJwt(jwt);
    }

    const logout = () => {
        setJwt(null);
    }

    const updateCode = (code: string | null) => {
        setCode(code);
    }

    return (
        <AuthContext.Provider value={{ jwt, code, login, logout, updateCode }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);