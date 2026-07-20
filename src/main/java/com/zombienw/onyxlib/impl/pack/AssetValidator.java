package com.zombienw.onyxlib.impl.pack;

import com.zombienw.onyxlib.impl.block.OnyxBlockDisplayImpl;
import com.zombienw.onyxlib.impl.block.OnyxBlockImpl;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Helper class for validating required/referenced assets used by elements.
 */
public class AssetValidator {

    public void validate(Path root, OnyxNamespaceImpl ns) throws FileNotFoundException {
        String namespace = ns.getKey().getNamespace();

        validateItems(root, ns, namespace);
        validateBlocks(root, ns, namespace);
    }

    private void validateItems(Path root, OnyxNamespaceImpl ns, String defaultNamespace) throws FileNotFoundException {
        for (OnyxItemImpl item : ns.getItems()) {
            if (item.getCustomModelPath() != null) {
                requireAssetPath(root, item.getCustomModelPath(), "models", ".json", defaultNamespace,
                        "Missing custom model for item '%s'".formatted(item.getId()));
            }
            if (item.getTexturePath() != null) {
                requireAssetPath(root, item.getTexturePath(), "textures", ".png", defaultNamespace,
                        "Missing texture for item '%s'".formatted(item.getId()));
            }
        }
    }

    private void validateBlocks(Path root, OnyxNamespaceImpl ns, String defaultNamespace) throws FileNotFoundException {
        for (OnyxBlockImpl block : ns.getBlocks()) {
            if (block.getCustomModelPath() != null) {
                requireAssetPath(root, block.getCustomModelPath(), "models", ".json", defaultNamespace,
                        "Missing custom model for block '%s'".formatted(block.getId()));
            } else if (block.getBlockDisplay() != null) {
                Map<String, String> textures = ((OnyxBlockDisplayImpl) block.getBlockDisplay()).buildTextureMap();
                for (Map.Entry<String, String> entry : textures.entrySet()) {
                    requireAssetPath(root, entry.getValue(), "textures", ".png", defaultNamespace,
                            "Missing texture for block '%s' on face '%s'".formatted(block.getId(), entry.getKey()));
                }
            }
        }
    }

    private void requireAssetPath(Path root, String targetPath, String folder, String extension, String defaultNamespace, String errorPrefix) throws FileNotFoundException {
        String targetNs = defaultNamespace;
        String localPath = targetPath;

        if (targetPath.contains(":")) {
            String[] parts = targetPath.split(":", 2);
            targetNs = parts[0];
            localPath = parts[1];
        }

        Path expectedPath = root.resolve(Path.of("assets", targetNs, folder, localPath + extension));
        if (!Files.exists(expectedPath)) {
            throw new FileNotFoundException(String.format(
                    "OnyxLib Validation Error: %s. Expected file at: %s",
                    errorPrefix, expectedPath.toString().replace('\\', '/')
            ));
        }
    }
}