document.addEventListener("DOMContentLoaded", async function () {
  console.log("Student Dashboard Page initialized.");

  const token = localStorage.getItem("accessToken");

  if (!token) {
    window.location.href = "/auth/login";
    return;
  }

  // Hiển thị ngày

  const today = new Date();

  const dateString = `Ngày ${today.getDate()}/${today.getMonth() + 1}/${today.getFullYear()}`;

  const dateElement = document.getElementById("current-date");

  if (dateElement) {
    dateElement.innerText = dateString;
  }

  try {
    const response = await fetch("/api/student/dashboard", {
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

    renderSchedules(data.schedules);

    renderAssignments(data.assignments);
  } catch (error) {
    console.error("Load dashboard error:", error);
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
                    <strong>
                        ${item.time}
                    </strong>
                </td>


                <td>
                    ${item.subject}
                </td>


                <td>
                    ${item.room}
                </td>


                <td>
                    ${item.teacher}
                </td>


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

                <h4>
                    ${item.title}
                </h4>


                <p>
                    ${item.className}
                    -
                    Hạn:
                    ${item.deadline}
                </p>


            </div>


            <a href="/student/bai-tap"
               class="btn-action">

               Làm bài

            </a>


        </div>


        `,
    )
    .join("");
}
