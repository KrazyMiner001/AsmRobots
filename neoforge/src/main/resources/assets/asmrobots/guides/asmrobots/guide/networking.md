---
navigation:
    title: Networking
    position: 6
item_ids:
  - asmrobots:relay_block
---

# Networking
Networking allows robots to share data.\
Networking requires at least 1 relay block and a robot with a [networking module](peripherals/networking-module.md).\
Networking works through these relay blocks. 
Each relay block has an address, which is simply its block coordinates.
It also has a list of 32-bit integers, the indices of which are called ports (the actual data structure is not a list, so it does not waste space on unused ports).\
A robot with a networking module can get or set any port of any relay, and can subscribe to changes on a relay.