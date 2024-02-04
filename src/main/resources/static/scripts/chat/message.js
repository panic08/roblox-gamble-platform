import { throwError } from "../page/error.js";
import { sendMessage } from "../queries/chatQueries.js";

const submitMessage = document.querySelector(".chat-submit");
const messageInput = document.querySelector(".chat-input");

const submitMessageHandleClick = async () => {
  let response = await sendMessage(messageInput.value);
  if (response.status !== 200) {
    response = await response.json();
    const message = response.message;
    throwError(message);
  }
  messageInput.value = "";
};

submitMessage.addEventListener("click", submitMessageHandleClick);

const ENTER_KEY = 13;

messageInput.addEventListener("keypress", (e) => {
  if (e.which === ENTER_KEY) {
    submitMessageHandleClick();
  }
});
