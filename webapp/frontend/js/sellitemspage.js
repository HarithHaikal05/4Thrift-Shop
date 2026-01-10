document.getElementById("sellForm").addEventListener("submit", (e) => {
  e.preventDefault();

  const reader = new FileReader();
  const file = document.getElementById("itemImage").files[0];

  reader.onload = function () {
    const newItem = {
      id: Date.now(),
      name: document.getElementById("itemName").value,
      price: document.getElementById("itemPrice").value,
      size: document.getElementById("itemSize").value,
      category: document.getElementById("itemCategory").value,
      desc: document.getElementById("itemDesc").value,
      image: reader.result
    };

    const items = JSON.parse(localStorage.getItem("items")) || [];
    items.push(newItem);
    localStorage.setItem("items", JSON.stringify(items));

    window.location.href = "homepage.html";
  };

  reader.readAsDataURL(file);
});
