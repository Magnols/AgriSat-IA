/* ==========================================================================
   AgriSat IA - Navegação e interações simuladas
   ========================================================================== */

document.addEventListener("DOMContentLoaded", () => {
  const app = document.querySelector("#app");
  const screens = Array.from(document.querySelectorAll(".screen"));
  const navButtons = Array.from(document.querySelectorAll(".bottom-nav button"));
  const routedButtons = Array.from(document.querySelectorAll("[data-target]"));
  const passwordInput = document.querySelector("#password");
  const passwordToggle = document.querySelector(".password-toggle");
  const toggleSms = document.querySelector(".toggle");
  const filterButtons = Array.from(document.querySelectorAll(".chip-row button, .map-tabs button"));

  function setActiveScreen(screenName) {
    screens.forEach((screen) => {
      screen.classList.toggle("is-active", screen.dataset.screen === screenName);
    });

    app.classList.toggle("app-visible", screenName !== "login");

    navButtons.forEach((button) => {
      button.classList.toggle("is-active", button.dataset.target === screenName);
    });

    const activeScreen = document.querySelector(`.screen[data-screen="${screenName}"] .screen-scroll`);
    if (activeScreen) {
      activeScreen.scrollTo({ top: 0, behavior: "smooth" });
    }
  }

  routedButtons.forEach((button) => {
    button.addEventListener("click", (event) => {
      const target = button.dataset.target;

      if (!target) {
        return;
      }

      event.preventDefault();
      setActiveScreen(target);
    });
  });

  document.querySelector(".login-form").addEventListener("submit", (event) => {
    event.preventDefault();
    setActiveScreen("dashboard");
  });

  passwordToggle.addEventListener("click", () => {
    const isHidden = passwordInput.type === "password";
    passwordInput.type = isHidden ? "text" : "password";
    passwordToggle.textContent = isHidden ? "◌" : "◉";
  });

  toggleSms.addEventListener("click", () => {
    toggleSms.classList.toggle("is-on");
  });

  filterButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const group = button.parentElement;
      group.querySelectorAll("button").forEach((item) => item.classList.remove("is-selected"));
      button.classList.add("is-selected");
    });
  });
});
