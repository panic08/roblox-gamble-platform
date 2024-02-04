import { url } from "../config.js";
import { getCookie } from "../helpers/getCookie.js";

const authorization = getCookie("Authorization");

export const getAllGames = async () => {
  const response = await fetch(url + "/api/v1/game/coinflip/getAll", {
    method: "GET",
  });
  return response.json();
};

export const joinGame = async (id, data) => {
  console.log(id);
  console.log(JSON.stringify(data));
  const response = await fetch(url + `/api/v1/game/coinflip/${id}/join`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return response;
};

export const createGame = async (data) => {
  const response = await fetch(url + `/api/v1/game/coinflip`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return response;
};
