import { url } from "../config.js";

export const getItem = () => {};

export const getItemMedia = async (id) => {
  let response = await fetch(url + `/api/v1/item/${id}/media`);
  const blob = await response.blob();

  const newBlob = new Blob([blob]);
  const newUrl = window.URL.createObjectURL(newBlob);
  return newUrl;
};
