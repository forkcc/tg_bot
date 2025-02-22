package ck.tg.bot.bot;

import ck.tg.bot.cmd.Cmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@RestController
public class TelegramWebhookController extends DefaultAbsSender implements InitializingBean, TelegramBot {
    private final BotProperties botProperties;
    private final List<Cmd> cmdList;
    protected TelegramWebhookController(BotProperties botProperties, List<Cmd> cmdList) {
        super(new DefaultBotOptions());
        this.botProperties = botProperties;
        this.cmdList = cmdList;
    }

    /**
     * 机器人消息回调
     * @param update 更新消息
     */
    @PostMapping
    public void onUpdateReceived(@RequestBody Update update){
        Optional<Cmd> optionalCmd = cmdList.parallelStream().filter(cmd -> cmd.verify(update)).findFirst();
        optionalCmd.ifPresent(cmd -> cmd.onUpdateReceived(update, TelegramWebhookController.this));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("注册机器人");
        SetWebhook setWebhook = new SetWebhook();
        setWebhook.setUrl(botProperties.getHookUrl());
        setWebhook.setMaxConnections(100);
        setWebhook.setDropPendingUpdates(false);
        execute(setWebhook);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
