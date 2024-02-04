import { getAllGames } from "../queries/gamesQueries.js";
import { showGame } from "./game.js";

export let gamesData;

export const addGameToArray = (game) => {
  gamesData.push(game);
};

export const showGames = async () => {
  gamesData = await getAllGames();
  for (let game of gamesData) {
    showGame(game);
  }
};
