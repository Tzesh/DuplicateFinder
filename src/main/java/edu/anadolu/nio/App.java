package edu.anadolu.nio;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        System.out.println("Detects and prints all duplicate files located in 'user.home' directory");

        Path start = Paths.get(System.getProperty("user.home"));

        DuplicateChecker visitor = new DuplicateChecker();

        try {
            Files.walkFileTree(start, visitor);
        } catch (IOException e) {
            System.out.println("IOExpection has occurred. This might be caused by the file is already in usage.");
        }

        Map<String, List<Path>> map = visitor.getMap();

        for (Map.Entry<String, List<Path>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": (" + entry.getValue().size() + ") " + entry.getValue().get(0).toAbsolutePath());
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = i; j < entry.getValue().size(); j++) {
                    try {
                        if (!checkEquality(entry.getValue().get(i), entry.getValue().get(j)))
                            System.out.println("Looks like a collision is happened.");
                    } catch (IOException e) {
                        System.out.println("IOExpection has occurred. This might be caused by the file is already in usage.");
                    }
                }
            }
        }

    }

    private static boolean checkEquality(Path p1, Path p2) throws IOException {
        /** Deprecated due to it is useless when we are dealing with big data, it's obvious it will throw "OutOfMemoryException"
         byte[] f1 = Files.readAllBytes(p1);
         byte[] f2 = Files.readAllBytes(p2);
         */
        byte[] f1 = readStream(p1);
        byte[] f2 = readStream(p2);
        return Arrays.equals(f1, f2);
    }

    private static byte[] readStream(Path path) throws IOException { // this method allows us to use 'stream's to manipulate bigger data without getting any kind of OutOfMemoryException

        InputStream is = new FileInputStream(path.toFile());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }


}
