/* =========================================
   1. SESSION SYNCHRONIZATION
   ========================================= */
(function syncSession() {
    const urlParams = new URLSearchParams(window.location.search);
    const loginSuccess = urlParams.get("loginSuccess");

    if (loginSuccess === "true") {
        localStorage.setItem("isLoggedIn", "true");
        window.history.replaceState({}, document.title, window.location.pathname);
    }
})();

/* =========================================
   2. MAIN LOGIC
   ========================================= */
document.addEventListener("DOMContentLoaded", () => {
    checkLoginState();
    loadProductsFromDB(); // <--- Calls the database
});

/* =========================================
   3. DATABASE FETCHING (AJAX)
   ========================================= */
function loadProductsFromDB() {
    console.log("Attempting to fetch products from Servlet...");

    // Pointing to the Servlet we just created.
    // Since html is in /frontend/html, we go up two levels (../../) to find the servlet mapping
    fetch('../../ProductListServlet')
    .then(response => {
        if (!response.ok) {
            throw new Error("Server responded with " + response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log("Data received from DB:", data);
        renderProducts(data);
        setupFilters(data); 
    })
    .catch(error => {
        console.error("Error fetching data:", error);
        document.getElementById("products").innerHTML = "<p style='color:white; text-align:center;'>Unable to load products. Server error.</p>";
    });
}

function renderProducts(products) {
    const container = document.getElementById("products");
    container.innerHTML = ""; // Clear any loading text

    if (products.length === 0) {
        container.innerHTML = "<p style='color:white; text-align:center;'>No products found in database.</p>";
        return;
    }

    products.forEach(product => {
        // Construct image path
        const imagePath = `../../assets/products/${product.image}`;
        
        const cardHTML = `
            <div class="product-card" data-name="${product.name}" data-category="${product.category}" data-size="${product.size}">
                <img src="${imagePath}" alt="${product.name}" onerror="this.src='https://via.placeholder.com/300x350?text=No+Image'">
                <h3>${product.name}</h3>
                <p>RM ${product.price.toFixed(2)}</p>
            </div>
        `;
        container.innerHTML += cardHTML;
    });
}

/* =========================================
   4. HELPER FUNCTIONS (Login & Filter)
   ========================================= */
function checkLoginState() {
    const loginBtn = document.getElementById("loginBtn");
    const profileBtn = document.getElementById("profileBtn");

    if (!loginBtn || !profileBtn) return;

    const isLoggedIn = localStorage.getItem("isLoggedIn");
    if (isLoggedIn === "true") {
        loginBtn.style.display = "none";
        profileBtn.style.display = "inline-block";
    } else {
        loginBtn.style.display = "inline-block";
        profileBtn.style.display = "none";
    }
}

function setupFilters(allProducts) {
    const searchInput = document.getElementById("searchInput");
    const searchBtn = document.getElementById("searchBtn");
    const typeFilter = document.getElementById("typeFilter"); 
    const sizeFilter = document.getElementById("sizeFilter");

    function applyFilters() {
        // Use live DOM elements because they were created dynamically
        const cards = document.querySelectorAll(".product-card");
        
        const keyword = searchInput.value.toLowerCase();
        const selectedType = typeFilter.value;
        const selectedSize = sizeFilter.value;

        cards.forEach(card => {
            const name = card.getAttribute("data-name").toLowerCase();
            const category = card.getAttribute("data-category");
            const size = card.getAttribute("data-size");

            const matchesKeyword = name.includes(keyword) || category.toLowerCase().includes(keyword);
            const matchesType = (selectedType === "all") || (category === selectedType);
            const matchesSize = (selectedSize === "all") || (size === selectedSize);

            if (matchesKeyword && matchesType && matchesSize) {
                card.style.display = "block";
            } else {
                card.style.display = "none";
            }
        });
    }

    if (searchBtn) searchBtn.addEventListener("click", applyFilters);
    if (searchInput) searchInput.addEventListener("keyup", applyFilters);
    if (typeFilter) typeFilter.addEventListener("change", applyFilters);
    if (sizeFilter) sizeFilter.addEventListener("change", applyFilters);
}

function goHome() {
    window.location.href = "homepage.html";
}
