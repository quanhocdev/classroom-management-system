document.addEventListener("DOMContentLoaded", function () {
  // 1. Chỉ dùng userRole trong localStorage để nhận biết trạng thái đăng nhập nhanh trên UI
  const userRole = localStorage.getItem("userRole");

  const dynamicContainer = document.getElementById("header-dynamic-container");
  const sidebarPublicContainer = document.getElementById(
    "sidebarPublicContainer",
  );

  if (!dynamicContainer) return;

  // TRƯỜNG HỢP 1: CHƯA ĐĂNG NHẬP (Không có userRole trong localStorage)
  if (!userRole) {
    dynamicContainer.innerHTML = `
      <div class="d-flex align-items-center">
        <button class="navbar-toggler border-0 p-0 me-2" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu">
          <span class="navbar-toggler-icon"></span>
        </button>
        <a href="/trang-chu" class="navbar-brand mb-0 text-primary fw-bold p-0">OurClass</a>
      </div>
      <div>
        <a href="/auth/login" class="btn btn-primary btn-sm px-3">Đăng nhập</a>
      </div>
    `;

    if (sidebarPublicContainer) {
      sidebarPublicContainer.innerHTML = `
        <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu">
          <div class="offcanvas-header border-bottom">
            <h5 class="offcanvas-title text-primary fw-bold">OurClass</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
          </div>
          <div class="offcanvas-body">
            <div class="list-group list-group-flush">
              <a href="/trang-chu" class="list-group-item list-group-item-action">🏠 Trang chủ</a>
              <a href="/about" class="list-group-item list-group-item-action">ℹ️ Giới thiệu</a>
              <hr />
              <a href="/auth/login" class="list-group-item list-group-item-action">🔑 Đăng nhập</a>
            </div>
          </div>
        </div>
      `;
    }
  }

  // TRƯỜNG HỢP 2: ĐÃ ĐĂNG NHẬP
  else {
    if (sidebarPublicContainer) sidebarPublicContainer.innerHTML = "";

    let brandText = "";
    let badgeClass = "";
    let badgeIcon = "";

    switch (userRole.toUpperCase()) {
      case "ADMIN":
        brandText = "Quản lý hệ thống";
        badgeClass = "bg-danger-subtle text-danger";
        badgeIcon = "🛡️ Admin";
        break;
      case "TEACHER":
        brandText = "Giảng dạy";
        badgeClass = "bg-success-subtle text-success";
        badgeIcon = "👨‍🏫 Teacher";
        break;
      case "STUDENT":
        brandText = "Góc học tập";
        badgeClass = "bg-info-subtle text-info";
        badgeIcon = "🎓 Student";
        break;
    }

    dynamicContainer.innerHTML = `
      <div class="d-flex align-items-center">
        <button class="navbar-toggler border-0 p-0 me-2" type="button" id="btn-toggle-sidebar">
          <span class="navbar-toggler-icon"></span>
        </button>
        <span class="navbar-brand mb-0 fw-bold p-0 ${userRole === "ADMIN" ? "text-danger" : userRole === "TEACHER" ? "text-success" : "text-info"}">${brandText}</span>
      </div>
      <div class="d-flex align-items-center gap-3">
        <span class="badge ${badgeClass} px-2 py-1 rounded d-none d-sm-inline">${badgeIcon}</span>
        <a href="#" id="btnLogoutDynamic" class="btn btn-outline-danger btn-sm px-2 py-1" style="font-size: 0.85rem;">Đăng xuất 🚪</a>
      </div>
    `;

    // Sự kiện bấm nút mục lục: Thu gọn / Mở rộng Sidebar
    const toggleBtn = document.getElementById("btn-toggle-sidebar");
    if (toggleBtn) {
      toggleBtn.addEventListener("click", function () {
        document.body.classList.toggle("sidebar-collapsed");
      });
    }

    // Sự kiện Đăng xuất (Logout)
    // Tìm đến khối xử lý Đăng xuất ở cuối file JS của bạn và sửa lại như sau:
    const logoutBtn = document.getElementById("btnLogoutDynamic");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", async function (e) {
        e.preventDefault();

        try {
          // Gửi request logout lên backend để dọn dẹp Cookie
          await fetch("/api/auth/logout", {
            method: "POST",
            credentials: "include",
          });
        } catch (error) {
          console.error("Lỗi khi gửi yêu cầu logout lên server:", error);
        } finally {
          // Dọn sạch localStorage của Client
          localStorage.removeItem("username");
          localStorage.removeItem("userRole");

          // THAY ĐỔI Ở ĐÂY: Chuyển hướng thẳng về trang đăng nhập
          window.location.href = "/auth/login";
        }
      });
    }
  }
});
