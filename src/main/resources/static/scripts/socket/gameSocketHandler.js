import { isAuth } from "../auth/checkAuth.js";
import { fillOtherSide, joinHandleClick, showGame } from "../coinflip/game.js";

import { addGameToArray, gamesData } from "../coinflip/showGames.js";
import { getAuthInfo } from "../queries/usersQueries.js";

const deleteGame = (game) => {
  const id = game.id;
  const deleteGameElement = document.querySelector(`[data-id="${id}"]`);
  deleteGameElement.remove();
};

let username;
if (isAuth()) {
  const authInfo = await getAuthInfo();
  username = authInfo.roblox_data.roblox_nickname;
}

export const gameSocketHandler = (message) => {
  const json = JSON.parse(message.body);
  const game = json.data;

  if (json.type === "create_coinFlip_session") {
    addGameToArray(game);
    showGame(game);
  } else if (json.type === "updating_coinFlip_session") {
    const room = document.querySelector(`[data-id="${game.id}"]`);

    const joinButton = room.querySelector(".game-btn-join");
    joinButton.removeEventListener("click", joinHandleClick);
    joinButton.classList.remove("active-btn");

    const gameUsers = room.querySelector(".game-users");

    fillOtherSide(game, gameUsers, room);

    const animateCoinFlip = room.querySelector(".coin-image");

    animateCoinFlip.classList.add("animate-flip");

    setTimeout(() => {
      const coinFlipBlock = animateCoinFlip.querySelector(".coin-flip");
      const coinPurpleBlock =
        animateCoinFlip.querySelector(".coin-yellow-block");
      const coinYellowBlock =
        animateCoinFlip.querySelector(".coin-purple-block");
      coinFlipBlock.style = "animation: none;";
      coinPurpleBlock.style = "animation: none;";
      coinYellowBlock.style = "animation: none;";
      if (game.winner_user.id == game.issuer_user.id) {
        coinYellowBlock.style = "display: none;";
      }
    }, 2000);

    const gameFromArray = gamesData.find(
      (gameFromArray) => gameFromArray.id === game.id
    );
    gameFromArray.salt = game.salt;
  } else if (json.type === "delete_coinFlip_session") {
    deleteGame(game);
  }
};
