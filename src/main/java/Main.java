import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static String source = "https://lenta.ru/";

    public static void main(String[] args) throws IOException {
        HashSet<String> map =  new ForkJoinPool().invoke(new SiteMapBuilder(source));
        map.forEach(System.out::println);
    }
}
