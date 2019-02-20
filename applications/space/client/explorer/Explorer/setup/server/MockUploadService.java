package server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MockUploadService extends HttpServlet {

    private static final String BASE64 = "base64,";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        documentResponse(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(getRequestParameters(request), response);
    }

    private Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        for (String data : getData(request).split("&")) {
            try {
                parameters.put(URLDecoder.decode(data.split("=")[0], "UTF-8"), URLDecoder.decode(data.split("=")[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return parameters;
    }

    private String getData(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void process(Map<String, String> parameters, HttpServletResponse response) throws IOException {
        String uri = saveFile(parameters);
        sendResponse(response, uri);
    }

    private void documentResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("{\"deprecated\":false,\"hasPendingOperations\":false,\"pages\":{\"3\":{\"id\":3,\"height\":1010,\"width\":714,\"aspectRatio\":0.7069306969642639},\"2\":{\"id\":2,\"height\":1010,\"width\":714,\"aspectRatio\":0.7069306969642639},\"1\":{\"id\":1,\"height\":1010,\"width\":714,\"aspectRatio\":0.7069306969642639},\"4\":{\"id\":4,\"height\":1010,\"width\":714,\"aspectRatio\":0.7069306969642639}},\"numberOfPages\":4,\"documentId\":\"419\",\"estimatedTimeToFinish\":0}");
    }

    private String saveFile(Map<String, String> parameters) throws IOException {
        try (OutputStream stream = new FileOutputStream("/Users/rdiaz/Desktop/" + parameters.get("filename"))) {
            stream.write(getBase64Data(parameters.get("data")));
            stream.close();
        }
        return "/Users/rdiaz/Desktop/" + parameters.get("filename");
    }

    private byte[] getBase64Data(String data) {
        return DatatypeConverter.parseBase64Binary((data.substring(data.indexOf(BASE64) + BASE64.length())));
    }

    private void sendResponse(HttpServletResponse response, String uri) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("{ \"id\": \"" + uri + "\" }");
    }
}
