document.addEventListener("DOMContentLoaded", async function () {
  console.log("Student Dashboard Page initialized.");

  // Hiển thị ngày (Giữ nguyên logic cũ rất tốt của bạn)
  const today = new Date();
  const dateString = `Ngày ${today.getDate()}/${today.getMonth() + 1}/${today.getFullYear()}`;
  const dateElement = document.getElementById("current-date");
  if (dateElement) {
    dateElement.innerText = dateString;
  }

  try {
    // Gọi API Dashboard của Student sử dụng Cookie (tự động đính kèm nhờ credentials: "include")
    const response = await fetch("/api/student/dashboard", {
      method: "GET",
      credentials: "include", // Đảm bảo trình duyệt tự gửi kèm HttpOnly Cookie "accessToken"
    });

    // Nếu token hết hạn hoặc không có quyền (401/403), dọn dẹp localStorage UI và đá về Login
    if (response.status === 401 || response.status === 403) {
      localStorage.removeItem("username");
      localStorage.removeItem("userRole");
      window.location.href = "/auth/login";
      return;
    }

    const data = await response.json();

    renderSchedules(data.schedules);
    renderAssignments(data.assignments);
  } catch (error) {
    console.error("Load dashboard error:", error);
    // Nếu lỗi kết nối nghiêm trọng, bạn cũng có thể điều hướng về trang đăng nhập
    window.location.href = "/auth/login";
  }
});

function renderSchedules(list) {
  const table = document.getElementById("schedule-list");
  if (!table) return;

  table.innerHTML = list
    .map(
      (item) => `
            <tr>
                <td>
                    <strong>${item.time}</strong>
                </td>
                <td>${item.subject}</td>
                <td>${item.room}</td>
                <td>${item.teacher}</td>
            </tr>
        `,
    )
    .join("");
}

function renderAssignments(list) {
  const box = document.getElementById("assignment-list");
  if (!box) return;

  box.innerHTML = list
    .map(
      (item) => `
        <div class="todo-item">
            <div class="todo-info">
                <h4>${item.title}</h4>
                <p>${item.className} - Hạn: ${item.deadline}</p>
            </div>
            <a href="/student/bai-tap" class="btn-action">Làm bài</a>
        </div>
        `,
    )
    .join("");
}
