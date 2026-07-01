---
navigation:
    title: Crafting Table Module
    position: 3
    parent: peripherals/peripherals.md
---

# Crafting Table Module
## IO Ports
The crafting table module defines 4 IO ports

| Name  | Id | Function - get                                    | Function - set                                                       |
|-------|----|---------------------------------------------------|----------------------------------------------------------------------|
| Index | 0  | Gets the item index used for the other IO ports   | Sets the item index used for the other IO ports                      |
| Count | 1  | Gets the count of the item at the current index   | N/A                                                                  |
| Item  | 2  | Gets the item id of the item at the current index | N/A                                                                  |
| Craft | 3  | N/A                                               | Instructs the crafting table to craft, the value provided is ignored |
