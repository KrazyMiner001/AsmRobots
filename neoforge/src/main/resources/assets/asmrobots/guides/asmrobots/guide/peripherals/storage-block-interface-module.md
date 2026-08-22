---
navigation:
    title: Storage Block Interface Module
    position: 11
    parent: peripherals/peripherals.md
item_ids:
  - asmrobots:storage_block_interface_module
---

# Storage Block Interface Module
Note: the storage block accessed using this interface is determined by raycasting to the first block in front of the robot.

## Relevant Variables
### Target Index
The target index refers to the index from which items will be removed, of either this item, or the storage block, depending on operation.

## IO Ports
The storage block interface module defines 4 IO ports

| Name                 | Id | Function - get                                                                                | Function - set                                                                                                             |
|----------------------|----|-----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| Target Index         | 0  | Get the target index                                                                          | Set the target index                                                                                                       |
| Pull                 | 1  | Get the last number of items transferred (always non-negative on success; -1 indicates error) | Transfer this number of items from the storage block to this item, if negative the operation goes in the reverse direction |
| Container Item       | 2  | Get the item id of the item in the storage block at the target index                          | N/A                                                                                                                        |
| Container Stack Size | 3  | Get the stack size of the item in the storage block at the target index                       | N/A                                                                                                                        |