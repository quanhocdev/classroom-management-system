/* /js/fragments/header.js */
document.addEventListener("DOMContentLoaded", function () {
  // Logic kiểm tra trạng thái đăng nhập từ localStorage hoặc Cookie (nếu có)
  const token =
    localStorage.getItem("accessToken") ||
    document.cookie.includes("loggedIn=true");
  const authZone = document.getElementById("auth-zone");

  if (token && authZone) {
    // Sau này bạn có thể bổ sung API lấy thông tin user để hiển thị tên ở đây
    authZone.innerHTML = `
            <span class="user-welcome" style="margin-right: 15px; color: #5f6368;">Xin chào!</span>
            <a href="#" id="btnLogout" class="btn-login" style="color: #d93025;">Đăng xuất</a>
        `;

    document
      .getElementById("btnLogout")
      .addEventListener("click", function (e) {
        e.preventDefault();
        // Xử lý xóa session/token
        localStorage.clear();
        // Redirect về trang chủ public
        window.location.href = "/";
      });
  }
});
