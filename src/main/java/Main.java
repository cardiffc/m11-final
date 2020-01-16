import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final String ROOT = "https://skillbox.ru/";
    private static String source = ROOT;

    public static void main(String[] args) throws IOException {
        HashSet<String> map =  new ForkJoinPool().invoke(new SiteMapBuilder(source));
      //  map.forEach(System.out::println);
    }
}
