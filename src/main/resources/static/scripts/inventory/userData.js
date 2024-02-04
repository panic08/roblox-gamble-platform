import { getAuthInfo, userGetAuthStat } from "../queries/usersQueries.js";

const userProfitElements = document.querySelectorAll(".user-profit-number");
const userGamesPlayedElements = document.querySelectorAll(
  ".user-games-played-number"
);

const usernameElements = document.querySelectorAll(".user-name");
const userLogoImages = document.querySelectorAll(".user-logo-image");

const initProfile = async () => {
  const userStat = await userGetAuthStat();
  const userAuthInfo = await getAuthInfo();

  const userData = userAuthInfo.roblox_data;

  userProfitElements.forEach(
    (userProfitElement) => (userProfitElement.innerHTML = userStat.total_profit)
  );
  userGamesPlayedElements.forEach((userGamesPlayedElement) => {
    userGamesPlayedElement.innerHTML = userStat.games_played;
  });

  userLogoImages.forEach(
    (userLogoImage) => (userLogoImage.src = userData.roblox_avatar_link)
  );
  usernameElements.forEach(
    (usernameElement) => (usernameElement.innerHTML = userData.roblox_nickname)
  );
};

initProfile();
