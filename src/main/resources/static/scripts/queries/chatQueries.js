import { url } from "../config.js";
import { getCookie } from "../helpers/getCookie.js";

const authorization = getCookie("Authorization");

export const sendMessage = async (message) => {
  const response = await fetch(`${url}/api/v1/chat`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message }),
  });
  return response;
};

export const sendAdminMessage = async (message) => {
  const response = await fetch(`${url}/api/v1/chat/admin`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authorization}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message }),
  });
  return response;
};
