import { isAuth } from "../auth/checkAuth.js";
import { closeCreateGameBlock, initCreateGameMenu } from "./createGameMenu.js";

const createGameBlockButton = document.querySelector(".create-game-block-btn");
const closeCreateGameBlockButton = document.querySelector(
  ".create-game-menu-close"
);

if (isAuth()) {
  createGameBlockButton.addEventListener("click", initCreateGameMenu);
}
closeCreateGameBlockButton.addEventListener("click", closeCreateGameBlock);
