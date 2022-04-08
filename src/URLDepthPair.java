import java.net.*;
import java.util.regex.*;


public class URLDepthPair { // Хранение URL'а и его глубины
    // Регулярное выражение 
    public static final String URL_REGEX = "(https?:\\/\\/)((\\w+\\.)+\\.(\\w)+[~:\\S\\/]*)";
    public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX,  Pattern.CASE_INSENSITIVE);
    private URL URL;
    private int depth;
    
    public URLDepthPair(URL url, int d) throws MalformedURLException {
	// Если входной URL абсолютный 
	URL = new URL(url.toString());

	depth = d;
    }
    
    @Override public String toString() {
	return "URL: " + URL.toString() + ", Depth: " + depth;
    }

    // Возвращает URL-адрес
    public URL getURL() {
	return URL;
    }
    
    // Возвращает глубину поиска URL-адресов
    public int getDepth() {
	return depth;
    } 

    // Возвращает имя хоста сервера
    public String getHost() {
	return URL.getHost();
    }
    
    // Возвращает ресурс
    public String getDocPath() {
	return URL.getPath();
    }
    
    // Проверка, что URL-адрес является абсолютным
    public static boolean isAbsolute(String url) {
	Matcher URLChecker = URL_PATTERN.matcher(url);
	if (!URLChecker.find()) {
	    return false;
	}
	return true;
    }
}
