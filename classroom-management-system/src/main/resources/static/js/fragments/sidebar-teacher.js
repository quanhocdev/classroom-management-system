document.addEventListener("DOMContentLoaded", function () {
  const btnLogout = document.getElementById("btnLogoutTeacher");

  if (btnLogout) {
    btnLogout.addEventListener("click", async function (e) {
      e.preventDefault();
      if (confirm("Thầy/Cô có chắc chắn muốn đăng xuất không?")) {
        try {
          await fetch("/logout", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
          });
          localStorage.clear();
          window.location.href = "/";
        } catch (error) {
          console.error("Lỗi đăng xuất:", error);
        }
      }
    });
  }

  // Active menu dựa vào URL
  const currentUrl = window.location.pathname;
  const menuLinks = document.querySelectorAll(".sidebar-menu li a");
  menuLinks.forEach((link) => {
    if (link.getAttribute("href") === currentUrl) {
      link.classList.add("active");
    }
  });
});
