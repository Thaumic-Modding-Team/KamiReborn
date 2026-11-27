# KAMI: Reborn Changelog
## 1.1.0
### Added
- Added Celestial Gateway, Celestial Pearls, and Celestial Translocator, an all new teleportation system
- Added Ichorium Hoe, an unbreakable hoe
- Added Ichorium Shield, an unbreakable shield
- Added Ichorium Sickle of the Harvester, an awakened version of the Ichorium Hoe
- Added Ichorium Shield of the Caster, an awakened version of the Ichorium Shield that can utilize foci like gauntlets
- Added Focus Effect: Dislocation, used to transfer blocks and entities from one location to another
- Added Infused Elemental Fire (all primal aspects), extremely dangerous fire that can terraform and destroy entire worlds if you're not careful
- Added `ru_ru.lang` courtesy of leskovetz
### Changed
- Awakened tool mode toggle now requires the player to sneak right-click empty air. This fixes incompatibilities with effects like the sounding infusion enchant.
- Awakened Ichorium Sword dragonslayer will now affect Ice and Fire Dragonsteel Armor
- (For Developers) Ichorium tool constructors are now public instead of package private
- Block of Ichor and Block of Ichorium are now Epic rarity
### Fixed
- Fixed Awakened Tools playing mode swap sounds for all nearby players
- Fixed Awakened Tools sending double messages when switching modes
- Fixed bug preventing Awakened Tools from breaking blocks with hardness of 0
- Fixed unused sound event error
- Fixed Awakened Ichorium Sword reequipping after every swing

---

## 1.0.6
### Added
- Added a config option to enable an alternate research background that is less colorful and saturated (highly recommended if the original is hard to look at).
---

## 1.0.5
### Changed
- Awakened Ichorium Sword titanslayer strike damage can now be configured
### Fixed
- Awakened Ichorium Sword now requires a full swing to perform a titanslayer strike

---

## 1.0.4
### Added
- Added a config option to make the Ichorium material overpowered (Tinkers' Construct / Construct's Armory).
- Added a config option to make the Ichorium tool material use a more overpowered trait (Tinkers' Construct).

---

## 1.0.3
### Fixed
- Fixed a ton of server crashes.

---

## 1.0.2
### Fixed
- Fixed a potential client crash with the Tinkers' Construct integration.

---

## 1.0.1
### Update Notes
Before updating, be sure to remove all items from any existing Bottomless Pouches. Failure to do may void pouch contents.
### Added
- Added underwater and flying mining speed bonus to awakened helm
- Added `zh_cn.lang` courtesy of ZHAY10086
### Changed
- Awakened Helm can no longer be enchanted with Aquatic Affinity (bonus speed is built into helm natively)
- Ichorium Pickaxe and Awakened Pickaxe now come pre-enchanted with Refining IV
- Ichorium Sword and Awakened Sword now come pre-enchanted with Arcing IV
### Fixed
- Fixed missing keybinding translation keys
- Fixed bottomless pouch focus desync
- Fixed Eternal infusion enchant applying with no components
- Fixed IllegalAccessError when using Awakened tools