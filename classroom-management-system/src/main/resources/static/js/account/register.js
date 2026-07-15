document.addEventListener("DOMContentLoaded", function () {
  const registerForm = document.getElementById("registerForm");
  const messageBox = document.getElementById("message");

  registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Xóa thông báo cũ
    messageBox.innerText = "Đang xử lý...";
    messageBox.className = "auth-message";

    const data = {
      username: document.getElementById("username").value,
      email: document.getElementById("email").value,
      password: document.getElementById("password").value,
    };

    try {
      const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      const result = await response.json();

      if (response.ok) {
        messageBox.innerText = "Đăng ký thành công! Đang chuyển hướng...";
        messageBox.classList.add("success");

        // Đợi 1.5 giây để user kịp nhìn thông báo thành công
        setTimeout(() => {
          window.location.href = "/auth/login";
        }, 1500);
      } else {
        messageBox.innerText =
          result.message ?? "Đăng ký thất bại, vui lòng thử lại.";
        messageBox.classList.add("error");
      }
    } catch (error) {
      messageBox.innerText = "Lỗi kết nối server!";
      messageBox.classList.add("error");
      console.error("Register Error:", error);
    }
  });
});
