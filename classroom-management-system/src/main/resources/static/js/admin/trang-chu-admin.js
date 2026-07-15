document.addEventListener("DOMContentLoaded", function () {
  // Lấy tên admin từ localStorage hiển thị lên tiêu đề nếu cần
  const username = localStorage.getItem("username");
  console.log(
    "Hệ thống Admin Dashboard đã khởi chạy cho tài khoản:",
    username || "Admin",
  );

  // Nơi cấu hình các biểu đồ hoặc gọi API lấy dữ liệu tổng quan sau này
});
