import { useRef, useState } from "react";
import { useAPI } from "../contexts/APIContext";

const RegistrationPage: React.FC = () => {

    // const { register } = useAPI();
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
    const [error, setError] = useState<string>("");

    const handleRegistration = (event: any) => {
        event.preventDefault();
        console.log("Registering user");
        // register(username, password, email, firstName, lastName);
    };

    return (
        <div className="registration-page">
            <div className="registration-page-content">
                <h1 className="registration-page-header">Register</h1>
                <form className="registration-page-form" onSubmit={handleRegistration}>
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
                    <button type="submit">Register</button>
                </form>
            </div>
        </div>
    );
};

export default RegistrationPage;