document.addEventListener("DOMContentLoaded", function() {
    
    // 1. Fetch User Data (Your existing code)
    fetch('GetProfileServlet')
        .then(response => {
            if (response.status === 401) {
                window.location.href = "signup&login.html?error=Please Login First";
                throw new Error("Not logged in");
            }
            return response.json();
        })
        .then(user => {
            document.getElementById('display-username').innerText = user.username;
            document.getElementById('display-email').innerText = user.email;
            const initial = user.username.charAt(0).toUpperCase();
            document.getElementById('avatar-initial').innerText = initial;
            if (user.role) {
                document.getElementById('display-role').innerText = user.role.toUpperCase();
            }
            
            // 2. AFTER loading user, Load the Orders
            loadOrderHistory();
        })
        .catch(error => console.log("Error loading profile:", error));

});

// --- NEW FUNCTION: MOCK ORDER HISTORY ---
function loadOrderHistory() {
    const orderContainer = document.querySelector('.order-list');
    
    // SIMULATED DATA (Since we don't have a GetOrdersServlet yet)
    // You can edit this list to show whatever you want for the demo!
    const mockOrders = [
        { id: "ORD-8821", date: "Jan 10, 2026", status: "Pending", price: "RM 120.00" },
        { id: "ORD-8804", date: "Jan 05, 2026", status: "Shipped", price: "RM 59.00" },
        { id: "ORD-7793", date: "Dec 28, 2025", status: "Arrived", price: "RM 240.00" }
    ];

    // Clear the "Empty" message
    orderContainer.innerHTML = '';

    // Create HTML for each order
    mockOrders.forEach(order => {
        
        // Decide the badge color based on text
        let badgeClass = "status-pending"; // Default
        if (order.status === "Arrived") badgeClass = "status-arrived";
        if (order.status === "Shipped") badgeClass = "status-shipped";

        const html = `
            <div class="order-item">
                <div class="order-info">
                    <span class="order-id">#${order.id}</span>
                    <span class="order-date">${order.date}</span>
                </div>
                
                <span class="status-badge ${badgeClass}">${order.status}</span>
                
                <div class="order-price">${order.price}</div>
            </div>
        `;
        
        orderContainer.innerHTML += html;
    });
}