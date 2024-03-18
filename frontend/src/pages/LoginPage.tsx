import '../styles/LoginRegistrationPage.css';
import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';

const LoginPage: React.FC = () => {
    const { login } = useAuth();
    const userRef = useRef<HTMLInputElement>(null);
    const API = "http://localhost:9001";
    const CLIENT_ID = "ae1c35b6915ac47d44c2";
    const GITHUB_AUTHORIZATION_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&scope=user:email`;
    const navigate = useNavigate();

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [errorMessage, setErrorMessage] = useState<string>("");

    const handleFormLogin = async (event: any) => {
        event.preventDefault();

        try {
            const response = await axios.post(
                `${API}/login?username=${username}&password=${password}`
            );
            console.log("Received JWT: " + response.data);
            login(response.data);
            console.log("Logged in successfully");

            setUsername("");
            setPassword("");
            console.log("Redirecting you to the home page");
            navigate("/home");
        } catch (error) {
            let errorMessage = "Unknown error occurred";
            if (axios.isAxiosError(error) && error.response) {
                switch (error.response.status) {
                    case 401:
                        switch (error.response.data.message) {
                            case "Invalid username":
                                errorMessage = "Invalid username";
                                break;
                            case "Invalid password":
                                errorMessage = "Invalid password";
                                break;
                            default:
                                errorMessage = "Invalid credentials";
                                break;
                        }
                        break;
                    default:
                        errorMessage = "Failed to log in";
                        break;
                }
            }
            
            setErrorMessage(errorMessage);
            console.log(error);
        }
    };

    const handleGitHubLogin = async (event: any) => {
        event.preventDefault();

        console.log("Redirecting you to GitHub");
        try {
            window.location.href = GITHUB_AUTHORIZATION_URL;
        } catch (error) {
            setErrorMessage("Failed to log in via GitHub");
            console.log(error);
        }
    };

    return (
        <div className="login-page">
            <div className="login-page-content">
                <h1 className="login-page-header">Login</h1>
                <form className="login-page-form" onSubmit={handleFormLogin}>
                    <input
                        type="text"
                        className="login-page-username"
                        ref={userRef}
                        value={username}
                        placeholder="Username"
                        onChange={(event) => setUsername(event.target.value)}
                        required
                    />
                    <input
                        type="password"
                        className="login-page-password"
                        ref={userRef}
                        value={password}
                        placeholder="Password"
                        onChange={(event) => setPassword(event.target.value)}
                        required
                    />
                    {errorMessage && <p className="login-page-error-message">{errorMessage}</p>}
                    <button className="login-page-login-button" type="submit">Login</button>
                </form>
                <div className="login-page-actions">
                    <button className="login-page-action-button" onClick={handleGitHubLogin}>Sign In via GitHub</button>
                    <button className="login-page-action-button" onClick={() => navigate('/signup')}>Sign Up</button>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;