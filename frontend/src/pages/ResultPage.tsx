import "../styles/HomePage.css";
import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useAPI } from "../contexts/APIContext";
import Navbar from "../components/Navbar";
import ProductCard from "../components/ProductCard";

const ResultPage: React.FC = () => {
    const { jwt } = useAuth();
    const { getUser, queryResults, getQueryResults } = useAPI();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const query = queryParams.get("q");

    useEffect(() => {
        if (jwt !== null) {
            getUser(jwt);
        }
    }, [jwt]);

    useEffect(() => {
        if (query !== null) {
            getQueryResults(query);
        } else {
            console.log("No query received");
        }
    }, [query]);

    return (
        <div className="page">
            <Navbar />
            <div className="page-content">
                <h1>
                    {queryResults.length > 0
                        ? queryResults.length +
                          (queryResults.length === 1
                              ? " result "
                              : " results ") +
                          "found for "
                        : "No results found for "}
                    {query}
                </h1>
                <div className="cards-container">
                    {queryResults.map((product, index) => {
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
    );
};

export default ResultPage;
