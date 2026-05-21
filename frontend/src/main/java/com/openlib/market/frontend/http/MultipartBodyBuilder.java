package com.openlib.market.frontend.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Construye manualmente un HttpRequest.BodyPublisher con codificación multipart/form-data
 * conforme a RFC 2046, ya que java.net.HttpClient no tiene soporte nativo.
 */
public class MultipartBodyBuilder {

    private final String boundary = UUID.randomUUID().toString();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    /** Agrega un campo de texto plano. */
    public MultipartBodyBuilder addField(String name, String value) {
        try {
            String part = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" +
                    value + "\r\n";
            output.write(part.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error building multipart field", e);
        }
        return this;
    }

    /** Agrega un archivo binario. */
    public MultipartBodyBuilder addFile(String name, String filename, String contentType, byte[] data) {
        try {
            String header = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n" +
                    "Content-Type: " + contentType + "\r\n\r\n";
            output.write(header.getBytes(StandardCharsets.UTF_8));
            output.write(data);
            output.write("\r\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error building multipart file", e);
        }
        return this;
    }

    /** Retorna el BodyPublisher listo para usar en HttpRequest. */
    public HttpRequest.BodyPublisher build() {
        try {
            String end = "--" + boundary + "--\r\n";
            output.write(end.getBytes(StandardCharsets.UTF_8));
            return HttpRequest.BodyPublishers.ofByteArray(output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error finalizing multipart body", e);
        }
    }

    /** El boundary debe incluirse en el Content-Type header del request. */
    public String getBoundary() {
        return boundary;
    }
}
