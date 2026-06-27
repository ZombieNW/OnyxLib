package com.zombienw.onyxlib.impl.pack;

import org.bukkit.plugin.Plugin;
import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static void extractAssetsFromJar(Plugin plugin, Path destRoot) throws IOException {
        File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

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
        Files.createDirectories(destZip.getParent());

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(destZip)))) {
            Files.walk(source).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                ZipEntry zipEntry = new ZipEntry(source.relativize(p).toString().replace('\\', '/'));
                try {
                    zos.putNextEntry(zipEntry);
                    Files.copy(p, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    public static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) return;

        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}