package com.hinaplugin.fallBackServer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommands extends Command {
    private final FallBackServer plugin;
    public ReloadCommands(FallBackServer plugin) {
        super("hubreload", "fallbackserver.commands.hubreload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        FallBackServer.config.load();
        this.plugin.getAudiences().sender(commandSender).sendMessage(MiniMessage.miniMessage().deserialize("<green>config.ymlを再読み込みしました．"));
    }
}
