package me.refluxo.module.util;

import org.bukkit.Bukkit;

public class Watcher implements ServerInfo {

    @Override
    public String getServerName() {
        return "Refluxo";
    }

    @Override
    public double getSystemLag() {
        long l = System.currentTimeMillis();
        Bukkit.getServer().getConsoleSender().sendMessage("§a§lGot System Lag");
        return (System.currentTimeMillis() - l / 1000);
    }

    @Override
    public double getTps() {
        return Bukkit.getServer().getTPS()[0];
    }

    @Override
    public double getTpsBefore() {
        return Bukkit.getServer().getTPS()[1];
    }

    @Override
    public int getPlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public double getRamUsage() {
        return ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
    }

    @Override
    public double getFreeRam() {
        return (Runtime.getRuntime().freeMemory()  / (1024 * 1024));
    }

}