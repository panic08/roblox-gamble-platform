import { isAuth } from "../auth/checkAuth.js";
import { formatNumber } from "../helpers/formatNumber.js";
import { throwError } from "../page/error.js";
import { joinGame } from "../queries/gamesQueries.js";
import {
  activeItemList,
  costs,
  deleteItemsFromList,
  updateDisplayValue,
} from "./menuComponent.js";

const menuContainer = document.querySelector(".game-menu-container");
const closeGameMenu = document.querySelector(".game-menu-close");
const valueForGame = document.querySelector(".value-for-game");
const joinGameButton = document.querySelector(".game-menu-btn");

let sessionId;

let coinflipJoinData = {
  user_items_ids: [],
};

const handleCloseGameMenuClick = () => {
  menuContainer.style.display = "none";
};

const handleJoinGameClick = async () => {
  if (
    costs.currentCost >= costs.minCost &&
    costs.currentCost <= costs.maxCost
  ) {
    coinflipJoinData.user_items_ids = activeItemList;
    const response = await joinGame(sessionId, coinflipJoinData);

    deleteItemsFromList(coinflipJoinData.user_items_ids);

    costs.currentCost = 0;

    updateDisplayValue();
    handleCloseGameMenuClick();
  } else if (costs.currentCost > costs.maxCost) {
    throwError("Too much value");
  } else {
    throwError("Too small value");
  }
};

export const initGameMenu = async (game) => {
  if (!isAuth()) {
    return;
  }
  console.log(game);
  sessionId = game.id;
  menuContainer.style.display = "flex";
  menuContainer.style.animation = "appear 0.15s linear";

  costs.minCost = game.minCost;
  costs.maxCost = game.maxCost;

  valueForGame.innerHTML =
    "Game Value: " +
    formatNumber(costs.minCost) +
    "-" +
    formatNumber(costs.maxCost);
};

joinGameButton.addEventListener("click", handleJoinGameClick);
closeGameMenu.addEventListener("click", handleCloseGameMenuClick);

const menuContainerHandleClick = (e) => {
  if (
    !document.querySelector(".game-menu-block").contains(e.target) &&
    menuContainer.style.display != "none"
  ) {
    menuContainer.style.display = "none";
  }
};

menuContainer.addEventListener("click", menuContainerHandleClick);
