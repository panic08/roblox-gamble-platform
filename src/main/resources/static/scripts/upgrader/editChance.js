import { activeItem as serverActiveItem } from "./upgraderServerItems.js";
import { activeItem as userActiveItem } from "./upgraderUserItems.js";

const chanceElement = document.querySelector(".chance-text-number");

export const editChance = () => {
  if (serverActiveItem && userActiveItem) {
    let chance = (userActiveItem.item_cost / serverActiveItem.item_cost) * 100;

    if (chance > 3) {
      chance = chance.toFixed(0);
    } else {
      chance = chance.toFixed(2);
    }

    chanceElement.innerHTML = chance + "%";
  }
};
