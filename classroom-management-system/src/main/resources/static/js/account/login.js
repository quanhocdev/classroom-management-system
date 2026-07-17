document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  const messageBox = document.getElementById("message");

  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    messageBox.innerText = "Đang xác thực...";
    messageBox.className = "auth-message";

    const data = {
      username: document.getElementById("username").value,
      password: document.getElementById("password").value,
    };

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include", // Rất quan trọng: Bắt buộc để trình duyệt nhận và lưu HttpOnly Cookie từ API trả về
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      const result = await response.json();

      if (response.ok) {
        messageBox.innerText = "Đăng nhập thành công!";
        messageBox.className = "auth-message success";

        /*
         * 1. KHÔNG LƯU ACCESS TOKEN VÀO LOCALSTORAGE NỮA!
         * Trình duyệt đã tự động lưu trữ 'accessToken' vào Cookie HttpOnly bảo mật.
         */

        /*
         * 2. Chỉ lưu thông tin không nhạy cảm để hiển thị nhanh trên giao diện (Tùy chọn)
         * (Ví dụ: Hiển thị chữ "Chào, [username]" ở góc màn hình)
         */
        localStorage.setItem("username", result.username);
        localStorage.setItem("userRole", result.role);

        setTimeout(function () {
          if (result.role === "ADMIN") {
            window.location.href = "/admin/trang-chu";
          } else if (result.role === "TEACHER") {
            window.location.href = "/teacher/trang-chu";
          } else if (result.role === "STUDENT") {
            window.location.href = "/student/trang-chu";
          } else {
            window.location.href = "/";
          }
        }, 800);
      } else {
        messageBox.innerText = result.message;
        messageBox.className = "auth-message error";
      }
    } catch (error) {
      messageBox.innerText = "Không thể kết nối đến máy chủ";
      messageBox.className = "auth-message error";
      console.error("Login Error:", error);
    }
  });
});
