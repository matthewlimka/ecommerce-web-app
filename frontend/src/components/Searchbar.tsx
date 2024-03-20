import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Searchbar: React.FC = () => {

    const [searchInput, setSearchInput] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (event: any) => {
        event.preventDefault();
        navigate(`/result?q=${searchInput}`);
    };

    return (
        <div className="search-bar">
            <form className="search-bar-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    className="search-bar-input"
                    value={searchInput}
                    placeholder="Search for a product"
                    required
                    onChange={(event) => {
                        setSearchInput(event.target.value);
                        event.target.setCustomValidity('');
                    }}
                    onInvalid={(event) => {
                        const target = event.target as HTMLInputElement;
                        target.setCustomValidity('Please enter a search term') }}
                />
                <button className="search-bar-button"><img className="search-bar-button-icon" src="/search-icon.svg" /></button>
            </form>
        </div>
    );
};

export default Searchbar;
