document.addEventListener("DOMContentLoaded", () => {
    loadCartFromServer();
});

function loadCartFromServer() {
    fetch('../../GetCartServlet')
    .then(res => res.json())
    .then(rawCartItems => {
        // 1. GROUP ITEMS: Convert raw list [A, B, A] into grouped object {A:{qty:2}, B:{qty:1}}
        const groupedItems = {};
        rawCartItems.forEach(item => {
            // Create a unique key based on ID and Size
            const key = `${item.id}-${item.size}`;
            if (!groupedItems[key]) {
                // If first time seeing this item+size combo, initialize it
                groupedItems[key] = { ...item, quantity: 0 };
            }
            // Increment quantity
            groupedItems[key].quantity++;
        });

        // Convert grouped object back to an array for rendering
        const finalCartList = Object.values(groupedItems);
        renderCart(finalCartList);
    })
    .catch(err => console.error("Error loading cart:", err));
}

function renderCart(items) {
    const container = document.querySelector(".cart");
    const header = container.querySelector("h1");
    
    // Clear old items
    container.querySelectorAll(".cart-item, .empty-msg").forEach(el => el.remove());

    let grandTotal = 0;
    
    if (items.length === 0) {
        const emptyMsg = document.createElement("p");
        emptyMsg.className = "empty-msg";
        emptyMsg.innerText = "Your cart is empty.";
        emptyMsg.style.textAlign = "center";
        emptyMsg.style.color = "#888";
        header.after(emptyMsg);
        document.getElementById("cartTotal").innerText = "0.00";
        return;
    }

    // Reverse items to show newest added at top (optional)
    [...items].reverse().forEach(item => {
        const itemTotal = item.price * item.quantity;
        grandTotal += itemTotal;
        
        const imagePath = item.image ? `../../assets/products/${item.image}` : "https://via.placeholder.com/120x150";
        // Fail-safe for size display
        const displaySize = item.size && item.size !== "undefined" ? item.size : "One Size";

        const itemHTML = document.createElement("div");
        itemHTML.classList.add("cart-item");
        
        // DATA attributes for the buttons
        itemHTML.dataset.productId = item.id;
        itemHTML.dataset.size = item.size; 

        itemHTML.innerHTML = `
            <img src="${imagePath}" alt="${item.name}" onerror="this.src='https://via.placeholder.com/120'">
            
            <div class="item-info">
                <h3>${item.name}</h3>
                <p style="font-size: 13px; color: #888; margin-top: 4px;">Size: ${displaySize}</p>
                <p style="margin-top: 4px;">RM <span class="price">${item.price.toFixed(2)}</span></p>
            </div>
            
            <div class="quantity-control">
                <button class="minus-btn">−</button>
                <span class="quantity">${item.quantity}</span>
                <button class="plus-btn">+</button>
            </div>

            <div class="item-total">
                RM ${itemTotal.toFixed(2)}
            </div>
        `;
        header.after(itemHTML);
    });

    document.getElementById("cartTotal").innerText = grandTotal.toFixed(2);
    attachQuantityListeners();
}

function attachQuantityListeners() {
    // Handle PLUS (+) Click
    document.querySelectorAll('.plus-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const parent = e.target.closest('.cart-item');
            const productId = parent.dataset.productId;
            const size = parent.dataset.size;

            // Use fetch to call the existing CartServlet to add another one
            fetch('../../CartServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `productId=${productId}&selectedSize=${size}`
            }).then(() => {
                // Reload cart to reflect changes
                loadCartFromServer();
            });
        });
    });

    // Handle MINUS (-) Click
    document.querySelectorAll('.minus-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const parent = e.target.closest('.cart-item');
            const productId = parent.dataset.productId;
            const size = parent.dataset.size;

            // Call the NEW Remove servlet
            fetch('../../RemoveFromCartServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `productId=${productId}&size=${size}`
            }).then(() => {
                // Reload cart to reflect changes
                loadCartFromServer();
            });
        });
    });
}