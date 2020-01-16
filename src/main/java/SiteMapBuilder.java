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

public class SiteMapBuilder extends RecursiveTask<HashSet> {
    private static String root;

    public SiteMapBuilder(String root) {
        this.root = root;
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
        List<SiteMapBuilder> taskList = new ArrayList<>();
        Elements preChilds = node.select("a[href]");
        HashSet<String> childs = new HashSet<>();
        HashSet<String> testChilds = new HashSet<>();
        for (Element el : preChilds) {
            String child = el.absUrl("href");
            if (checkURL(child) && testChilds.add(child))
            childs.add(child);
        }
        for (String child : childs) {
            System.out.println(child);
            SiteMapBuilder task = new SiteMapBuilder(child);
            task.fork();
            taskList.add(task);
        }
        System.out.println(taskList.size());
        taskList.forEach(ForkJoinTask::join);
        HashSet<String> test = new HashSet<>();
            return test;
        }


    private static boolean checkURL(String url) {
        return url.startsWith(Main.ROOT) && url.endsWith("/") && !url.equals(root) && !url.equals(Main.ROOT);
    }

}
