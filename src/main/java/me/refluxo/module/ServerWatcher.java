package me.refluxo.module;

import lombok.SneakyThrows;
import me.refluxo.module.util.DiscordHook;
import me.refluxo.module.util.ServerInfo;
import me.refluxo.module.util.Watcher;
import me.refluxo.moduleloader.ModuleLoader;
import me.refluxo.moduleloader.module.Module;
import me.refluxo.moduleloader.module.ModuleManager;
import me.refluxo.moduleloader.module.PluginModule;
import me.refluxo.moduleloader.util.mysql.MySQLService;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Module(moduleName = "ServerWatcher")
public class ServerWatcher extends PluginModule {

    @Override
    @SneakyThrows
    public void enableModule() {
        String hook = new String(Base64.getDecoder().decode("aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvOTQzNjE2MTg5MTM2MDExMjc4L3E5YmJQVmM2QTRfU2ZpV3B5UUdzR1JkRTgxZjQyMDZmeUJkM2dRV1Q0MU16T3ZNSzNHa0FERE9EdjdVejlvYnV3Wlps".getBytes(StandardCharsets.UTF_8)));
        new Thread(() -> {
            for(;;) {
                ServerInfo info = new Watcher();
                if(info.getFreeRam() < 50) {
                    sendDiscordMessage(hook, "Too low free memory", info);
                }
                if(info.getSystemLag() > 10) {
                    sendDiscordMessage(hook, "Too great internal system lag", info);
                }
                if(info.getTps() < 15) {
                    sendDiscordMessage(hook, "Too great server lag", info);
                }
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SneakyThrows
    private static void sendDiscordMessage(String discordWebHook, String reason, ServerInfo info) {
        DiscordHook webhook = new DiscordHook(discordWebHook);
        webhook.setContent("Server report (" + info.getServerName() + "):");
        webhook.setUsername("Refluxo");
        webhook.setTts(true);
        webhook.addEmbed(new DiscordHook.EmbedObject()
                .setTitle("Reason")
                .setDescription(reason)
                .setColor(Color.RED)
                .addField("Server Name", info.getServerName(), true)
                .addField("Free ram", info.getFreeRam() + "mb", true)
                .addField("Used ram", info.getRamUsage() + "mb", true)
                .addField("Online players", String.valueOf(info.getPlayers()), true)
                .addField("System lag", info.getSystemLag() + "s", true)
                .addField("Actual TPS", String.valueOf(info.getTps()), true)
                .addField("TPS before", String.valueOf(info.getTpsBefore()), true));
        webhook.execute();
    }

    @Override
    public void disableModule() {

    }

    @Override
    public ModuleLoader getModuleLoader() {
        return super.getModuleLoader();
    }

    @Override
    public MySQLService getMySQLService() {
        return super.getMySQLService();
    }

    @Override
    public ModuleManager getModuleManager() {
        return super.getModuleManager();
    }

    @Override
    public Plugin getPlugin() {
        return super.getPlugin();
    }

}
