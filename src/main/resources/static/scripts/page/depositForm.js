const cancel = document.querySelector(".deposit-close");
const depositContainer = document.querySelector(".deposit-container");

const cancelHandleClick = () => {
  depositContainer.style.display = "none";
};

cancel.addEventListener("click", cancelHandleClick);
