# HandleHello Cross Version Notes

- 1.14.4 - 1.18.2 -- No changes
- 1.19 - 1.19.3
  - `ServerboundHelloPacket#getGameProfile()#getName()` -> `ServerboundHelloPacket#name()`
- 1.19.4 - 1.21.1
  - `this.shadow$getConnection()` -> `this.connection`

## Forge Ranges (diff due to mappings)

- 1.14.4 - 1.16.5
- 1.17.1 - 1.18.2
- 1.19 - 1.19.3
- 1.19.4 - 1.21.1

### Entrypoints to match DisplayTest changes

- 1.14.4 - v1
- 1.17.1 - v2
- 1.19 - v3
- 1.19.4 - v4
