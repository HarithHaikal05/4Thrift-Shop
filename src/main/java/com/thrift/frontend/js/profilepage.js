document.addEventListener("DOMContentLoaded", () => {

  /* ===============================
     AUTH GUARD (FRONTEND)
  ================================ */
  if (localStorage.getItem("isLoggedIn") !== "true") {
    window.location.href = "signup&login.html?error=Please login first";
    return;
  }

  /* ===============================
     FETCH PROFILE DATA
  ================================ */
  fetch("GetProfileServlet")
    .then(response => {
      if (response.status === 401) {
        throw new Error("Not logged in");
      }
      return response.json();
    })
    .then(user => {
      document.getElementById("display-username").innerText = user.username;
      document.getElementById("display-email").innerText = user.email;

      const initial = user.username.charAt(0).toUpperCase();
      document.getElementById("avatar-initial").innerText = initial;

      if (user.role) {
        document.getElementById("display-role").innerText = user.role.toUpperCase();
      }
    })
    .catch(() => {
      console.log("Guest mode / Live Server");
    });

  /* ===============================
     LOAD ORDER HISTORY
  ================================ */
  loadOrderHistory();

  /* ===============================
     LOGOUT BUTTON
  ================================ */
  const logoutBtn = document.getElementById("logoutBtn");

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("isLoggedIn");
      window.location.href = "homepage.html";
    });
  }

});

/* ===============================
   ORDER HISTORY LOGIC
================================ */
function loadOrderHistory() {
  const orderContainer = document.querySelector(".order-list");

  const mockOrders = [
    { id: "ORD-8821", date: "Jan 10, 2026", status: "Pending", price: "RM 120.00" },
    { id: "ORD-8804", date: "Jan 05, 2026", status: "Shipped", price: "RM 59.00" },
    { id: "ORD-7793", date: "Dec 28, 2025", status: "Arrived", price: "RM 240.00" }
  ];

  orderContainer.innerHTML = "";

  mockOrders.forEach(order => {
    let badgeClass = "status-pending";
    let actionHTML = "";

    if (order.status === "Arrived") {
      badgeClass = "status-arrived";
    } 
    else if (order.status === "Shipped") {
      badgeClass = "status-shipped";
      actionHTML = `
        <button class="confirm-btn" onclick="markAsArrived(this)">
          Confirm Receipt
        </button>
      `;
    }

    orderContainer.innerHTML += `
      <div class="order-item">
        <div class="order-info">
          <span class="order-id">#${order.id}</span>
          <span class="order-date">${order.date}</span>
        </div>

        <div style="display:flex; align-items:center; gap:10px;">
          <span class="status-badge ${badgeClass}">${order.status}</span>
          ${actionHTML}
        </div>

        <div class="order-price">${order.price}</div>
      </div>
    `;
  });
}

/* ===============================
   CONFIRM RECEIPT BUTTON
================================ */
function markAsArrived(button) {
  const container = button.parentElement;
  const badge = container.querySelector(".status-badge");

  badge.innerText = "Arrived";
  badge.classList.remove("status-shipped");
  badge.classList.add("status-arrived");

  button.remove();
  alert("Thanks! Order marked as received.");
}
