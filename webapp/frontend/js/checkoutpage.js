document.addEventListener("DOMContentLoaded", () => {
    // Fetch cart data to display the total
    fetch('../../GetCartServlet')
    .then(res => res.json())
    .then(items => {
        if (items.length === 0) {
            alert("Your cart is empty!");
            window.location.href = "cartpage.html";
            return;
        }

        let total = 0;
        items.forEach(item => {
            // Note: GetCartServlet usually returns flat list. 
            // If you updated it to return grouped, adjust logic. 
            // Assuming flat list [ItemA, ItemA, ItemB]:
            total += item.price;
        });

        document.getElementById("displayTotal").innerText = total.toFixed(2);
    })
    .catch(err => console.error("Error loading cart for checkout:", err));
});