import { createGame } from "../queries/gamesQueries.js";
import { activeItemList, deleteItemsFromList } from "./createMenuComponent.js";
const createGameContainer = document.querySelector(
  ".create-game-menu-container"
);

const createGameButton = document.querySelector(".create-game-menu-btn");

let coinflipCreateData = {
  user_items_ids: [],
};

const handleCreateGameClick = async () => {
  if (activeItemList) {
    coinflipCreateData.user_items_ids = activeItemList;
    await createGame(coinflipCreateData);
    const itemsListsElement = document.querySelectorAll(
      ".game-menu-items-list"
    );
    deleteItemsFromList();
    closeCreateGameBlock();
  }
};

export const initCreateGameMenu = () => {
  createGameContainer.style.animation = "appear 0.15s linear";
  createGameContainer.style.display = "flex";
};

export const closeCreateGameBlock = () => {
  createGameContainer.style.display = "none";
};

createGameButton.addEventListener("click", handleCreateGameClick);

const createGameContainerHandleClick = (e) => {
  if (
    !document.querySelector(".create-game-menu-block").contains(e.target) &&
    createGameContainer.style.display != "none"
  ) {
    createGameContainer.style.display = "none";
  }
};

createGameContainer.addEventListener("click", createGameContainerHandleClick);
