---
navigation:
title: Networking Module
position: 8
parent: peripherals/peripherals.md
item_ids:
- asmrobots:networking_module
---

# Networking Module
Also see [networking](../networking.md).
## IO Ports

| Name                        | Id | Function - get                                                                 | Function - set                                                                                                                                                         |
|-----------------------------|----|--------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Port                        | 0  | Get the port which will be read                                                | Set the port which will be read                                                                                                                                        |
| Value                       | 1  | Get the value from the relay with the specified address, at the specified port | Set the value of the relay with the specified address, at the speficied port                                                                                           |
| Relay Address X             | 2  | Get the x coordinate of the relay to interface with                            | Set the x coordinate of the relay to interface with                                                                                                                    |
| Relay Address Y             | 3  | Get the y coordinate of the relay to interface with                            | Set the y coordinate of the relay to interface with                                                                                                                    |
| Relay Address Z             | 4  | Get the z coordinate of the relay to interface with                            | Set the z coordinate of the relay to interface with                                                                                                                    |
| Subscribed Port Index       | 5  | Get the current index into the subscribed ports list                           | Set the index to be used on the subscribed ports list                                                                                                                  |
| Subscribed Port Value       | 6  | Get the port at the current index into the subscribed ports list               | Set the port at the current index into the subscribed ports list                                                                                                       |
| Remove Subscribed Port Port | 7  | N/A                                                                            | Remove the port at the current index into the subscribed ports list, or the last port if the index is greater than the size of the list. Ignores the `value` argument. |
