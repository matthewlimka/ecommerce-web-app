import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import axios from "axios";

const LoginPage: React.FC = () => {

    const { login } = useAuth();
    const userRef = useRef<HTMLInputElement>(null);
    const API = 'http://localhost:9001';
    const clientId = 'ae1c35b6915ac47d44c2';
    const redirectUri = 'http://localhost:3000/oauth/callback'; // API + '/oauth2/token'
    const githubAuthorizationUrl = `https://github.com/login/oauth/authorize?response_type=code&scope=user&client_id=${clientId}&redirect_uri=${redirectUri}`;
    const navigate = useNavigate();

    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('')
    const [error, setError] = useState<string>("");

    const handleFormLogin = async (event: any) => {
        event.preventDefault()

        try {
            const response = await axios.post(`${API}/login?username=${username}&password=${password}`)
            console.log('Received JWT: ' + response.data)
            login(response.data)
            console.log('Logged in successfully')

            setUsername('')
            setPassword('')
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

    const handleGitHubLogin = async (event: any) => {
        event.preventDefault()

        console.log('Redirecting you to GitHub')
        try {
            window.location.href = githubAuthorizationUrl
            console.log('Redirecting you to GitHub login page')
        } catch (error) {
            setError('Failed to log in via GitHub')
            console.log(error)
        }
    }

    return (
        <div className="loginPage">
            <h1>Login</h1>
            <div className="loginForm">
                <form onSubmit={handleFormLogin}>
                    <input
                        type="text"
                        ref={userRef}
                        value={username}
                        placeholder="Username"
                        onChange={event => setUsername(event.target.value)}
                        required
                    />
                    <input
                        type="password"
                        ref={userRef}
                        value={password}
                        placeholder="Password"
                        onChange={event => setPassword(event.target.value)}
                        required
                    />
                    <button type="submit">Login</button>
                </form>
            </div>
            {error && <p>{error}</p>}
            <button onClick={handleGitHubLogin}>Sign In via GitHub</button>
            <button>Sign Up</button>
        </div>
    )
}

export default LoginPage;