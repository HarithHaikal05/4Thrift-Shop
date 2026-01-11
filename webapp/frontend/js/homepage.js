/* =========================================
   1. SESSION SYNCHRONIZATION (Runs Immediately)
   ========================================= */
(function syncSession() {
    const urlParams = new URLSearchParams(window.location.search);
    const loginSuccess = urlParams.get("loginSuccess");

    if (loginSuccess === "true") {
        console.log("Login detected via URL. setting isLoggedIn = true");
        localStorage.setItem("isLoggedIn", "true");
        
        // Remove the parameter from the URL bar so it looks clean
        window.history.replaceState({}, document.title, window.location.pathname);
    }
})();

/* =========================================
   2. MAIN LOGIC (Runs when page loads)
   ========================================= */
document.addEventListener("DOMContentLoaded", () => {

    // --- A. RUN LOGIN CHECK ---
    checkLoginState();

    // --- B. SETUP SEARCH & FILTERS ---
    setupFilters();

    // --- C. OPTIONAL: LOAD DYNAMIC ITEMS ---
    // Only run this if you want to load items from LocalStorage 
    // instead of the hardcoded HTML ones.
    // loadItems(); 
});

/* =========================================
   3. HELPER FUNCTIONS
   ========================================= */

function checkLoginState() {
    const loginBtn = document.getElementById("loginBtn");
    const profileBtn = document.getElementById("profileBtn");

    // Safety check: ensure buttons exist in HTML
    if (!loginBtn || !profileBtn) {
        console.error("Error: Login or Profile button not found in HTML.");
        return;
    }

    const isLoggedIn = localStorage.getItem("isLoggedIn");

    if (isLoggedIn === "true") {
        console.log("User is logged in. Showing Profile button.");
        loginBtn.style.display = "none";
        profileBtn.style.display = "inline-block";
    } else {
        console.log("User is NOT logged in. Showing Login button.");
        loginBtn.style.display = "inline-block";
        profileBtn.style.display = "none";
    }
}

function setupFilters() {
    const searchInput = document.getElementById("searchInput");
    const searchBtn = document.getElementById("searchBtn");
    const typeFilter = document.getElementById("typeFilter"); // Matches your HTML <select>
    const products = document.querySelectorAll(".product-card");

    // Function to filter based on all inputs
    function applyFilters() {
        const keyword = searchInput ? searchInput.value.toLowerCase() : "";
        const selectedType = typeFilter ? typeFilter.value : "all";

        products.forEach(product => {
            const name = product.dataset.name.toLowerCase();
            const category = product.dataset.category; // e.g. "Denim"
            
            // Check Keyword
            const matchesKeyword = name.includes(keyword) || category.toLowerCase().includes(keyword);
            
            // Check Dropdown Type
            const matchesType = (selectedType === "all") || (category === selectedType);

            // Toggle Visibility
            if (matchesKeyword && matchesType) {
                product.style.display = "block";
            } else {
                product.style.display = "none";
            }
        });
    }

    // Add Event Listeners
    if (searchBtn) searchBtn.addEventListener("click", applyFilters);
    if (searchInput) searchInput.addEventListener("keyup", applyFilters);
    if (typeFilter) typeFilter.addEventListener("change", applyFilters);
}

// Global function for the logo click
function goHome() {
    window.location.href = "homepage.html";
}