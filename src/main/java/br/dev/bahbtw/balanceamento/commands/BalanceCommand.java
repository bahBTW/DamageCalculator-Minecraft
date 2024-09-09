package br.dev.bahbtw.balanceamento.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BalanceCommand implements CommandExecutor {

    private JavaPlugin plugin;

    public BalanceCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String permission = plugin.getConfig().getString("permissions.balancear");

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Correct usage: /balance <base damage> <attack speed>");
            return false;
        }

        try {
            double baseDamage = Double.parseDouble(args[0]);
            double attackSpeed = Double.parseDouble(args[1]);

            double balancedDamage = calculateBalancedDamage(baseDamage, attackSpeed);

            double damagePerSecond = balancedDamage * attackSpeed;
            double damagePerMinute = damagePerSecond * 60;

            double criticalDamage = balancedDamage * 1.5;

            double criticalDamagePerSecond = criticalDamage * attackSpeed;
            double criticalDamagePerMinute = criticalDamagePerSecond * 60;

            sender.sendMessage(ChatColor.GREEN + "Total damage: " + ChatColor.YELLOW + balancedDamage);
            sender.sendMessage(ChatColor.GREEN + "Average damage per second: " + ChatColor.YELLOW + damagePerSecond);
            sender.sendMessage(ChatColor.GREEN + "Average damage per minute: " + ChatColor.YELLOW + damagePerMinute);
            sender.sendMessage(ChatColor.GREEN + "Critical damage: " + ChatColor.RED + criticalDamage);
            sender.sendMessage(ChatColor.GREEN + "Average critical damage per second: " + ChatColor.RED + criticalDamagePerSecond);
            sender.sendMessage(ChatColor.GREEN + "Average critical damage per minute: " + ChatColor.RED + criticalDamagePerMinute);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Base damage and attack speed must be numbers.");
        }

        return true;
    }

    private double calculateBalancedDamage(double baseDamage, double attackSpeed) {
        double K = 10;
        return (baseDamage * attackSpeed) / K;
    }
}
