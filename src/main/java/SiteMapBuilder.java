import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.RecursiveTask;

public class SiteMapBuilder extends RecursiveTask<HashSet> {
    private String root;

    public SiteMapBuilder(String root) {
        this.root = root;
    }

    @Override
    protected HashSet compute() {
        Document node = null;
        try {
            node = Jsoup.connect(root).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashSet<SiteMapBuilder> taskList = new HashSet<>();
        Elements preChilds = node.select("a");
        HashSet<String> childs = new HashSet<>();
        preChilds.forEach(pc -> {
            String child = pc.attr("abs:href");
            if (child.contains(root)) {
                childs.add(child);
            }
        });
        HashSet<String> urls = new HashSet<>();
        urls.add("ROOT: " + root);
        childs.forEach(child -> {
            if (!child.equals("") && !child.equals(root)) {
                    urls.add("\tchild: " + child);
            }
        });
        for (String child : childs) {
                if (!child.equals("")) {
                    SiteMapBuilder task = new SiteMapBuilder(child);
                    task.fork();
                    taskList.add(task);
                }
        }
        for (SiteMapBuilder task : taskList)
        {
            task.join();
        }
        return urls;
    }

}
