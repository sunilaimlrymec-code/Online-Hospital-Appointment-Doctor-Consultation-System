/* ==========================================================================
   Shared helpers: JSON API wrapper + nav bar rendering
   ========================================================================== */

/**
 * Wraps fetch() for our JSON REST API: sends/receives JSON, includes the
 * session cookie automatically (same-origin), and throws a normal Error
 * with the server's message on non-2xx responses so callers can just
 * try/catch.
 */
async function apiFetch(url, options = {}) {
  const response = await fetch(url, {
    credentials: "same-origin",
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });

  const isJson = (response.headers.get("content-type") || "").includes("application/json");
  const data = isJson ? await response.json().catch(() => null) : null;

  if (!response.ok) {
    const error = new Error((data && data.message) || `Request failed (${response.status})`);
    error.status = response.status;
    error.data = data;
    throw error;
  }
  return data;
}

/** Renders the top navigation bar into #navHost, based on who is logged in. */
function renderNav(activeSession) {
  const host = document.getElementById("navHost");
  if (!host) return;
  host.className = "top-nav";

  let links = "";

  if (activeSession === "patient") {
    links = `
      <a href="/pages/dashboard.html">Dashboard</a>
      <a href="/pages/book-appointment.html">Book Appointment</a>
      <a href="/pages/view-appointments.html">My Appointments</a>
      <a href="#" id="navLogout" class="is-primary">Logout</a>
    `;
  } else if (activeSession === "doctor") {
    links = `
      <a href="/pages/doctor-dashboard.html">Dashboard</a>
      <a href="#" id="navLogout" class="is-primary">Logout</a>
    `;
  } else {
    links = `
      <a href="/pages/login.html">Patient Login</a>
      <a href="/pages/doctor-login.html">Doctor Login</a>
    `;
  }

  host.innerHTML = `
    <div class="top-nav__inner">
      <a href="/" class="top-nav__brand">
        <span class="material-icons">local_hospital</span> HospitalCare
      </a>
      <nav class="top-nav__links">${links}</nav>
    </div>
  `;

  const logoutBtn = document.getElementById("navLogout");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", async (e) => {
      e.preventDefault();
      const endpoint = activeSession === "doctor" ? "/api/doctor/auth/logout" : "/api/auth/logout";
      try {
        await apiFetch(endpoint, { method: "POST" });
      } finally {
        window.location.href = activeSession === "doctor" ? "/pages/doctor-login.html" : "/pages/login.html";
      }
    });
  }
}

/**
 * Guards a page: checks the relevant session endpoint, renders the nav,
 * and redirects to the right login page if the guard fails.
 * Returns the current user/doctor object on success, or null after redirecting.
 */
async function requireSession(role) {
  const endpoint = role === "doctor" ? "/api/doctor/auth/me" : "/api/auth/me";
  const loginPage = role === "doctor" ? "/pages/doctor-login.html" : "/pages/login.html";

  try {
    const session = await apiFetch(endpoint);
    renderNav(role);
    return session;
  } catch (err) {
    window.location.href = loginPage;
    return null;
  }
}

/** For public-ish pages (landing-adjacent forms): render nav without forcing a redirect. */
async function renderPublicNav() {
  try {
    const user = await apiFetch("/api/auth/me");
    renderNav("patient");
    return { role: "patient", session: user };
  } catch (e) {
    // not a patient session, try doctor
  }
  try {
    const doctor = await apiFetch("/api/doctor/auth/me");
    renderNav("doctor");
    return { role: "doctor", session: doctor };
  } catch (e) {
    renderNav(null);
    return { role: null, session: null };
  }
}
