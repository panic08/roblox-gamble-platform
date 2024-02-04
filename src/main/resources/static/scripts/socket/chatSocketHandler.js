const messages = document.querySelector(".chat-messages");

let messageCounter = 0;

export const chatSocketHandler = (message) => {
  // Обработка пришедшего сообщения
  const json = JSON.parse(message.body);
  if (json.type === "create_chat_message") {
    const data = json.data;

    const newMessage = document.createElement("div");
    newMessage.className = "message";

    const user = data.user;
    const userData = user.roblox_data;

    const newMessageLogo = document.createElement("img");
    newMessageLogo.className = "message-logo";
    newMessageLogo.src = userData.roblox_avatar_link;

    const newMessageUsername = document.createElement("div");
    newMessageUsername.className = `message-username ${
      user.role === "STAFF" ? "admin-username" : ""
    }`;
    newMessageUsername.innerHTML = userData.roblox_nickname;

    const newMessageText = document.createElement("div");
    newMessageText.className = "message-text";
    newMessageText.innerHTML = data.message;

    const newMessageTextBlock = document.createElement("div");
    newMessageTextBlock.className = "message-text-block";
    newMessageTextBlock.append(newMessageUsername);
    newMessageTextBlock.append(newMessageText);

    newMessage.append(newMessageLogo);
    newMessage.append(newMessageTextBlock);

    messages.prepend(newMessage);

    if (messageCounter === 50) {
      messages.removeChild(messages.lastChild);
    } else {
      messageCounter++;
    }
  }
};
