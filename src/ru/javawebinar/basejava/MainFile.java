package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        File directory = new File("..\\basejava\\src");
        printDirectory(directory, "");
    }

    private static void printDirectory(File directory, String space) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(space + "File name: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(space + "Directory name: " + file.getName());
                    printDirectory(file, space + "  ");
                }
            }
        }
    }
}
