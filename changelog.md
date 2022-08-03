### Version 3.2.0 for Minecraft 1.18.2

- Added some functionality to fix item transfers in Ex Compressum
- Backported some things from 1.19 to support Cooking for Blockheads

### Version 3.1.0 for Minecraft 1.18.2

- Fixed potential crash on startup for some mods
- Fixed potential crash on dedicated servers under some mod combinations
- Fixed potential crash on first startup when config is generated
- Fixed crashes under some config edge-cases, instead fall back to default
- Fixed ItemCraftedEvent not being recognized on Forge

### Version 3.0.3 for Minecraft 1.18.2

- Added workaround for new tag loading order
- Fixed classloading issues in resource reload listeners

### Version 3.0.2 for Minecraft 1.18.2

- Fixed crash on startup on Forge when used with Farming for Blockheads

### Version 3.0.1 for Minecraft 1.18.2

- Updated to Minecraft 1.18.2
- Fixed stats not being registered

### Version 2.4.3 for Minecraft 1.18.x

- Fixed block entities not syncing correctly on Fabric
- Fixed config not syncing correctly on Fabric
- Fixed config sync to include all fields if an object is marked as synced
- Fixed incorrect ContainerScreenDrawEvent.Foreground binding on Forge resulting in different behaviour between Forge and Fabric
- Fixed weird mixin issue in Forge farmland by just doing it differently instead

### Version 2.4.2 for Minecraft 1.18.x

- Fixed ClientStartedEvent not being fired on Forge

### Version 2.4.1 for Minecraft 1.18.x

- Fixed crash on corrupted configs

### Version 2.4.0 for Minecraft 1.18.x

- Fixed custom events not being fired on Forge event bus
- Fixed LivingDamageEvent on Fabric not taking armor into account
- Fixed dedicated server crash on Forge
- Fixed final fields being included in config loading on Forge

### Version 2.3.1 for Minecraft 1.18

- Fixed occasional ConcurrentModificationException on startup with Forge

### Version 2.3.0 for Minecraft 1.18

- Fixed dedicated server crash on Fabric when accessing config dir 
- Fixed local configs not reloading once leaving multiplayer
- Fixed local configs not reloading when changing them while on the main menu

### Version 2.2.0 for Minecraft 1.18

- Fixed configs not loading properly on servers
- Fixed configs not syncing properly to clients
- Added support for custom farmlands
- Added support for custom damage sources
- Added support for loot modifiers on Fabric
- Added support for forced poses on Fabric
- Added support for entity APIs on Fabric
- Fixed entity registration on Forge

### Version 2.1.1 for Minecraft 1.18

- Fixed unknown discriminator errors on Forge
- Fixed config not supporting negative values on Forge

### Version 2.1.0 for Minecraft 1.18

- Updated world gen support for Minecraft 1.18
- Updated block entities support for Minecraft 1.18

### Version 2.0.0 for Minecraft 1.18

- Updated to Minecraft 1.18