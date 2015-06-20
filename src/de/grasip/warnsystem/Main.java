package de.grasip.warnsystem;
 
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
 
 
public class Main extends JavaPlugin implements Listener{
	
	private String PREFIX = "§6l[§4§lWarnSystem§6§l] ";
        public MySQL mysql;
       
        public void onEnable(){
                Bukkit.getPluginManager().registerEvents(this, this);
                loadConfig();
                mysql=new MySQL(getConfig().getString("Config.MySQL.User"),getConfig().getString("Config.MySQL.Password"),getConfig().getString("Config.MySQL.Host"),getConfig().getString("Config.MySQL.DB"));
                mysql.connect();
                mysql.Update("CREATE TABLE IF NOT EXISTS list_warns(player varchar(30),warner varchar(30),time varchar(30),grund varchar(30))");
        }
       
        public void onDisable(){
                mysql.close();
        }
       
        public boolean onCommand(final CommandSender sender, Command cmd, String commandlabel,String[] args){
                Player p = (Player) sender;
                if(p.hasPermission("warnsystem.warns")){
                if(cmd.getName().equalsIgnoreCase("warns")){
                        if(args.length==0){
                                p.sendMessage(PREFIX + "§c§l/warns [Spieler]");
                                return true;
                        }
                        }
                       
                        if(args.length==1){
                                String player = args[0];
                                List<String> list = getWarns(player);
                                if(list.isEmpty()){
                                        p.sendMessage(PREFIX + "§c§lNo entries were found!");
                                        return true;
                                }
                               
                                p.sendMessage(PREFIX + "§c§lPlayer: §e§l"+player);
                                for(String g : list){
                                        p.sendMessage("§c§l"+g);
                                }
                        }
                }
               if(p.hasPermission("warnsystem.warn")){
                if(cmd.getName().equalsIgnoreCase("warn")){
                        if(args.length==0||args.length==1){
                                p.sendMessage(PREFIX + "§c§l/warn [Player] [Reason]");
                                return true;
                        }
                        }
                       
                        if(args.length>1){
                                String player = args[0];
                                String reason="";
                               
                                for(int i = 1; i < args.length; i++){
                                	reason=reason+args[i]+" ";
                                }
                               
                                Bukkit.broadcastMessage(PREFIX + "§c§lThe Player §b§l"+player+"§c§l was cautioned with reason: §c"+reason);
                                WarnUser(p,player,reason);
                        }
                       
                }
               
               
                return false;
        }
 
        public List<String> getWarns(String user){
                List<String> list = new ArrayList<>();
                try{
                       
                        ResultSet rs = mysql.Query("SELECT warner,time,grund FROM list_warns WHERE player='"+user.toLowerCase()+"'");
                       
                        while(rs.next()){
                                list.add("Reason: "+rs.getString(3)+ " Time:"+rs.getString(2)+" Warner:"+rs.getString(1));
                        }
                       
                }catch(Exception err){
                        System.err.println(err);
                }
                return list;
        }
       
        public void WarnUser(Player warner,String user, String grund){
                Date myDate = new Date();
                SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                mysql.Update("INSERT INTO list_warns(player,warner,time,grund) VALUES ('"+user.toLowerCase()+"','"+warner.getName()+"','"+df2.format(myDate)+"','"+grund+"')");
        }
       
        public void loadConfig(){
            getConfig().addDefault("Config.MySQL.Host", "Insert");
            getConfig().addDefault("Config.MySQL.User", "Insert");
            getConfig().addDefault("Config.MySQL.Password", "Insert");
            getConfig().addDefault("Config.MySQL.DB", "Insert");
            getConfig().options().copyDefaults(true);
            saveConfig();
          }
       
}