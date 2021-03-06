package nl.shizleshizle.hediumcore.sql;

import nl.shizleshizle.hediumcore.objects.User;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager extends MySQL {
    private MySQL database;

    private static SQLManager instance = new SQLManager();

    public static SQLManager getInstance() {
        return instance;
    }

    public void setup() {
        try {
            this.database = new MySQL();
            if (getConnection() != null) {
                Statement account = database.getConnection().createStatement();
                Statement permissionGroup = database.getConnection().createStatement();
                Statement permission = database.getConnection().createStatement();
                Statement playerGroup = database.getConnection().createStatement();
                Statement homes = database.getConnection().createStatement();
                Statement warp = database.getConnection().createStatement();
                Statement spawn = database.getConnection().createStatement();
                account.execute("CREATE TABLE IF NOT EXISTS Account (uuid varchar(100), username varchar(150), nickname varchar(200), rawNick varchar(200), PRIMARY KEY (uuid));");
                permissionGroup.execute("CREATE TABLE IF NOT EXISTS PermissionGroup (groupId int, groupName varchar(150), PRIMARY KEY (groupId));");
                permission.execute("CREATE TABLE IF NOT EXISTS Permission (groupId int, permission varchar(300), PRIMARY KEY (groupId, permission), "
                        + "FOREIGN KEY (groupId) REFERENCES PermissionGroup(groupId) ON DELETE CASCADE ON UPDATE CASCADE);");
                playerGroup.execute("CREATE TABLE IF NOT EXISTS PlayerGroup (groupId int, uuid varchar(100), PRIMARY KEY (groupId, uuid), "
                        + "FOREIGN KEY (groupId) REFERENCES PermissionGroup(groupId) ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY (uuid) REFERENCES Account(uuid) ON DELETE CASCADE ON UPDATE CASCADE);");
                homes.execute("CREATE TABLE IF NOT EXISTS Home (posX double, posY double, posZ double, worldId varchar(150), uuid varchar(100), name varchar(100), PRIMARY KEY (uuid, name), "
                        + "FOREIGN KEY (uuid) REFERENCES Account(uuid) ON DELETE CASCADE ON UPDATE CASCADE);");
                warp.execute("CREATE TABLE IF NOT EXISTS Warp (posX double, posY double, posZ double, yaw double, pitch double, worldId varchar(150), name varchar(100), PRIMARY KEY (worldId, name));");
                spawn.execute("CREATE TABLE IF NOT EXISTS Spawn (posX double, posY double, posZ double, yaw double, pitch double, worldId varchar(150), name varchar(100), PRIMARY KEY(worldId, name));");
                account.close();
                permissionGroup.close();
                permission.close();
                playerGroup.close();
                homes.close();
                warp.close();
                spawn.close();
            }
        } catch (SQLException sql) {
            Bukkit.getServer().getLogger().info("Hedium Core: Database >> Error: " + sql);
        }
    }

    public void getReady() {
        if (!hasConnection()) {
            database.openConnection();
        }
    }

    public void enterUserInDatabase(User p) {
        try {
            getReady();
            Statement query = database.getConnection().createStatement();
            ResultSet rs = query.executeQuery("SELECT * FROM Account WHERE uuid='" + p.getUniqueId() + "';");
            if (!rs.next()) {
                Statement change = database.getConnection().createStatement();
                change.execute("INSERT INTO Account VALUES ('" + p.getUniqueId().toString() + "', '" + p.getName() + "', '" + p.getName() + "', '" + p.getName() + "');"); // feels redundant but it'll work
                change.close();
            }
            rs.close();
            query.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Hedium Core: SQL Enter User >> Error: " + e);
        }
    }
}
