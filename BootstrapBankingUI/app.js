document.addEventListener("DOMContentLoaded", () => {
  const menuToggle = document.getElementById("menu-toggle");
  const sidebar = document.getElementById("sidebar");
  const content = document.getElementById("main-content");
  const links = sidebar.querySelectorAll("a");
  const sections = content.querySelectorAll("section");
  const loginSection = document.getElementById("login");
  const netbankingSection = document.getElementById("netbanking");

  // Toggle sidebar
  menuToggle.addEventListener("click", () => {
    sidebar.classList.toggle("active");
    content.classList.toggle("shift");
  });

  // Show section based on click
  links.forEach((link) => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const sectionId = link.getAttribute("data-section");

      // Hide all sections
      sections.forEach((section) => section.classList.add("d-none"));

      // If Netbanking is clicked, show login first
      if (sectionId === "netbanking") {
        loginSection.classList.remove("d-none");
      } else {
        document.getElementById(sectionId).classList.remove("d-none");
      }

      // Close sidebar on mobile
      if (sidebar.classList.contains("active")) {
        sidebar.classList.remove("active");
        content.classList.remove("shift");
      }
    });
  });

  // Handle login form submission
  document
    .getElementById("login-form")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;

      if (username === "user" && password === "password") {
        alert("Login successful!");
        loginSection.classList.add("d-none"); // hide login
        netbankingSection.classList.remove("d-none"); // show netbanking
      } else {
        alert("Invalid credentials");
      }
    });

  // Show home by default
  document.getElementById("home").classList.remove("d-none");
});
