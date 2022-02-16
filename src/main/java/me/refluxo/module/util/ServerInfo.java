package me.refluxo.module.util;

import org.jetbrains.annotations.NotNull;

public interface ServerInfo {

    String getServerName();

    double getSystemLag();

    double getTps();

    @NotNull double getTpsBefore();

    int getPlayers();

    double getRamUsage();

    double getFreeRam();

}
