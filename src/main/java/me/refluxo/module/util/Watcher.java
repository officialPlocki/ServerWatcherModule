package me.refluxo.module.util;

import eu.thesimplecloud.api.CloudAPI;
import org.bukkit.Bukkit;

public class Watcher implements ServerInfo {

    /**
     * This function returns the name of the server that is running this code
     *
     * @return The name of the server.
     */
    @Override
    public String getServerName() {
        return CloudAPI.getInstance().getThisSidesName();
    }

    /**
     * Returns the system lag in seconds
     *
     * @return The time in seconds since the system started.
     */
    @Override
    public double getSystemLag() {
        long l = System.currentTimeMillis();
        return (System.currentTimeMillis() - l / 1000);
    }

    /**
     * Returns the current TPS (ticks per second) of the server
     *
     * @return The TPS of the server.
     */
    @Override
    public double getTps() {
        return Bukkit.getServer().getTPS()[0];
    }

    /**
     * Returns the TPS before the last tick
     *
     * @return The TPS before the command was executed.
     */
    @Override
    public double getTpsBefore() {
        return Bukkit.getServer().getTPS()[1];
    }

    /**
     * It returns the number of players online
     *
     * @return The number of players online.
     */
    @Override
    public int getPlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    /**
     * Returns the amount of RAM used by the JVM in MB
     *
     * @return The total memory minus the free memory.
     */
    @Override
    public double getRamUsage() {
        return ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
    }

    /**
     * Returns the amount of free RAM in megabytes
     *
     * @return The amount of free memory in megabytes.
     */
    @Override
    public double getFreeRam() {
        return (Runtime.getRuntime().freeMemory()  / (1024 * 1024));
    }

}
