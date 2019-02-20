package org.monet.api.space.setupservice.impl.library;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class LibraryRestfull {

    public static class RequestParameter implements Entry<String, ContentBody> {

        private String key;
        private ContentBody value;

        public RequestParameter(String key, ContentBody value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public ContentBody getValue() {
            return value;
        }

        @Override
        public ContentBody setValue(ContentBody value) {
            this.value = value;
            return value;
        }

    }

    private static String getReaderContent(Reader reader) throws IOException {
        StringBuffer content = new StringBuffer();
        BufferedReader bufferedReader;
        String line;

        bufferedReader = new BufferedReader(reader);
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + "\n");
        }

        return content.toString();
    }

    public static String execute(String url, HashMap<String, ContentBody> parameters, String certificateFilename, String certificatePassword) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        StringBuilder requestArgsBuilder = new StringBuilder();
        HttpResponse response;

        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (Entry<String, ContentBody> param : parameters.entrySet()) {
            entityBuilder.addPart(param.getKey(), param.getValue());

            if (param.getValue() instanceof StringBody) {
                if (param.getKey().equals("op")) continue;
                requestArgsBuilder.append(param.getKey());
                requestArgsBuilder.append("=");
                requestArgsBuilder.append(LibraryRestfull.getReaderContent(((StringBody) param.getValue()).getReader()).trim());
                requestArgsBuilder.append("&");
            }
        }

        String timestamp = String.valueOf((new Date()).getTime());
        entityBuilder.addPart("timestamp", new StringBody(timestamp));

        requestArgsBuilder.append("timestamp=");
        requestArgsBuilder.append(timestamp);

        String signature = LibrarySigner.signText(requestArgsBuilder.toString(), certificateFilename, certificatePassword);
        entityBuilder.addPart("signature", new StringBody(signature));

        post.setEntity(entityBuilder.build());

        response = client.execute(post);

        int status = response.getStatusLine().getStatusCode();

        if (status < 200 || status >= 300)
            throw new HttpException(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");

        return LibraryRestfull.getReaderContent(reader).trim();
    }

}
