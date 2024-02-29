import "../styles/HomePage.css";
import Navbar from "../components/Navbar";
import { useEffect } from "react";
import { useAPI } from "../contexts/APIContext";
import ProductCard from "../components/ProductCard";

const HomePage: React.FC = () => {
    const { user, products, getProducts } = useAPI();

    useEffect(() => {
        getProducts();
    }, []);

    return (
        <div className="page">
            <Navbar />
            <div className="pageContent">
                <h1>Welcome to ShoppersGate{user ? " " + user?.username : ""}!</h1>
                <div className="cardsContainer">
                    <h2>Top Products</h2>
                    <div className="products">
                        {products.map((product, index) => {
                            return (
                                <ProductCard
                                    productId={product.productId}
                                    productName={product.productName}
                                    price={product.price}
                                    stockQuantity={product.stockQuantity}
                                />
                            );
                        })}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;
