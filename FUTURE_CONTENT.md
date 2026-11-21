# Future Content
This is a list of general content that did not make the initial cut for the first release of Kami: Reborn. This is not a guarantee the content will be in future releases, but everything listed is being considered.

| Feature                          | Replacement | Description                                                                                                    |
|:---------------------------------|:-----------:|:---------------------------------------------------------------------------------------------------------------|
| Awakened Ichorium Shield         |             | An upgraded Ichorium shield. Grants +20 hp when in offhand. Can be used like a caster's gauntlet with keybind. |
| Awakened Ichorium Sickle         |             | An upgraded Ichorium Hoe. (Effects TBD)                                                                        |
| Bedrock Portal                   |             | Portal to Bedrock Dimension. Opened by right-clicking bedrock with an Awakened Pickaxe.                        |
| Celestial Gateway                |             | Instantly teleport between linked Celestial Gateways. Cannot teleport across dimensions.                       |
| Elemental Fire: Aer              |             | Places an elemental fire that transforms certain blocks to other blocks (dirt and wood to sand for example).   |
| Elemental Fire: Aqua             |             | Places an elemental fire that transforms Nether blocks to other blocks (netherrack to snow for example).       |
| Elemental Fire: Ignis            |             | Places an elemental fire that transforms certain blocks to their Nether counterparts.                          |
| Elemental Fire: Ordo             |             | Effect TBD.                                                                                                    |
| Elemental Fire: Perditio         |             | Places an elemental fire that turns all other types of elemental fire to normal fire.                          |
| Elemental Fire: Terra            |             | Places an elemental fire that transforms most blocks into dirt.                                                |
| Focus Effect: Celestial Teleport |             | Instantly teleport to a linked Celestial Gateway                                                               |
| Focus Effect: Dislocation        |             | Reposition a block from one location to another                                                                |

**Celestial Gateway Rework**
Celestial Gateway is split into two features, the block and the spell effect. The spell effect is bound to the block and can be cast
to teleport the player to the block regardless of dimension or position. When the spell effect teleport takes place, the tile entity
will save the position and world of the player. If the player interacts with the block with a caster, they will return to the saved
position. Each player would need their own save point in the block saved to the tile data.
