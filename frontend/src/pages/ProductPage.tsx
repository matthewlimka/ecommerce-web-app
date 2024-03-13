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
    const { user, product, getProduct, getCart, addToCart } = useAPI();
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isNaN(productId)) {
            getProduct(productId);
        }
    }, []);

    useEffect(() => {
        if (jwt !== null) {
            getCart(jwt);
        }
    }, [jwt]);

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
        addToCart(jwt!, product!, quantity, quantity * product!.price);
    }

    const handleBuyNow = (event: any) => {
        event.preventDefault();
        handleAddToCart(event);
        navigate(`/cart${user?.userId}`);
    }

    return (
        <div className="page">
            <Navbar />
            <div className="product-page-content">
                <div className="product-page-image-section">
                    <img src="https://via.placeholder.com/300" alt="Product" />
                </div>
                <div className="product-page-detail-section">
                    <h1 className="product-page-product-name">{product?.productName}</h1>
                    <h2 className="product-page-price">${product?.price.toFixed(2)}</h2>
                    <div className="product-page-quantity">
                        <h3 className="product-page-quantity-header">Quantity</h3>
                        <button onClick={decreaseQuantity} className="product-page-quantity-button">-</button>
                        <span className="product-page-quantity-value">{quantity}</span>
                        <button onClick={increaseQuantity} className="product-page-quantity-button">+</button>
                        <span className="product-page-stock-quantity-value">{product?.stockQuantity} available</span>
                    </div>
                    <div className="product-page-detail-section-buttons">
                        <button onClick={handleAddToCart} className="product-page-action-button">Add to Cart</button>
                        <button onClick={handleBuyNow} className="product-page-action-button">Buy Now</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProductPage;