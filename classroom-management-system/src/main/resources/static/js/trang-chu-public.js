/* /js/trang-chu-public.js */

document.addEventListener("DOMContentLoaded", function () {
  console.log("Public homepage initialized.");

  const token = localStorage.getItem("accessToken");

  const role = localStorage.getItem("userRole");

  const heroButtons = document.querySelector(".hero-buttons");

  if (token && heroButtons) {
    const primaryBtn = heroButtons.querySelector(".btn-hero-primary");

    if (primaryBtn) {
      primaryBtn.innerText = "Đi tới trang quản lý lớp";

      primaryBtn.addEventListener("click", function (e) {
        e.preventDefault();

        if (role === "ADMIN") {
          window.location.href = "/admin/trang-chu";
        } else if (role === "TEACHER") {
          window.location.href = "/teacher/trang-chu";
        } else if (role === "STUDENT") {
          window.location.href = "/student/trang-chu";
        } else {
          window.location.href = "/auth/login";
        }
      });
    }
  }
});
