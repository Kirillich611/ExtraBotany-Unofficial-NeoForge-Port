# ExtraBotany on NeoForge 1.21.1

This checkout builds ExtraBotany: Reburn for Minecraft **1.21.1**, **NeoForge 21.1.248 (validated)**, and **Java 21**. `Xplat` contains shared code; `NeoForge` supplies the active loader. The retained Fabric sources are not part of this port's build.

## IntelliJ IDEA and Gradle

Open the root `settings.gradle` as a Gradle project. Select a Java 21 installation under **Settings → Build Tools → Gradle → Gradle JVM**, then reload the project. Use the included wrapper instead of a separately installed Gradle.

```powershell
.\gradlew.bat build spotlessJavaCheck
.\gradlew.bat :NeoForge:runGameTestServer
.\gradlew.bat :NeoForge:runClient
.\gradlew.bat :NeoForge:runData
```

The distributable jar is `NeoForge/build/libs/extrabotany-neoforge-1.21.1-1.9.3-SNAPSHOT.jar`. Commit generated data from `NeoForge/src/generated/resources` after changing data providers. `spotlessApply` applies Java formatting; `build` also runs PMD.

## Dependencies and network troubleshooting

This version targets **Botania 457.1-SNAPSHOT**, our compatibility build of the supplied friend's Botania 457 port. The working project is `C:\Modds\Botania\Botania\1.21.1_NeoForge_457_РАБОЧАЯ_ExtraBotany`; the friend's reference copy remains unchanged. Build and cache this dependency before building ExtraBotany:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/build-botania457.ps1 -Offline
.\gradlew.bat :NeoForge:assemble :NeoForge:runGameTestServer --offline
```

Use both new runtime jars together. The compatibility build restores enchanted soil, overgrowth seeds and ender-air bottles used by ExtraBotany, and resolves renamed Botania item/block IDs. Existing quartz block **item stacks** whose old IDs were reused for crystals require manual migration; test existing saves on a backup first.
If Gradle cannot download mod dependencies but PowerShell can reach their Maven repositories, populate the local mirror:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/cache-neoforge-dependencies.ps1
.\gradlew.bat build --offline
```

The mirror uses upstream repositories and lives in ignored `.codex-backup/maven`. Offline builds also require Gradle plugins, Minecraft artifacts, and build tools to have been downloaded previously; this script only caches the selected mod dependencies and NeoForge artifacts.

## Earlier validation on Botania 448

Seven GameTests cover recipe serialization, matching and assembly of all 108 ordinary crafting recipes, flask-to-cocktail brew transfer, mana/relic capabilities and brew components, flower state after breaking and replanting, boss packet serialization, and simulated/executed water-bowl draining. They do not replace a complete playthrough or boss fight.

Client checks on 2026-10-07 used `C:\Apps\Modrinth App\profiles\NeoForge 1.21.1` with NeoForge 21.1.248. The separate `ExtraBotany_port_test_2026-10-07` world loaded successfully. Excalibur and a brew-bearing mana cocktail were obtained, Bellflower was placed, Gaia III was summoned with AI disabled, the Starry Idol armor set was equipped, and Lexica Botania's ExtraBotany category opened. Boss-bar rendering and book contents were checked in native game screenshots. Create's JEI registration completed after the water-bowl fix.

Automatic QuickPlay entry exposed an Inventory Profiles Next initialization failure; entering through the normal main menu succeeded. Full boss fights and every item interaction remain outside this smoke test. Optional EMI and KubeJS integrations require separate runtime checks because that profile does not include those mods.

The old `scripts/upload_releases.sh` targets upstream Forge/Fabric releases; the port's CI only builds and archives NeoForge artifacts.

## Earlier crafting and shader fixes on Botania 448

The Pleiades Combat Maid Suit uses four `extrabotany:das_rheingold`, a `botania:terrasteel_chestplate`, and three `botania:gaia_ingot`, arranged as `G G / GTG / III`. This matches the supplied 1.20.1 sources. Manual crafting and taking the result succeeded with the profile's mods and configuration.

Default mana cocktails now store their single remaining sip in Botania's native component. Legacy `swigsLeft` custom data remains readable, so cocktails from the earlier port can still be used in flask recipes. Book examples also use the remaining-uses component.

The Botania snapshot passed RGB colors with zero alpha into the 1.21.1 book model and item tint APIs. ExtraBotany restores opaque alpha for its own item colors and Botania's item callbacks and book renderer. This fixes the invisible held lexicon and Gaia ingots with Iris shaders without disabling the 3D book. Client regression checks use a temporary copy of the Modrinth profile, its Complementary Reimagined/Euphoria shader pack, and only the ExtraBotany test world.

The final jar was checked in that copied profile: Gaia ingots rendered in the inventory and crafting grid, the Pleiades suit was crafted and taken, and the held lexicon rendered with shaders enabled. Native screenshots are saved under `.codex-backup/profile-crafting-shaders/screenshots/` (`2026-10-07_12.34.25.png` for crafting and `2026-10-07_12.35.35.png` for the book). The installed profile jar matches the build's SHA-256.

## Botania 457 migration validation

ExtraBotany **1.9.3** migrates its API calls, mana/relic lookups, equipment, flower state synchronization, HUDs, capabilities, and mixins to Botania **457.1-SNAPSHOT**. Data generation recreated 201 recipe definitions for current registry names; all 108 ordinary crafting recipes pass matching and assembly checks.

Nine required GameTests passed with the final dependency on 2026-10-07. PMD and Spotless checks passed. Tests additionally cover old Botania names, flower update packets and normal/floating redstone-sensitive flowers. Logs: `.codex-backup/extrabotany457-book-checks.log` and `.codex-backup/extrabotany457-pool-checks.log`.

The new Botania renderer uses its own book model, so the old ExtraBotany lexicon mixin was removed and opaque alpha fixed in Botania itself. The mana surface now uses the standard entity vertex format and translucent render type, normalized atlas UVs, overlay and upward normals, retaining the animated mana sprite.

New paired releases are stored under `C:\Modds\builds\ExtraBotany\1.21.1\1.9.3\` and `C:\Modds\builds\Botania\1.21.1\457.1\`. Source directories retain their names across releases; the unchanged friend's project remains a reference copy.
Final client checks used `.codex-backup/profile-botania457`, a copy of the Modrinth profile with 188 mods and only the test world. The final jars loaded this world; legacy lexicon and all three pool variants survived migration. Animated mana surfaces and the held book rendered with Complementary Reimagined r5.9.3 / Euphoria Patches 1.10.5 enabled and disabled. Gaia ingots and the Pleiades Combat Maid Suit had visible inventory textures. Screenshots: `2026-10-07_18.12.26.png` (shaders), `18.13.21.png` (Gaia ingots), `18.14.26.png` (maid suit), `18.14.59.png` (no shaders), under that profile's `screenshots/` directory. A complete playthrough and full boss fight remain untested.