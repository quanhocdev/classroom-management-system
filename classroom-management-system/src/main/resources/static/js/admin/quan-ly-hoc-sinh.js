document.addEventListener("DOMContentLoaded", function () {
  // 1. Tự động load danh sách học sinh ngay khi vào trang
  fetchStudents();

  const btnSubmitStudent = document.getElementById("btnSubmitStudent");
  if (btnSubmitStudent) {
    btnSubmitStudent.addEventListener("click", async function () {
      const form = document.getElementById("createStudentForm");
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
        role: "STUDENT", // Xác định rõ vai trò tạo mới là học sinh
      };

      try {
        // Gọi đúng API tạo tài khoản người dùng của bạn
        const response = await fetch("/api/admin/users/create", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include", // Truyền Cookie chứa JWT
          body: JSON.stringify(payload),
        });

        const data = await response.json();

        if (response.status === 201 || response.ok) {
          showAlert("Đăng ký tài khoản học sinh thành công!", "success");

          // Render dòng mới vừa tạo lên đầu bảng học sinh
          addNewRowToTable(data, true);

          setTimeout(() => {
            form.reset();
            alertBox.classList.add("d-none");
            const modalEl = document.getElementById("createStudentModal");
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) {
              modal.hide();
            }
          }, 1500);
        } else {
          showAlert(
            data.message || "Lỗi không thể đăng ký tài khoản học sinh!",
            "danger",
          );
        }
      } catch (error) {
        showAlert("Không thể kết nối đến hệ thống máy chủ!", "danger");
        console.error("Lỗi kết nối:", error);
      }
    });
  }

  // 2. Hàm gọi API lấy danh sách học sinh
  async function fetchStudents() {
    const tbody = document.getElementById("studentTableBody");
    try {
      // Gọi API GET /api/admin/students đã cấu hình ở AdminStudentApiController
      const response = await fetch("/api/admin/students", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // Truyền Cookie chứa JWT
      });

      if (!response.ok) {
        throw new Error(`Server trả về lỗi: ${response.status}`);
      }

      const students = await response.json();
      tbody.innerHTML = "";

      if (students.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-muted">Chưa có học sinh nào trong danh sách.</td></tr>`;
        return;
      }

      students.forEach((student) => {
        addNewRowToTable(student, false);
      });
    } catch (error) {
      console.error("Lỗi fetchStudents:", error);
      tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-danger">Không thể kết nối để lấy danh sách học sinh!</td></tr>`;
    }
  }

  // Hàm hiển thị thông báo lỗi/thành công
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
    const tbody = document.getElementById("studentTableBody");
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
