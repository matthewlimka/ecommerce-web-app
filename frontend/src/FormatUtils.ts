import Product from "./models/Product"

const FormatUtils = {
    formatProduct(product: Product) {
        return {
            ...product,
            price: parseFloat(product.price.toFixed(2)),
        }
    }
}

export default FormatUtils;