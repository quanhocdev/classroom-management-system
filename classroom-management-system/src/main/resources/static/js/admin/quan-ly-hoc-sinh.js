document.addEventListener("DOMContentLoaded", function () {
  const fileInput = document.getElementById("subjectImage");
  const previewZone = document.getElementById("previewZone");
  const uploadPrompt = document.getElementById("uploadPrompt");
  const imagePreview = document.getElementById("imagePreview");
  const btnRemoveImage = document.getElementById("btnRemoveImage");
  const form = document.getElementById("createSubjectForm");
  const btnSubmit = document.getElementById("btnSubmit");
  const submitSpinner = document.getElementById("submitSpinner");
  const alertContainer = document.getElementById("alertContainer");

  // 1. Xử lý Preview ảnh khi người dùng chọn file
  fileInput.addEventListener("change", function () {
    const file = this.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        imagePreview.src = e.target.result;
        previewZone.classList.remove("d-none");
        uploadPrompt.classList.add("d-none");
      };
      reader.readAsDataURL(file);
    }
  });

  // 2. Xóa ảnh đã chọn (Reset preview)
  btnRemoveImage.addEventListener("click", function (e) {
    e.stopPropagation(); // Tránh kích hoạt sự kiện click mở file input của thẻ cha
    fileInput.value = "";
    imagePreview.src = "";
    previewZone.classList.add("d-none");
    uploadPrompt.classList.remove("d-none");
  });

  // 3. Hàm hiển thị thông báo động (Alert Notification)
  function showAlert(message, type = "danger") {
    alertContainer.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show border-0 shadow-sm" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
  }

  // 4. Xử lý sự kiện Submit Form (Gửi dữ liệu dạng multipart/form-data)
  form.addEventListener("submit", async function (e) {
    e.preventDefault(); // Chặn hành vi load lại trang mặc định

    // Trạng thái Loading của nút Submit
    btnSubmit.disabled = true;
    submitSpinner.classList.remove("d-none");
    alertContainer.innerHTML = ""; // Xóa các thông báo cũ

    // Xây dựng payload DTO khớp cấu trúc yêu cầu @RequestPart("data")
    const requestDTO = {
      code: document.getElementById("subjectCode").value.trim(),
      name: document.getElementById("subjectName").value.trim(),
      description: document.getElementById("subjectDesc").value.trim(),
    };

    // Khởi tạo FormData để gửi lên API của Backend
    const formData = new FormData();

    // Đóng gói đối tượng JSON data thành một Blob chứa định dạng application/json
    formData.append(
      "data",
      new Blob([JSON.stringify(requestDTO)], {
        type: "application/json",
      }),
    );

    // Đóng gói file hình ảnh đại diện (nếu có)
    if (fileInput.files[0]) {
      formData.append("image", fileInput.files[0]);
    }

    try {
      // Thực hiện gọi fetch API gửi tới Endpoint Backend của bạn
      const response = await fetch("/api/admin/subjects", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        showAlert(
          "✨ Thêm mới môn học thành công và đã đồng bộ lên đám mây!",
          "success",
        );
        form.reset();
        btnRemoveImage.click(); // Reset vùng hình ảnh về mặc định
      } else {
        // Xử lý lỗi trả về từ hệ thống (Ví dụ: Trùng mã môn học từ RuntimeException)
        const errorText = await response.text();
        showAlert(
          `Lỗi hệ thống: ${errorText || "Không thể khởi tạo thực thể môn học."}`,
        );
      }
    } catch (error) {
      console.error("Error creating subject:", error);
      showAlert(
        "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại đường truyền mạng.",
      );
    } finally {
      // Tắt trạng thái Loading
      btnSubmit.disabled = false;
      submitSpinner.classList.add("d-none");
    }
  });
});
