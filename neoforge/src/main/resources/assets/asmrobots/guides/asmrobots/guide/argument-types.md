---
navigation:
    title: Argument Types
    parent: instructions.md
---

# Register
Reference to one of the system registers.\
Typically used as either a source or destination argument, data gets written to the register or read from its value.\
e.g: `pop a0`

# Immediate 32
A 32-bit integer.\
Typically used as a source argument, the integer specified is used as the value.\
e.g: `push 15`

# Immediate Float 32
A 32-bit floating point number.\
Typically used as a source argument, the value specified is used as the value.\
The system does not have floating point operations currently, but floating point numbers are used for some IO operations.\
e.g: `push 15.2`

# Pointer
Pointer to a value in memory.\
Only used in the move instructions.\
Reads or write data from the specified memory address.\
Has the format `offset`(`baseAddress`) where `offset` is an immediate32 and `offset` is a register.\
e.g. `mov 13(rz), -5(a0)`

# Condition
A condition code, used in conditional branching.\
In `jcond address, condition, arg1, arg2`, the branch happens only if `condition` evaluates to `true` on the arguments.\
The condition codes are:

| Code | Meaning                 | Symbolic Representation |
|------|-------------------------|-------------------------|
| eq   | equals                  | `arg1 == arg2`          |
| lt   | less than               | `arg1 < arg2`           |
| le   | less than or equal to   | `arg1 <= arg2`          |
| gt   | greater than            | `arg1 > arg2`           |
| ge   | grater than or equal to | `arg1 >= arg2`          |

# Label
A reference to a predefined label.\
Used in branching and the move instructions, represents the memory address of the corresponding label.
e.g. 
```
test_label:
    jump test_label
```