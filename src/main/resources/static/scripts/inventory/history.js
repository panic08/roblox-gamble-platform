import {
  getAuthInfo,
  userGamesGetAll,
  userGetAuthStat,
} from "../queries/usersQueries.js";

const historyList = document.querySelector(".history-list");
const historyButtons = document.querySelectorAll(".history-btn");

const user = await getAuthInfo();
const id = user.id;

const gamesPerPage = 5;

let startIndex = 0;
let endIndex = gamesPerPage;

const userStat = await userGetAuthStat();
const gamesPlayed = userStat.games_played;

const gamesHistoryMap = new Map();

const showHistoryGames = async () => {
  let games = [];

  if (gamesHistoryMap.get(startIndex)) {
    for (let i = startIndex; i < endIndex; i++) {
      games.push(gamesHistoryMap.get(i));
    }
  } else {
    games = await userGamesGetAll(id, startIndex, endIndex);

    for (let i = startIndex; i < endIndex; i++) {
      gamesHistoryMap.set(i, games[i - startIndex]);
    }
  }

  let index = 0;
  historyList.innerHTML = "";

  games.forEach((game) => {
    const amount = game.win
      ? "+" + game.amount + " F$"
      : "-" + game.amount + " F$";

    const gameElement = `
    <div class="history-game ${index % 2 === 0 ? "game-gray-color" : ""}">
      <div class="history-game-id">${game.id}</div>  
      <div class="history-game-amount ${
        game.win ? "amount-green" : "amount-red"
      }">${amount}</div>  
      <div class="history-game-type">${game.type}</div>  
    </div>`;
    historyList.innerHTML += gameElement;
    index++;
  });
};

const historyButtonHandleClick = (e) => {
  const button = e.target.closest(".history-btn");

  if (button.classList.contains("more-left-btn")) {
    if (startIndex < gamesPerPage * 5) {
      return;
    }
    endIndex -= gamesPerPage * 5;
    startIndex -= gamesPerPage * 5;
  } else if (button.classList.contains("left-btn")) {
    if (startIndex < gamesPerPage) {
      return;
    }
    endIndex -= gamesPerPage;
    startIndex -= gamesPerPage;
  } else if (button.classList.contains("right-btn")) {
    if (endIndex > gamesPlayed - gamesPerPage) {
      return;
    }
    startIndex += gamesPerPage;
    endIndex += gamesPerPage;
  } else {
    if (endIndex > gamesPlayed - gamesPerPage * 5) {
      return;
    }
    startIndex += gamesPerPage * 5;
    endIndex += gamesPerPage * 5;
  }
  showHistoryGames();
};

const initHistory = async () => {
  showHistoryGames();
};

initHistory();

historyButtons.forEach((historyButton) =>
  historyButton.addEventListener("click", historyButtonHandleClick)
);
