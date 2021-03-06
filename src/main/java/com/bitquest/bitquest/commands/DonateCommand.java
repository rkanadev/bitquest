package com.bitquest.bitquest.commands;

import com.bitquest.bitquest.BitQuest;
import com.bitquest.bitquest.User;
import com.bitquest.bitquest.Wallet;

import java.io.IOException;
import java.text.ParseException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonateCommand extends CommandAction {
    private BitQuest bitQuest;

    public DonateCommand(BitQuest plugin) {
        bitQuest = plugin;
    }

    public boolean run(CommandSender sender, Command cmd, String label, String[] args, final Player player) {
        if (args.length == 1) {
            try {
                final Long bits = Long.parseLong(args[0]);
                final Long sat = bits * bitQuest.DENOMINATION_FACTOR;
                final User user = new User(bitQuest.db_con, player.getUniqueId());
                final Long balance = user.wallet.getBalance(0);

                if (balance > sat) {
                    if (user.wallet.payment(System.getenv("DONATION_ADDRESS"), sat)) {
                        player.sendMessage(ChatColor.GREEN + "Thanks for your support!");
                        bitQuest.updateScoreboard(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Donation failed");
                    }
                } else {
                    player.sendMessage(
                            ChatColor.DARK_RED
                                    + "Not enough balance to donate "
                                    + ChatColor.LIGHT_PURPLE
                                    + ""
                                    + bits
                                    + " "
                                    + BitQuest.DENOMINATION_NAME);
                }

                return true;
            } catch (Exception e) {
                System.out.println(e);
                player.sendMessage(ChatColor.RED + "Command failed.");
                return true;
            }
        } else {
            return false;
        }
    }
}