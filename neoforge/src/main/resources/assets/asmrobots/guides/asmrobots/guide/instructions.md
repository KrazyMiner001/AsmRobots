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

## Call
Syntax: `call address`\
Function: Same as except it pushes the program counter after this instruction to the call stack, so that it can be returned to later\
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

# Floating Point Extension
Note: Requires the [floating point upgrade](upgrades/floating-point-upgrade.md)\
Note: All argument inputs will be treated as single-precision (32-bit) floating point numbers unless otherwise specified.\

## FAdd
Syntax: `fadd target, arg1, arg2`\
Function: adds the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FSub
Syntax: `fsub target, arg1, arg2`\
Function: subtracts the argument `arg2` from `arg1` and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FMul
Syntax: `fmul target, arg1, arg2`\
Function: multiplies the arguments `arg1` and `arg2` together and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FDiv
Syntax: `fdiv target, arg1, arg2`\
Function: divides the argument `arg1` by `arg2` and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FSqrt
Syntax: `fsqrt target, arg`\
Function: computes the square root of `arg` and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FFMA
Syntax: `ffma target, arg1, arg2`\
Function: fused multiply add; performs `target = target + arg1 * arg2`
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FRem
Syntax: `frem target, arg1, arg2`\
Function: computes the remainder of `arg1` divided by `arg2`, according to the IEEE 754 standard, and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FMin
Syntax: `fmin target, arg1, arg2`\
Function: stores the smaller of `arg1` and `arg2` in `target`. If either is `NaN`, `target` is `NaN`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FMax
Syntax: `add target, arg1, arg2`\
Function: stores the larger of `arg1` and `arg2` in `target`. If either is `NaN`, target is `NaN`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg1   | register, immediate32, immediateFloat32 |
| arg2   | register, immediate32, immediateFloat32 |

## FNext
Syntax: `fnext target, arg`\
Function: stores the next float value nearest to `arg`, in the direction of positive infinity, in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FPrev
Syntax: `fprev target, arg`\
Function: stores the next float value nearest to `arg`, in the direction of negative infinity, in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FAbs
Syntax: `fabs target, arg`\
Function: stores the absolute value of `arg` in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FLog
Syntax: `flog target, arg`\
Function: stores the binary logarithm (base 2) of `arg` in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FExp
Syntax: `fexp target, arg`\
Function: stores the value of `2` raised to the power of `arg` in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## FJCond
Syntax: `fjcond address, condition, arg1, arg2`\
Function: Jumps to `address` only if the condition is true. See [Conditions](./argument-types.md)\
Argument Types:

| Name      | Types                                   |
|-----------|-----------------------------------------|
| address   | register, immediate32, label            |
| condition | condition                               |
| arg1      | register, immediate32, immediateFloat32 |
| arg2      | register, immediate32, immediateFloat32 |

## FToI
Syntax: `ftoi target, arg`\
Function: converts the value in `arg` into an integer (rounded toward zero; `NaN` becomes 0; numbers greater than the maximum 32-bit integer become it; numbers smaller than the maximum 32-bit integer become it) and stores the result in `target`\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

## IToF
Syntax: `itof target, arg`\
Function: converts the value in `arg` (interpretation it as a 32-bit integer) into a float and stores the result in `target`. The resulting value is the closest float to the specified value, choosing the one with zero at the least significant bit of the mantissa if it is exactly between two floats\
Argument Types:

| Name   | Types                                   |
|--------|-----------------------------------------|
| target | register                                |
| arg    | register, immediate32, immediateFloat32 |

# Memory Mapping Extension
Note: Requires the [memory mapping upgrade](upgrades/memory-map-upgrade.md)\

## MapIO
Syntax: `mapio outIdentifier, startAddress, size, parameter`\
Function: creates a memory map using `parameter`. 
The start of the mapped memory will be at `startAddress`. 
The end of the mapped memory will be at `startAddress` + `size` - `1`. 
Within this range, memory operations will instead interface with the mapped memory defined by `parameter` at the address `realAddress - startAddress`.
An identifier, which is used to reference this memory map for the `unmap` instruction, is stored in `outIdentifier`\
Argument Types:

| Name          | Types                        |
|---------------|------------------------------|
| outIdentifier | register                     |
| startAddress  | register, immediate32, label |
| size          | register, immediate32        |
| parameter     | register, immediate32        |

## Unmap
Syntax: `unmap identifier`\
Function: deletes the memory map with identifier `identifier`
Argument Types:

| Name       | Types    |
|------------|----------|
| identifier | register |