package edu.anadolu.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {

        Path start = Paths.get(System.getProperty("user.home"));

        DuplicateChecker visitor = new DuplicateChecker();

        try {
            Files.walkFileTree(start, visitor);
        } catch (IOException e) {
            System.out.println("IOExpection has occurred.");
        }

        Map<String, List<Path>> map = visitor.getMap();

        for (Map.Entry<String, List<Path>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": (" + entry.getValue().size() + ") " + entry.getValue().get(0).toAbsolutePath());
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = i; j < entry.getValue().size(); j++) {
                    try {
                        if (!checkEquality(entry.getValue().get(i), entry.getValue().get(j)))
                            System.out.println("There is a collision.");
                    } catch (IOException e) {
                        System.out.println("IOExpection has occurred.");
                    }
                }
            }
        }

    }

    private static boolean checkEquality(Path p1, Path p2) throws IOException {
        byte[] f1 = Files.readAllBytes(p1);
        byte[] f2 = Files.readAllBytes(p2);
        return Arrays.equals(f1, f2);
    }
}
