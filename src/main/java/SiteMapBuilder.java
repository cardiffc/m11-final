import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.zip.CheckedInputStream;

public class SiteMapBuilder extends RecursiveTask<HashSet> {
    private static String root;

    public SiteMapBuilder(String root) {
        this.root = root;
        System.out.println(root);

    }
    private String getUrl() {
        return this.root;
    }
    @Override
    protected HashSet compute() {
        Document node = null;
        try {
            Thread.sleep(1000);
            node = Jsoup.connect(root).maxBodySize(0).get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        HashSet<String> childs = new HashSet<>();
        Elements toSelect = node.select("a");
        toSelect.forEach(element -> {
            String child = element.attr("abs:href");
            if (checkURL(child)) {
                childs.add(child);
            }
        });
        List<SiteMapBuilder> taskList = new ArrayList<>();
        for (String child : childs) {
            SiteMapBuilder newTask = new SiteMapBuilder(child);
            newTask.fork();
            taskList.add(newTask);
        }
        taskList.forEach(t -> System.out.println(t.getUrl()));
        HashSet<String> test = new HashSet<>();

        return test;
    }
    private static boolean checkURL(String url) {
        return url.startsWith(Main.ROOT) && url.endsWith("/") && !url.equals(root) && !url.equals(Main.ROOT);
    }
}
