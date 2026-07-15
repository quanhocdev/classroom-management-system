/* /js/trang-chu-public.js */
document.addEventListener("DOMContentLoaded", function () {
  console.log("Public homepage initialized.");

  // Tự động kiểm tra trạng thái login để đổi nút kêu gọi hành động (CTA) nếu cần
  const token = localStorage.getItem("accessToken");
  const heroButtons = document.querySelector(".hero-buttons");

  // Đoạn code này chạy minh họa nếu hệ thống có phân quyền lưu ở Front-end
  if (token && heroButtons) {
    // Có thể thay đổi hành vi nút bấm chính trên trang chủ nếu đã đăng nhập
    const primaryBtn = heroButtons.querySelector(".btn-hero-primary");
    if (primaryBtn) {
      primaryBtn.innerText = "Đi tới trang quản lý lớp";
      // Lắng nghe điều hướng nhanh về khu vực dashboard tương ứng
      primaryBtn.addEventListener("click", function (e) {
        e.preventDefault();
        // Tùy chỉnh phân luồng giả định dựa trên việc lưu role
        window.location.href = "/auth/login";
      });
    }
  }
});
