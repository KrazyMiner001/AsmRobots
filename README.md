# ASM Robots
## Table of Contents
* [About](#about)
* [Why I made this](#why-i-made-this)
* [Documentation](#documentation)
* [Download](#download)
* [Building](#building)

## About
A Minecraft mod around coding robots in assembly.\
This mod is similar to mods such as [Computer Craft](https://modrinth.com/mod/cc-tweaked) and [OpenComputers](https://modrinth.com/mod/opencomputers), except with 2 main differences:
1. You program an entity instead of a block
2. You program in assembly instead of in lua

## Why I made this
- I like coding in assembly and wanted to make something with assembly myself (and because doing this with assembly is easier than writing a compiler)
- I was inspired by [Mnemonimov](https://store.steampowered.com/app/3854110/Mnemonimov/) releasing (great game btw, if you also like assembly you should try it)
- I wanted to make something like Computer Craft for a while
  - I chose to use entities to distinguish it from Computer Craft and because I haven't seen much in the way of programmable entities in Minecraft

## Documentation
Documentation can be accessed from the code editor in the robot's UI.\
I will probably make it available out of the game at some point.

## Download
The mod can be downloaded from Modrinth (not yet, but it will be there)

## Installation
Install the mod by putting the file into `minecraft/mods/` alongside a version of [LDLib2](https://modrinth.com/mod/ldlib), [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) and [GuideME](https://modrinth.com/mod/guideme) on a neoforge install for 26.1.2.\
If you do not know how to install NeoForge, see [their user guide](https://docs.neoforged.net/user/docs/).

## Building
To build the mod, clone this repo and run `gradle neoforge:build`