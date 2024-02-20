import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Searchbar: React.FC = () => {

    const [searchInput, setSearchInput] = useState('');
    const navigate = useNavigate();

    const handleSearch = () => {
        navigate(`/result?q=${searchInput}`)
    }
    
    return (
        <div>
            <input
                type="text"
                value={searchInput}
                placeholder="Search for a product"
                onChange={event => setSearchInput(event.target.value)}
            />
            <button onClick={handleSearch}>Search</button>
        </div>
    )
}

export default Searchbar;