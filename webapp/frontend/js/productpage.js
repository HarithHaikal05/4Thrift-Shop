document.addEventListener("DOMContentLoaded", () => {
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
        
        // Stock Logic: Red if low, "Sold Out" if 0
        const stockColor = product.stock < 5 ? "#e10600" : "#fff";
        const stockText = product.stock > 0 ? `${product.stock} items left` : "SOLD OUT";
        const isDisabled = product.stock <= 0 ? "disabled" : "";
        const btnText = product.stock <= 0 ? "Out of Stock" : "Add to Cart";
        const btnStyle = product.stock <= 0 ? "background: #444; cursor: not-allowed;" : "";

        container.innerHTML = `
            <div class="left">
                <img src="${imagePath}" alt="${product.name}" onerror="this.src='https://via.placeholder.com/500'">
            </div>
            <div class="right">
                <h1>${product.name}</h1>
                <span class="price">RM ${product.price.toFixed(2)}</span>
                
                <p style="color: ${stockColor}; font-weight: bold; margin-bottom: 20px;">
                    Availability: ${stockText}
                </p>

                <p class="desc">${product.description}</p>

                <form action="../../CartServlet" method="POST">
                    <input type="hidden" name="productId" value="${product.id}">
                    
                    <div style="margin-bottom: 20px;">
                        <label style="display:block; color:#888; font-size:12px; letter-spacing:1px; margin-bottom:8px;">SELECT SIZE</label>
                        <select name="selectedSize" style="width:100%; padding:12px; background:#111; color:#fff; border:1px solid #444; border-radius:4px;">
                            <option value="S">Small (S)</option>
                            <option value="M">Medium (M)</option>
                            <option value="L">Large (L)</option>
                            <option value="XL">Extra Large (XL)</option>
                        </select>
                    </div>

                    <button type="submit" class="add-btn" ${isDisabled} style="${btnStyle}">${btnText}</button>
                </form>

                <div class="meta" style="margin-top: 20px; color: #666; font-size: 13px;">
                    Category: <span style="color:#aaa">${product.category}</span>
                </div>
            </div>
        `;
    });
}