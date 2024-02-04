const login = document.querySelector(".header-login");
const loginContainer = document.querySelector(".login-container");
const deposit = document.querySelector(".header-deposit");
const depositContainer = document.querySelector(".deposit-container");

const loginClickHandler = () => {
  loginContainer.style.display = "flex";
  loginContainer.style.animation = "appear 0.15s linear 0s";
};

export const depositClickHandler = () => {
  depositContainer.style.display = "flex";
  depositContainer.style.animation = "appear 0.15s linear 0s";
};

login.addEventListener("click", loginClickHandler);
deposit.addEventListener("click", depositClickHandler);

const loginContainerHandleClick = (e) => {
  if (
    !document.querySelector(".login-block").contains(e.target) &&
    loginContainer.style.display != "none"
  ) {
    loginContainer.style.display = "none";
  }
};

const depositContainerHandleClick = (e) => {
  if (
    !document.querySelector(".deposit-block").contains(e.target) &&
    depositContainer.style.display != "none"
  ) {
    depositContainer.style.display = "none";
  }
};

loginContainer.addEventListener("click", loginContainerHandleClick);
depositContainer.addEventListener("click", depositContainerHandleClick);
