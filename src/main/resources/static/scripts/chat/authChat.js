import { isAuth } from "../auth/checkAuth.js";

const chatForm = document.querySelector(".chat-form");
const chatUnAuth = document.querySelectorAll(".chat-unauth");

if (!isAuth()) {
  chatUnAuth.forEach((element) => (element.style.display = "block"));
  chatForm.style.display = "none";
}
