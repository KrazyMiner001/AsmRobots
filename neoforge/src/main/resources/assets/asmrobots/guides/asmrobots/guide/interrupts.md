---
navigation:
    title: Interrupts
    position: 3
---

# Interrupts
Peripherals can fire interrupts to the system.\
Each time an interrupt is fired, it is sent with an id, which is added to the "pending interrupts" stack.\
On each machine cycle, if there are pending interrupts and interrupts are enabled, the system:
1. Removes the most recent interrupt from the interrupt stack
2. Pushes that interrupt's id to the top of the stack
3. Pushes the current program counter to the top of the call stack (the same as the call instruction would)
4. Sets the program counter to the address of the `_int` label, or 0 if it does not exist
5. Disables interrupts

The system will still accumulate pending interrupts while interrupts are disabled, so it is recommended to clear interrupts before enabling interrupts except where this is desired.