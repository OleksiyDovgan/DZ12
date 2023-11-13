import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class TextAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the book for analysis: ");
        String bookName = scanner.nextLine();

        String filePath = "src/" + bookName + ".txt";

        try {
            if (!Files.exists(Paths.get(filePath))) {
                System.out.println("The specified book is not found.");
                return;
            }

            String bookContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

            String[] words = bookContent.split("\\s+");

            List<String> filteredWords = Arrays.stream(words)
                    .filter(word -> word.length() > 2)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            Map<String, Long> wordCount = filteredWords.stream()
                    .collect(Collectors.groupingBy(String::toString, Collectors.counting()));

            List<Map.Entry<String, Long>> sortedWords = wordCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toList());

            System.out.println("Top 10 words:");
            sortedWords.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

            long totalUniqueWords = wordCount.size();

            String outputFileName = "src/" + bookName + "_statistic.txt";
            try (PrintWriter writer = new PrintWriter(outputFileName)) {
                sortedWords.forEach(entry -> writer.println(entry.getKey() + ": " + entry.getValue()));
                writer.println("Total unique words: " + totalUniqueWords);
            }

            System.out.println("Statistics saved to " + outputFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}