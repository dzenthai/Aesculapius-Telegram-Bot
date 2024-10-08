# Healthcare-Telegram-Bot

## **Description**

Healthcare-Telegram-Bot is a Telegram bot designed to assist users with health-related questions. It leverages the OpenAI API under the hood and is built using Java, Spring Boot, Redis, and Docker.

---

## **Key Features**

- **Health Assistance**: Provides answers to health-related questions using the OpenAI API.
- **Persistent Context**: Uses Redis to store the conversation context for more coherent and context-aware responses.
- **Voice to Text**: Transcribes voice messages to text and responds accordingly.
- **Simple Commands**: Includes commands to start a conversation, get bot information, and clear the conversation context and cache.

---

## **Technologies**

- **Java**: The primary programming language.
- **Spring Boot**: Used for API key and Telegram token management, as well as connecting to Redis.
- **Redis**: Stores conversation context to maintain continuity in dialogue.
- **Docker**: Facilitates containerization for easy deployment.

---

## **Installation Guide**

### **Prerequisites**

- Java 21
- Docker
- Redis

### **Installation and Startup Steps**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/dzenthai/Healthcare-Telegram-Bot.git
   cd Healthcare-Telegram-Bot
   
2. **Add Environment Variables**
   Create an .env file and add necessary environment variables such as Telegram bot tokens and API keys.

3. **Build the Project Using Maven**
   ```bash
   mvn clean install

4. **Run the Application Using Docker**
   ```bash
   docker-compose up --build

## **Additional Information**

### **Commands**

- `/start`: Begins a conversation with the bot.
- `/info`: Displays information about the bot.
- `/clear`: Clears the conversation context and cache (Redis).

### **Bot Context Configuration**

In the `ChatGptService` class, the `SYSTEM_MESSAGE` variable sets the bot's context:

```java
private static final String SYSTEM_MESSAGE = "...";

