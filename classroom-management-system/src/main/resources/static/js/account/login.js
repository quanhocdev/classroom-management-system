document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  const messageBox = document.getElementById("message");

  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Xóa thông báo cũ
    messageBox.innerText = "Đang xác thực...";
    messageBox.className = "auth-message";

    const data = {
      username: document.getElementById("username").value,
      password: document.getElementById("password").value,
    };

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      const result = await response.json();

      if (response.ok) {
        messageBox.innerText = "Đăng nhập thành công! Đang chuyển hướng...";
        messageBox.classList.add("success");

        // Giả định lưu token trạng thái đăng nhập cho Header nhận diện
        localStorage.setItem("accessToken", result.token || "true");

        // Phân luồng điều hướng dựa vào Role nhận được từ server
        setTimeout(function () {
          switch (result.role) {
            case "ADMIN":
              window.location.href = "/admin/trang-chu";
              break;
            case "TEACHER":
              window.location.href = "/teacher/trang-chu";
              break;
            case "STUDENT":
              window.location.href = "/student/trang-chu";
              break;
            default:
              window.location.href = "/"; // Quay về trang chủ nếu không khớp
          }
        }, 800);
      } else {
        messageBox.innerText = result.message ?? "Sai username hoặc password";
        messageBox.classList.add("error");
      }
    } catch (error) {
      messageBox.innerText = "Không thể kết nối đến máy chủ";
      messageBox.classList.add("error");
      console.error("Login Error:", error);
    }
  });
});
