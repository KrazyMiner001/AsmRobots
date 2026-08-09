---
navigation:
title: Memory Map Upgrade
position: 5
parent: upgrades/upgrades.md
item_ids:
- asmrobots:memory_map_upgrade
---

# Memory Map Upgrade
The memory map upgrade enables memory mapping for the robot. 
See [here](../instructions.md#memory-mapping-extension) for the instructions to interface with memory mapping.\
\
If memory maps are created and then the upgrade is removed from the robot, the memory maps will persist but will not function until the upgrade is re-added.\
\
When creating a memory map, a parameter called `parameter` is required. 
This parameter works similarly to IO addresses.
The parameter mod 1000 is passed to the relevant peripheral, this is referred to as the `identifier` in peripheral documentation.
The prefix identifies which peripheral it refers to, with 0 going to the robot, 1 going to the first module, etc.\
\
For example: With an [SSD](../peripherals/ssd-module.md) in the first module slot, you would use `1000` for the first example on that page, and `1008` for the second example.