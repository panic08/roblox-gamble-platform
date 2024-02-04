import { gamesData } from "./showGames.js";

const gameInfoContainer = document.querySelector(".game-info-container");
const gameInfoCloseButton = document.querySelector(".game-info-close");

const serverSeedBlock = document.querySelector(".server-seed-block");
const clientSeedBlock = document.querySelector(".client-seed-block");
const saltBlock = document.querySelector(".salt-block");

const gameInfoCloseButtonHandleClick = () => {
  gameInfoContainer.style.display = "none";
};

export const gameInfoButtonHandleClick = (id) => {
  gameInfoContainer.style.animation = "appear 0.15s linear";
  gameInfoContainer.style.display = "flex";

  const game = gamesData.find((game) => game.id === id);

  serverSeedBlock.innerHTML = game.server_seed;
  clientSeedBlock.innerHTML = game.client_seed;
  saltBlock.innerHTML = game.salt || "empty";
};

gameInfoCloseButton.addEventListener("click", gameInfoCloseButtonHandleClick);

const gameInfoContainerHandleClick = (e) => {
  if (
    !document.querySelector(".game-info-block").contains(e.target) &&
    gameInfoContainer.style.display != "none"
  ) {
    gameInfoContainer.style.display = "none";
  }
};

gameInfoContainer.addEventListener("click", gameInfoContainerHandleClick);
