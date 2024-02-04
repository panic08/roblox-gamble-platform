import { deleteCookie } from "../helpers/deleteCookie.js";
import { setCookie } from "../helpers/setCookie.js";
import { getByRobloxNickname, login } from "../queries/authQueries.js";

const submit = document.querySelector(".login-block-btn-submit");
const cancel = document.querySelector(".login-block-btn-cancel");
const loginContainer = document.querySelector(".login-container");
const loginInput = document.querySelector(".login-block-input");
const phrasesBlock = document.querySelector(".phrases");
const phrasesText = document.querySelector(".phrases-text");

let isPhrases = false;
let token;

const generatePhrases = async () => {
  if (loginInput.value) {
    let phrases = await getByRobloxNickname(loginInput.value);

    if (phrases !== null) {
      token = phrases.headers.get("Phrase-Token");
      const phrasesJSON = await phrases.json();

      if (token) {
        setCookie("Phrase-Token", token, 21);

        isPhrases = true;
        phrasesText.innerHTML = phrasesJSON.phrases;
        phrasesBlock.style.display = "flex";
        loginInput.style.display = "none";
      }
    }
  }
};

const loginByToken = async () => {
  const response = await login(token);
  if (response.status === 200) {
    const authToken = response.headers.get("Authorization");
    setCookie("Authorization", authToken, 7);
    location.reload();
  } else {
    const json = await response.json();
    const message = json.message;
    const messageBlock = document.createElement("div");
    messageBlock.className = "login-error-message";
    messageBlock.innerHTML = message;

    const oldError = document.querySelector(".login-error-message");
    if (oldError) {
      oldError.replaceWith(messageBlock);
    } else {
      phrasesBlock.after(messageBlock);
    }
  }
};

const cancelClickHandler = () => {
  loginContainer.style.display = "none";
};

submit.addEventListener("click", () => {
  if (isPhrases) loginByToken();
  else generatePhrases();
});

cancel.addEventListener("click", () => {
  if (isPhrases) {
    phrasesBlock.style.display = "none";
    loginInput.style.display = "block";
    deleteCookie("Phrase-Token");
    isPhrases = false;
  } else {
    cancelClickHandler();
  }
  const Error = document.querySelector(".login-error-message");
  if (Error) {
    Error.remove();
  }
});
