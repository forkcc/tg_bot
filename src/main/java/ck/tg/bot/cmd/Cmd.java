package ck.tg.bot.cmd;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public abstract class Cmd {
    /**
     * 检查是否应该执行
     * @param update 通知消息
     * @return 返回true执行
     */
    public abstract boolean verify(Update update);

    /**
     * 进入待执行阶段
     * @param update 通知消息
     * @param sender 消息发送客户端
     */
    @SneakyThrows
    public final void onUpdateReceived(Update update, DefaultAbsSender sender){
        try{
            onExecute(update, sender);
        }catch (Exception e){
            log.error("无法正确执行命令", e);
            throw e;
        }
    }
    protected abstract void onExecute(Update update, DefaultAbsSender sender);
}
