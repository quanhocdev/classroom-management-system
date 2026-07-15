/* /static/js/fragments/sidebar-admin.js */
document.addEventListener("DOMContentLoaded", function () {
  const btnLogout = document.getElementById("btnLogout");

  if (btnLogout) {
    btnLogout.addEventListener("click", async function (e) {
      e.preventDefault();

      if (confirm("Bạn có chắc chắn muốn đăng xuất không?")) {
        try {
          // Gọi tới API /logout mặc định của Spring Security
          const response = await fetch("/logout", {
            method: "POST",
            headers: {
              // Spring Security yêu cầu phương thức POST cho Logout khi tắt CSRF
              "Content-Type": "application/x-www-form-urlencoded",
            },
          });

          // Xóa thông tin lưu ở LocalStorage của trình duyệt
          localStorage.removeItem("accessToken");
          localStorage.removeItem("username");

          // CustomLogoutSuccessHandler sẽ lo việc điều hướng,
          // nhưng ta chủ động ép chuyển hướng về trang chủ public cho chắc chắn
          window.location.href = "/";
        } catch (error) {
          console.error("Đã xảy ra lỗi khi đăng xuất:", error);
          alert("Không thể kết nối tới máy chủ để đăng xuất.");
        }
      }
    });
  }

  // Tự động active màu menu dựa theo URL hiện tại của trình duyệt
  const currentUrl = window.location.pathname;
  const menuLinks = document.querySelectorAll(".sidebar-menu li a");

  menuLinks.forEach((link) => {
    if (link.getAttribute("href") === currentUrl) {
      menuLinks.forEach((item) => item.classList.remove("active"));
      link.classList.add("active");
    }
  });
});
