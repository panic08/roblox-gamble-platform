import { isAuth } from "../auth/checkAuth.js";
import { formatNumber } from "../helpers/formatNumber.js";
import { getItemMedia } from "../queries/itemsQueries.js";
import { getAuthInfo } from "../queries/usersQueries.js";
import { gameInfoButtonHandleClick } from "./gameInfoBlock.js";
import { initGameMenu } from "./gameMenu.js";
import { gamesData } from "./showGames.js";

export const joinHandleClick = (e) => {
  initGameMenu(gamesData.find((game) => game.id == e.target.dataset.id));
};

let username;

if (isAuth()) {
  const response = await getAuthInfo();
  username = response.roblox_data.roblox_nickname;
}

export const fillOtherSide = async (game, gameUsers, gameElement) => {
  const otherUsername = game.other_side_user.roblox_data.roblox_nickname;
  const otherLogo = game.other_side_user.roblox_data.roblox_avatar_link;
  const otherItems = game.other_side_items;

  const otherLogoImage = document.createElement("img");
  otherLogoImage.src = otherLogo;

  const otherLogoElement = document.createElement("div");
  otherLogoElement.className = "other-side-logo";
  otherLogoElement.append(otherLogoImage);

  const otherUsernameElement = document.createElement("div");
  otherUsernameElement.className = "other-side-username";
  otherUsernameElement.innerHTML = otherUsername;

  const otherSideInfo = document.createElement("div");
  otherSideInfo.className = "other-side-info";
  otherSideInfo.append(otherLogoElement);
  otherSideInfo.append(otherUsernameElement);

  const otherItemsList = document.createElement("div");
  otherItemsList.className = "other-side-items-list";
  for (let i = 0; i < otherItems.length; ++i) {
    if (i >= 5) break;
    const otherItem = otherItems[i];
    const otherItemElement = document.createElement("div");
    otherItemElement.className = "other-side-item";
    const otherItemImage = document.createElement("img");
    const src = await getItemMedia(otherItem.id);
    otherItemImage.src = src;
    otherItemElement.append(otherItemImage);
    otherItemsList.append(otherItemElement);
  }

  const otherItemsElement = document.createElement("div");
  otherItemsElement.className = "other-side-items";
  otherItemsElement.append(otherItemsList);

  if (otherItems.length > 5) {
    const count = otherItems.length - 5;
    const otherItemsNumber = document.createElement("div");
    otherItemsNumber.className = "other-side-items-number";
    otherItemsNumber.innerHTML = `+${count} Items`;
    otherItemsElement.append(otherItemsNumber);
  }

  const otherSide = document.createElement("div");
  otherSide.className = "other-side";
  otherSide.append(otherSideInfo);
  otherSide.append(otherItemsElement);

  const costElement = gameElement.querySelector(".cost");
  const minMaxCostElement = gameElement.querySelector(".min-max-cost");

  if (gameUsers.querySelector(".other-side")) {
    gameUsers.querySelector(".other-side").remove();
  }

  updateCost(game, costElement, minMaxCostElement);

  gameUsers.append(otherSide);
};

const updateCost = (game, costElement, minMaxCostElement) => {
  let cost = 0;
  const firstItems = game.issuer_items;

  firstItems.forEach((firstItem) => {
    cost += firstItem.cost;
  });

  if (game.other_side_items) {
    const otherItems = game.other_side_items;
    otherItems.forEach((otherItem) => {
      cost += otherItem.cost;
    });
  }

  game.cost = cost;
  game.minCost = (cost * 0.9).toFixed(1);
  game.maxCost = (cost * 1.1).toFixed(1);

  costElement.innerHTML = formatNumber(game.cost) + " F$";
  minMaxCostElement.innerHTML =
    formatNumber(game.minCost) + "-" + formatNumber(game.maxCost) + " F$";
};

export const showGame = async (game) => {
  //first user
  const firstUsername = game.issuer_user.roblox_data.roblox_nickname;
  const firstLogo = game.issuer_user.roblox_data.roblox_avatar_link;
  const firstItems = game.issuer_items;

  const firstLogoImage = document.createElement("img");
  firstLogoImage.src = firstLogo;

  const firstLogoElement = document.createElement("div");
  firstLogoElement.className = "first-side-logo";
  firstLogoElement.append(firstLogoImage);

  const firstUsernameElement = document.createElement("div");
  firstUsernameElement.className = "first-side-username";
  firstUsernameElement.innerHTML = firstUsername;

  const firstSideInfo = document.createElement("div");
  firstSideInfo.className = "first-side-info";
  firstSideInfo.append(firstLogoElement);
  firstSideInfo.append(firstUsernameElement);

  const firstItemsList = document.createElement("div");

  firstItemsList.className = "first-side-items-list";
  for (let i = 0; i < firstItems.length; ++i) {
    if (i >= 5) break;
    const firstItem = firstItems[i];
    const firstItemElement = document.createElement("div");
    firstItemElement.className = "first-side-item";
    const firstItemImage = document.createElement("img");
    const src = await getItemMedia(firstItem.id);
    firstItem.src = src;
    firstItemImage.src = src;
    firstItemElement.append(firstItemImage);
    firstItemsList.append(firstItemElement);
  }

  const firstItemsElement = document.createElement("div");
  firstItemsElement.className = "first-side-items";
  firstItemsElement.append(firstItemsList);

  if (firstItems.length > 5) {
    const count = firstItems.length - 5;
    const firstItemsNumber = document.createElement("div");
    firstItemsNumber.className = "first-side-items-number";
    firstItemsNumber.innerHTML = `+${count} Items`;
    firstItemsElement.append(firstItemsNumber);
  }

  const firstSide = document.createElement("div");
  firstSide.className = "first-side";
  firstSide.append(firstSideInfo);
  firstSide.append(firstItemsElement);

  const gameUsers = document.createElement("div");
  gameUsers.className = "game-users";
  gameUsers.append(firstSide);
  //second user
  if (game.other_side_user) {
    fillOtherSide(game, gameUsers, gameElement);
  } else {
    const otherUsername = "No one";

    const otherLogoElement = document.createElement("div");
    otherLogoElement.className = "other-side-logo";

    const otherUsernameElement = document.createElement("div");
    otherUsernameElement.className = "other-side-username";
    otherUsernameElement.innerHTML = otherUsername;

    const otherSideInfo = document.createElement("div");
    otherSideInfo.className = "other-side-info";
    otherSideInfo.append(otherLogoElement);
    otherSideInfo.append(otherUsernameElement);

    const otherSide = document.createElement("div");
    otherSide.className = "other-side";
    otherSide.append(otherSideInfo);

    gameUsers.append(otherSide);
  }

  //other elements

  const costElement = document.createElement("div");
  costElement.className = "cost";

  const minMaxCostElement = document.createElement("div");
  minMaxCostElement.className = "min-max-cost";

  const costs = document.createElement("div");
  costs.className = "costs";
  costs.append(costElement);
  costs.append(minMaxCostElement);

  const coinImageYellow = document.createElement("img");
  coinImageYellow.src = "/img/PCoin.png";
  coinImageYellow.className = "coin-real";

  const coinImageYellowShadow = document.createElement("img");
  coinImageYellowShadow.src = "/img/PCoin.png";
  coinImageYellowShadow.className = "coin-shadow";

  const coinImagePurple = document.createElement("img");
  coinImagePurple.src = "/img/YCoin.png";
  coinImagePurple.className = "coin-real";

  const coinImagePurpleShadow = document.createElement("img");
  coinImagePurpleShadow.src = "/img/YCoin.png";
  coinImagePurpleShadow.className = "coin-shadow";

  const coinYellowBlock = document.createElement("div");
  coinYellowBlock.className = "coin-yellow-block";
  coinYellowBlock.append(coinImageYellow);
  coinYellowBlock.append(coinImageYellowShadow);

  const coinPurpleBlock = document.createElement("div");
  coinPurpleBlock.className = "coin-purple-block";
  coinPurpleBlock.append(coinImagePurple);
  coinPurpleBlock.append(coinImagePurpleShadow);

  const coinImageBlock = document.createElement("div");
  coinImageBlock.className = "coin-image";

  const coinFlipBlock = document.createElement("div");
  coinFlipBlock.className = "coin-flip";

  coinFlipBlock.append(coinYellowBlock);
  coinFlipBlock.append(coinPurpleBlock);

  coinImageBlock.append(coinFlipBlock);

  const joinButton = document.createElement("div");
  joinButton.innerHTML = "JOIN";

  if (
    isAuth() &&
    !game.other_side_user &&
    game.issuer_user.roblox_data.roblox_nickname !== username
  ) {
    joinButton.className = "game-btn-join game-btn active-btn";
    joinButton.addEventListener("click", joinHandleClick);
  } else {
    joinButton.className = "game-btn-join game-btn";
  }
  joinButton.dataset.id = game.id;

  const fairnessButton = document.createElement("div");
  fairnessButton.innerHTML = "FAIRNESS";
  fairnessButton.className = "game-btn-info game-btn";

  fairnessButton.addEventListener("click", () =>
    gameInfoButtonHandleClick(game.id)
  );

  const gameButtons = document.createElement("div");
  gameButtons.className = "game-btns";
  gameButtons.append(joinButton);
  gameButtons.append(fairnessButton);

  const gameContent = document.createElement("div");
  gameContent.className = "game-content";
  gameContent.append(costs);
  gameContent.append(coinImageBlock);
  gameContent.append(gameButtons);

  const gameElement = document.createElement("div");
  gameElement.className = "game";
  gameElement.dataset.id = game.id;

  gameElement.append(gameUsers);
  gameElement.append(gameContent);

  updateCost(game, costElement, minMaxCostElement);

  const games = document.querySelector(".games");
  games.append(gameElement);
};
