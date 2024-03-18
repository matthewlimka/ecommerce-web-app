import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAPI } from "../contexts/APIContext";

const RegistrationPage: React.FC = () => {

    const { registrationSuccess, resetRegistrationSuccess, registerUser } = useAPI();
    const usernameRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const confirmPasswordRef = useRef<HTMLInputElement>(null);
    const emailRef = useRef<HTMLInputElement>(null);
    const firstNameRef = useRef<HTMLInputElement>(null);
    const lastNameRef = useRef<HTMLInputElement>(null);

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [confirmPassword, setConfirmPassword] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [firstName, setFirstName] = useState<string>("");
    const [lastName, setLastName] = useState<string>("");
    const [errorMessage, setErrorMessage] = useState<string>("");
    const navigate = useNavigate();

    const handleRegistration = (event: any) => {
        event.preventDefault();
        if (password !== confirmPassword) {
            setErrorMessage("Passwords do not match");
            return;
        }
        
        registerUser(username, password, email, firstName, lastName);
        if (!registrationSuccess) {
            setErrorMessage("Failed to register");
        }
    };

    const handleRegistrationSuccess = (event: any) => {
        resetRegistrationSuccess();
        navigate('/login');
    }

    return (
        <div className="registration-page">
            <div className="registration-page-content">
                {registrationSuccess ? (
                    <div className="registration-page-content-after-registration">
                        <h1 className="registration-page-success-header">Success!</h1>
                        <p className="registration-page-success-message">Your account has been created</p>
                        <button className="registration-page-success-action-button" onClick={handleRegistrationSuccess}>Back to login</button>
                    </div>
                ) : (
                    <div className="registration-page-content-before-registration">
                        <h1 className="registration-page-header">Register</h1>
                        <form className="registration-page-form" onSubmit={handleRegistration}>
                            <div className="registration-page-form-row">
                                <input
                                    type="text"
                                    className="registration-page-first-name"
                                    ref={firstNameRef}
                                    value={firstName}
                                    onChange={(event) => setFirstName(event.target.value)}
                                    placeholder="First Name"
                                    required
                                />
                                <input
                                    type="text"
                                    className="registration-page-last-name"
                                    ref={lastNameRef}
                                    value={lastName}
                                    onChange={(event) => setLastName(event.target.value)}
                                    placeholder="Last Name"
                                    required
                                />
                            </div>
                            <div className="registration-page-form-row">
                                <input
                                    type="text"
                                    className="registration-page-username"
                                    ref={usernameRef}
                                    value={username}
                                    onChange={(event) => setUsername(event.target.value)}
                                    placeholder="Username"
                                    required
                                />
                                <input
                                    type="email"
                                    className="registration-page-email"
                                    ref={emailRef}
                                    value={email}
                                    onChange={(event) => setEmail(event.target.value)}
                                    placeholder="Email"
                                    required
                                />
                            </div>
                            <div className="registration-page-form-row">
                                <input
                                    type="password"
                                    className="registration-page-password"
                                    ref={passwordRef}
                                    value={password}
                                    onChange={(event) => setPassword(event.target.value)}
                                    placeholder="Password"
                                    required
                                />
                                <input
                                    type="password"
                                    className="registration-page-confirm-password"
                                    ref={confirmPasswordRef}
                                    value={confirmPassword}
                                    onChange={(event) => setConfirmPassword(event.target.value)}
                                    placeholder="Confirm Password"
                                    required
                                />
                            </div>
                            {errorMessage && <p className="registration-page-error-message">{errorMessage}</p>}
                            <button type="submit">Register</button>
                        </form>
                    </div>
                )}
            </div>
        </div>
    );
};

export default RegistrationPage;