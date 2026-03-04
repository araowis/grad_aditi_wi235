const BASE_URL = "http://localhost:3000";

function requireLogin() {
  const user = localStorage.getItem("username");
  if (!user) {
    alert("Please login first.");
    window.location.href = "../pages/netbanking.html";
    return null;
  }
  return user;
}

function openAccount(e) {
  const formData = {
    fullName: document.getElementById("fullName").value,
    dob: document.getElementById("dob").value,
    gender: document.getElementById("gender").value,
    phone: document.getElementById("phone").value,
    email: document.getElementById("email").value,
    address: document.getElementById("address").value,
    accountType: document.getElementById("accountType").value,
    initialDeposit: document.getElementById("initialDeposit").value,
    idProofType: document.getElementById("idProofType").value,
    idNumber: document.getElementById("idNumber").value,
  };

  console.log("Open Account Form Data:", formData);

  document.getElementById("result").innerText =
    "Application submitted successfully! Check console for data.";
}

async function closeAccount() {
  const user = requireLogin();
  if (!user) return;

  const res = await fetch(BASE_URL + "/close-account", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username: user }),
  });

  const data = await res.json();
  document.getElementById("result").innerText = data.message;
}

async function deposit() {
  const user = requireLogin();
  if (!user) return;

  const amount = document.getElementById("amount").value;

  const res = await fetch(BASE_URL + "/deposit", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username: user, amount: parseFloat(amount) }),
  });

  const data = await res.json();
  document.getElementById("result").innerText = data.message;
}

async function applyLoan() {
  const user = requireLogin();
  if (!user) return;

  const amount = document.getElementById("amount").value;

  const res = await fetch(BASE_URL + "/loan", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username: user, amount: parseFloat(amount) }),
  });

  const data = await res.json();
  document.getElementById("result").innerText = data.message;
}
