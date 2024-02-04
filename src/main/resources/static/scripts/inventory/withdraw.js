import { activeItemList, deleteItemsFromList } from "../components/itemList.js";
import { depositClickHandler } from "../page/header.js";
import { withdraw } from "../queries/withdrawQueries.js";

const withdrawButton = document.querySelector(".user-btn");

let withdrawData = {
  user_items_ids: [],
};

const handleWithdrawClick = async () => {
  if (activeItemList.length > 0) {
    withdrawData.user_items_ids = activeItemList;
    await withdraw(withdrawData);

    withdrawButton.innerHTML = "Withdraw";
    deleteItemsFromList();
    depositClickHandler();
  }
};

withdrawButton.addEventListener("click", handleWithdrawClick);
