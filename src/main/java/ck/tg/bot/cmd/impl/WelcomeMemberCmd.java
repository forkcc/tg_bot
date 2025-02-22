package ck.tg.bot.cmd.impl;

import ck.tg.bot.bot.BotProperties;
import ck.tg.bot.cmd.Cmd;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@RequiredArgsConstructor
public class WelcomeMemberCmd extends Cmd {
    private final BotProperties botProperties;
    @Override
    public boolean verify(Update update) {
        return update.hasMessage() && update.getMessage().isGroupMessage() && !update.getMessage().getNewChatMembers().isEmpty();
    }

    @Override
    @SneakyThrows
    protected void onExecute(Update update, DefaultAbsSender sender) {
        for (User newChatMember : update.getMessage().getNewChatMembers()) {
            String message = String.format(botProperties.getWelcomeText(), newChatMember.getUserName());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
            sendMessage.setText(message);
            sender.execute(sendMessage);
        }
    }
}
