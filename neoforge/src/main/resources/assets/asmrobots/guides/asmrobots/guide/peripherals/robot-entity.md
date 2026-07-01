---
navigation:
    title: Robot Entity
    position: 1
    parent: peripherals/peripherals.md
---

# Robot Entity
The most essential of the peripherals, the robot itself.

## IO Ports
The robot defines 8 IO ports

| Name        | Id | Function - get                                                                      | Function - set                                                                                                                                      |
|-------------|----|-------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| Velocity    | 1  | Gets the robot's current speed                                                      | Sets the robot's speed to the (floating point) value provided, limited to [-1,1], blocks/second                                                     |
| Print Int   | 2  | N/A                                                                                 | Writes the value provided, as a 32-bit integer, to chat                                                                                             |
| Print Float | 3  | N/A                                                                                 | Writes the value provided, as a 32-bit floating point number, to chat                                                                               |
| Feet Block  | 4  | Gets the id of the block beneath the robot                                          | N/A                                                                                                                                                 |
| Attack      | 5  | Gets whether the robot is in attacking mode or not: 0 means false, 1 means true     | If the provided value is 0, instructs the robot to stop attacking/breaking blocks, otherwise instructs the robot to begin attacking/breaking blocks |
| Notify Bump | 6  | Gets whether the "bump" interrupt is currently enabled: 0 means false, 1 means true | If the provided value is 0, disables the "bump" interrupt, otherwise enables the "bump interrupt"                                                   |
| Yaw         | 7  | Gets the robot's current yaw                                                        | Sets the robot's target yaw                                                                                                                         |
| Pitch       | 8  | Gets the robot's current pitch                                                      | Sets the robot's target pitch                                                                                                                       |

## Interrupts
The robot define 1 interrupt

| Name | Id | Function                                                 |
|------|----|----------------------------------------------------------|
| Bump | 0  | Fires an interrupt whenever the robot bumps into a block |
