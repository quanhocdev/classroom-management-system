document.addEventListener("DOMContentLoaded", function () {
  // Lấy tên giáo viên đã lưu ở localStorage khi đăng nhập thành công
  const username = localStorage.getItem("username");
  const teacherNameElement = document.getElementById("teacherName");

  if (teacherNameElement && username) {
    teacherNameElement.innerText = username;
  }

  // Sau này bạn có thể viết thêm các hàm fetch API tại đây
  // để lấy sĩ số thực tế từ cơ sở dữ liệu và hiển thị lên các card
  console.log("Trang chủ giáo viên đã sẵn sàng!");
});
