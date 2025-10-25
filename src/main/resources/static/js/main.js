// === API Base & Token ===
const API_URL = "/api/cars";
const token = localStorage.getItem("token");
if (!token) window.location.href = "login.html";

// === Helper ===
function escapeHtml(text) {
  if (text == null) return "";
  return String(text).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
}

// === DOM refs ===
const carTableBody = document.getElementById("carTableBody");
const modalBackdrop = document.getElementById("modalBackdrop");
const carForm = document.getElementById("carForm");
const addNewBtn = document.getElementById("addNewBtn");
const cancelBtn = document.getElementById("cancelBtn");
const logoutForm = document.getElementById("logoutForm");
const modalTitle = document.getElementById("modalTitle");

// === Modal show/hide using Tailwind classes ===
function showModal() {
  modalBackdrop.classList.remove("hidden");
  modalBackdrop.classList.add("flex");
}
function hideModal() {
  modalBackdrop.classList.remove("flex");
  modalBackdrop.classList.add("hidden");
  carForm.reset();
  document.getElementById("carId").value = "";
}

// === clear error visuals ===
function clearErrors() {
  document.querySelectorAll(".error").forEach(e => e.classList.remove("show"));
  ["licensePlateNumber","make","model","year","color","bodyType","engineType","transmission"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.style.borderColor = "";
  });
}

// === Render table ===
function renderCarTable(cars) {
  carTableBody.innerHTML = "";
  cars.forEach((car, index) => {
    // alternate row colors: even = black, odd = dark gray
    const tr = document.createElement("tr");
    tr.className = index % 2 === 0 ? "bg-black hover:bg-gray-700" : "bg-gray-800 hover:bg-gray-700";
    tr.innerHTML = `
      <td class="py-3 px-4">${car.carId ?? ""}</td>
      <td class="py-3 px-4">${escapeHtml(car.licensePlateNumber)}</td>
      <td class="py-3 px-4">${escapeHtml(car.make)}</td>
      <td class="py-3 px-4">${escapeHtml(car.model)}</td>
      <td class="py-3 px-4">${car.year ?? ""}</td>
      <td class="py-3 px-4">${escapeHtml(car.color)}</td>
      <td class="py-3 px-4">${escapeHtml(car.bodyType)}</td>
      <td class="py-3 px-4">${escapeHtml(car.engineType)}</td>
      <td class="py-3 px-4">${escapeHtml(car.transmission)}</td>
      <td class="py-3 px-4 text-center">
        <button class="mr-2 text-orange-400 hover:text-orange-300" onclick="window.editCar(${car.carId})">✏️ Edit</button>
        <button class="text-red-400 hover:text-red-300" onclick="window.deleteCar(${car.carId})">🗑️ Delete</button>
      </td>
    `;
    carTableBody.appendChild(tr);
  });
}

// === Load cars ===
function fetchCars() {
  fetch(API_URL, { headers: { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" } })
    .then(res => {
      if (res.status === 401) { localStorage.removeItem("token"); window.location.href = "/login.html"; return; }
      if (!res.ok) throw new Error("Failed to fetch");
      return res.json();
    })
    .then(renderCarTable)
    .catch(err => console.error("Error fetching cars:", err));
}

// === Open Add Modal ===
function openCreateModal() {
  modalTitle.textContent = "Add Car";
  carForm.reset();
  document.getElementById("carId").value = "";
  clearErrors();
  showModal();
}

// === Validate form (same rules as before) ===
function validateForm() {
  let ok = true;
  const required = ["licensePlateNumber","make","model","color","bodyType","engineType","transmission"];
  required.forEach(id => {
    const el = document.getElementById(id), err = document.getElementById(`error-${id}`);
    if (!el || !err) return;
    if (!el.value || el.value.trim() === "") { err.classList.add("show"); el.style.borderColor = "#ff6b6b"; ok = false; } else { err.classList.remove("show"); el.style.borderColor = ""; }
  });
  const yearEl = document.getElementById("year"), yearVal = yearEl.value.trim(), num = parseInt(yearVal,10);
  const yearErr = document.getElementById("error-year");
  if (!yearVal || isNaN(num) || num < 1990 || num > 2025) { yearErr.classList.add("show"); yearEl.style.borderColor = "#ff6b6b"; ok = false; } else { yearErr.classList.remove("show"); yearEl.style.borderColor = ""; }
  return ok;
}

// === Save car (POST / PUT) ===
function saveCar(e) {
  e.preventDefault();
  clearErrors();
  if (!validateForm()) return;

  const payload = {
    licensePlateNumber: document.getElementById("licensePlateNumber").value.trim(),
    make: document.getElementById("make").value.trim(),
    model: document.getElementById("model").value.trim(),
    year: parseInt(document.getElementById("year").value.trim(), 10),
    color: document.getElementById("color").value.trim(),
    bodyType: document.getElementById("bodyType").value.trim(),
    engineType: document.getElementById("engineType").value.trim(),
    transmission: document.getElementById("transmission").value.trim()
  };

  const id = document.getElementById("carId").value;
  const method = id ? "PUT" : "POST";
  const url = id ? `${API_URL}/${id}` : API_URL;

  fetch(url, {
    method,
    headers: { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (res.status === 401) { localStorage.removeItem("token"); window.location.href = "/login.html"; return; }
      if (!res.ok) return res.text().then(t => { throw new Error(t || `Status ${res.status}`); });
      return res.json().catch(() => null);
    })
    .then(() => { hideModal(); fetchCars(); })
    .catch(err => console.error("Error saving car:", err));
}

// === Edit car (global) ===
window.editCar = function(id) {
  fetch(`${API_URL}/${id}`, { headers: { "Authorization": `Bearer ${token}` } })
    .then(res => {
      if (res.status === 401) { localStorage.removeItem("token"); window.location.href = "/login.html"; return; }
      if (!res.ok) throw new Error("Not found");
      return res.json();
    })
    .then(car => {
      modalTitle.textContent = "Edit Car";
      document.getElementById("carId").value = car.carId ?? "";
      document.getElementById("licensePlateNumber").value = car.licensePlateNumber ?? "";
      document.getElementById("make").value = car.make ?? "";
      document.getElementById("model").value = car.model ?? "";
      document.getElementById("year").value = car.year ?? "";
      document.getElementById("color").value = car.color ?? "";
      document.getElementById("bodyType").value = car.bodyType ?? "";
      document.getElementById("engineType").value = car.engineType ?? "";
      document.getElementById("transmission").value = car.transmission ?? "";
      clearErrors();
      showModal();
    })
    .catch(err => console.error("Error loading car:", err));
};

// === Delete car (global) ===
// === Delete Car ===
function deleteCar(id) {
  // No confirm() popup — delete directly
  fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  })
    .then(res => {
      if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "login.html";
        return;
      }
      if (res.ok) {
        // Refresh table on success
        fetchCars();
      } else {
        // Log actual backend error for debugging
        res.text().then(text => console.error("Delete failed:", text));
      }
    })
    .catch(err => console.error("Error deleting car:", err));
}


// === INIT ===
document.addEventListener("DOMContentLoaded", () => {
  fetchCars();
  addNewBtn.addEventListener("click", e => { e.preventDefault(); openCreateModal(); });
  cancelBtn.addEventListener("click", e => { e.preventDefault(); hideModal(); });
  carForm.addEventListener("submit", saveCar);

  ["licensePlateNumber","make","model","year","color","bodyType","engineType","transmission"].forEach(id => {
    const el = document.getElementById(id);
    if (!el) return;
    ["input","change"].forEach(evt => el.addEventListener(evt, () => {
      const err = document.getElementById(`error-${id}`);
      if (err) err.classList.remove("show");
      el.style.borderColor = "";
    }));
  });

 if (logoutForm) {
  logoutForm.addEventListener("submit", e => {
    e.preventDefault();
    fetch("http://localhost:8080/api/auth/logout", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${localStorage.getItem('token')}`,
      },
    })
      .then(res => res.json())
      .then(data => {
        console.log(data.message);
        localStorage.removeItem('token');
        window.location.href = "login.html";
      })
      .catch(err => console.error("Logout failed:", err));
  });
}
});
