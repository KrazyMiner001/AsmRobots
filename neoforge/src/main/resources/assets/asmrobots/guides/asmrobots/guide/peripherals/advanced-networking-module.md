---
navigation:
title: Advanced Networking Module
position: 10
parent: peripherals/peripherals.md
item_ids:
- asmrobots:advanced_networking_module
---

# Networking Module
Also see [networking](../networking.md).
## IO Ports

| Name            | Id | Function - get                                      | Function - set                                                                         |
|-----------------|----|-----------------------------------------------------|----------------------------------------------------------------------------------------|
| Relay Address X | 0  | Get the x coordinate of the relay to interface with | Set the x coordinate of the relay to interface with                                    |
| Relay Address Y | 1  | Get the y coordinate of the relay to interface with | Set the y coordinate of the relay to interface with                                    |
| Relay Address Z | 2  | Get the z coordinate of the relay to interface with | Set the z coordinate of the relay to interface with                                    |
| Port Offset     | 3  | Get the offset used for addresses to ports          | Set the offset that will be added to a memory address to determine which port to fetch |

## Memory Mapping 
| Name             | Id | Function                                                            |
|------------------|----|---------------------------------------------------------------------|
| Networked Memory | 0  | Create a memory map which accesses memory at `address + portOffset` |