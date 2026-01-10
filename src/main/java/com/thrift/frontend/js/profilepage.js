document.addEventListener("DOMContentLoaded", function() {
    
    // 1. Fetch User Data (Login Check)
    fetch('GetProfileServlet')
        .then(response => {
            if (response.status === 401) {
                // If checking design on Live Server, ignore this error
                // window.location.href = "signup&login.html?error=Please Login First";
            }
            return response.json();
        })
        .then(user => {
            // Update Profile Info
            document.getElementById('display-username').innerText = user.username;
            document.getElementById('display-email').innerText = user.email;
            const initial = user.username.charAt(0).toUpperCase();
            document.getElementById('avatar-initial').innerText = initial;
            if (user.role) {
                document.getElementById('display-role').innerText = user.role.toUpperCase();
            }
        })
        .catch(err => console.log("Guest mode or Live Server"));

    // 2. ALWAYS Load Orders (So you can see the new button)
    loadOrderHistory();
});

// --- ORDER HISTORY LOGIC ---
function loadOrderHistory() {
    const orderContainer = document.querySelector('.order-list');
    
    // MOCK DATA: This list creates the fake orders you see
    const mockOrders = [
        { id: "ORD-8821", date: "Jan 10, 2026", status: "Pending", price: "RM 120.00" },
        { id: "ORD-8804", date: "Jan 05, 2026", status: "Shipped", price: "RM 59.00" }, // Button appears here
        { id: "ORD-7793", date: "Dec 28, 2025", status: "Arrived", price: "RM 240.00" }
    ];

    // Clear the box before adding new stuff
    orderContainer.innerHTML = '';

    mockOrders.forEach(order => {
        let badgeClass = "status-pending"; 
        let actionHTML = ""; // Default is no button

        // LOGIC: Decide colors and buttons
        if (order.status === "Arrived") {
            badgeClass = "status-arrived";
        } 
        else if (order.status === "Shipped") {
            badgeClass = "status-shipped";
            // Add the button ONLY if status is 'Shipped'
            actionHTML = `<button onclick="markAsArrived(this)" class="confirm-btn">Confirm Receipt</button>`;
        }

        const html = `
            <div class="order-item">
                <div class="order-info">
                    <span class="order-id">#${order.id}</span>
                    <span class="order-date">${order.date}</span>
                </div>
                
                <div style="display: flex; align-items: center;">
                    <span class="status-badge ${badgeClass}">${order.status}</span>
                    ${actionHTML} </div>
                
                <div class="order-price">${order.price}</div>
            </div>
        `;
        orderContainer.innerHTML += html;
    });
}

// --- BUTTON CLICK FUNCTION ---
function markAsArrived(button) {
    // 1. Find the badge next to this button
    const container = button.parentElement;
    const badge = container.querySelector('.status-badge');

    // 2. Change text to ARRIVED
    badge.innerText = "Arrived";

    // 3. Change color to Green
    badge.classList.remove('status-shipped');
    badge.classList.add('status-arrived');

    // 4. Remove the button
    button.remove();

    alert("Thanks! Order marked as received.");
}