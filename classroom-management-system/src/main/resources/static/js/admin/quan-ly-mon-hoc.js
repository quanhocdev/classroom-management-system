document.addEventListener("DOMContentLoaded", function () {
  const subjectForm = document.getElementById("subjectForm");

  if (subjectForm) {
    subjectForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const form = e.target;

      // Đóng gói dữ liệu JSON như logic cũ của bạn
      const data = {
        code: form.code.value,
        name: form.name.value,
        description: form.description.value,
      };

      const formData = new FormData();
      formData.append(
        "data",
        new Blob([JSON.stringify(data)], {
          type: "application/json",
        }),
      );

      // Thêm hình ảnh nếu có file được chọn
      if (form.image.files.length > 0) {
        formData.append("image", form.image.files[0]);
      }

      try {
        // Gọi API gửi lên Server
        const response = await fetch("/api/admin/subjects", {
          method: "POST",
          body: formData,
        });

        if (response.ok) {
          alert("Tạo môn học thành công");
          window.location.href = "/admin/mon-hoc";
        } else {
          alert("Có lỗi xảy ra trong quá trình tạo môn học");
        }
      } catch (error) {
        console.error("Lỗi kết nối API:", error);
        alert("Không thể kết nối đến máy chủ");
      }
    });
  }
});
