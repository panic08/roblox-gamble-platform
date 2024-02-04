const openChat = document.querySelector(".open-chat");
const chat = document.querySelector(".chat");
const openChatImage = document.querySelector(".open-chat-image");

let rotate = 0;

const openChatHandleClick = () => {
  chat.classList.toggle("full-chat");
  openChatImage.style = `transform: rotate(${rotate}deg)`;
  rotate = rotate === 0 ? 180 : 0;
};

openChat.addEventListener("click", openChatHandleClick);
