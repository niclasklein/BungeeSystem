package de.flashbeatzz.bungeesystem;

import de.flashbeatzz.bungeesystem.banmanager.BanManager;
import de.flashbeatzz.bungeesystem.banmanager.PostLoginListener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeSystem extends Plugin {

    private static BungeeSystem instance;

    public static BungeeSystem getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        Data.mySQL.closeConnection();
    }

    @Override
    public void onEnable() {
        instance = this;
        Data.mysqlCfg = new Config("mysql", getDataFolder().getName());
        Data.mysqlCfg.addDefault("MySQL.Host", "localhost");
        Data.mysqlCfg.addDefault("MySQL.User", "root");
        Data.mysqlCfg.addDefault("MySQL.Password", "pass");
        Data.mysqlCfg.addDefault("MySQL.Database", "db");
        Data.mysqlCfg.addDefault("MySQL.Port", 3306);
        Data.mysqlCfg.copyAndSave(true);

        Data.mySQL = new MySQL();
        Data.mySQL.openConnection();

        System.out.println("Starting CommunicationSystem-Server ...");
        getProxy().getScheduler().runAsync(this, new SocketReadThread());
        System.out.println("Successfully started.");

        new BanManager();
        new LevelSystem();
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new UUIDLibrary());
    }

}
