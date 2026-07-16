document.addEventListener("DOMContentLoaded", async function () {
  console.log("Teacher Dashboard Page initialized.");

  const token = localStorage.getItem("accessToken");

  if (!token) {
    window.location.href = "/auth/login";

    return;
  }

  // Hiển thị tên giáo viên từ localStorage

  const username = localStorage.getItem("username");

  const teacherNameElement = document.getElementById("teacherName");

  if (teacherNameElement && username) {
    teacherNameElement.innerText = username;
  }

  try {
    const response = await fetch("/api/teacher/dashboard", {
      method: "GET",

      headers: {
        Authorization: "Bearer " + token,
      },
    });

    if (response.status === 401 || response.status === 403) {
      localStorage.removeItem("accessToken");

      window.location.href = "/auth/login";

      return;
    }

    const data = await response.json();

    console.log("Teacher dashboard data:", data);

    // Sau này render dữ liệu thật ở đây

    /*
        
        Ví dụ backend trả:

        {
            totalClasses: 5,
            totalStudents: 120,
            todaySchedules: [...]
        }


        document.getElementById("totalClasses")
            .innerText = data.totalClasses;


        document.getElementById("totalStudents")
            .innerText = data.totalStudents;

        */
  } catch (error) {
    console.error("Load teacher dashboard error:", error);
  }
});
