/* /static/js/student/trang-chu-student.js */
document.addEventListener("DOMContentLoaded", function () {
  console.log("Student Dashboard Page initialized.");

  // 1. Hiển thị ngày hôm nay
  const today = new Date();
  const dateString = `Ngày ${today.getDate()}/${today.getMonth() + 1}/${today.getFullYear()}`;
  document.getElementById("current-date").innerText = dateString;

  // 2. Giả lập gọi dữ liệu Lịch học của Học sinh (Có thể thế bằng gọi fetch API về sau)
  const mockSchedules = [
    {
      time: "07:30 - 09:00",
      subject: "Toán Đại Số",
      room: "Phòng A102",
      teacher: "Thầy Trần Văn Bình",
    },
    {
      time: "09:15 - 10:45",
      subject: "Ngữ Văn",
      room: "Phòng B204",
      teacher: "Cô Nguyễn Thị Mai",
    },
    {
      time: "14:00 - 15:30",
      subject: "Tiếng Anh",
      room: "Phòng C11",
      teacher: "Cô Lê Hoàng Yến",
    },
  ];

  const scheduleList = document.getElementById("schedule-list");
  if (mockSchedules.length > 0) {
    scheduleList.innerHTML = mockSchedules
      .map(
        (item) => `
            <tr>
                <td><strong>${item.time}</strong></td>
                <td>${item.subject}</td>
                <td><span style="color: #1a73e8; font-weight: 500;">${item.room}</span></td>
                <td>${item.teacher}</td>
            </tr>
        `,
      )
      .join("");
  } else {
    scheduleList.innerHTML = `<tr><td colspan="4" class="text-center">Hôm nay không có tiết học nào.</td></tr>`;
  }

  // 3. Giả lập gọi dữ liệu Bài tập về nhà
  const mockAssignments = [
    {
      title: "Bài tập hàm số bậc hai",
      class: "Lớp 10A1 - Toán",
      deadline: "Hôm nay",
      isUrgent: true,
    },
    {
      title: "Viết bài luận mẫu nghị luận xã hội",
      class: "Lớp 10A1 - Văn",
      deadline: "Ngày mai",
      isUrgent: false,
    },
  ];

  const assignmentList = document.getElementById("assignment-list");
  if (mockAssignments.length > 0) {
    assignmentList.innerHTML = mockAssignments
      .map(
        (todo) => `
            <div class="todo-item">
                <div class="todo-info">
                    <h4>${todo.title}</h4>
                    <p>${todo.class} • Hạn nộp: <span class="${todo.isUrgent ? "deadline-alert" : ""}">${todo.deadline}</span></p>
                </div>
                <a href="/student/bai-tap" class="btn-action">Làm bài</a>
            </div>
        `,
      )
      .join("");
  } else {
    assignmentList.innerHTML = `<p class="text-center">Tuyệt vời! Bạn không còn bài tập nào chưa nộp.</p>`;
  }
});
