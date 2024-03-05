import "../styles/ProductCard.css";
import { Link } from "react-router-dom";
import Product from "../models/Product";

const ProductCard = ({ productId, productName, price, stockQuantity }: Product) => {
    return (
        <Link to={`/product/${productId}`} className="product-card">
            <div className="product-card-image">
                <img src="https://via.placeholder.com/150" alt="Product" />
            </div>
            <h3 className="product-card-product-name">{productName}</h3>
            <div className="product-card-details">
                <p className="product-card-details-price">${price}</p>
                <p className="product-card-details-stock-quantity">{stockQuantity} in stock</p>
            </div>
        </Link>
    );
};

export default ProductCard;