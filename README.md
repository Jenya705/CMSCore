# CMSCore
Minestom server core

# Features
* Item attributes
* Instance configuration
  * PVP
  * Generator
  * Loader

# Repository

check out: https://jitpack.io/#Jenya705/CMSCore

# Creating items

```java
EasyCustomItem
                .builder()
                .material(Material.STICK)
                .attribute(Attribute.ATTACK_DAMAGE, 1, AttributeOperation.MULTIPLY_TOTAL) // if this item is used this attribute modified will be applied to entity
                .displayName(Component
                        .text("Damage doubler")
                        .color(NamedTextColor.DARK_RED)
                )
                .key("cms:damage_doubler") // item key
                .build()
                .fastRegister() // registering item in registry
```

# Instances

## Supported generators
* Void
* Flat
  * Layers (layers is array of material names)

## Supported loaders
* Anvil
  * Path value is path to anvil world

## How to use it?

```json
"test_instance": {
      "generator": {
         "type": "flat"
      },
      "pvp": true
}
```

creating instance with flat generator and enabled pvp

```json
"flat_instance": {
      "loader": {
        "path": "./world",
        "type": "anvil"
      },
      "pvp": true
}
```

creating instance with loading anvil world and enabled pvp

