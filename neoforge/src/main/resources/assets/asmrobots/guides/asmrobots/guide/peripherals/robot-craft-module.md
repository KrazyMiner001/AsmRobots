---
navigation:
title: Robot Craft Module
position: 9
parent: peripherals/peripherals.md
item_ids:
- asmrobots:robot_craft_module
---

# Robot Craft Module
## Explanation
This module involves a lot of lists, which may be confusing.\
The "list of recipes" refers to the list of every possible crafting recipe for the "robot craft" recipe type.\
Each recipe has a number of input ingredients, this is referred to as the "list of recipe inputs".\
An ingredient is a set of possible items to be used in a recipe (it is multiple items because it is common to use tags to allow several possible items).
This set of possible items for each ingredient, converted to a list, is referred to as the "list of recipe input item options".\
Feedback on this naming, or even this entire way of doing this, would be greatly appreciated.

## IO Ports

| Name                          | Id | Function - get                                                                   | Function - set                                                      |
|-------------------------------|----|----------------------------------------------------------------------------------|---------------------------------------------------------------------|
| Recipes Index                 | 0  | Get the index to be used in the list of recipes                                  | Set the index to be used in the list of recipes                     |
| Selected Recipe Output Item   | 1  | Get the output item of the selected recipe                                       | N/A                                                                 |
| Selected Recipe Output Count  | 2  | Get the output count of the selected recipe                                      | N/A                                                                 |
| Recipe Input Index            | 3  | Get the index to be used in the list of recipe inputs                            | Set the index to be used in the list of recipe inputs               |
| Recipe Input Ingredient Index | 4  | Get the index to be used in the list of recipe input item options                | Set the index to be used in the list of recipe input item options   |
| Recipe Input Item             | 5  | Get the item needed for the selected recipe's selected input's selected item     | N/A                                                                 |
| Recupe Input Count            | 6  | Get the number of items needed for the selected recipe's selected input          | N/A                                                                 |
| Craft                         | 7  | Get the last amount of items crafted. Defaults to -1 before anything is crafted. | Attempt to craft the selected recipe. Ignores the `value` argument. |
