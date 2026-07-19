package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._09_WebCrawlerMultithreaded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Multithreaded web crawler (LeetCode 1242).
 *
 * Crawl every URL on the SAME host as startUrl, using a thread pool, never
 * visiting a URL twice. The two interesting problems are dedup-under-race and
 * knowing when the (dynamically growing) work is finished:
 *
 *   - visited is a concurrent set; visited.add(url) returns true for exactly
 *     ONE thread -> that thread schedules the crawl. This is the atomic
 *     check-and-claim, no separate contains()+add() race.
 *   - `pending` counts outstanding tasks. We increment BEFORE submitting a
 *     task (from inside the parent, before the parent decrements). So the
 *     count can only reach 0 once the whole graph is drained -> that is our
 *     termination signal.
 */
public class WebCrawler {

    /** The blocking "network" dependency the crawler is given. */
    public interface HtmlParser {
        List<String> getUrls(String url);
    }

    private final HtmlParser parser;
    private final ExecutorService pool;
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final AtomicInteger pending = new AtomicInteger(0);
    private final Object done = new Object();   // monitor: main waits until pending hits 0

    public WebCrawler(HtmlParser parser, int threads) {
        this.parser = parser;
        this.pool = Executors.newFixedThreadPool(threads);
    }

    public List<String> crawl(String startUrl) {
        String host = hostOf(startUrl);

        visited.add(startUrl);              // claim the seed so no one re-adds it
        submit(startUrl, host);

        synchronized (done) {
            while (pending.get() > 0) {
                try { done.wait(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); return new ArrayList<>(visited); }
            }
        }
        pool.shutdown();
        return new ArrayList<>(visited);
    }

    /** Schedule one crawl task; MUST bump `pending` before handing work to the pool. */
    private void submit(String url, String host) {
        pending.incrementAndGet();
        pool.execute(() -> {
            try {
                for (String next : parser.getUrls(url)) {
                    // same host AND we are the first to claim it -> crawl it
                    if (host.equals(hostOf(next)) && visited.add(next)) {
                        submit(next, host);
                    }
                }
            } finally {
                // last task out turns off the lights
                if (pending.decrementAndGet() == 0) {
                    synchronized (done) { done.notifyAll(); }
                }
            }
        });
    }

    /** host of "http://news.yahoo.com/foo" -> "news.yahoo.com". */
    private static String hostOf(String url) {
        String noScheme = url.substring(url.indexOf("://") + 3);
        int slash = noScheme.indexOf('/');
        return slash == -1 ? noScheme : noScheme.substring(0, slash);
    }

    // ---- demo ----
    public static void main(String[] args) {
        // A tiny fake web: news.yahoo.com is one host, news.google.com another.
        Map<String, List<String>> web = new HashMap<>();
        web.put("http://news.yahoo.com/news", Arrays.asList(
                "http://news.yahoo.com/news",           // self-link (dedup'd)
                "http://news.yahoo.com/tech",
                "http://news.google.com/other"));        // different host -> skipped
        web.put("http://news.yahoo.com/tech", Arrays.asList(
                "http://news.yahoo.com/sports",
                "http://news.yahoo.com/news"));           // back-edge (dedup'd)
        web.put("http://news.yahoo.com/sports", Arrays.asList(
                "http://news.yahoo.com/tech"));           // cycle (dedup'd)

        HtmlParser parser = url -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {} // simulate latency
            return web.getOrDefault(url, new ArrayList<>());
        };

        List<String> result = new WebCrawler(parser, 4).crawl("http://news.yahoo.com/news");
        result.sort(String::compareTo);
        System.out.println("Crawled (" + result.size() + "):");
        result.forEach(u -> System.out.println("  " + u));
        System.out.println("Expected 3 news.yahoo.com URLs (news/tech/sports), no google, no dupes.");
    }
}
