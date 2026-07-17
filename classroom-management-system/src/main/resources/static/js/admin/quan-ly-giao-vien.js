document.addEventListener("DOMContentLoaded", function () {
  // 1. Tự động load danh sách giáo viên ngay khi vào trang
  fetchTeachers();

  const btnSubmitTeacher = document.getElementById("btnSubmitTeacher");
  if (btnSubmitTeacher) {
    btnSubmitTeacher.addEventListener("click", async function () {
      const form = document.getElementById("createTeacherForm");
      const alertBox = document.getElementById("modalAlert");

      const username = document.getElementById("username").value.trim();
      const email = document.getElementById("email").value.trim();
      const password = document.getElementById("password").value.trim();

      if (!username || !email || !password) {
        showAlert("Vui lòng nhập đầy đủ thông tin yêu cầu!", "danger");
        return;
      }

      // Khớp chuẩn với AdminCreateUserRequestDTO ở Backend của bạn
      const payload = {
        username: username,
        email: email,
        password: password,
        role: "TEACHER", // Xác định rõ vai trò tạo mới là giáo viên
      };

      try {
        // Gọi đúng API /api/admin/users/create bạn đã viết sẵn
        const response = await fetch("/api/admin/users/create", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include", // Truyền Cookie chứa JWT
          body: JSON.stringify(payload),
        });

        const data = await response.json();

        // Trả về 201 Created (khớp với @ResponseStatus(HttpStatus.CREATED) của bạn)
        if (response.status === 201 || response.ok) {
          showAlert("Đăng ký tài khoản giáo viên thành công!", "success");

          // Render dòng mới vừa tạo lên đầu bảng
          addNewRowToTable(data, true);

          setTimeout(() => {
            form.reset();
            alertBox.classList.add("d-none");
            const modalEl = document.getElementById("createTeacherModal");
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) {
              modal.hide();
            }
          }, 1500);
        } else {
          showAlert(
            data.message || "Lỗi không thể đăng ký tài khoản!",
            "danger",
          );
        }
      } catch (error) {
        showAlert("Không thể kết nối đến hệ thống máy chủ!", "danger");
        console.error("Lỗi kết nối:", error);
      }
    });
  }

  // 2. Hàm gọi API lấy danh sách giáo viên
  async function fetchTeachers() {
    const tbody = document.getElementById("teacherTableBody");
    try {
      // Gọi API GET /api/admin/teachers đã viết ở AdminTeacherApiController
      const response = await fetch("/api/admin/teachers", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // Truyền Cookie chứa JWT
      });

      if (!response.ok) {
        throw new Error(`Server trả về lỗi: ${response.status}`);
      }

      const teachers = await response.json();
      tbody.innerHTML = "";

      if (teachers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-muted">Chưa có giáo viên nào trong danh sách.</td></tr>`;
        return;
      }

      teachers.forEach((teacher) => {
        addNewRowToTable(teacher, false);
      });
    } catch (error) {
      console.error("Lỗi fetchTeachers:", error);
      tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-danger">Không thể kết nối để lấy danh sách giáo viên!</td></tr>`;
    }
  }

  // Hàm hiển thị thông báo
  function showAlert(message, type) {
    const alertBox = document.getElementById("modalAlert");
    if (alertBox) {
      alertBox.className = `alert alert-${type} py-2 px-3 small mb-3`;
      alertBox.innerText = message;
      alertBox.classList.remove("d-none");
    }
  }

  // Hàm thêm 1 hàng mới vào bảng (Khớp chuẩn thuộc tính của UserResponseDTO)
  function addNewRowToTable(user, addToTop = false) {
    const tbody = document.getElementById("teacherTableBody");
    if (!tbody) return;

    if (tbody.querySelector(".text-center")) {
      tbody.innerHTML = "";
    }

    const newRow = document.createElement("tr");
    newRow.innerHTML = `
            <td>${user.id}</td>
            <td class="fw-semibold">${user.username}</td>
            <td>${user.email}</td>
            <td><span class="badge bg-success-subtle text-success border-0 px-2 py-1">${user.status || "ACTIVE"}</span></td>
            <td><span class="badge bg-secondary-subtle text-secondary border-0 px-2 py-1">${user.provider || "LOCAL"}</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-secondary me-1">Sửa</button>
                <button class="btn btn-sm btn-outline-danger">Xóa</button>
            </td>
        `;

    if (addToTop) {
      tbody.insertBefore(newRow, tbody.firstChild);
    } else {
      tbody.appendChild(newRow);
    }
  }
});
