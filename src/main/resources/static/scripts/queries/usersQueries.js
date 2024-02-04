import { url } from "../config.js";
import { getCookie } from "../helpers/getCookie.js";

const authorization = getCookie("Authorization");

export const userItemsGetAll = async (id, minIndex, maxIndex) => {
  let path;
  if (minIndex && maxIndex) {
    path = `/api/v1/user/${id}/item/getAll?min_index=${minIndex}&max_index=${maxIndex}`;
  } else {
    path = `/api/v1/user/${id}/item/getAll`;
  }
  const response = await fetch(url + path, {
    method: "GET",
  });
  return response.json();
};

export const getAuthInfo = async () => {
  const response = await fetch(url + "/api/v1/user/getAuthInfo", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${authorization}`,
    },
  });
  if (response.status === 200) {
    return response.json();
  }
};

export const userGetAuthStat = async () => {
  const response = await fetch(url + "/api/v1/user/game/getAuthStat", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${authorization}`,
    },
  });
  return response.json();
};

export const userGamesGetAll = async (id, minIndex, maxIndex) => {
  const response = await fetch(
    url +
      `/api/v1/user/${id}/game/getAll?min_index=${minIndex}&max_index=${maxIndex}`,
    {
      method: "GET",
    }
  );
  return response.json();
};
