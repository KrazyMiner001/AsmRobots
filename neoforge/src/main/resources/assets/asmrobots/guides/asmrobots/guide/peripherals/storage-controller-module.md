---
navigation:
    title: Storage Controller Module
    position: 6
    parent: peripherals/peripherals.md
item_ids:
  - asmrobots:storage_controller_module
---

# Storage Controller Module
## IO Ports
The storage controller module defines 5 IO ports

| Name                        | Id | Function - get                                                                              | Function - set                                                                                                                                 |
|-----------------------------|----|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| Source Container Index      | 0  | Gets the index of the source container                                                      | Sets the index of the source container                                                                                                         |
| Destination Container Index | 1  | Gets the index of the destination container                                                 | Sets the index of the destination container                                                                                                    |
| Source Item Index           | 2  | Gets the index of the item in the source container                                          | Sets the index of the item in the source container                                                                                             |
| Destination Item Index      | 3  | Gets the index of the item in the destination container                                     | Sets the index of the item in the destination container                                                                                        |
| Move                        | 4  | Gets the number of items moved in the last action, -1 if it failed or none has happened yet | Attempts to move the specified value amount of items from the source container's source index to the destination container's destination index |
