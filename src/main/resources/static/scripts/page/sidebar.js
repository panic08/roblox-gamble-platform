const sidebarActivateButton = document.querySelector(".header-mobile-only");
const sidebar = document.querySelector(".sidebar");

const sidebarActivateButtonHandleClick = () => {
  sidebar.classList.toggle("active-sidebar");
};

sidebarActivateButton.addEventListener(
  "click",
  sidebarActivateButtonHandleClick
);
