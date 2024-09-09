package br.dev.bahbtw.balanceamento.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CompareCommand implements CommandExecutor {

    private JavaPlugin plugin;

    public CompareCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String permission = plugin.getConfig().getString("permissions.comparar");

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Correct usage: /compare <weapon damage> <attack speed> <armor points> <toughness>");
            return false;
        }

        try {
            double weaponDamage = Double.parseDouble(args[0]);
            double attackSpeed = Double.parseDouble(args[1]);
            double armorPoints = Double.parseDouble(args[2]);
            double toughness = Double.parseDouble(args[3]);

            double totalDamage = calculateDamage(weaponDamage, armorPoints, toughness);
            double criticalDamage = calculateDamage(weaponDamage * 1.5, armorPoints, toughness);

            double damagePerSecond = totalDamage * attackSpeed;
            double damagePerMinute = damagePerSecond * 60;

            double criticalDamagePerSecond = criticalDamage * attackSpeed;
            double criticalDamagePerMinute = criticalDamagePerSecond * 60;

            sender.sendMessage(ChatColor.GREEN + "Total damage per hit: " + ChatColor.YELLOW + totalDamage);
            sender.sendMessage(ChatColor.GREEN + "Total critical damage per hit: " + ChatColor.RED + criticalDamage);
            sender.sendMessage(ChatColor.GREEN + "Average damage per second: " + ChatColor.YELLOW + damagePerSecond);
            sender.sendMessage(ChatColor.GREEN + "Average damage per minute: " + ChatColor.YELLOW + damagePerMinute);
            sender.sendMessage(ChatColor.GREEN + "Average critical damage per second: " + ChatColor.RED + criticalDamagePerSecond);
            sender.sendMessage(ChatColor.GREEN + "Average critical damage per minute: " + ChatColor.RED + criticalDamagePerMinute);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Weapon damage, attack speed, armor points, and toughness must be numbers.");
        }

        return true;
    }

    private double calculateDamage(double weaponDamage, double defensePoints, double toughness) {
        double damageReduction = Math.min(20, Math.max(defensePoints / 5, defensePoints - ((4 * weaponDamage) / (toughness + 8))));
        double damage = weaponDamage * (1 - damageReduction / 25);
        return Math.round(damage * 100.0) / 100.0;
    }
}
