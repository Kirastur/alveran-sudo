## Overview
The main purpose of this plugin is that a player
can accquire temporary permission by his own (like Linux "sudo").
This plugin is written for a very specific scenario,
so don't expect a general "sudo" solution here.

## Example
You have a trusted player who sometimes needs a special permission,
but you don't want to walk him around all the time with this right.
So you set up a scenario where he can claim the permission on his own.
In a role-play world the player can go to a temple and gets a "blessing",
e.g. he executes a command there or triggers a command block.

For example the player sometimes wants to edit a command block and
therefor needs OP status. Then the "blessing" could be
a LuckPerms group containing the "luckperms.autoop" permission which gives
the player OP if "auto-op" is set to TRUE in LuckPerms configuration.

## Prerequisits
This plugin depends on LuckPerms and WorldGuard.

## Protection
We have a two-factor-authentication here
1. Conscious decision (Player must stay in a specific region)
2. Authorization (Player must have a "can get" permission)
and an entitled source must execute the command

## Action
If both requirements are fulfilled and the command is executed,
the player is temporary added to a "blessing" group.
You can configure how long the group membership should remain and
if the group membership should get lost when the player logs out.

## Notification
Every time the player gets the membership, a whisper is sent
to the player, regardless if the membership was received by
the "action" above or manually by a LuckPerms command.
You can configure if also a whisper should be sent
when the player looses the group membership, e.g. after the time runs out.

## Usage
1. Create a region in WorldGuard and name it "alveran".
2. Create a LuckPerms Group and name it "alveran".
3. Give your player the permission "alveran.acolyte".
4. With you player move inside the region.
5. Execute "/alveran" from the console or form a command block.

Now you can see the result:
I. A message printed in the cosole that the player was blessed.
II. The player gets a message about his blessing.
III. In LuckPerms the player has an additional temp group

The flags of the region do not matter (e.g. passthrough).
If more than one player with the appropriate permission stay
inside this region, all of them are blessed.

## Commands
Simply execute "/alveran" from a command block or from console.
If enabled in config, "/alveran" can be executed from players as well.
You can optional add a world name.
If no word name is given, the world is taken from the player or
the command block object. If the command is entered in the console,
the system tries the string "world" if no world name is given.

## Permissions
alveran.use
  Should user be able to use the alveran command?
  default: op
alveran.acolyte
  Should user be able to retrieve the alveran blessing?
  default: false

## Configuration
All configuration is done in the default config file
  plugins/Alveran/config.yml
Every config option has a detailed description there.
There is no way to change the configuration at runtime.

## Localization
The whisper sent to the player depends on the player language.
The language is taken from the player's locale.
Per default, only the "default" langage is active
which means all messages are sent in english.
Please see the example in the config file how to
modify the messages and add additional langauges.

## API
This plugin has a simple API.
The API can be used to call Alveran from other plugins.
  boolean performBlessing(World)
    Executes the blessing, the same as calling the "/alveran" command.
	return: true=OK, false=ERROR
  boolean unblessPlayer(Player)
    Removes the blessing from the given player.
	return: true=OK, false=ERROR
  boolean isPlayerBlessed(Player)
	return: true = Player has blessing (is groupmember), false = has not
Get the API object with 
  AlveranAPI alveranAPI = AlveranProvider.getAPI();

## Updates
There is no auto-update for this plugin.

## Privacy
The plugin uses bstats to collect statistical data.
This contains:
  * Number of players online
  * Minecraft server typ, name and version
  * Java version
  * Operating system type and version
  * Number of CPU Cores
This can be disabled in plugins/bstats/config.yml
please see https://bStats.org/ for more information

## Source Code
Source code is avail via Github.
  https://github.com/Kirastur/alveran-sudo

## Support
For support pleae contact us at discord
  https://discord.gg/MBJjqUHQHR

## Homepage
  https://www.spigotmc.org/resources/alveran-sudo.87291/
  
