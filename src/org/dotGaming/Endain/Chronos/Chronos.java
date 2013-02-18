package org.dotGaming.Endain.Chronos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Chronos extends JavaPlugin {
	private static Chronos instance;
	private double day;
	private double night;
	private double dawn;
	private double dusk;
	private double time;
	private BukkitTask timeTask;
	
	// Called when the plugin is enabled
	@Override
	public void onEnable() {
		// Set the instance of this plugin
		instance = this;
		// Initialize time variables
		this.day = 1.0;
		this.night = 1.0;
		this.dusk = 1.0;
		this.dawn = 1.0;
		this.time = this.getServer().getWorlds().get(0).getTime();
		// Schedule the timetask
		timeTask = this.getServer().getScheduler().runTaskTimer(this, new TimeControl(), 0, 1);
		// Register commands
		this.getCommand("chronos").setExecutor(this);
	}
	
	// Called when the plugin is disabled
	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTask(timeTask.getTaskId());
	}
	
	// Set the speed of the day time period
	public static void setDay(double mult) {
		instance.day = mult;
	}
	
	// Set the speed of the night time period
	public static void setNight(double mult) {
		instance.night = mult;
	}
	
	// set the speed of the dawn time period
	public static void setDawn(double mult) {
		instance.dawn = mult;
	}
	
	// Set the speed of the dusk time period
	public static void setDusk(double mult) {
		instance.dusk = mult;
	}
	
	// Set the speed of all time periods
	public static void setTime(double mult) {
		instance.day = mult;
		instance.night = mult;
		instance.dawn = mult;
		instance.dusk = mult;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.isOp()) {
			if(command.getName().equalsIgnoreCase("chronos")) {
				if(args[0].equalsIgnoreCase("day")) {
					setDay(Double.parseDouble(args[1]));
				} else if(args[0].equalsIgnoreCase("night")) {
					setNight(Double.parseDouble(args[1]));
				} else if(args[0].equalsIgnoreCase("dusk")) {
					setDusk(Double.parseDouble(args[1]));
				} else if(args[0].equalsIgnoreCase("dawn")) {
					setDawn(Double.parseDouble(args[1]));
				} else {
					setTime(Double.parseDouble(args[0]));
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
		}
		return true;
	}
	
	private class TimeControl implements Runnable {
		@Override
		public void run() {
			double tempTime = time % 24000;
			if(tempTime > 0 && tempTime <= 12000) {
				// In Daytime
				time += day;
			} else if(tempTime > 12000 && tempTime <= 13800) {
				// In Dusk
				time += dusk;
			} else if(tempTime > 13800 && tempTime <= 22200) {
				// In Nighttime
				time += night;
			} else {
				// In Sunrise
				time += dawn;
			}
			// Update the server time;
			instance.getServer().getWorlds().get(0).setTime((long)time);
		}
		
	}
}
