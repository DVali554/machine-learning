// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
   Map<String, Integer> classCounts = new HashMap();
   Map<String, Map<String, Integer>> wordCounts = new HashMap();

   public NaiveBayes() {
   }

   void train(List<String[]> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String[] var3 = (String[])var2.next();
         this.classCounts.merge(var3[1], 1, Integer::sum);
         this.wordCounts.putIfAbsent(var3[1], new HashMap());
         String[] var4 = var3[0].split(" ");
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            ((Map)this.wordCounts.get(var3[1])).merge(var7, 1, Integer::sum);
         }
      }

   }

   String predict(String var1) {
      return (String)this.classCounts.keySet().stream().max(Comparator.comparing((var2) -> {
         return (Integer)this.classCounts.get(var2) + Arrays.stream(var1.split(" ")).mapToInt((var2x) -> {
            return (Integer)((Map)this.wordCounts.get(var2)).getOrDefault(var2x, 0);
         }).sum();
      })).orElse((Object)null);
   }

   static List<String[]> readCSV(String var0) throws IOException {
      ArrayList var1 = new ArrayList();
      BufferedReader var2 = new BufferedReader(new FileReader(var0));

      String var3;
      try {
         while((var3 = var2.readLine()) != null) {
            var1.add(var3.split(","));
         }
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var1;
   }

   public static void main(String[] var0) throws IOException {
      NaiveBayes var1 = new NaiveBayes();
      List var2 = readCSV("train.csv");
      List var3 = readCSV("test.csv");
      var1.train(var2);
      long var4 = var3.stream().filter((var1x) -> {
         return var1.predict(var1x[0]).equals(var1x[1]);
      }).count();
      System.out.println("Accuracy: " + (double)var4 * 100.0 / (double)var3.size() + "%");
   }
}