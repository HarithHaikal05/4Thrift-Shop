document.addEventListener("DOMContentLoaded", () => {
    /* FETCH PROFILE INFO */
    fetch("../../GetProfileServlet")
    .then(res => {
        if (!res.ok) throw new Error("Not Logged In");
        return res.json();
    })
    .then(user => {
        document.getElementById("display-username").innerText = user.username;
        document.getElementById("display-email").innerText = user.email;
        document.getElementById("avatar-initial").innerText = user.username.charAt(0).toUpperCase();
        document.getElementById("display-role").innerText = user.role.toUpperCase();
    })
    .catch(err => {
        console.log(err);
        window.location.href = "signupLoginpage.html"; // Redirect if not logged in
    });

    /* FETCH REAL ORDERS FROM DB */
    loadOrderHistory();

    /* LOGOUT LOGIC */
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", (e) => {
            e.preventDefault();
            localStorage.removeItem("isLoggedIn");
            window.location.href = "../../LogoutServlet";
        });
    }
});

function loadOrderHistory() {
    const orderContainer = document.querySelector(".order-list");

    fetch("../../GetOrdersServlet")
    .then(res => res.json())
    .then(orders => {
        orderContainer.innerHTML = "";

        if (orders.length === 0) {
            orderContainer.innerHTML = "<p class='empty-msg'>No orders yet.</p>";
            return;
        }

        orders.forEach(order => {
            // Determine Badge Color
            let badgeClass = "status-pending";
            if (order.status === "Arrived") badgeClass = "status-arrived";
            else if (order.status === "Shipped") badgeClass = "status-shipped";

            // Format Date (Clean up the Java timestamp)
            const dateObj = new Date(order.date);
            const dateStr = dateObj.toLocaleDateString("en-US", { year: 'numeric', month: 'short', day: 'numeric' });

            orderContainer.innerHTML += `
                <div class="order-item">
                    <div class="order-info">
                        <span class="order-id">#ORD-${order.id}</span>
                        <span class="order-date">${dateStr}</span>
                    </div>
            
                    <div style="display:flex; align-items:center; gap:10px;">
                        <span class="status-badge ${badgeClass}">${order.status}</span>
                    </div>
            
                    <div class="order-price">RM ${order.total.toFixed(2)}</div>
                </div>
            `;
        });
    })
    .catch(err => console.error("Error loading orders:", err));
}