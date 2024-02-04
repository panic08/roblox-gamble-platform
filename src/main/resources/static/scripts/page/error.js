const errorContainer = document.querySelector(".error-container");
const errorText = document.querySelector(".error-text");
const errorClose = document.querySelector(".error-close");
const errorBlock = document.querySelector(".error-block");

export const throwError = (error) => {
  errorContainer.style.display = "flex";
  errorBlock.style.animation = "appear 0.15s linear 0s";
  errorText.innerHTML = error;
};

const errorCloseHandleClick = () => {
  errorContainer.style.display = "none";
};

errorClose.addEventListener("click", errorCloseHandleClick);

const errorContainerHandleClick = (e) => {
  if (
    !document.querySelector(".error-block").contains(e.target) &&
    errorContainer.style.display != "none"
  ) {
    errorContainer.style.display = "none";
  }
};

errorContainer.addEventListener("click", errorContainerHandleClick);
