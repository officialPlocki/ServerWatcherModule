package me.refluxo.module.util;

import org.jetbrains.annotations.NotNull;

public interface ServerInfo {

    /**
     * Returns the name of the server
     *
     * @return The server name.
     */
    String getServerName();

    /**
     * Returns the system lag
     *
     * @return The time it takes for the system to respond to a request.
     */
    double getSystemLag();

    /**
     * Returns the number of transactions per second
     *
     * @return The number of transactions per second.
     */
    double getTps();

    /**
     * Returns the number of transactions per second before the transaction began
     *
     * @return The number of transactions per second before the test was started.
     */
    @NotNull double getTpsBefore();

    /**
     * Get the number of players in the game
     *
     * @return The number of players in the game.
     */
    int getPlayers();

    /**
     * Returns the amount of RAM used by the current process
     *
     * @return The amount of RAM being used by the program.
     */
    double getRamUsage();

    /**
     * Returns the amount of free RAM in bytes
     *
     * @return The amount of free RAM in the system.
     */
    double getFreeRam();

}
