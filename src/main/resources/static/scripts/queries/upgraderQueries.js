import { url } from "../config.js";
import { getCookie } from "../helpers/getCookie.js";

const authorization = getCookie("Authorization");

export const upgraderItemsGetAll = async (id, minIndex, maxIndex) => {
  const response = await fetch(
    url +
      `/api/v1/game/upgrader/item/getAll?min_index=${minIndex}&max_index=${maxIndex}`,
    {
      method: "GET",
    }
  );
  return response.json();
};

export const upgradeItem = async (data) => {
  const response = await fetch(url + `/api/v1/game/upgrader`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return response.json();
};

export const adminAddItemToUpgrader = async (data) => {
  const response = await fetch(url + `/api/v1/game/upgrader/item`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return response;
};
