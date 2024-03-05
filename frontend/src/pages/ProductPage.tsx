import "../styles/ProductPage.css";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import Navbar from "../components/Navbar";

const ProductPage: React.FC = () => {

    const params = useParams<{ productId?: string }>();
    const productId: number = parseInt(params.productId!);
    const { jwt } = useAuth();
    const { product, getProduct, addToCart } = useAPI();
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isNaN(productId)) {
            getProduct(productId);
        }
    }, []);

    const decreaseQuantity = (event: any) => {
        event.preventDefault();
        if (quantity > 1) {
            setQuantity(quantity - 1);
        }
    }

    const increaseQuantity = (event: any) => {
        event.preventDefault();
        setQuantity(quantity + 1);
    }

    const handleAddToCart = (event: any) => {
        event.preventDefault();
        addToCart(jwt!, productId, quantity);
    }

    const handleBuyNow = (event: any) => {
        event.preventDefault();
        handleAddToCart(event);
        navigate('/cart');
    }

    return (
        <div className="page">
            <Navbar />
            <div className="product-page-content">
                <div className="left-section">
                    <img src="https://via.placeholder.com/300" alt="Product" />
                </div>
                <div className="right-section">
                    <h1 className="product-name">{product?.productName}</h1>
                    <h2 className="price">${product?.price}</h2>
                    <div className="quantity">
                        <h2>Quantity</h2>
                        <button onClick={decreaseQuantity}>-</button>
                        <span>{quantity}</span>
                        <button onClick={increaseQuantity}>+</button>
                        <span>{product?.stockQuantity} available</span>
                    </div>
                    <div className="buttons-section">
                        <button onClick={handleAddToCart}>Add to Cart</button>
                        <button onClick={handleBuyNow}>Buy Now</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProductPage;