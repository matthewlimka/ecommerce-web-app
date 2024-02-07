import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import axios from "axios";

const OAuthCallbackPage = () => {

    const { code, login, updateCode } = useAuth();
    const API = 'http://localhost:9001';
    const navigate = useNavigate();

    const [error, setError] = useState<string>("");

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const receivedCode = urlParams.get('code');
        if (receivedCode !== null) {
            console.log(`Received authorization code: ${receivedCode}`);
            updateCode(receivedCode);
        }
    }, [])

    useEffect(() => {
        if (code !== null) {
            exchangeJwt();
        }   else {
            setError('No code received');
            console.log(error);
        }
    }, [code])

    const exchangeJwt = async () => {
        console.log('Exchanging authorization code for JWT...')
        try {
            const response = await axios.get(`${API}/oauth2/token?code=${code}`)
            console.log(`Received JWT: ${response.data}`)
            login(response.data)
            console.log('Logged in successfully')
            console.log('Redirecting you to the home page')
            navigate('/home')
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 401) {
                    setError('Invalid credentials')
                } else {
                    setError('Failed to log in')
                }
            } else {
                setError('Unknown error occurred')
            }
            console.log(error)
        }
    }

    return (
        <div>
            <h1>Processing GitHub login...</h1>
        </div>
    )
}

export default OAuthCallbackPage;