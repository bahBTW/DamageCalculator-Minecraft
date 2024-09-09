package br.dev.bahbtw.balanceamento;

import br.dev.bahbtw.balanceamento.commands.CompareCommand;
import br.dev.bahbtw.balanceamento.commands.BalanceCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Balanceamento extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("compare").setExecutor(new CompareCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
    }
}
