package ck.tg.bot.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "bot")
public class BotProperties {
    private String username;
    private String token;
    private String hookUrl;
    private String welcomeText;
}