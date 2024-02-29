import "../styles/ProductCard.css";
import Product from "../models/Product";

const ProductCard = ({ productName, price, stockQuantity }: Product) => {
    return (
        <div className="productCard">
            <div className="productImage">
                <img src="https://via.placeholder.com/150" alt="Product" />
            </div>
            <h3>{productName}</h3>
            <div className="productDetails">
                <p>${price}</p>
                <p>{stockQuantity} in stock</p>
            </div>
        </div>
    );
};

export default ProductCard;
