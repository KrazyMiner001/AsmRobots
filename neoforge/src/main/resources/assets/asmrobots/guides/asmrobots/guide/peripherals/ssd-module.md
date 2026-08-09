---
navigation:
title: Solid State Drive Module
position: 7
parent: peripherals/peripherals.md
item_ids:
- asmrobots:solid_state_drive_module
---

# SSD Module
NOTE: The SSD module is useless without [memory mapping](../upgrades/memory-map-upgrade.md)
## IO Ports
The SSD module defines only one IO port

| Name | Id | Function - get                                                                                              | Function - set |
|------|----|-------------------------------------------------------------------------------------------------------------|----------------|
| Size | 0  | Get the number of bytes that the SSD can hold. This should always be 128, but a custom SSD could change it. | N/A            |

## Memory Mapping IDs
When the SSD is mapped into memory, the identifier is used as an offset.\
For example: If the identifier `000` is mapped to memory address `0`, then memory address `5` will return the byte at `5` on the SSD.
If identifier `008` is mapped to memory address `0`, then memory address `5` will return the byte at `13` on the SSD.
