import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OllamaConnection {
    public static String run(String prompt,String model) {
        try {
            // URL of Ollama's API endpoint (replace with actual URL if different)
            String urlString = "http://localhost:11434/api/generate"; // Adjust as needed

            // Prepare HTTP request
            HttpClient client = HttpClient.newHttpClient();
            String jsonInputString = "{ \"model\": \""+model+"\",\"prompt\": \""+prompt+"\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return response from Ollama
            return response.body();
        } catch (Exception e) {
            System.out.println("Failed to receive model response!");
            return null;
        }
    }
}
