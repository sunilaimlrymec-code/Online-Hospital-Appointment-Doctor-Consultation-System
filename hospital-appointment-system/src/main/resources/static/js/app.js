/* ==========================================================================
   Hospital Appointment & Telemedicine System — shared frontend behavior
   ========================================================================== */

/* ---------------- Generic form validation ---------------- */

function validateForm(formId) {
  const form = document.getElementById(formId);
  if (!form) return true;

  const inputs = form.querySelectorAll("input, select");
  let valid = true;

  inputs.forEach((input) => {
    const tip = input.parentElement.querySelector(".form-error");

    if (input.value.trim() === "") {
      input.classList.add("error");
      if (tip) tip.style.display = "block";
      valid = false;
    } else {
      input.classList.remove("error");
      if (tip) tip.style.display = "none";
    }
  });

  if (!valid) {
    alert("All fields are required");
  }
  return valid;
}

function validateDoctorRegister() {
  let valid = true;

  function check(id, tipId) {
    const el = document.getElementById(id);
    const tip = document.getElementById(tipId);
    if (!el) return;

    if (el.value.trim() === "") {
      el.classList.add("error");
      if (tip) tip.style.display = "block";
      valid = false;
    } else {
      el.classList.remove("error");
      if (tip) tip.style.display = "none";
    }
  }

  check("name", "nameTip");
  check("speciality", "specTip");
  check("disease", "diseaseTip");
  check("email", "emailTip");
  check("password", "passTip");

  if (!valid) {
    alert("Please fill all required fields");
  }
  return valid;
}

function validateLogin() {
  const email = document.getElementById("email");
  const password = document.getElementById("password");
  const emailTip = document.getElementById("emailTip");
  const passTip = document.getElementById("passTip");
  let valid = true;

  if (email && email.value.trim() === "") {
    email.classList.add("error");
    if (emailTip) emailTip.style.display = "block";
    valid = false;
  } else if (email) {
    email.classList.remove("error");
    if (emailTip) emailTip.style.display = "none";
  }

  if (password && password.value.trim() === "") {
    password.classList.add("error");
    if (passTip) passTip.style.display = "block";
    valid = false;
  } else if (password) {
    password.classList.remove("error");
    if (passTip) passTip.style.display = "none";
  }

  if (!valid) {
    alert("Please fill all required fields");
  }
  return valid;
}

/* ---------------- Booking page: cascading dropdowns (JSON API) ---------------- */

function initBookingPage() {
  const diseaseSelect = document.getElementById("diseaseSelect");
  const doctorSelect = document.getElementById("doctorSelect");
  const timeSelect = document.getElementById("timeSelect");
  const appointmentDate = document.getElementById("appointmentDate");

  if (!diseaseSelect || !doctorSelect || !timeSelect || !appointmentDate) {
    return; // not on the booking page
  }

  function loadDoctors(diseaseId) {
    doctorSelect.innerHTML = "<option value=''>Loading...</option>";
    timeSelect.innerHTML = "<option value=''>Select Time Slot</option>";
    timeSelect.disabled = true;

    if (!diseaseId) {
      doctorSelect.innerHTML = "<option value=''>Select Doctor</option>";
      return;
    }

    fetch("/api/doctors?diseaseId=" + encodeURIComponent(diseaseId))
      .then((r) => r.json())
      .then((doctors) => {
        let html = "<option value=''>Select Doctor</option>";
        doctors.forEach((d) => {
          html += `<option value="${d.id}">Dr. ${d.name}</option>`;
        });
        doctorSelect.innerHTML = html;
      });
  }

  function loadSlots() {
    if (!doctorSelect.value || !appointmentDate.value) return;

    timeSelect.disabled = true;
    timeSelect.innerHTML = "<option value=''>Loading...</option>";

    fetch(
      "/api/appointments/slots?doctorId=" +
        encodeURIComponent(doctorSelect.value) +
        "&date=" +
        encodeURIComponent(appointmentDate.value)
    )
      .then((r) => {
        if (!r.ok) {
          return r.json().then((err) => {
            throw new Error(err.message || "Could not load slots (" + r.status + ")");
          });
        }
        return r.json();
      })
      .then((slots) => {
        if (!Array.isArray(slots) || slots.length === 0) {
          timeSelect.innerHTML = "<option value=''>No slots available</option>";
          timeSelect.disabled = true;
          return;
        }
        let html = "<option value=''>Select Time Slot</option>";
        slots.forEach((s) => {
          html += `<option value="${s.value}">${s.label}</option>`;
        });
        timeSelect.innerHTML = html;
        timeSelect.disabled = false;
      })
      .catch((err) => {
        timeSelect.innerHTML = "<option value=''>Error loading slots</option>";
        timeSelect.disabled = true;
        console.error("Slot lookup failed:", err.message);
      });
  }

  diseaseSelect.addEventListener("change", () => loadDoctors(diseaseSelect.value));
  doctorSelect.addEventListener("change", loadSlots);

  /* ----- Custom 15-day calendar ----- */
  const calendarGrid = document.getElementById("calendarGrid");
  const monthYear = document.getElementById("monthYear");

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const maxDate = new Date(today);
  maxDate.setDate(today.getDate() + 15);

  let currentMonth = today.getMonth();
  let currentYear = today.getFullYear();

  function formatDate(d) {
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  window.changeMonth = function (step) {
    currentMonth += step;
    if (currentMonth < 0) {
      currentMonth = 11;
      currentYear--;
    }
    if (currentMonth > 11) {
      currentMonth = 0;
      currentYear++;
    }
    renderCalendar();
  };

  function renderCalendar() {
    calendarGrid.innerHTML = "";
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const days = new Date(currentYear, currentMonth + 1, 0).getDate();

    monthYear.innerText = new Date(currentYear, currentMonth).toLocaleString("default", {
      month: "long",
      year: "numeric",
    });

    for (let i = 0; i < firstDay; i++) {
      calendarGrid.appendChild(document.createElement("div"));
    }

    for (let d = 1; d <= days; d++) {
      const dateObj = new Date(currentYear, currentMonth, d);
      dateObj.setHours(0, 0, 0, 0);

      const cell = document.createElement("div");
      cell.className = "calendar-day";
      cell.innerText = String(d);

      if (dateObj < today || dateObj > maxDate) {
        cell.classList.add("disabled");
      } else {
        cell.classList.add("active");
        cell.onclick = () => {
          document.querySelectorAll(".calendar-day").forEach((x) => x.classList.remove("selected"));
          cell.classList.add("selected");
          appointmentDate.value = formatDate(dateObj);
          loadSlots();
        };
      }
      calendarGrid.appendChild(cell);
    }
  }

  renderCalendar();

  const form = document.querySelector(".booking-form");
  if (form) {
    form.addEventListener("submit", (e) => {
      if (!appointmentDate.value) {
        alert("Please select appointment date");
        e.preventDefault();
        return;
      }
      if (!timeSelect.value) {
        alert("Please select appointment time");
        e.preventDefault();
        return;
      }
      timeSelect.disabled = false;
    });
  }
}

/* ---------------- Appointment card countdown / live status ---------------- */

function initAppointmentCards() {
  const cards = document.querySelectorAll(".appointment-card[data-datetime]");
  if (!cards.length) return;

  function format(ms) {
    const totalSeconds = Math.floor(ms / 1000);
    const hrs = Math.floor(totalSeconds / 3600);
    const mins = Math.floor((totalSeconds % 3600) / 60);
    const secs = totalSeconds % 60;
    return (
      String(hrs).padStart(2, "0") + ":" + String(mins).padStart(2, "0") + ":" + String(secs).padStart(2, "0")
    );
  }

  cards.forEach((card) => {
    const dateTimeStr = card.dataset.datetime;
    if (!dateTimeStr) return;

    const statusEl = card.querySelector(".status");
    const timerEl = card.querySelector(".timer");
    const videoBtn = card.querySelector(".video-btn");
    const reviewLinkWrap = card.querySelector(".review-link");

    const startTime = new Date(dateTimeStr).getTime();
    const oneHour = 60 * 60 * 1000;
    const twentyFourHours = 24 * 60 * 60 * 1000;
    const endTime = startTime + oneHour;
    const hideAfter = endTime + twentyFourHours;

    function updateCard() {
      const now = Date.now();

      if (now > hideAfter) {
        card.remove();
        return;
      }

      if (now < startTime) {
        timerEl.textContent = "Starts in " + format(startTime - now);
        statusEl.textContent = "Scheduled";
        statusEl.className = "status scheduled";
        if (videoBtn) {
          videoBtn.disabled = true;
          videoBtn.textContent = "Video Call Locked";
        }
      } else if (now >= startTime && now <= endTime) {
        timerEl.textContent = "Ends in " + format(endTime - now);
        statusEl.textContent = "Live";
        statusEl.className = "status live";
        if (videoBtn) {
          videoBtn.disabled = false;
          videoBtn.textContent = "Join Video Call";
        }
      } else {
        timerEl.textContent = "--";
        statusEl.textContent = "Completed";
        statusEl.className = "status completed";
        if (videoBtn) videoBtn.style.display = "none";
        if (reviewLinkWrap) reviewLinkWrap.style.display = "inline-flex";
      }
    }

    updateCard();
    setInterval(updateCard, 1000);
  });
}

document.addEventListener("DOMContentLoaded", () => {
  initBookingPage();
  initAppointmentCards();
});
