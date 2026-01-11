document.addEventListener("DOMContentLoaded", () => {
    // Get ID from URL (e.g., productpage.html?id=5)
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get("id");

    if (id) {
        fetchProductDetails(id);
    } else {
        document.getElementById("productContainer").innerHTML = "<p>Product not found.</p>";
    }
});

function fetchProductDetails(id) {
    fetch(`../../ProductDetailServlet?id=${id}`)
    .then(res => res.json())
    .then(product => {
        const container = document.getElementById("productContainer");
        const imagePath = `../../assets/products/${product.image}`;
        
        // Stock Color Logic
        const stockColor = product.stock < 5 ? "#e10600" : "#fff";
        
        container.innerHTML = `
            <div class="left">
                <img src="${imagePath}" alt="${product.name}" onerror="this.src='https://via.placeholder.com/500'">
            </div>
            <div class="right">
                <h1>${product.name}</h1>
                <span class="price">RM ${product.price.toFixed(2)}</span>
                
                <p style="color: ${stockColor}; font-weight: bold; margin-bottom: 20px;">
                    Availability: ${product.stock} items left
                </p>

                <p>Size: ${product.size} | Category: ${product.category}</p>
                <p class="desc">${product.description}</p>

                <form action="../../CartServlet" method="POST">
                    <input type="hidden" name="productId" value="${product.id}">
                    <button type="submit" class="add-btn">Add to Cart</button>
                </form>
            </div>
        `;
    });
}