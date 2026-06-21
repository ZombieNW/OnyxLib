---
id: getting-started
title: Getting Started
sidebar_position: 1
---

# OnyxLib

OnyxLib is a framework for creating custom content using PaperMC. All custom items map directly to real, pre-existing vanilla objects.

## Getting Started

This page will walk you through setting up OnyxLib, creating your first item, and generating a resource pack.

---

## 0. Download OnyxLib
[Download from GitHub](https://github.com/ZombieNW/OnyxLib/releases/latest)

## 1. Adding OnyxLib to Your Plugin

Install the core OnyxLib plugin on your server. Then, add OnyxLib as a dependency to your plugin's source code. *May I recommend using JitPack with OnyxLib's GitHub.*

### API Usage

In your plugin's main class, create your namespace with `OnyxLib.namespace()`. Ideally this should be done during initialization.

```java
package com.myname.myplugin;

import com.zombienw.onyxlib.api.OnyxLib;
import com.zombienw.onyxlib.api.OnyxNamespace;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create your plugin's namespace
        OnyxNamespace ns = OnyxLib.namespace(this);
        
        // Register your item
        ns.item("ruby") // item id
          .baseItem(Material.EMERALD) // item material
          .displayName("Ruby") // item "name"
          .texture("items/ruby"); // Do NOT include .png
    }
}
```

## 2. Set Up Your Resource Textures

OnyxLib scans assets from your plugin JAR's resource folder following standard Minecraft conventions to generate resource packs.

### Directory Layout
Place your texture file in `src/main/resources` like this:

```text
myplugin/
└── src/
    └── main/
        └── resources/
            └── assets/
                └── myplugin/          <-- must match your plugin's lowercase name
                    └── textures/
                        └── items/
                            └── ruby.png
```

**Note:** In your code, `.texture("items/ruby")` points to `assets/myplugin/textures/items/ruby.png`. Including `.png` will throw an error.

## 3. Generate Resource Pack

After creating your items, and your textures are in place, build your plugin `.jar` file, drop it in your server's `plugins/` folder **alongside** `OnyxLib.jar`, and start the server.

1. Log in as an admin (have OP or `onyxlib.admin`)
2. Run the pack generation command:
```text
/onyx generatePack
```
3. OnyxLib will asynchronously compile the zip file. Look for the confirmation message in chat.
```text
[OnyxLib] Resource pack generated: resourcepack.zip (1 items)
```

You can find the ready-to-use zip archive located at `plugins/OnyxLib/resourcepack.zip`.

## 4. Get Your Items

To test your new item, use the built-in give command:

```text
/onyx give myplugin:ruby
```

This command gives you an item that looks like your custom texture, functions on top of your vanilla base item's mechanics, and safely retains its identity under the hood.
