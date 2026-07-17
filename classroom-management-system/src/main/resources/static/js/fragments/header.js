document.addEventListener("DOMContentLoaded", async function () {
  const dynamicContainer = document.getElementById("header-dynamic-container");
  const sidebarPublicContainer = document.getElementById(
    "sidebarPublicContainer",
  );

  if (!dynamicContainer) return;

  let userRole = null;
  let isLoggedIn = false;

  try {
    // Gọi API check session ngầm, trình duyệt sẽ tự đính kèm Cookie lên
    const checkResponse = await fetch("/api/auth/me", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include", // Luôn gửi kèm cookie để backend check
    });

    if (checkResponse.ok) {
      const userData = await checkResponse.json();

      // Kiểm tra trạng thái đăng nhập thực tế mà Server trả về
      if (userData.isLoggedIn) {
        userRole = userData.role;
        isLoggedIn = true;
        localStorage.setItem("userRole", userRole); // Đồng bộ nhanh thông tin hiển thị
        localStorage.setItem("username", userData.username);
      } else {
        // Nếu Server bảo chưa đăng nhập, dọn dẹp sạch sẽ
        localStorage.removeItem("userRole");
        localStorage.removeItem("username");
      }
    }
  } catch (error) {
    console.error("Lỗi kết nối đến máy chủ:", error);
    // Phương án dự phòng duy nhất khi mất mạng hoàn toàn
    userRole = localStorage.getItem("userRole");
    isLoggedIn = !!userRole;
  }

  // =========================================================================
  // TRƯỜNG HỢP 1: CHƯA ĐĂNG NHẬP (Hoặc Token hết hạn) -> Vẽ giao diện Public
  // =========================================================================
  if (!isLoggedIn || !userRole) {
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

  // =========================================================================
  // TRƯỜNG HỢP 2: ĐÃ ĐĂNG NHẬP HỢP LỆ -> Vẽ giao diện Quản trị
  // =========================================================================
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

    // Sự kiện nút đóng/mở Sidebar nhanh
    const toggleBtn = document.getElementById("btn-toggle-sidebar");
    if (toggleBtn) {
      toggleBtn.addEventListener("click", function () {
        document.body.classList.toggle("sidebar-collapsed");
      });
    }

    // Sự kiện xử lý Đăng xuất (Logout)
    const logoutBtn = document.getElementById("btnLogoutDynamic");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", async function (e) {
        e.preventDefault();

        try {
          await fetch("/api/auth/logout", {
            method: "POST",
            credentials: "include",
          });
        } catch (error) {
          console.error("Lỗi khi gửi yêu cầu logout lên server:", error);
        } finally {
          localStorage.removeItem("username");
          localStorage.removeItem("userRole");
          window.location.href = "/auth/login";
        }
      });
    }
  }
});
