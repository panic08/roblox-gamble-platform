import { url } from "../config.js";

export const getByRobloxNickname = async (name) => {
  const response = await fetch(
    `${url}/api/v1/auth/phrase/getByRobloxNickname?roblox_nickname=${name}`,
    {
      method: "GET",
    }
  );
  if (response.status === 200) {
    return response;
  } else {
    return null;
  }
};

export const login = async (token) => {
  const response = await fetch(url + "/api/v1/auth/login", {
    method: "POST",
    headers: {
      "Phrase-Token": token,
    },
  });
  return response;
};
