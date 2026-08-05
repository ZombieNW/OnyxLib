# OnyxLib
### _Server-side content-creation library for PaperMC_

![Language](https://badgen.net/badge/language/Java/orange) ![License](https://badgen.net/badge/license/MIT/red) ![Framework](https://badgen.net/badge/framework/PaperMC/blue)

OnyxLib is a work-in-progress library for creating server-side content to use on PaperMC servers. It aims to make it easy to add custom items, blocks, assets, and other content types as modular "content packs."

## Key features
- Modular architecture: each plugin built with OnyxLib acts as an independent content pack.
- Automatic asset merge: a utility collects and merges installed pack assets into a single usable package.
- API surface for registering custom items, blocks, recipes, and resources.
- Fluent custom GUI (chest menu) builder for interactive inventories.
- Server-side only, no client mods required.

## Requirements
- PaperMC server compatible with your target Minecraft version.
- Java 17+ (or the Java version recommended by your target PaperMC release).
- Build tool: Maven or Gradle (support depends on the plugin template you use).

## Quick install
1. Add OnyxLib as a dependency in your plugin project (Maven/Gradle).  
2. Implement your plugin using the provided skeleton and APIs.  
3. Drop the plugin JAR into the server `plugins/` folder and restart the server.

Example dependency and packaging details will be provided once official artifacts are published.

## Quick usage
- /onyx generatepack  
  Gathers and merges all installed content packs’ assets into a single package ready for the server.
- /onyx give \<namespace:item_id\> [amount]  
  Gives the sender a registered OnyxLib item/block.
- /onyx gui  
  **Operator only.** Opens an in-game menu listing every registered OnyxLib element across all namespaces; clicking an item gives you one.

## Developing a content pack
Recommended structure:
- assets/
- data/
- config.yml
- plugin.yml (or pack manifest)

During plugin initialization, register your custom items/blocks via OnyxLib’s APIs and include the required resource files in assets/ and data/ to be merged into the generated pack.

## Minimal example (pseudo)
1. Extend your plugin main class.
2. In onEnable():
   - Register a custom item.
   - Register a block or recipe.
3. Provide related resources under `assets/` and `data/`.

Concrete code examples and templates will be added to the documentation as the project matures.

## Custom GUIs
OnyxLib includes a fluent builder for custom inventory GUIs (chest menus), so plugins don't need to hand-roll `InventoryHolder`/`InventoryClickEvent` boilerplate.

```java
OnyxGui menu = OnyxLib.gui("My Shop", 3)
    .fill(new ItemStack(Material.GRAY_STAINED_GLASS_PANE))
    .item(13, new ItemStack(Material.DIAMOND), click -> {
        click.getPlayer().sendMessage("You clicked the diamond!");
    })
    .onClose(player -> player.sendMessage("Menu closed."));

menu.open(player);
```

- `OnyxLib.gui(title, rows)` creates a new menu (1-6 rows). Titles can be a `Component` or a plain `String`.
- `.item(slot, stack, onClick)` places a button; `onClick` receives an `OnyxGuiClickEvent` with the player, slot, and click type. Pass `null` (or omit the consumer) for a purely decorative button.
- `.fill(stack)` fills every currently-empty slot, handy for borders/backgrounds.
- `.cancelClicksByDefault(false)` disables the default click-cancelling if you want players to be able to take/move items.
- `.onClose(player -> ...)` runs when the menu is closed.
- Menus are NOT registered like items/blocks — each `OnyxLib.gui(...)` call returns a fresh, standalone instance you configure and `.open(player)` yourself. Reuse one instance to share state across viewers, or build a new one per player for per-player state.
- In-game, operators can run `/onyx gui` to browse and pull any registered element from a generated menu — a working example of the API in action.

## Contributing
Contributions and bug reports are welcome.
1. Open an issue describing the change or problem.
2. Submit a pull request with a clear description and tests/examples where appropriate.
3. Follow project coding conventions.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Support
Open an issue on the repository for questions, bug reports, or feature requests.
