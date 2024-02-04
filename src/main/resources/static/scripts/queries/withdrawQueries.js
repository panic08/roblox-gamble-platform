import { url } from "../config.js";
import { getCookie } from "../helpers/getCookie.js";

const authorization = getCookie("Authorization");

export const withdraw = async (data) => {
  const response = await fetch(url + `/api/v1/withdrawal`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return response;
};
