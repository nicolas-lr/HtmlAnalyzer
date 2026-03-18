import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

public class HtmlAnalyzer {

    private static final int MAX_LINES = 10000; 
    private static final int TIMEOUT_MS = 5000;  

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }

        String urlInput = args[0];
        try {
            URI uri = new URI(urlInput);
            if (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https")) {
                throw new IllegalArgumentException("Apenas HTTP/HTTPS são permitidos.");
            }

            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            List<String> htmlLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                int lineCount = 0;

                while ((inputLine = reader.readLine()) != null) {
                    htmlLines.add(inputLine);
                    lineCount++;
                    
                    if (lineCount > MAX_LINES) {
                        System.out.println("URL connection error");
                        return;
                    }
                }
            }

            String result = deepestTextFinder(htmlLines.toArray(new String[0]));
            System.out.println(result);

        } catch (URISyntaxException | IOException | IllegalArgumentException e) {
            System.out.println("URL connection error");
        }
    }

    private static String deepestTextFinder(String[] htmlArray) {
        Pattern openingTag = Pattern.compile("^<([a-zA-Z0-9]+)>$");
        Pattern closingTag = Pattern.compile("^</([a-zA-Z0-9]+)>$");
        Stack<String> tagStack = new Stack<>();

        int deepestLevel = -1;
        String deepestText = "";

        for (String tempLine : htmlArray) {
            String htmlLine = tempLine.trim();
            if (htmlLine.isEmpty()) continue;

            Matcher matcherOpening = openingTag.matcher(htmlLine);
            Matcher matcherEnding = closingTag.matcher(htmlLine);

            if (matcherOpening.matches()) {
                tagStack.push(matcherOpening.group(1));
            } 
            else if (matcherEnding.matches()) {
                String closingTagName = matcherEnding.group(1);
                
                if (tagStack.isEmpty() || !tagStack.pop().equals(closingTagName)) {
                    return "malformed HTML";
                }
            } 
            else {
                int currentLevel = tagStack.size();
                if (currentLevel > deepestLevel) {
                    deepestLevel = currentLevel;
                    deepestText = htmlLine;
                }
            }
        }

        return tagStack.isEmpty() ? deepestText : "malformed HTML";
    }
}