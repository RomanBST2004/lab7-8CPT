import java.util.*;


public class URLPool { // Отслеживает URL, которые необходимо обработать и также URL-адреса которые уже были обработаны
    private int maxDepth;

    private int waitCount = 0;

    private LinkedList<URLDepthPair> pendingURLs;

    private LinkedList<URLDepthPair> processedURLs;
    
    private HashSet<String> seenURLs; // Все URL - уже обработанные = оставшиеся необработанные

    // Пул URLPool с максимальной глубиной
    public URLPool(int max) {
	pendingURLs = new LinkedList<URLDepthPair>();
	processedURLs = new LinkedList<URLDepthPair>();
	seenURLs = new HashSet<String>();
	
	maxDepth = max;
    }

    public synchronized int getWaitCount() {
	return waitCount;
    }

    public synchronized void add(URLDepthPair nextPair) {
	String newURL = nextPair.getURL().toString();

	String trimURL = (newURL.endsWith("/")) ? newURL.substring(0, newURL.length() -1) : newURL;
	if (seenURLs.contains(trimURL)){
	    return;
	}
	seenURLs.add(trimURL);
	
	if (nextPair.getDepth() < maxDepth) {
	    pendingURLs.add(nextPair);
	    notify(); // уведомляет о новом URL
	}
	processedURLs.add(nextPair);
    }

    public synchronized URLDepthPair get() { // Приостанавливает поток до добавления нового URL
	while (pendingURLs.size() == 0) {
	    waitCount++;
	    try {
		wait();
	    }
	    catch (InterruptedException e) {
		System.out.println("Ignoring unexpected InterruptedException - " + 
				   e.getMessage());
	    }
	    waitCount--;
	}

	return pendingURLs.removeFirst();
    }

    // Выводит всех обработанных URL-адресов
    public synchronized void printURLs() {
	System.out.println("\nUnique URLs Found: " + processedURLs.size());
	while (!processedURLs.isEmpty()) {
	    System.out.println(processedURLs.removeFirst());
	}
    }
}