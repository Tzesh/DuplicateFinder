package edu.anadolu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class DuplicateChecker extends SimpleFileVisitor<Path> {

    private final Map<String, List<Path>> map = new HashMap<>();

    /**
     * Deprecated
     * public static void checkDuplicates(Map<String, List<Path>> duplicateAllowedMap) {
     * for (Map.Entry<String, List<Path>> entry : duplicateAllowedMap.entrySet()) {
     * if (entry.getValue().size() > 1) {
     * System.out.println("Duplicates detected! Duplicate files and their locations are:");
     * for (int i = 0; i < entry.getValue().size(); i++) System.out.println(entry.getValue().get(i));
     * }
     * }
     * }
     */

    static Map<String, List<Path>> sortMap(Map<String, List<Path>> unsortMap) {
        return unsortMap.entrySet().stream()
                .filter(map -> map.getValue().size() > 1)
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2.size() - o1.size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<String, List<Path>> getMap() {
        return sortMap(map);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        if (dir.toAbsolutePath().toString().contains("Recycle.Bin")) return FileVisitResult.SKIP_SUBTREE;

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (!Files.isReadable(file)) return FileVisitResult.CONTINUE;
        if (!Files.isRegularFile(file)) return FileVisitResult.CONTINUE;
        if (Files.isHidden(file)) return FileVisitResult.CONTINUE;

        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String checksum = getFileChecksum(md5Digest, file.toFile());

        List<Path> l = map.getOrDefault(checksum, new ArrayList<>());
        l.add(file.toAbsolutePath());
        map.put(checksum, l);

        return FileVisitResult.CONTINUE;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = fis.read(byteArray)) != -1) digest.update(byteArray, 0, bytesCount);

        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }
}
