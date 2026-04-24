# Planned Content
## Thaumic Tinkerer: Kami Reborn
|                      Feature                      |   Code   |  Model   | Texture  | Config | Research | Recipe |
|:-------------------------------------------------:|:--------:|:--------:|:--------:|:------:|:--------:|:------:|
| (Armor - Awakened) Boots of the Horizontal Shield |          |          |          |        |          |        |
|   (Armor - Awakened) Cowl of the Abyssal Depths   |          |          |          |        |          |        |
| (Armor - Awakened) Leggings of the Burning Mantle |          |          |          |        |          |        |
|   (Armor - Awakened) Robes of the Stratosphere    |          |          |          |        |          |        |
|             (Armor) Ichorcloth Boots              |          |          |          |        |          |        |
|              (Armor) Ichorcloth Cowl              |          |          |          |        |          |        |
|            (Armor) Ichorcloth Leggings            |          |          |          |        |          |        |
|             (Armor) Ichorcloth Pouch              |          |          |          |        |          |        |
|              (Armor) Ichorcloth Robe              |          |          |          |        |          |        |
|           (Tool) Awakened Ichorium Axe            |          |          |          |        |          |        |
|         (Tool) Awakened Ichorium Pickaxe          |          |          |          |        |          |        |
|          (Tool) Awakened Ichorium Shovel          |          |          |          |        |          |        |
|          (Tool) Awakened Ichorium Sword           |          |          |          |        |          |        |
|                (Tool) Ichorium Axe                | COMPLETE | COMPLETE | COMPLETE |  N/A   |          |        |
|              (Tool) Ichorium Pickaxe              | COMPLETE | COMPLETE | COMPLETE |  N/A   |          |        |
|              (Tool) Ichorium Shovel               | COMPLETE | COMPLETE | COMPLETE |  N/A   |          |        |
|               (Tool) Ichorium Sword               | COMPLETE | COMPLETE | COMPLETE |  N/A   |          |        |
|                       Ichor                       |          |          |          |        |          |        |
|                    Ichor Block                    |          |          |          |        |          |        |
|                    Ichor Cloth                    |          |          |          |        |          |        |
|                  Ichorium Ingot                   |          |          |          |        |          |        |
|                  Ichorium Nugget                  |          |          |          |        |          |        |
|             Infusion Enchant: Eternal             |          |   N/A    | COMPLETE |        |          |        |
|                    Proto Clay                     |          |          |          |        |          |        |
|             Sword of the Dragonslayer             |          |          |          |        |          |        |

---

# Chopping Block
## Thaumic Tinkerer: Kami
| Feature                          | Replacement | Description                                                                                                     |
|:---------------------------------|:-----------:|:----------------------------------------------------------------------------------------------------------------|
| Bedrock Portal                   |             | Portal to Bedrock Dimension. Opened by right-clicking bedrock with an Awakened Pickaxe.                         |
| Celestial Gateway                |             | Instantly teleport between linked Celestial Gateways. Cannot teleport across dimensions.                        |
| Celestial Pearl                  |             | Item used to link Celestial Gateways                                                                            |
| Elemental Fire: Aer              |             | Places a elemental fire that transforms certain blocks to other blocks (dirt and wood to sand for example).     |
| Elemental Fire: Aqua             |             | Places a special elemental fire that transforms Nether blocks to other blocks (netherrack to snow for example). |
| Elemental Fire: Ignis            |             | Places a special elemental fire that transforms certain blocks to their Nether counterparts.                    |
| Elemental Fire: Perditio         |             | Places a special elemental fire that turns all other types of elemental fire to normal fire.                    |
| Elemental Fire: Terra            |             | Places a special elemental fire that transforms most blocks into dirt.                                          |
| Focus Effect: Celestial Teleport |             | Instantly teleport to a linked Celestial Gateway                                                                |
| Focus Effect: Dislocation        |             | Reposition a block from one location to another                                                                 |

**Celestial Gateway Rework**
Celestial Gateway is split into two features, the block and the spell effect. The spell effect is bound to the block and can be cast
to teleport the player to the block regardless of dimension or position. When the spell effect teleport takes place, the tile entity
will save the position and world of the player. If the player interacts with the block with a caster, they will return to the saved
position. Each player would need their own save point in the block saved to the tile data.
