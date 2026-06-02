import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

public class TestApi {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        // 1. Login to get token and userId
        String loginJson = "{\"email\":\"vendedor_urgente@openlib.com\", \"password\":\"Password123\"}";
        HttpRequest loginReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(loginJson))
            .build();
            
        HttpResponse<String> loginResp = client.send(loginReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("Login Response: " + loginResp.statusCode() + " " + loginResp.body());
        
        if (loginResp.statusCode() != 200) return;
        
        // Parse basic json
        String body = loginResp.body();
        String token = body.split("\"token\":\"")[1].split("\"")[0];
        String userId = body.split("\"userId\":\"")[1].split("\"")[0];
        System.out.println("Token: " + token);
        System.out.println("UserId: " + userId);
        
        // 2. Publish Book
        String boundary = "----TestBoundary123";
        String isbn = "978-" + UUID.randomUUID().toString().substring(0, 8);
        String multipartBody = 
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"titulo\"\r\n\r\n" +
            "API Test Book\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"isbn\"\r\n\r\n" +
            isbn + "\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"stock\"\r\n\r\n" +
            "50\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"precio\"\r\n\r\n" +
            "12.5\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"descripcion\"\r\n\r\n" +
            "Desc\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"categoria\"\r\n\r\n" +
            "FICCION\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"autor\"\r\n\r\n" +
            "Test Author\r\n" +
            "--" + boundary + "--\r\n";
            
        HttpRequest pubReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/vendedores/" + userId + "/libros"))
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .header("Authorization", "Bearer " + token)
            .POST(HttpRequest.BodyPublishers.ofString(multipartBody))
            .build();
            
        HttpResponse<String> pubResp = client.send(pubReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("Publish Response: " + pubResp.statusCode() + " " + pubResp.body());
        
        // 3. Get Inventory
        HttpRequest invReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/vendedores/" + userId + "/libros"))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
            
        HttpResponse<String> invResp = client.send(invReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("Inventory Response: " + invResp.statusCode() + " " + invResp.body());

        // 4. Add to Cart
        String cartBody = "{\"libroIsbn\":\"" + isbn + "\", \"cantidad\": 1}";
        HttpRequest cartReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/carrito/usuarios/" + userId + "/carrito/items"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .POST(HttpRequest.BodyPublishers.ofString(cartBody))
            .build();
            
        HttpResponse<String> cartResp = client.send(cartReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("Cart Add Response: " + cartResp.statusCode() + " " + cartResp.body());
        
        // 5. Get Cart
        HttpRequest getCartReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/carrito/usuarios/" + userId + "/carrito"))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
            
        HttpResponse<String> getCartResp = client.send(getCartReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get Cart Response: " + getCartResp.statusCode() + " " + getCartResp.body());
    }
}
