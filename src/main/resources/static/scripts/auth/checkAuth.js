import { getCookie } from "../helpers/getCookie.js";

export const isAuth = () => {
  if (getCookie("Authorization")) return true;
  else return false;
};
