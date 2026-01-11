document.addEventListener("DOMContentLoaded", () => {
    // 1. Fetch Profile Info
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
    .catch(() => window.location.href = "signupLoginpage.html");

    // 2. Fetch Orders
    loadOrderHistory();

    // 3. Logout Logic
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
    const pendingContainer = document.getElementById("pending-list");
    const completedContainer = document.getElementById("completed-list");

    fetch("../../GetOrdersServlet")
    .then(res => res.json())
    .then(orders => {
        // Clear both containers
        pendingContainer.innerHTML = "";
        completedContainer.innerHTML = "";

        let hasPending = false;
        let hasCompleted = false;

        orders.forEach(order => {
            const dateObj = new Date(order.date);
            const dateStr = dateObj.toLocaleDateString("en-US", { month: 'short', day: 'numeric', year: 'numeric' });

            // --- HTML FOR THE ORDER ROW ---
            // Note: We use unique IDs for buttons to attach events easily if needed
            const orderHTML = `
                <div class="order-item">
                    <div class="order-info">
                        <span class="order-id">#ORD-${order.id}</span>
                        <span class="order-date">${dateStr}</span>
                    </div>
            
                    <div style="display:flex; align-items:center; gap:15px;">
                        ${getOrderAction(order)} <button class="view-btn" onclick="openOrderDetails(${order.id})">View Items</button>
                    </div>
            
                    <div class="order-price">RM ${order.total.toFixed(2)}</div>
                </div>
            `;

            // --- SORTING LOGIC ---
            if (order.status === "Completed") {
                completedContainer.innerHTML += orderHTML;
                hasCompleted = true;
            } else {
                // If status is 'Paid' or anything else, it goes to Pending
                pendingContainer.innerHTML += orderHTML;
                hasPending = true;
            }
        });

        // Empty State Messages
        if (!hasPending) pendingContainer.innerHTML = "<p class='empty-msg'>No pending orders.</p>";
        if (!hasCompleted) completedContainer.innerHTML = "<p class='empty-msg' style='color:#666'>No completed orders.</p>";

    })
    .catch(err => console.error("Error loading orders:", err));
}

// Helper to decide if we show a "RECEIVED" button or a "COMPLETED" badge
function getOrderAction(order) {
    if (order.status === "Completed") {
        return `<span class="completed-badge">COMPLETED</span>`;
    } else {
        // It is pending, so show the Button
        return `<button class="received-btn" onclick="markAsReceived(${order.id})">RECEIVED</button>`;
    }
}

// --- ACTION: MARK AS RECEIVED ---
function markAsReceived(orderId) {
    if(!confirm("Confirm you received Order #" + orderId + "?")) return;

    fetch("../../UpdateOrderStatusServlet", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `orderId=${orderId}&status=Completed`
    })
    .then(res => {
        if(res.ok) {
            // Reload lists to move the item to the bottom section
            loadOrderHistory();
        } else {
            alert("Error updating order.");
        }
    });
}

// --- MODAL LOGIC (Same as before) ---
function openOrderDetails(orderId) {
    const modal = document.getElementById("orderModal");
    const list = document.getElementById("modal-items-list");
    list.innerHTML = "<p>Loading items...</p>";
    modal.style.display = "block";

    fetch(`../../GetOrderItemsServlet?orderId=${orderId}`)
    .then(res => res.json())
    .then(items => {
        list.innerHTML = "";
        if (items.length === 0) { list.innerHTML = "<p>No items found.</p>"; return; }
        items.forEach(item => {
            list.innerHTML += `
                <div class="modal-item">
                    <div style="flex:2">
                        <span style="color:#fff; font-weight:bold;">${item.name}</span><br>
                        <span style="font-size:12px; color:#666;">Size: ${item.size}</span>
                    </div>
                    <div style="flex:1; text-align:right;">x${item.qty}</div>
                    <div style="flex:1; text-align:right; color:#e10600;">RM ${item.price.toFixed(2)}</div>
                </div>`;
        });
    });
}

function closeModal() { document.getElementById("orderModal").style.display = "none"; }
window.onclick = function(event) {
    const modal = document.getElementById("orderModal");
    if (event.target == modal) modal.style.display = "none";
}