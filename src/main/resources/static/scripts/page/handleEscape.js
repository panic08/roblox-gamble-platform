let popups = document.querySelectorAll(".popup");

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") {
    for (const popup of popups) {
      if (getComputedStyle(popup, null).display !== "none") {
        popup.style.display = "none";
        break;
      }
    }
  }
});
