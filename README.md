## Overview
This is a plugin for bukkit/spigot minecraft server.

## General Purpose
The main purpose of this plugin is that a player can accquire temporary permission by his own (like Linux "sudo").
This plugin is written for a very specific scenario, so don't expect a general "sudo" solution here.

## Usage example
You have a trusted player who sometimes needs a special permission, but you don't want to walk him around all the time with this right.
So you set up a scenario where he can claim the permission on his own.
In a role-play world the player can go to a temple and gets a "blessing", e.g. he executes a command there or triggers a command block.

## Integration in LuckPerms autoop
For example the player sometimes wants to edit a command block, and for this he needs OP status.
Then the "blessing" could be a LuckPerms group containing the "luckperms.autoop" permission which gives the player OP.
(For this, "auto-op" must be set to TRUE in LuckPerms configuration).

## Documentation
Please see the Github Wiki.

## Support
For support please contact us as discord https://discord.gg/MBJjqUHQHR

## Homepage
https://www.spigotmc.org/resources/alveran-sudo.87291/
