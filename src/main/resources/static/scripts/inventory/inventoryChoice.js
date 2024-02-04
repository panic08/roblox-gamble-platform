const userHistory = document.querySelector(".user-history");
const userInventory = document.querySelector(".user-inventory");

const inventoryBlock = document.querySelector(".inventory-block");
const historyBlock = document.querySelector(".history-block");

const withdrawButton = document.querySelector(".user-btn");

const userHistoryHandleClick = () => {
  withdrawButton.style.display = "none";

  userHistory.classList.add("active-user-choice");
  userInventory.classList.remove("active-user-choice");

  inventoryBlock.style.display = "none";
  historyBlock.style.display = "block";
};

const userInventoryHandleClick = () => {
  withdrawButton.style.display = "flex";

  userInventory.classList.add("active-user-choice");
  userHistory.classList.remove("active-user-choice");

  historyBlock.style.display = "none";
  inventoryBlock.style.display = "block";
};

userHistory.addEventListener("click", userHistoryHandleClick);
userInventory.addEventListener("click", userInventoryHandleClick);
