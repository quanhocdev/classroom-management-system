document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  const messageBox = document.getElementById("message");

  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    messageBox.innerText = "Đang xác thực...";
    messageBox.className = "auth-message";

    // Chuyển dữ liệu sang định dạng Form URL Encoded
    const formData = new URLSearchParams();
    formData.append("username", document.getElementById("username").value);
    formData.append("password", document.getElementById("password").value);

    try {
      // Gửi trực tiếp lên cổng xử lý của Spring Security đã cấu hình
      const response = await fetch("/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData.toString(),
      });

      // Nếu LoginSuccessHandler của bạn trả về JSON chứa Role:
      if (response.ok) {
        const result = await response.json();
        messageBox.innerText = "Đăng nhập thành công! Đang chuyển hướng...";
        messageBox.className = "auth-message success";

        localStorage.setItem("accessToken", "true");

        const userRole = result.role ? result.role.toUpperCase() : "";

        setTimeout(function () {
          if (userRole === "ADMIN" || userRole === "ROLE_ADMIN") {
            window.location.href = "/admin/trang-chu";
          } else if (userRole === "TEACHER" || userRole === "ROLE_TEACHER") {
            window.location.href = "/teacher/trang-chu";
          } else if (userRole === "STUDENT" || userRole === "ROLE_STUDENT") {
            window.location.href = "/student/trang-chu";
          } else {
            window.location.href = "/";
          }
        }, 800);
      } else {
        messageBox.innerText = "Sai username hoặc password";
        messageBox.className = "auth-message error";
      }
    } catch (error) {
      messageBox.innerText = "Không thể kết nối đến máy chủ";
      messageBox.className = "auth-message error";
      console.error("Login Error:", error);
    }
  });
});
