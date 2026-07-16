/* /static/js/fragments/sidebar-student.js */
document.addEventListener("DOMContentLoaded", function () {
  console.log("Student Sidebar loaded.");

  // 1. Tự động cập nhật trạng thái Active dựa theo URL hiện tại
  const currentPath = window.location.pathname;
  const navItems = document.querySelectorAll(".sidebar-nav .nav-item");

  navItems.forEach((item) => {
    item.classList.remove("active");
    if (item.getAttribute("href") === currentPath) {
      item.classList.add("active");
    }
  });

  // 2. Giả lập hoặc gọi API lấy tên thật của học sinh (đã đăng nhập) hiển thị lên Sidebar
  const studentNameEl = document.getElementById("sidebar-student-name");
  if (studentNameEl) {
    // Có thể lấy username từ JWT claim hoặc API profile
    const mockStudentName = localStorage.getItem("username");
    studentNameEl.innerText = mockStudentName;
  }
});
