/*
 * Copyright 2013 ShadowLordAlpha (Bukkit username/pen name)
 * 
 * This file is part of Block Messenger.
 *
 *  Block Messenger is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block Messenger is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Block Messenger.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gmail.alpha70.shadow;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BlockMsgCmds implements CommandExecutor {
	
	BlockMsg plugin;
	Player player;
	FileConfiguration config;
	boolean isBlockMsg = false;
	static HashMap<String, String> warpLocation = new HashMap<String, String>();
	static HashMap<String, Location> pos1 = new HashMap<String, Location>();
	static HashMap<String, Location> pos2 = new HashMap<String, Location>();
	
	public BlockMsgCmds(BlockMsg main) {
		plugin = main;
		config = main.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLable, String[] arg) {
		
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			plugin.getLogger().info("You must be a player to use this command!");
			return true;
		}
				
		Location position = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
	
		if (cmd.getName().equalsIgnoreCase("bm")) {
			if (arg.length == 0) {
				if (sender.hasPermission("blockmsg.help")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "---------Block Message Help---------");
					sender.sendMessage(ChatColor.GOLD + "bm: Shows the Block Message help");
					sender.sendMessage(ChatColor.GOLD + "bm pos1: Set point one for a Block Area");
					sender.sendMessage(ChatColor.GOLD + "bm pos2: Set point two for a Block Area");
					sender.sendMessage(ChatColor.GOLD + "bm clear: Clear the set points");
					sender.sendMessage(ChatColor.GOLD + "bm set: Set a Block Message at Current Location/Within set positions");
					sender.sendMessage(ChatColor.GOLD + "bm msg %sign%: Set a Block Message at the current Locaion from a sign");
					sender.sendMessage(ChatColor.GOLD + "bm del:Delete the Block Message at this Location");
					sender.sendMessage(ChatColor.GOLD + "bm msg [message]: Set a new Block Message over an old one");
					sender.sendMessage(ChatColor.GOLD + "bm start: Allow Commands and Warps to affect you");
					sender.sendMessage(ChatColor.GOLD + "bm stop: Warps and Commands do not affect you");
					sender.sendMessage(ChatColor.GOLD + "bm cmd add [command(s)]: Set a new Commands at this Locaion");
					sender.sendMessage(ChatColor.GOLD + "bm cmd del: Delete all the commands on this Block Message");
					sender.sendMessage(ChatColor.GOLD + "bm warp set: Set a warp Start point");
					sender.sendMessage(ChatColor.GOLD + "bm warp to: Set a warp End point");
					sender.sendMessage(ChatColor.GOLD + "bm reload: Reload Block Message (untested)");
					sender.sendMessage(ChatColor.DARK_RED + "bm delall: Delete all Block Messages");
					return true;
					
				} else {
					if (config.getBoolean("Permissions.sendMsg")) {
						String msg = config.getString("Permissions.help");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
					
			if (arg[0].equalsIgnoreCase("pos1")) {
				if (sender.hasPermission("blockmsg.pos")) {
					pos1.put(sender.getName(), position);
					sender.sendMessage(ChatColor.DARK_GREEN + "Position 1 set");
					return true;
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.pos");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("pos2")) {
				if (sender.hasPermission("blockmsg.pos")) {
					pos2.put(sender.getName(), position);
					sender.sendMessage(ChatColor.DARK_GREEN + "Position 2 set");
					return true;
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.pos");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("clear")) {
				if (sender.hasPermission("blockmsg.pos")) {
					pos1.remove(sender.getName());
					pos2.remove(sender.getName());
					sender.sendMessage(ChatColor.DARK_RED + "Positions cleared");
					return true;
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.pos");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("set")) {
				if (sender.hasPermission("blockmsg.set")) {
					if (arg.length > 1) {
						String name = "";
						for (int i = 1; i < arg.length; i++) {
							name = name + arg[i] + " ";
						}
						name = name.replace(".", "").replace(":", "").replace(" ", "");
						if (pos1.containsKey(sender.getName()) && pos2.containsKey(sender.getName())) {
							sender.sendMessage(ChatColor.DARK_RED + "You have set your own name this may cover another Block Message");
							String[] s = CustomMethods.setCubeWithin(pos1.get(sender.getName()), pos2.get(sender.getName()));
							config.set("Location." + name + ".pos1", s[0]);
							config.set("Location." + name + ".pos2", s[1]);
							sender.sendMessage(ChatColor.DARK_GREEN + "Block Message set to Area");
							plugin.saveConfig();
							pos1.remove(player.getName());
							pos2.remove(player.getName());
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Positions not set");
							sender.sendMessage(ChatColor.DARK_RED + "You have set your own name this may cover another Block Message");
							String[] s = CustomMethods.setCubeWithin(player.getLocation(), player.getLocation());
							config.set("Location." + name + ".pos1", s[0]);
							config.set("Location." + name + ".pos2", s[1]);
							sender.sendMessage(ChatColor.DARK_GREEN + "Block Message set to Block");
							plugin.saveConfig();
							return true;
						}
					} else {
						if (pos1.containsKey(sender.getName()) && pos2.containsKey(sender.getName())) {
							String[] s = CustomMethods.setCubeWithin(pos1.get(sender.getName()), pos2.get(sender.getName()));
							config.set("Location." + s[0] + ".pos1", s[0]);
							config.set("Location." + s[0] + ".pos2", s[1]);
							sender.sendMessage(ChatColor.DARK_GREEN + "Block Message set to Area");
							plugin.saveConfig();
							pos1.remove(player.getName());
							pos2.remove(player.getName());
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Positions not set");
							String[] s = CustomMethods.setCubeWithin(player.getLocation(), player.getLocation());
							config.set("Location." + s[0] + ".pos1", s[0]);
							config.set("Location." + s[0] + ".pos2", s[1]);
							sender.sendMessage(ChatColor.DARK_GREEN + "Block Message set to Block");
							plugin.saveConfig();
							return true;
						}
					}
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.set");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("msg")) {
				if (sender.hasPermission("blockmsg.msg")) {
					if (arg.length > 1) {
						for (String s : config.getConfigurationSection("Location.").getKeys(false)) {
							if (CustomMethods.isWithin(player.getLocation(), "Location." + s)) {
								if (arg[1].equalsIgnoreCase("%sign%")) {
									BlockMsgListener.signLocation.put(sender.getName(), s);
									String msg = config.getString("Message.Sign.Ready");
									msg = msg.replace("%player%", player.getDisplayName());
									msg = ChatColor.translateAlternateColorCodes('&', msg);
									sender.sendMessage(msg);
									return true;
								} else {
									String message = "";
									for (int i = 1; i < arg.length; i++) {
										message = message + arg[i] + " ";
									}
									config.set("Location." + s + ".message", message);
									config.set("Location." + s + ".messageCreator", message);
									plugin.saveConfig();
									String msg = config.getString("Message.Msg.Success");
									msg = msg.replace("%player%", player.getDisplayName());
									msg = ChatColor.translateAlternateColorCodes('&', msg);
									sender.sendMessage(msg);
									return true;
								}
							} else {
								String testname = s;
									if (arg[1].equals(testname)) {
										if (arg[2].equalsIgnoreCase("%sign%")) {
											BlockMsgListener.signLocation.put(sender.getName(), s);
											String msg = config.getString("Message.Sign.Ready");
											msg = msg.replace("%player%", player.getDisplayName());
											msg = ChatColor.translateAlternateColorCodes('&', msg);
											sender.sendMessage(msg);
											return true;
										} else {
											String message = "";
											for (int i = 2; i < arg.length; i++) {
												message = message + arg[i] + " ";
											}
											config.set("Location." + s + ".message", message);
											config.set("Location." + s + ".messageCreator", message);
											plugin.saveConfig();
											String msg = config.getString("Message.Msg.Success");
											msg = msg.replace("%player%", player.getDisplayName());
											msg = ChatColor.translateAlternateColorCodes('&', msg);
											sender.sendMessage(msg);
											return true;
										}
									}
								}
						}
						String msg = config.getString("Message.Msg.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						String msg = config.getString("Message.Msg.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					}
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.msg");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			if (arg[0].equalsIgnoreCase("cmd")) {
				if (sender.hasPermission("blockmsg.cmd")) {
					if (arg.length > 1) {
						for (String s : config.getConfigurationSection("Location.").getKeys(false)) {
							if (CustomMethods.isWithin(player.getLocation(), "Location." + s)) {
								if (arg[1].equalsIgnoreCase("add")) {
									String message = config.getString("Location." + s + ".command", "");
									for (int i = 2; i < arg.length; i++) {
										message = message + arg[i] + " ";
									}
									message = message + "<!!!>";
									config.set("Location." + s + ".command", message);
									config.set("Location." + s + ".commandCreator", sender.getName());
									plugin.saveConfig();
									String msg = config.getString("Message.Cmd.AddSuccess");
									msg = msg.replace("%player%", player.getDisplayName());
									msg = ChatColor.translateAlternateColorCodes('&', msg);
									sender.sendMessage(msg);
									return true;
								}
								
								if (arg[1].equalsIgnoreCase("del")) {
									config.set("Location." + s + ".command", null);
									plugin.saveConfig();
									String msg = config.getString("Message.Cmd.DelSuccess");
									msg = msg.replace("%player%", player.getDisplayName());
									msg = ChatColor.translateAlternateColorCodes('&', msg);
									sender.sendMessage(msg);
									return true;
								}
								
								return true;
							} else {
									if (arg[1].equals(s)) {
										if (arg[2].equalsIgnoreCase("add")) {
											String message = config.getString("Location." + s + ".command", "");
											for (int i = 3; i < arg.length; i++) {
												message = message + arg[i] + " ";
											}
											message = message + "<!!!>";
											config.set("Location." + s + ".command", message);
											config.set("Location." + s + ".commandCreator", sender.getName());
											plugin.saveConfig();
											String msg = config.getString("Message.Cmd.AddSuccess");
											msg = msg.replace("%player%", player.getDisplayName());
											msg = ChatColor.translateAlternateColorCodes('&', msg);
											sender.sendMessage(msg);
											return true;
										}
										
										if (arg[2].equalsIgnoreCase("del")) {
											config.set("Location." + s + ".command", null);
											plugin.saveConfig();
											String msg = config.getString("Message.Cmd.DelSuccess");
											msg = msg.replace("%player%", player.getDisplayName());
											msg = ChatColor.translateAlternateColorCodes('&', msg);
											sender.sendMessage(msg);
											return true;
										}
									}
							}
						}
						String msg = config.getString("Message.Cmd.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						String msg = config.getString("Message.Cmd.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					}
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.cmd");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("warp")) {
				if (sender.hasPermission("blockmsg.warp")) {
					if (arg.length ==  2 || arg.length ==  3) {
							for (String s : config.getConfigurationSection("Location.").getKeys(false)) {
								if (CustomMethods.isWithin(player.getLocation(), "Location." + s)) {
									if (arg[1].equalsIgnoreCase("set")) {
										warpLocation.put(sender.getName(), "Location." + s);
										String msg = config.getString("Message.Warp.SuccessSet");
										msg = msg.replace("%player%", player.getDisplayName());
										msg = ChatColor.translateAlternateColorCodes('&', msg);
										sender.sendMessage(msg);
										return true;
									}
								} else {
									if (arg[1].equals(s)) {
										if (arg[2].equalsIgnoreCase("set")) {
											warpLocation.put(sender.getName(), "Location." + s);
											String msg = config.getString("Message.Warp.SuccessSet");
											msg = msg.replace("%player%", player.getDisplayName());
											msg = ChatColor.translateAlternateColorCodes('&', msg);
											sender.sendMessage(msg);
											return true;
										}
									}
								}
							}
							if (arg[1].equalsIgnoreCase("to")) {
								if (warpLocation.containsKey(sender.getName())) {
									String s1 = CustomMethods.warplocation(player.getLocation());
									config.set(warpLocation.get(player.getName()) + ".warp", s1);
									config.set(warpLocation.get(player.getName()) + ".warpCreator", sender.getName());
									plugin.saveConfig();
									warpLocation.remove(player.getName());
									String msg = config.getString("Message.Warp.SuccessTo");
									msg = msg.replace("%player%", player.getDisplayName());
									msg = ChatColor.translateAlternateColorCodes('&', msg);
									sender.sendMessage(msg);
									return true;
								}
							} 
						}
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.warp");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("del")) {
				if (sender.hasPermission("blockmsg.delete")) {
					if (arg.length > 1) {
						String name = arg[1];
						name = name.replace(".", "").replace(":", "").replace(" ", "");
						config.set("Location." + name, null);
						plugin.saveConfig();
						String msg = config.getString("Message.Delete.Success");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						for (String s : config.getConfigurationSection("Location").getKeys(false)) {
							if (CustomMethods.isWithin(player.getLocation(), "Location." + s)) {
								config.set("Location." + s, null);
								plugin.saveConfig();
								String msg = config.getString("Message.Delete.Success");
								msg = msg.replace("%player%", player.getDisplayName());
								msg = ChatColor.translateAlternateColorCodes('&', msg);
								sender.sendMessage(msg);
								return true;
							}
						}
						String msg = config.getString("Message.Delete.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;	
					}
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.delete");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("delall")) {
				if (sender.hasPermission("blockmsg.delall")) {
					for (String s : config.getConfigurationSection("Location.").getKeys(false)) {
						config.set("Location." + s, null);
					}
					plugin.saveConfig();
					String msg = config.getString("Message.Delall.Success");
					msg = msg.replace("%player%", player.getDisplayName());
					msg = ChatColor.translateAlternateColorCodes('&', msg);
					sender.sendMessage(msg);
					return true;
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.delall");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("blockmsg.reload")) {
					plugin.reloadConfig();
					String msg = config.getString("Message.Reload.Success");
					msg = msg.replace("%player%", player.getDisplayName());
					msg = ChatColor.translateAlternateColorCodes('&', msg);
					sender.sendMessage(msg);
					return true;
				} else {
					if (config.getBoolean("Permission.sendMsg")) {
						String msg = config.getString("Permissions.reload");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("start")) {
				if (sender.hasPermission("blockmsg.start")) {
					if (arg.length == 1) {
						BlockMsgListener.stop.remove(sender.getName());
						String msg = config.getString("Message.Start.Success");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					}
					String msg = config.getString("Message.Start.Fail");
					msg = msg.replace("%player%", player.getDisplayName());
					msg = ChatColor.translateAlternateColorCodes('&', msg);
					sender.sendMessage(msg);
					return true;
				} else {
					if (config.getBoolean("Permissions.sendMsg")) {
						String msg = config.getString("Permissions.start");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("stop")) {
				if (sender.hasPermission("blockmsg.stop")) {
					if (arg.length == 1) {
						BlockMsgListener.stop.add(sender.getName());
						String msg = config.getString("Message.Stop.Success");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					}
					String msg = config.getString("Message.Stop.Fail");
					msg = msg.replace("%player%", player.getDisplayName());
					msg = ChatColor.translateAlternateColorCodes('&', msg);
					sender.sendMessage(msg);
					return true;
				} else {
					if (config.getBoolean("Permissions.sendMsg")) {
						String msg = config.getString("Permissions.stop");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					} else {
						return true;
					}
				}
			}
			
			if (arg[0].equalsIgnoreCase("list")) {
				if (sender.hasPermission("blockmsg.list")) {
					if (arg.length == 1) {
						sender.sendMessage(ChatColor.GOLD + "--------Block Messages--------");
						for (String s1 : config.getConfigurationSection("Location.").getKeys(false)) {
							sender.sendMessage(s1);
						}
						return true;
					} else {
						String msg = config.getString("Message.List.Fail");
						msg = msg.replace("%player%", player.getDisplayName());
						msg = ChatColor.translateAlternateColorCodes('&', msg);
						sender.sendMessage(msg);
						return true;
					}
				} else {
					String msg = config.getString("Permissions.list");
					msg = msg.replace("%player%", player.getDisplayName());
					msg = ChatColor.translateAlternateColorCodes('&', msg);
					sender.sendMessage(msg);
					return true;
				}
			}
			
			if (arg[0].equalsIgnoreCase("creator")) {
				if (sender.hasPermission("blockmsg.creator")) {
					for (String s : config.getConfigurationSection("Location.").getKeys(false)) {
						if (CustomMethods.isWithin(player.getLocation(), "Location." + s)) {
							String msg = config.getString("Location." + s + ".messageCreator");
							msg = msg.replace("%player%", player.getDisplayName());
							msg = ChatColor.translateAlternateColorCodes('&', msg);
							sender.sendMessage(msg);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
