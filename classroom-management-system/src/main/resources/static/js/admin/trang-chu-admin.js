document.addEventListener("DOMContentLoaded", async function () {
  // BƯỚC 1: KHÔNG đọc từ localStorage nữa.
  // Gọi trực tiếp API Dashboard. Trình duyệt tự đính kèm HttpOnly Cookie "accessToken" qua fetch.
  try {
    const response = await fetch("/api/admin/dashboard", {
      method: "GET",
      // KHÔNG cần truyền header "Authorization: Bearer..." thủ công bằng tay nữa!
    });

    // BƯỚC 2: Nếu API trả về 401 (Chưa đăng nhập) hoặc 403 (Sai quyền Admin)
    if (response.status === 401 || response.status === 403) {
      // Dọn dẹp thông tin UI thừa (nếu có)
      localStorage.removeItem("username");
      localStorage.removeItem("userRole");

      window.location.href = "/auth/login";
      return;
    }

    const data = await response.json();
    console.log("Admin data:", data);

    const adminName = document.getElementById("adminName");
    if (adminName) {
      adminName.innerText = data.username;
    }
  } catch (error) {
    console.error("Không gọi được API admin:", error);
    // Nếu lỗi kết nối server, tạm thời giữ nguyên hoặc đá về tùy bạn chọn
    window.location.href = "/auth/login";
  }
});
