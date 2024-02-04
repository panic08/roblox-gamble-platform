import { isAuth } from "../auth/checkAuth.js";
const auth = document.querySelectorAll(".auth");
const unAuth = document.querySelectorAll(".un-auth");

export const check = () => {
  if (isAuth()) {
    unAuth.forEach((element) => (element.style.display = "none"));
    auth.forEach((element) => (element.style.display = "block"));
  } else {
    auth.forEach((element) => (element.style.display = "none"));
    unAuth.forEach((element) => (element.style.display = "block"));
  }
};

check();
