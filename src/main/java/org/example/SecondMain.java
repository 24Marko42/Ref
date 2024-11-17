package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SecondMain {
    public static void main(String[] args) {
        // Имя директории (фамилия) и файла (имя)
        String surname = "Kuznetsov";
        String name = "@markodeadnow";

        // Создаем пути для работы
        Path baseDir = Paths.get(surname); // Основная директория
        Path nameFile = baseDir.resolve(name); // Файл внутри основной директории
        Path dir1 = baseDir.resolve("dir1"); // Вложенная директория dir1
        Path dir2 = dir1.resolve("dir2"); // Вложенная директория dir2
        Path dir3 = dir2.resolve("dir3"); // Вложенная директория dir3

        try {
            // a. Создаем основную директорию <surname>
            Files.createDirectories(baseDir);
            System.out.println("Directory created: " + baseDir);

            // b. Создаем файл <name> внутри директории <surname>
            Files.createFile(nameFile);
            System.out.println("File created: " + nameFile);

            // c. Создаем вложенные директории dir1/dir2/dir3
            Files.createDirectories(dir3);

            // Копируем файл <name> в директорию dir3
            Path copiedFile = dir3.resolve(name); // Новый путь для копии
            Files.copy(nameFile, copiedFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied to: " + copiedFile);

            // d. Создаем файл file1 внутри директории dir1
            Path file1 = dir1.resolve("file1");
            Files.createFile(file1);
            System.out.println("File created: " + file1);

            // e. Создаем файл file2 внутри директории dir2
            Path file2 = dir2.resolve("file2");
            Files.createFile(file2);
            System.out.println("File created: " + file2);

            // f. Рекурсивный обход директории <surname>
            System.out.println("Contents of " + baseDir + ":");
            Files.walkFileTree(baseDir, new SimpleFileVisitor<>() {
                // Перед входом в директорию выводим ее имя с пометкой "D"
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    System.out.println("D " + dir);
                    return FileVisitResult.CONTINUE;
                }

                // При посещении файла выводим его имя с пометкой "F"
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("F " + file);
                    return FileVisitResult.CONTINUE;
                }
            });

            // g. Удаление директории dir1 со всем содержимым
            Files.walkFileTree(dir1, new SimpleFileVisitor<>() {
                // Удаляем файлы в директории
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                // После удаления файлов удаляем директории
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("Directory dir1 and its contents deleted");

        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода
        }
    }
}
/*Files.createDirectories автоматически создает путь, включая промежуточные директории.

Files.walkFileTree выполняет обход дерева каталогов, с помощью SimpleFileVisitor различая файлы и директории.

Для удаления директории используется обход в обратном порядке (post-order traversal) через postVisitDirectory.*/