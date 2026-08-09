---
navigation:
    title: Getting Started
    position: 2
---

# Getting Started
# Overview

The robot is a 32-bit computer, inspired by RISC-V, with an 8-bit byte size.\
The following terminology shall be used for units of memory:

| Name | Bit Size |
|------|----------|
| Byte | 8 bits   |
| Half | 16 bits  |
| Word | 32 bits  |

# Modules
The system has 4 slots for peripherals called modules.\
For more information see IO Interfacing

# Upgrades
The system has 2 slots for upgrades.\
For more information see [upgrades](upgrades/upgrades.md)

# Registers
The system has 48 general purpose registers and 3 special registers.\
\
The registers `a0` through to `a15` are argument registers and are caller-saved. 
They should be used sequentially for function arguments and for function returns.
The stack can be used if more space is needed.\
\
The registers `t0` through to `t15` are temporary registers and are called-saved.
Their purpose is to store temporary data in a subroutine without having to use the stack to store data.\
\
The registers `s0` through to `s15` are saved registers and are callee-saved.
Their purpose is to store more permanent data, which must persist between subroutine calls.\
\
The register `sp` is the stack pointer.\
The register `pc` is the program counter.\
The register `rz` is the "zero register", and always has a value of zero.

# Instructions
The instructions roughly follow the pattern `mnemonic destination, source1, source2, ...`\
For a complete reference see: [Instructions](./instructions.md)

# IO Interfacing
The system uses `in` and `out` instructions to interface with peripherals.\
There is no memory mapping currently, but it may be added in future. If it is added it will be limited to more expensive modules
\
\
The basic syntax of the `in` instruction is: `in target, ioAddress` and basic syntax of the `out` instruction is: `out ioAddress, source`

## IO Addresses
The main part of an IO address is a number between 0 and 999 (inclusive).
This will be expanded in future if needed.
IO addresses additionally have a prefix indicating which peripheral to address.
The robot has a prefix of 0, and the modules follow in sequential order.\
### Examples:
`out 2, a0` would send the value in `a0` to IO address `2` of the robot\
`out 1003, 5` would send the value `5` to IO address `3` of the first module
