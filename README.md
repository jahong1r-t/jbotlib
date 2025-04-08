# JBotLib

**A Java library for building Telegram bots with ease and flexibility**

JBotLib is a lightweight and powerful Java library designed to simplify Telegram bot development. By leveraging annotations like `@BotCommand`, `@AutoReply`, `@AdminOnly`, and `@ScheduledTask`, JBotLib reduces boilerplate code, allowing developers to focus on bot logic while handling the complexity of the Telegram Bot API under the hood.

## 🚀 Features

- **Annotation-Driven Development**: Use annotations to define bot commands, auto-replies, admin-only commands, and scheduled tasks.
- **Simplified Message Handling**: Send text, photos, videos, documents, polls, and media groups with a single method call.
- **Interactive Keyboards**: Easily create reply keyboards, inline keyboards, URL buttons, and pagination keyboards.
- **Chat Management**: Manage chat settings, restrict/unrestrict members, pin messages, and more.
- **Event Logging**: Log user actions, bot actions, errors, and warnings with support for file logging and contextual data.
- **Extensible Architecture**: Modular design with services like `MessageService`, `KeyboardBuilder`, `ChatService`, and `AnnotationService`.

## 📦 Installation

### Prerequisites
- Java 11 or higher
- Maven (for dependency management)
- A Telegram Bot Token (get one from [BotFather](https://t.me/BotFather))

### Add JBotLib to Your Project

1. Add the JBotLib dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>jbotlib</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. Clone the repository and build the project:

```bash
git clone https://github.com/yourusername/jbotlib.git
cd jbotlib
mvn install
```

## 🛠️ Getting Started

### 1. Create a Simple Bot

Create a new class that extends `JBotLib` and implement the required methods:

```java
package bot;

import bot.JBotLib;
import bot.annotations.AdminOnly;
import bot.annotations.AutoReply;
import bot.annotations.BotCommand;
import bot.annotations.ScheduledTask;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.ChatService;
import service.EventLogger;
import service.KeyboardBuilder;
import service.MessageService;

public class MyBot extends JBotLib {
    @Override
    public String getBotUsername() {
        return "MyTestBot";
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN_HERE";
    }

    @BotCommand("/start")
    public void startCommand(Long chatId, MessageService sender, KeyboardBuilder builder) {
        sender.sendMessage(chatId, "Welcome to MyBot!");
    }

    @AutoReply("hello")
    public void replyToHello(Long chatId, MessageService sender) {
        sender.sendMessage(chatId, "Hi there!");
    }

    @AdminOnly
    @BotCommand("/ban")
    public void banUser(Long chatId, Long userId, MessageService sender, ChatService manager, EventLogger logger) {
        if (userId != null) {
            manager.restrictChatMember(userId, String.valueOf(chatId));
            sender.sendMessage(chatId, "User has been banned.");
            logger.logChatEvent(chatId, "user_banned", "User ID: " + userId);
        } else {
            sender.sendMessage(chatId, "Please specify a user to ban.");
        }
    }

    @ScheduledTask(intervalSeconds = 10)
    public void sendPeriodicMessage(Long chatId, MessageService sender, EventLogger logger) {
        if (chatId != null) {
            sender.sendMessage(chatId, "This is a scheduled reminder every 10 seconds!");
            logger.logChatEvent(chatId, "scheduled_message", "Reminder sent");
        }
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());
            System.out.println("Bot started successfully!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
```

### 2. Run Your Bot

1. Replace `YOUR_BOT_TOKEN_HERE` with your actual bot token.
2. Compile and run the `MyBot` class.
3. Open Telegram, find your bot (e.g., `@MyTestBot`), and start interacting!

### 3. Test Your Bot

- Send `/start` to see the welcome message.
- Type "hello" to get an auto-reply.
- Use `/ban` (as an admin) to ban a user.
- Wait 10 seconds to receive a scheduled message.

## 📖 Usage Examples

### Sending a Photo with a Keyboard

```java
@BotCommand("/photo")
public void sendPhoto(Long chatId, MessageService sender, KeyboardBuilder builder) {
    InputFile photo = new InputFile(new File("path/to/photo.jpg"));
    ReplyKeyboard keyboard = builder.keyboard(new String[][]{{"Like", "Share"}});
    sender.sendPhoto(chatId, "Check out this *photo*!", photo, keyboard);
}
```

### Creating a Pagination Keyboard

```java
@BotCommand("/items")
public void showItems(Long chatId, MessageService sender, KeyboardBuilder builder) {
    ArrayList<String> messages = new ArrayList<>(List.of("Page 1: Items 1-10", "Page 2: Items 11-20"));
    ArrayList<String> data = new ArrayList<>(List.of("item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10", "item11"));
    builder.sendPaginationKeyboard(chatId, messages, data, 1, null);
}
```

### Logging an Event

```java
@BotCommand("/log")
public void logEvent(Long chatId, EventLogger logger) {
    logger.logUserAction(chatId, "executed_log_command");
}
```

## 🧩 Key Components

- **MessageService**: Handles sending and editing messages (text, photos, videos, documents, polls, media groups).
- **KeyboardBuilder**: Creates reply keyboards, inline keyboards, URL buttons, and pagination keyboards.
- **ChatService**: Manages chat operations like restricting members, setting chat photos, and pinning messages.
- **AnnotationService**: Processes annotations (`@BotCommand`, `@AutoReply`, `@ScheduledTask`, `@AdminOnly`) to handle bot logic.
- **EventLogger**: Logs events, errors, and warnings with support for file logging and contextual data.

## 📜 Documentation

Detailed JavaDoc documentation is available in the source code for each class and method. Key classes include:

- [`MessageService`](./src/main/java/service/MessageService.java)
- [`KeyboardBuilder`](./src/main/java/service/KeyboardBuilder.java)
- [`ChatService`](./src/main/java/service/ChatService.java)
- [`AnnotationService`](./src/main/java/service/AnnotationService.java)
- [`EventLogger`](./src/main/java/service/EventLogger.java)

## 🤝 Contributing

We welcome contributions! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m "Add your feature"`).
4. Push to your branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

Please ensure your code follows the project's coding standards and includes appropriate tests.

## 🐞 Reporting Issues

If you encounter any bugs or have feature requests, please open an issue on the [GitHub Issues page](https://github.com/yourusername/jbotlib/issues).

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

## 📧 Contact

For questions or support, reach out to us at [your.email@example.com](mailto:your.email@example.com) or join our [Telegram Support Group](https://t.me/your_support_group).

---

**Built with ❤️ by jahong1r-t**
