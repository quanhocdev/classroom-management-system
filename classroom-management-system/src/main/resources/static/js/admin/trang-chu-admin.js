document.addEventListener("DOMContentLoaded", async function () {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    window.location.href = "/auth/login";
    return;
  }

  try {
    const response = await fetch("/api/admin/dashboard", {
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

    console.log("Admin data:", data);

    document.getElementById("adminName").innerText = data.username;
  } catch (error) {
    console.error("Không gọi được API admin:", error);
  }
});
