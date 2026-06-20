---
id: resource-pack
title: Resource Pack Generator
sidebar_position: 4
---

# Resource Pack Generator

Resource pack generation is a core feature of OnyxLib. Need not the worries of compiling a bunch of assets and json files into a singular resourcepack, OnyxLib does it for you!

## How It Works

- **Automatic ID Assignment:** OnyxLib automatically gives every registered item a unique identifier based on its namespace and ID.
- **Texture Compilation:** OnyxLib reads your code's `.texture()` paths to find the matching image files inside your plugin's JAR.
- **Custom Model Creation:** OnyxLib uses item identifiers to generate the required custom model data JSON files.
- **Pack Generation:** OnyxLib bundles all models, textures, and JSON files into a single resource pack.

## Directory Structure

For the generator to find your textures, place your image files in your plugin's `src/main/resources/` directory following normal Minecraft conventions:

```text
src/main/resources/
└── assets/
    └── myplugin/
        └── textures/
            └── items/
                └── strawberry.png
```

## Generating the Pack

Trigger the generator from the server console or in-game as an admin (`onyxlib.admin`) or as an operator.

```text
/onyx generatePack
```

This will run in the background. Once complete, a `resourcepack.zip` file will be generated inside in the `plugins/OnyxLib/` folder. Then use `server.properties` or a dedicated plugin to host the resource pack. :D
