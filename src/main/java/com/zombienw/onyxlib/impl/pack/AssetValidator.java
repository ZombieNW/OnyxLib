package com.zombienw.onyxlib.impl.pack;

import com.zombienw.onyxlib.impl.block.OnyxBlockDisplayImpl;
import com.zombienw.onyxlib.impl.block.OnyxBlockImpl;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class AssetValidator {

    public void validate(Path root, OnyxNamespaceImpl ns) throws FileNotFoundException {
        String namespace = ns.getKey().getNamespace();

        validateItems(root, ns, namespace);
        validateBlocks(root, ns, namespace);
    }

    private void validateItems(Path root, OnyxNamespaceImpl ns, String namespace) throws FileNotFoundException {
        for (OnyxItemImpl item : ns.getItems()) {
            if (item.getCustomModelPath() != null) {
                requirePath(root, "models/" + item.getCustomModelPath() + ".json", namespace,
                        "Missing custom model for item '%s'".formatted(item.getId()));
            }
            if (item.getTexturePath() != null) {
                requirePath(root, "textures/" + item.getTexturePath() + ".png", namespace,
                        "Missing texture for item '%s'".formatted(item.getId()));
            }
        }
    }

    private void validateBlocks(Path root, OnyxNamespaceImpl ns, String namespace) throws FileNotFoundException {
        for (OnyxBlockImpl block : ns.getBlocks()) {
            if (block.getCustomModelPath() != null) {
                requirePath(root, "models/" + block.getCustomModelPath() + ".json", namespace,
                        "Missing custom model for block '%s'".formatted(block.getId()));
            } else if (block.getBlockDisplay() != null) {
                Map<String, String> textures = ((OnyxBlockDisplayImpl) block.getBlockDisplay()).buildTextureMap();
                for (Map.Entry<String, String> entry : textures.entrySet()) {
                    requirePath(root, "textures/" + entry.getValue() + ".png", namespace,
                            "Missing texture for block '%s' on face '%s'".formatted(block.getId(), entry.getKey()));
                }
            }
        }
    }

    private void requirePath(Path root, String localPath, String namespace, String errorPrefix) throws FileNotFoundException {
        Path expectedPath = root.resolve("assets/" + namespace + "/" + localPath);
        if (!Files.exists(expectedPath)) {
            throw new FileNotFoundException(String.format(
                    "OnyxLib Validation Error: %s. Expected file at: %s",
                    errorPrefix, expectedPath.toString().replace('\\', '/')
            ));
        }
    }
}