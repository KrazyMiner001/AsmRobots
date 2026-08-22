---
navigation:
    title: Chunk Load Upgrade
    position: 6
    parent: upgrades/upgrades.md
item_ids:
- asmrobots:chunk_load_upgrade
---

# Chunk Load Upgrade
The chunk load upgrades causes the robot to load the chunk that it is currently in.\
More specifically (these details should not matter to most people): 
The chunk load upgrades makes the robot, every time it ticks, add a chunk loading ticket at its current position with a radius of 1.
The ticket has a timeout of 10 ticks, and has the flags of LOADING, KEEP_DIMENSION_ACTIVE, PERSIST, and SIMULATION.\
I am not very familiar with chunk loading so feedback on this would be appreciated from anyone more experienced with this.