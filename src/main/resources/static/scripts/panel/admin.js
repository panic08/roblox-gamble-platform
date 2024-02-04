import { ADMIN_ID } from "../config.js";
import { formatNumber } from "../helpers/formatNumber.js";
import { throwError } from "../page/error.js";
import { sendAdminMessage } from "../queries/chatQueries.js";
import { getItemMedia } from "../queries/itemsQueries.js";
import { adminAddItemToUpgrader } from "../queries/upgraderQueries.js";
import { getAuthInfo, userItemsGetAll } from "../queries/usersQueries.js";

const adminItemsElement = document.querySelector(".admin-items-list");
const messageInput = document.querySelector(".message-input");
const postMessageButton = document.querySelector(".post-btn");
const addToUpgraderButton = document.querySelector(".add-upgrader-btn");

let itemsArray;

const setItemMedia = async (id) => {
  const src = await getItemMedia(id);
  return src;
};

const checkIsAdmin = async () => {
  const user = await getAuthInfo();

  if (!user) {
    window.location.href = "/";
  } else if (user.role !== "STAFF") {
    window.location.href = "/";
  }
};

const itemCountMap = new Map();

let activeItemId;

const itemsIdMap = new Map();

const itemElementHandleClick = (e) => {
  const itemElement = e.target.closest(".admin-item");
  if (itemElement.classList.contains("active-admin-item")) {
    itemElement.classList.remove("active-admin-item");
    activeItemId = null;
  } else {
    const activeItemElement = document.querySelector(".active-admin-item");
    if (activeItemElement) {
      activeItemElement.classList.remove("active-admin-item");
    }

    itemElement.classList.add("active-admin-item");

    activeItemId = parseInt(itemElement.dataset.item_id);
  }
};

const addToUpgrader = async () => {
  if (activeItemId) {
    const sendItem = itemsIdMap.get(activeItemId).pop();
    await adminAddItemToUpgrader({
      user_item_id: sendItem,
    });

    const decreaseCountItemElement = document.querySelector(
      `[data-item_id="${activeItemId}"]`
    );
    const countElement = decreaseCountItemElement.querySelector(".item-count");
    if (countElement.innerHTML == "x1") {
      decreaseCountItemElement.remove();
    } else {
      countElement.innerHTML =
        "x" + (parseInt(countElement.innerHTML.slice(1)) - 1);
    }
  } else {
    throwError("Choose item");
  }
};

addToUpgraderButton.addEventListener("click", addToUpgrader);

const addItem = async (item) => {
  if (itemCountMap.get(item.item_id) > 0) {
    itemsIdMap.get(item.item_id).push(item.id);
    itemCountMap.set(item.item_id, itemCountMap.get(item.item_id) + 1);

    const editItem = document.querySelector(`[data-item_id="${item.item_id}"]`);
    const count = editItem.querySelector(".item-count");

    count.innerHTML = "x" + (parseInt(count.innerHTML.slice(1)) + 1);
  } else {
    itemsIdMap.set(item.item_id, [item.id]);

    itemCountMap.set(item.item_id, 1);

    const itemElement = document.createElement("div");
    itemElement.className = "admin-item";
    itemElement.dataset["item_id"] = item.item_id;
    itemElement.addEventListener("click", itemElementHandleClick);

    const src = await setItemMedia(item.item_id);

    itemElement.innerHTML = `
  <img class="item-image" src="${src}">
   <div class="item-name">${item.item_full_name}</div>
   <div class="item-count">x1</div>
   <div class="item-value">${formatNumber(item.item_cost) + " F$"}</div>
  `;

    adminItemsElement.append(itemElement);
  }
};

const fillItems = async () => {
  itemsArray = await userItemsGetAll(ADMIN_ID);

  for (const item of itemsArray) {
    await addItem(item);
  }
};

const postMessage = async () => {
  const message = messageInput.value;
  const response = await sendAdminMessage(message);

  if (response.status !== 200) {
    const responseJSON = await response.json();
    const errorMessage = responseJSON.message;
    throwError(errorMessage);
  }
  messageInput.value = "";
};

postMessageButton.addEventListener("click", postMessage);

const ENTER_KEY = 13;

messageInput.addEventListener("keypress", (e) => {
  if (e.which === ENTER_KEY) {
    postMessage();
  }
});

const initAdminPage = () => {
  checkIsAdmin();
  fillItems();
};

initAdminPage();
