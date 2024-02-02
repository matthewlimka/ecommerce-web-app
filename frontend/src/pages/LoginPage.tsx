import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import axios from "axios";

const LoginPage: React.FC = () => {

    const { jwt, login } = useAuth();
    const userRef = useRef<HTMLInputElement>(null);
    const API = 'http://localhost:9001';
    const navigate = useNavigate();

    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');

    const handleFormLogin = async (event: any) => {
        event.preventDefault()

        await axios.post(`${API}/login?username=${username}&password=${password}`)
            .then(response => {
                console.log(response)
                console.log('Received JWT: ' + response.data)
                login(response.data)
                console.log('Logged in successfully')

                setUsername('')
                setPassword('')
                console.log('Redirecting you to the home page')
                navigate('/home')
            })
            .catch(error => {
                if (error.response?.status === 401) {
                    console.log('Invalid credentials')
                } else {
                    console.log('Failed to log in')
                }
            })
    }

    const handleGitHubLogin = async (event: any) => {
        event.preventDefault()

        console.log('Redirecting you to GitHub')
        await axios.get(`${API}/oauth2/authorization/github`)
            .then(response => {
                console.log('Received JWT: ' + response.data.token)
                login(response.data.token)
                console.log('Logged in successfully')

                console.log('Redirecting you to the home page')
                navigate('/home')
            })
            .catch(error => {
                console.log('Failed to log in via GitHub')
            })
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
            <button onClick={handleGitHubLogin}>Sign In via GitHub</button>
            <button>Sign Up</button>
        </div>
    )
}

export default LoginPage;