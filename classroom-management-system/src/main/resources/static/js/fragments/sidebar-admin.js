document.addEventListener("DOMContentLoaded", function () {
  const btnLogout = document.getElementById("btnLogout");

  if (btnLogout) {
    btnLogout.addEventListener("click", function (e) {
      e.preventDefault();

      if (confirm("Bạn có chắc chắn muốn đăng xuất không?")) {
        localStorage.removeItem("accessToken");

        localStorage.removeItem("userRole");

        localStorage.removeItem("username");

        window.location.href = "/";
      }
    });
  }

  const currentUrl = window.location.pathname;

  const menuLinks = document.querySelectorAll(".sidebar-menu li a");

  menuLinks.forEach((link) => {
    if (link.getAttribute("href") === currentUrl) {
      menuLinks.forEach((item) => item.classList.remove("active"));

      link.classList.add("active");
    }
  });
});
