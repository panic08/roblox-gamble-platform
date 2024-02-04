import { isAuth } from "../auth/checkAuth.js";
import { userGetAuthStat } from "../queries/usersQueries.js";

const profitElement = document.querySelector(".profit");

if (isAuth()) {
  const response = await userGetAuthStat();
  profitElement.innerHTML += " " + response.total_profit;
} else {
  profitElement.innerHTML += " " + 0;
}
