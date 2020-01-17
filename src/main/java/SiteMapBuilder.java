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
         //  System.out.println(root);

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
        List<SiteMapBuilder> taskList = new ArrayList<>();
        HashSet<String> childs = new HashSet<>();
        Elements toSelect = node.select("a");
        toSelect.forEach(element -> {
            childs.add(element.attr("abs:href"));
        });

        for (String child : childs) {
            if (checkURL(child))
            {
                SiteMapBuilder newTask = new SiteMapBuilder(child);
                newTask.fork();
                System.out.println(newTask.getUrl());
                taskList.add(newTask);


                //  System.out.println(newTask.getUrl());
            }

        }
        taskList.forEach(ForkJoinTask::join);


        HashSet<String> test = new HashSet<>();
        return test;
    }


    private static boolean checkURL(String url) {
        return url.startsWith(Main.ROOT) && url.endsWith("/") && !url.equals(root) && !url.equals(Main.ROOT);
    }

}
