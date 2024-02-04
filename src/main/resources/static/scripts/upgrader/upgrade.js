import { throwError } from "../page/error.js";
import { upgradeItem } from "../queries/upgraderQueries.js";
import {
  deleteActiveItem as deleteServerActiveItem,
  activeItem as serverActiveItem,
} from "./upgraderServerItems.js";
import {
  addItems,
  addWinItemToArray,
  deleteActiveItem as deleteUserActiveItem,
  activeItem as userActiveItem,
} from "./upgraderUserItems.js";

const upgradeButton = document.querySelector(".upgrade-btn");

const chanceBlock = document.querySelector(".chance-block");
const chanceBorders = document.querySelectorAll(".border-changeable");
const chanceLines = document.querySelectorAll(".chance-line-cross");
const isWin = document.querySelector(".chance-is-win");

const chanceTitle = document.querySelector(".chance-text-title");
const chanceNumber = document.querySelector(".chance-text-number");

const itemToUpgradeImage = document.querySelector(".item-to-upgrade-image");
const itemToReceiveImage = document.querySelector(".item-to-receive-image");

const itemToUpgradeCost = document.querySelector(".item-to-upgrade-cost");
const itemToReceiveCost = document.querySelector(".item-to-receive-cost");

const itemToUpgradeTitle = document.querySelector(".item-to-upgrade-title");
const itemToReceiveTitle = document.querySelector(".item-to-receive-title");

const defaultSrcWeaponImage = "/img/upgrader-knifes.png";

export let isPlaying = false;

const upgradeButtonHandleClick = async () => {
  if (isPlaying) {
    throwError("Game is already playing");
  } else if (serverActiveItem && userActiveItem) {
    if (serverActiveItem.item_cost <= userActiveItem.item_cost) {
      throwError("Item to receive must be more expensive than item to upgrade");
    } else {
      isPlaying = true;

      const response = await upgradeItem({
        from_user_item_id: userActiveItem.id,
        to_upgrader_item_id: serverActiveItem.id,
      });

      chanceBlock.style.animation = "spin 3s ease-in-out";

      console.log(response);
      setTimeout(() => {
        chanceTitle.style.display = "none";
        chanceNumber.style.display = "none";
        isWin.style.display = "block";
        if (response.win) {
          isWin.classList.add("win-text");
          isWin.innerHTML = "WIN";

          itemToUpgradeImage.src = itemToReceiveImage.src;
          itemToUpgradeCost.innerHTML = itemToReceiveCost.innerHTML;
          itemToUpgradeTitle.innerHTML = itemToReceiveTitle.innerHTML;
        } else {
          isWin.classList.add("loose-text");
          isWin.innerHTML = "LOOSE";
        }
      }, 3000);

      setTimeout(() => {
        chanceTitle.style.display = "block";
        chanceNumber.style.display = "block";

        isWin.classList.remove("win-text");
        isWin.classList.remove("loose-text");
        isWin.style.display = "none";
        isWin.innerHTML = "";
        chanceBorders.forEach((chanceBorder) => {
          if (response.win) {
            chanceBorder.classList.remove("win-border");
          } else {
            chanceBorder.classList.remove("loose-border");
          }
        });
        chanceLines.forEach((chanceLine) => {
          if (response.win) {
            chanceLine.classList.remove("win-line");
          } else {
            chanceLine.classList.remove("loose-line");
          }
        });

        if (response.win) {
          const item = response.winUserItem;
          addItems(item);
          addWinItemToArray(item);
        } else {
          document
            .querySelector(".server-item-list-block")
            .querySelector(`[data-id="${serverActiveItem.id}"]`).style.display =
            "flex";
        }
        deleteServerActiveItem();
        deleteUserActiveItem();

        chanceBlock.style.animation = "";

        itemToUpgradeCost.innerHTML = "0 F$";
        itemToReceiveCost.innerHTML = "0 F$";
        itemToUpgradeTitle.innerHTML = "User's Items";
        itemToReceiveTitle.innerHTML = "User's Items";
        itemToUpgradeImage.src = defaultSrcWeaponImage;
        itemToReceiveImage.src = defaultSrcWeaponImage;

        chanceNumber.innerHTML = "0%";

        isPlaying = false;
      }, 6000);

      if (response.win) {
        chanceBorders.forEach((chanceBorder) =>
          chanceBorder.classList.add("win-border")
        );
        chanceLines.forEach((chanceLine) =>
          chanceLine.classList.add("win-line")
        );
      } else {
        chanceBorders.forEach((chanceBorder) =>
          chanceBorder.classList.add("loose-border")
        );
        chanceLines.forEach((chanceLine) =>
          chanceLine.classList.add("loose-line")
        );
      }
    }
  } else {
    throwError("Choose items");
  }
};

upgradeButton.addEventListener("click", upgradeButtonHandleClick);
