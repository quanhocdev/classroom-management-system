// Giả sử đây là đoạn xử lý submit form trong login.js của bạn
const form = document.getElementById("loginForm"); // thay bằng ID form của bạn

form.addEventListener("submit", async function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
      credentials: "include", // Để nhận Cookie từ Server
    });

    // BƯỚC QUAN TRỌNG: Kiểm tra nếu phản hồi không thành công (ví dụ: 401, 403, 500)
    if (!response.ok) {
      if (response.status === 401) {
        alert("Sai tài khoản hoặc mật khẩu. Vui lòng thử lại!");
      } else {
        alert(`Đăng nhập thất bại! Lỗi hệ thống: ${response.status}`);
      }
      return; // Dừng xử lý tiếp, không gọi response.json() nữa
    }

    // Nếu response.ok === true (200 OK), lúc này mới an tâm đọc JSON
    const data = await response.json();
    console.log("Đăng nhập thành công:", data);

    // Lưu role để render UI
    localStorage.setItem("userRole", data.role);
    localStorage.setItem("username", data.username);

    // Chuyển hướng theo vai trò
    if (data.role === "ADMIN") {
      window.location.href = "/admin/trang-chu";
    } else if (data.role === "TEACHER") {
      window.location.href = "/teacher/trang-chu"; // hoặc đường dẫn tương ứng của bạn
    } else {
      window.location.href = "/student/trang-chu"; // hoặc đường dẫn tương ứng của bạn
    }
  } catch (error) {
    console.error("Login Error:", error);
  }
});
