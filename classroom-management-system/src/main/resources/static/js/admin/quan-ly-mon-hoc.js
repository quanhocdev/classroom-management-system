document.addEventListener("DOMContentLoaded", function () {
  console.log("===== Subject Page Loaded =====");

  // ==========================
  // FORM
  // ==========================
  const subjectForm = document.getElementById("createSubjectForm");

  if (!subjectForm) {
    console.error("Không tìm thấy form createSubjectForm");
    return;
  }

  // ==========================
  // BUTTON
  // ==========================
  const btnSubmit = document.getElementById("btnSubmit");
  const spinner = document.getElementById("submitSpinner");

  // ==========================
  // IMAGE
  // ==========================
  const imageInput = document.getElementById("subjectImage");
  const preview = document.getElementById("imagePreview");
  const previewZone = document.getElementById("previewZone");
  const uploadPrompt = document.getElementById("uploadPrompt");
  const btnRemoveImage = document.getElementById("btnRemoveImage");

  // ==========================
  // PREVIEW IMAGE
  // ==========================
  imageInput.addEventListener("change", function () {
    const file = this.files[0];

    if (!file) {
      return;
    }

    console.log("Đã chọn:", file);

    // Chỉ cho chọn ảnh
    if (!file.type.startsWith("image/")) {
      alert("Chỉ được chọn file ảnh.");

      imageInput.value = "";

      return;
    }

    // Giới hạn 5MB
    if (file.size > 5 * 1024 * 1024) {
      alert("Ảnh tối đa 5MB.");

      imageInput.value = "";

      return;
    }

    const reader = new FileReader();

    reader.onload = function (e) {
      preview.src = e.target.result;

      previewZone.classList.remove("d-none");

      uploadPrompt.classList.add("d-none");
    };

    reader.readAsDataURL(file);
  });

  // ==========================
  // REMOVE IMAGE
  // ==========================
  btnRemoveImage.addEventListener("click", function () {
    imageInput.value = "";

    preview.src = "";

    previewZone.classList.add("d-none");

    uploadPrompt.classList.remove("d-none");
  });

  // ==========================
  // SUBMIT
  // ==========================
  subjectForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    console.log("===== Submit =====");

    const form = e.target;

    const data = {
      code: form.code.value.trim(),
      name: form.name.value.trim(),
      description: form.description.value.trim(),
    };

    console.log("JSON:", data);

    const formData = new FormData();

    formData.append(
      "data",
      new Blob([JSON.stringify(data)], {
        type: "application/json",
      }),
    );

    if (imageInput.files.length > 0) {
      formData.append("image", imageInput.files[0]);
    }

    // Disable nút
    btnSubmit.disabled = true;

    spinner.classList.remove("d-none");

    btnSubmit.innerHTML = `
        <span
            id="submitSpinner"
            class="spinner-border spinner-border-sm me-2"
            role="status"
            aria-hidden="true">
        </span>
        Đang lưu...
    `;

    try {
      console.log("Đang gửi request...");

      const response = await fetch("/api/admin/subjects", {
        method: "POST",
        body: formData,
      });

      console.log("Status:", response.status);

      const responseText = await response.text();

      console.log(responseText);

      if (response.ok) {
        alert("Tạo môn học thành công!");

        window.location.href = "/admin/mon-hoc";

        return;
      }

      alert("Lỗi: " + responseText);
    } catch (err) {
      console.error(err);

      alert("Không thể kết nối tới server.");
    } finally {
      btnSubmit.disabled = false;

      btnSubmit.innerHTML = `
            <span
                id="submitSpinner"
                class="spinner-border spinner-border-sm me-2 d-none"
                role="status"
                aria-hidden="true">
            </span>
            Lưu môn học
        `;
    }
  });
});
