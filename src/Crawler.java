import java.net.*;

// Создает несколько потоков для обработки URL-адресов
public class Crawler {
    private URLPool pool;
    public int numThreads = 4; // количество потоков

    public Crawler(String root, int max) throws MalformedURLException {
	pool = new URLPool(max);
	URL rootURL = new URL(root);
	pool.add(new URLDepthPair(rootURL, 0));
    }

  
    public void crawl() {   // Генерирует потоки CrawlerTask для обработки URL-адресов
	for (int i = 0; i < numThreads; i++) {
	    CrawlerTask crawler = new CrawlerTask(pool);
	    Thread thread = new Thread(crawler);
	    thread.start();
	}

	while (pool.getWaitCount() != numThreads) { // Если все URL ожидают, то метод crawl() завершен
	    try {
		Thread.sleep(500);
	    }
	    catch (InterruptedException e) {
		System.out.println("Ignoring unexpected InterruptedException - " +
				   e.getMessage());
	    }
	}

	pool.printURLs();
    }

    public static void main(String[] args) { // Создается crawler, который просматривает ссылки, начиная с корневого URL-адреса
	// Print usage if user syntax is wrong
	if (args.length < 2 || args.length > 5) {
	    System.err.println("Usage: java Crawler <URL> <depth> " + // Отображение подсказки для ввода данных при неправильном инпуте 
			       "<patience> -t <threads>"); 
	    System.exit(1);
	}

	// Вызов crawl
	
	try { 
	    Crawler crawler = new Crawler(args[0], Integer.parseInt(args[1]));
	        
	    switch (args.length) {
	    case 3: 
		CrawlerTask.maxPatience = Integer.parseInt(args[2]);
		break;
	    case 4: 
		crawler.numThreads = Integer.parseInt(args[3]);
		break;
	    case 5: 
		CrawlerTask.maxPatience = Integer.parseInt(args[2]);
		crawler.numThreads = Integer.parseInt(args[4]);
		break;
	    }
	    crawler.crawl();
	}
	catch (MalformedURLException e) {
	    System.err.println("Error: The URL " + args[0] + " is not valid");
	    System.exit(1);
	}
	System.exit(0);
    }
}
