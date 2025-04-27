# MCRS - Runescape in Minecraft via Paper

A modular Minecraft RPG-style skills plugin using the Paper API and SQLite.

## Features

- Skill system with leveling and XP
- Asynchronous SQLite persistence
- Auto-save and manual `/saveall` command
- List of all skills with the `/stats` command

## Dev Notes

- Java 21+
- Paper 1.21.5

## Setup

- Download the latest JAR from the releases page.
- Drop in your server's `/plugins` folder.

## Permissions

#### Admin Permissions

- `mcrs.admin.saveall` - Manually save all players' data

#### Player Permissions

- `mcrs.player.stats` - View player stats