import { url } from "../config.js";
import { chatSocketHandler } from "./chatSocketHandler.js";

const socket = new SockJS(url + "/socket");

// Создаем объект Stomp
const stompClient = Stomp.over(socket);

stompClient.debug = null;
// Подключаемся к серверу
stompClient.connect({}, function (frame) {
  stompClient.subscribe("/chat/topic", function (message) {
    chatSocketHandler(message);
  });
  if (document.querySelector(".games")) {
    import("./gameSocketHandler.js").then(({ gameSocketHandler }) => {
      stompClient.subscribe("/game/coinflip/topic", function (message) {
        gameSocketHandler(message);
      });
    });
  }
});
