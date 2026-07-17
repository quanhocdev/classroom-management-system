document.addEventListener("DOMContentLoaded", async function () {
  console.log("Teacher Dashboard Page initialized.");

  // 1. Hiển thị nhanh tên giáo viên từ localStorage lên UI (Tránh bị trống giao diện khi đang fetch)
  const username = localStorage.getItem("username");
  const teacherNameElement = document.getElementById("teacherName");
  if (teacherNameElement && username) {
    teacherNameElement.innerText = username;
  }

  try {
    // 2. Gọi API Dashboard của Teacher (Trình duyệt tự đính kèm HttpOnly Cookie "accessToken")
    const response = await fetch("/api/teacher/dashboard", {
      method: "GET",
      credentials: "include", // Rất quan trọng để gửi kèm Cookie
    });

    // 3. Nếu token hết hạn hoặc sai quyền (401/403) -> Xóa sạch data UI và đá về trang đăng nhập
    if (response.status === 401 || response.status === 403) {
      localStorage.removeItem("username");
      localStorage.removeItem("userRole");
      window.location.href = "/auth/login";
      return;
    }

    const data = await response.json();
    console.log("Teacher dashboard data:", data);

    // 4. Render dữ liệu thật lên UI sau khi gọi API thành công
    /*
    if (data) {
      const totalClassesElement = document.getElementById("totalClasses");
      const totalStudentsElement = document.getElementById("totalStudents");

      if (totalClassesElement) totalClassesElement.innerText = data.totalClasses;
      if (totalStudentsElement) totalStudentsElement.innerText = data.totalStudents;
    }
    */
  } catch (error) {
    console.error("Load teacher dashboard error:", error);
    // Nếu lỗi kết nối hệ thống, cũng đá về Login để đảm bảo an toàn
    window.location.href = "/auth/login";
  }
});
