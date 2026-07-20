package com.zombienw.onyxlib.impl.pack;

import org.bukkit.plugin.Plugin;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Helper class for managing file operations.
 */
public class FileUtils {

    public static void extractAssetsFromJar(Plugin plugin, Path destRoot) throws IOException {
        File pluginFile;
        try {
            pluginFile = Path.of(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new IOException("Failed to resolve plugin JAR URI path", e);
        }

        try (JarFile jar = new JarFile(pluginFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith("assets/") && !entry.isDirectory()) {
                    Path destPath = destRoot.resolve(entry.getName());
                    Files.createDirectories(destPath.getParent());

                    try (InputStream is = jar.getInputStream(entry)) {
                        Files.copy(is, destPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    public static void zipDirectory(Path source, Path destZip) throws IOException {
        Files.deleteIfExists(destZip);
        if (destZip.getParent() != null) {
            Files.createDirectories(destZip.getParent());
        }

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(destZip)));
             var walk = Files.walk(source)) {

            for (Path p : (Iterable<Path>) walk.filter(p -> !Files.isDirectory(p))::iterator) {
                ZipEntry zipEntry = new ZipEntry(source.relativize(p).toString().replace('\\', '/'));
                zos.putNextEntry(zipEntry);
                Files.copy(p, zos);
                zos.closeEntry();
            }
        }
    }

    public static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) return;

        try (var walk = Files.walk(dir)) {
            var paths = walk.sorted(Comparator.reverseOrder()).toList();
            for (Path path : paths) {
                Files.delete(path);
            }
        }
    }
}