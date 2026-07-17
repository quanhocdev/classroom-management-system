document.addEventListener("DOMContentLoaded", function () {
  // Chỉ cần dùng userRole trong localStorage để nhận biết nhanh trạng thái hiển thị UI
  const userRole = localStorage.getItem("userRole");
  const footerContainer = document.getElementById("footer-dynamic-container");

  if (!footerContainer) return;

  // TRƯỜNG HỢP: ĐÃ ĐĂNG NHẬP (Có userRole trong localStorage)
  if (userRole) {
    // Render Footer Portal nhỏ gọn gọn cho Admin, Teacher, Student
    footerContainer.className =
      "mt-auto py-3 text-center text-muted border-top bg-white";
    footerContainer.innerHTML = `
      <small>© 2026 <span class="fw-bold text-primary">OurClass</span> Portal. Tất cả quyền được bảo lưu.</small>
    `;
  }
  // TRƯỜNG HỢP: CHƯA ĐĂNG NHẬP (Không có userRole trong localStorage)
  else {
    // Render Footer công cộng lớn
    footerContainer.className = "mt-auto bg-dark text-light py-4";
    footerContainer.innerHTML = `
      <div class="container text-center">
        <h5>Hệ thống lớp học OurClass</h5>
        <p class="text-muted small mb-0">Địa chỉ: Thành phố Hồ Chí Minh, Việt Nam</p>
        <small class="text-secondary">© 2026 OurClass công cộng.</small>
      </div>
    `;
  }
});
