# OnyxLib
### _Server-side content-creation library for PaperMC_

![Language](https://badgen.net/badge/language/Java/orange) ![License](https://badgen.net/badge/license/MIT/red) ![Framework](https://badgen.net/badge/framework/PaperMC/blue)

OnyxLib is a work-in-progress library for creating server-side content to use on PaperMC servers. It aims to make it easy to add custom items, blocks, assets, and other content types as modular "content packs."

## Key features
- Modular architecture: each plugin built with OnyxLib acts as an independent content pack.
- Automatic asset merge: a utility collects and merges installed pack assets into a single usable package.
- API surface for registering custom items, blocks, recipes, and resources.
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

## Contributing
Contributions and bug reports are welcome.
1. Open an issue describing the change or problem.
2. Submit a pull request with a clear description and tests/examples where appropriate.
3. Follow project coding conventions.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Support
Open an issue on the repository for questions, bug reports, or feature requests.
