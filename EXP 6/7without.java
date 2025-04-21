import java.io.*;
import java.util.*;
public class NaiveBayes {
Map<String, Integer> classCounts = new HashMap<>();
Map<String, Map<String, Integer>> wordCounts = new HashMap<>();
void train(List<String[]> data) {
for (String[] row : data) {
classCounts.merge(row[1], 1, Integer::sum);
wordCounts.putIfAbsent(row[1], new HashMap<>());
for (String word : row[0].split(" "))
wordCounts.get(row[1]).merge(word, 1, Integer::sum);
}
}
String predict(String text) {
return classCounts.keySet().stream().max(Comparator.comparing(c ->
classCounts.get(c) + Arrays.stream(text.split(" "))
.mapToInt(w -> wordCounts.get(c).getOrDefault(w, 0)).sum())).orElse(null);
}
static List<String[]> readCSV(String file) throws IOException {
List<String[]> data = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new FileReader(file))) {
String line; while ((line = br.readLine()) != null) data.add(line.split(","));
}
return data;
}
public static void main(String[] args) throws IOException {
NaiveBayes nb = new NaiveBayes();
List<String[]> train = readCSV("train.csv"), test = readCSV("test.csv");
nb.train(train);
long correct = test.stream().filter(t -> nb.predict(t[0]).equals(t[1])).count();
System.out.println("Accuracy: " + (correct * 100.0 / test.size()) + "%");
}
}