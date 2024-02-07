import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import axios from "axios";

const OAuthCallbackPage = () => {

    const { jwt, code, login, updateCode } = useAuth();
    const API = 'http://localhost:9001';
    // const clientId = 'ae1c35b6915ac47d44c2';
    // const clientSecret = '1eccae288f68a3c96d194228145260ffd8160d8e';
    // const redirectUri = 'http://localhost:3000/oauth/callback'; // API +'/oauth2/token'
    // const githubAccessTokenUrl = `https://github.com/login/oauth/access_token?grant_type=authorization_code&code=${code}&redirect_uri=${redirectUri}`;
    const navigate = useNavigate();

    const [accessToken, setAccessToken] = useState<string>("");
    const [error, setError] = useState<string>("");

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const receivedCode = urlParams.get('code');
        if (receivedCode !== null) {
            console.log('Received code: ' + receivedCode);
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

    // To try restore the exchange of authorization code for access token and frontend then send the access token to backend for JWT instead of doing it in backend
    const exchangeAccessToken = async () => {
        console.log('Exchanging authorization code for access token...')
        try {
            const response = await axios.post(`https://github.com/login/oauth/access_token?client_id=ae1c35b6915ac47d44c2&client_secret=1eccae`);
        } catch (error) {
            if (axios.isAxiosError(error)) {
                setError('Failed to exchange authorization code for access token')
            } else {
                setError('Unknown error occurred')
            }
            console.log(error)
        }
    }

    const exchangeJwt = async () => {
        console.log('Exchanging authorization code for JWT...')
        try {
            const response = await axios.get(`${API}/oauth2/token?code=${code}`)
            console.log('Received JWT: ' + response.data)
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