import { activeItemList, initItemList } from "../components/itemList.js";
import { userItemsGetAll } from "../queries/usersQueries.js";

const inventoryBlock = document.querySelector(".inventory-block");

const userButton = document.querySelector(".user-btn");

const changeWithDrawText = () => {
  if (activeItemList.length === 0) {
    userButton.innerHTML = userButton.innerText.split(" ")[0];
  } else {
    userButton.innerHTML =
      userButton.innerText.split(" ")[0] + ` (${activeItemList.length})`;
  }
};

const gameInventoryArray = [
  {
    menuBlock: inventoryBlock,
    updateFunction: () => changeWithDrawText(),
  },
];

const initInventory = () => {
  initItemList(gameInventoryArray, userItemsGetAll);
};

initInventory();
