import { isAuth } from "../auth/checkAuth.js";
import { formatNumber } from "../helpers/formatNumber.js";
import { getItemMedia } from "../queries/itemsQueries.js";
import { getAuthInfo, userItemsGetAll } from "../queries/usersQueries.js";
import { editChance } from "./editChance.js";
import { isPlaying } from "./upgrade.js";

const listBlockElement = document.querySelector(".to-upgrade-items-block");
const costElement = document.querySelector(".item-to-upgrade-cost");
const itemImageElement = document.querySelector(".item-to-upgrade-image");
const itemNameElement = document.querySelector(".item-to-upgrade-title");

let itemListElement;

export let activeItem;

let mediaMap = new Map();

const setItemMedia = async (id) => {
  let src;
  if (mediaMap.get(id)) {
    src = mediaMap.get(id);
  } else {
    src = await getItemMedia(id);
    mediaMap.set(id, src);
  }
  return src;
};

const handleItemListSearchInput = (e) => {
  if (!isLoading) {
    const items = itemListElement.querySelectorAll(".game-menu-item-block");
    items.forEach((item) => {
      const itemName = item.lastElementChild.innerText;
      if (itemName.toLowerCase().includes(e.target.value.toLowerCase())) {
        item.style.display = "flex";
      } else {
        item.style.display = "none";
      }
    });
  }
};

export const deleteActiveItem = () => {
  activeItem = undefined;
};

const addItemToArray = (itemBlock) => {
  if (!isPlaying) {
    const item = itemsList.find((item) => item.id == itemBlock.dataset.id);

    if (activeItem) {
      const disabledBlock = itemListElement.querySelector(
        `[data-id="${activeItem.id}"]`
      );
      disabledBlock.style.display = "flex";
    }
    activeItem = item;
    itemBlock.style.display = "none";

    costElement.innerHTML = formatNumber(item.item_cost) + " F$";
    itemImageElement.src = mediaMap.get(item.item_id);
    itemImageElement.style.opacity = "1";

    itemNameElement.innerHTML = item.item_full_name;

    editChance();
  }
};

export const addWinItemToArray = (item) => {
  itemsList.push(item);
};

let itemsList;

let options = {
  root: null,
  rootMargin: "0px",
  threshold: 0,
};

let observer = new IntersectionObserver(async (entries, observer) => {
  for (const entry of entries) {
    if (entry.isIntersecting) {
      triggers.forEach((trigger) => observer.unobserve(trigger));
      triggers = [];

      const newList = await userItemsGetAll(user.id, startIndex, endIndex);
      itemsList = itemsList.concat(newList);

      if (newList.length > 0) {
        startIndex = endIndex + 1;
        endIndex *= 2;

        isLoading = true;
        for (const item of newList) {
          await addItems(item);
        }
        isLoading = false;

        addTriggers();
      }
    }
  }
}, options);

export const addItems = async (item) => {
  const itemName = document.createElement("div");
  itemName.className = "game-menu-item-name";
  itemName.innerHTML = item.item_full_name;

  const itemImage = document.createElement("img");
  itemImage.className = "game-menu-item-image";
  const src = await setItemMedia(item.item_id);
  itemImage.src = src;

  const itemCost = document.createElement("div");
  itemCost.className = "game-menu-item-cost";
  itemCost.innerHTML = formatNumber(item.item_cost) + "F$";

  const itemBlock = document.createElement("div");
  itemBlock.className = "game-menu-item-block";
  itemBlock.dataset.id = item.id;
  itemBlock.append(itemCost);
  itemBlock.append(itemImage);
  itemBlock.append(itemName);
  itemBlock.addEventListener("click", () => addItemToArray(itemBlock));

  itemListElement.append(itemBlock);
};

let itemsListElement;
let itemsListBlock;

let startIndex = 0;
let endIndex = 10;

const user = await getAuthInfo();

let triggers = [];

let isLoading = false;

const addTriggers = () => {
  triggers.push(itemListElement.lastElementChild);

  observer.observe(itemListElement.lastElementChild);
};

export const initUpgraderUserItems = async () => {
  if (!isAuth()) {
    return;
  }

  const activeLists = document.querySelectorAll(".game-menu-items-list");
  activeLists.forEach((item) => item.remove());

  itemsList = await userItemsGetAll(user.id, startIndex, endIndex);

  startIndex = endIndex + 1;
  endIndex *= 2;

  itemsListBlock = document.createElement("div");
  itemsListBlock.className = "item-list-block";

  itemsListElement = document.createElement("div");
  itemsListElement.className = "game-menu-items-list";

  const itemListSearch = document.createElement("input");
  itemListSearch.className = "game-menu-search-input";
  itemListSearch.addEventListener("input", handleItemListSearchInput);
  itemListSearch.placeholder = "Search for an item";

  const blockTitle = document.createElement("div");
  blockTitle.className = "game-block-title";
  blockTitle.innerHTML = "My Inventory";

  const itemListSearchBlock = document.createElement("div");
  itemListSearchBlock.className = "game-menu-search";
  itemListSearchBlock.append(blockTitle);
  itemListSearchBlock.append(itemListSearch);

  itemsListBlock.append(itemListSearchBlock);
  itemsListBlock.append(itemsListElement);

  listBlockElement.prepend(itemsListBlock);

  itemListElement = document.querySelector(".game-menu-items-list");

  isLoading = true;
  for (const item of itemsList) {
    await addItems(item);
  }
  isLoading = false;

  if (itemsList) {
    addTriggers();
  }
};
