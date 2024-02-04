import { isAuth } from "../auth/checkAuth.js";
import { formatNumber } from "../helpers/formatNumber.js";
import { getItemMedia } from "../queries/itemsQueries.js";
import { getAuthInfo, userItemsGetAll } from "../queries/usersQueries.js";
import { activeItemList as activeCreateItemList } from "./createMenuComponent.js";

const menuBlock = document.querySelector(".game-menu-block");
const currentValue = document.querySelector(".current-value");

export let activeItemList = [];

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
    const itemListElement = e.target.parentElement.nextElementSibling;
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

export const deleteItemsFromList = () => {
  activeItemList.forEach((id) => {
    const deleteItems = document.querySelectorAll(
      `.game-menu-items-list [data-id="${id}"]`
    );

    deleteItems.forEach((item) => item.remove());
    if (activeCreateItemList.indexOf(parseInt(id)) !== -1) {
      activeCreateItemList.splice(
        activeCreateItemList.indexOf(parseInt(id)),
        1
      );
    }
  });

  activeItemList = [];
};

export let costs = {
  currentCost: 0,
  minCost: 0,
  maxCost: 0,
};

const updateValue = (item, itemActive) => {
  const cost = item.item_cost;

  if (itemActive) {
    costs.currentCost -= cost;
  } else {
    costs.currentCost += cost;
  }
  updateDisplayValue();
};

export const updateDisplayValue = () => {
  currentValue.innerHTML = `Your Value: ${costs.currentCost}`;
};

const addItemToArray = (itemBlock) => {
  const item = itemsList.find((item) => item.id == itemBlock.dataset.id);
  const itemActive = itemBlock.classList.contains("active-item") ? true : false;

  if (itemActive) {
    itemBlock.classList.remove("active-item");

    const index = activeItemList.indexOf(parseInt(itemBlock.dataset.id));
    activeItemList.splice(index, 1);
  } else {
    itemBlock.classList.add("active-item");

    activeItemList.push(parseInt(itemBlock.dataset.id));
  }

  updateValue(item, itemActive);
};

export let itemsList;

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

const addItems = async (item) => {
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

  const itemListElement = menuBlock.querySelector(".game-menu-items-list");

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
  let targetElement = document.querySelectorAll(".game-menu-items-list");

  targetElement.forEach((element) => {
    triggers.push(element.lastElementChild);

    observer.observe(element.lastElementChild);
  });
};

export const initItemList = async () => {
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

  const itemListSearchBlock = document.createElement("div");
  itemListSearchBlock.className = "game-menu-search";
  itemListSearchBlock.append(itemListSearch);

  itemsListBlock.append(itemListSearchBlock);
  itemsListBlock.append(itemsListElement);

  menuBlock.prepend(itemsListBlock);

  isLoading = true;
  for (const item of itemsList) {
    await addItems(item);
  }
  isLoading = false;

  if (itemsList.length > 0) {
    addTriggers();
  }
};
