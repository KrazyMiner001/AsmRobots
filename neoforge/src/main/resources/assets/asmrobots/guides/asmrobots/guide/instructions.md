---
navigation:
    title: Instructions
    position: 3
---

# Instructions

# Arithmetic
Note: All argument values are treated as signed 32-bit integers unless otherwise specified
## Add
Syntax: `add target, arg1, arg2`\
Function: adds the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Sub
Syntax: `sub target, arg1, arg2`\
Function: subtracts the argument `arg2` from `arg1` and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Mul
Syntax: `mul target, arg1, arg2`\
Function: multiplies the arguments `arg1` and `arg2` together and stores the least significant word of the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Mulh
Syntax: `mulh target, arg1, arg2`\
Function: multiplies the arguments `arg1` and `arg2` together and stores the most significant word of the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Div
Syntax: `div target, arg1, arg2`\
Function: Divides `arg1` by `arg2` and stores the value, rounded towards zero, in `result` 
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Rem
Syntax: `rem target, arg1, arg2`\
Function: Divides `arg1` by `arg2` and stores the remainder in `result`
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Sll
Syntax: `sll target, arg1, arg2`\
Function: Bitshifts `arg1` by `arg2` bits to the left and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Srl
Syntax: `srl target, arg1, arg2`\
Function: Bitshifts `arg1` by `arg2` bits to the right (filling the leftmost bits with `0`) and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |
## Sra
Syntax: `sra target, arg1, arg2`\
Function: Bitshifts `arg1` by `arg2` bits to the right (filling the leftmost bits with copies of the sign-bit) and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

# Logic
## And
Syntax: `and target, arg1, arg2`\
Function: Takes the bitwise and of the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## Or
Syntax: `or target, arg1, arg2`\
Function: Takes the bitwise or of the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## Xor
Syntax: `xor target, arg1, arg2`\
Function: Takes the bitwise or of the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## Not
Syntax: `or target, arg1`\
Function: Takes the bitwise negation of `arg1` and stores the result in `target`
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |

# Load and Store
## Mov
Syntax: `mov target, source`\
Function: Moves the value from `source` into `target`\
Argument Types:

| Name   | Types                                            |
|--------|--------------------------------------------------|
| target | register, pointer                                |
| source | register, immediate32, immediateFloat32, pointer |

## Movh
Syntax: `mov target, source`\
Function: Moves the least significant half from `source` into `target`\
Argument Types:

| Name   | Types                                            |
|--------|--------------------------------------------------|
| target | register, pointer                                |
| source | register, immediate32, immediateFloat32, pointer |

## Movb
Syntax: `mov target, source`\
Function: Moves the least significant byte from `source` into `target`\
Argument Types:

| Name   | Types                                            |
|--------|--------------------------------------------------|
| target | register, pointer                                |
| source | register, immediate32, immediateFloat32, pointer |

# Branch
## Ret
Syntax: `ret`\
Function: Pops the value from the top of the call stack and sets the program counter to it

## Jump
Syntax: `jump address`\
Function: Sets the program counter to the value in `address`\
Argument Types:

| Name    | Types                        |
|---------|------------------------------|
| address | register, immediate32, label |

## JCond
Syntax: `jcond address, condition, arg1, arg2`\
Function: Jumps to `address` only if the condition is true. See [Conditions](./argument-types.md)\
Argument Types:

| Name      | Types                                   |
|-----------|-----------------------------------------|
| address   | register, immediate32, label            |
| condition | condition                               |
| arg1      | register, immediate32, immediateFloat32 |
| arg2      | register, immediate32, immediateFloat32 |

# Stack
## Push
Syntax: `push value`\
Function: Puts `value` at the top of the stack and decrements the stack pointer accordingly\
Argument Types:

| Name  | Types                                   |
|-------|-----------------------------------------|
| value | register, immediate32, immediateFloat32 |

## Pushh
Syntax: `push value`\
Function: Puts the least significant half of `value` at the top of the stack and decrements the stack pointer accordingly\
Argument Types:

| Name  | Types                                   |
|-------|-----------------------------------------|
| value | register, immediate32, immediateFloat32 |

## Pushb
Syntax: `push value`\
Function: Puts the least significant byte of `value` at the top of the stack and decrements the stack pointer accordingly\
Argument Types:

| Name  | Types                                   |
|-------|-----------------------------------------|
| value | register, immediate32, immediateFloat32 |

## Pop
Syntax: `pop target`\
Function: Puts the value at the top of the stack in `target` and increments the stack pointer accordingly\
Argument Types:

| Name   | Types    |
|--------|----------|
| target | register |

## Poph
Syntax: `pop target`\
Function: Puts the least significant half of the value at the top of the stack in `target` and increments the stack pointer accordingly\
Argument Types:

| Name   | Types    |
|--------|----------|
| target | register |

## Popb
Syntax: `pop target`\
Function: Puts the least significant byte of the value at the top of the stack in `target` and increments the stack pointer accordingly\
Argument Types:

| Name   | Types    |
|--------|----------|
| target | register |

# IO
For more info see [Getting Started - IO Interfacing](./getting_started.md)
## In
Syntax: `in target, ioAddress`\
Function: Read data at `ioAddress` into `target`\
Argument Types:

| Name      | Types                                   |
|-----------|-----------------------------------------|
| target    | register                                |
| ioAddress | register, immediate32, immediateFloat32 |

## Out
Syntax: `in ioAddress, value`\
Function: Write data from `value` to `ioAddress`\
Argument Types:

| Name      | Types                                   |
|-----------|-----------------------------------------|
| ioAddress | register, immediate32, immediateFloat32 |
| value     | register, immediate32, immediateFloat32 |

# Interrupts
See [Interrupts](./interrupts.md) for more information
## DI
Syntax: `di`
Function: Disables interrupts

## EI
Syntax: `ei`
Function: Enables interrupts

## CI
Syntax `ci`
Function: Clears pending interrupts; should mostly be used when interrupts are disabled, before re-enabling interrupts

# Other
## Nop
Syntax `nop`\
Function: No-operation; does nothing

## Halt
Syntax `halt`\
Function: Stops execution of code