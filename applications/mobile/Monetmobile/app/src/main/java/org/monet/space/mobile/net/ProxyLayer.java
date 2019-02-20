package org.monet.space.mobile.net;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.URLUtil;

import com.google.inject.Inject;

import org.monet.space.mobile.R;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.model.ChatItem;
import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;
import org.monet.mobile.service.Response;
import org.monet.mobile.service.Result;
import org.monet.mobile.service.errors.NotLoggedError;
import org.monet.mobile.service.requests.*;
import org.monet.mobile.service.results.*;
import org.monet.space.mobile.exception.ConnectionException;
import org.monet.space.mobile.exception.SerializeException;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.HttpClientHelper;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.helpers.StreamHelper;
import org.monet.space.mobile.model.Preferences;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class ProxyLayer {

    private static final String SERVICE_URL_SUFIX = "/mobile/api/";
    private static final String DEVICE_ID = "deviceId";

    private Context context;
    private RegistryMatcher matcher;
    private Persister persister;
    private String businessUnitUrl;
    private String accountName;
    private CommonsHttpOAuthConsumer consumer;

    private static class NotLoggedException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public ProxyLayer(Context context, String accountName) throws Exception {
        this.context = context;
        this.accountName = accountName;

        this.createPersister();
        this.initialize();
    }

    @SuppressLint("SimpleDateFormat")
    private void createPersister() {
        this.matcher = new RegistryMatcher();
        this.matcher.bind(Date.class, new Transform<Date>() {
            private SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z");
            private SimpleDateFormat mobileFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

            @Override
            public Date read(String value) throws Exception {
                try {
                    return defaultFormat.parse(value);
                } catch (ParseException exception) {
                    return mobileFormat.parse(value);
                }
            }

            @Override
            public String write(Date value) throws Exception {
                return defaultFormat.format(value);
            }
        });

        this.persister = new Persister(this.matcher);
    }

    private void initialize() throws Exception {
        this.refreshToken();
    }

    private Account getAccount() {
        for (Account account : AccountManager.get(this.context).getAccounts()) {
            if (account.name.equals(this.accountName))
                return account;
        }
        return null;
    }

    private void refreshToken() throws Exception {
        AccountManager accountManager = AccountManager.get(this.context);
        Account account = this.getAccount();

        final String authtoken = accountManager.blockingGetAuthToken(account, FederationAccountAuthenticator.AUTHTOKEN_TYPE, true);
        if (authtoken == null)
            throw new Exception("Null token");

        final String businessUnit = accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT);
        final String secret = context.getString(R.string.secret);

        try {
            String[] nameComponents = account.name.split(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER_REGEX);
            this.businessUnitUrl = URLUtil.guessUrl(nameComponents[1]) + SERVICE_URL_SUFIX;

            this.consumer = new CommonsHttpOAuthConsumer(businessUnit, secret);
            this.consumer.setTokenWithSecret(authtoken, secret);
        } catch (Exception ex) {
            Log.error(ex);
            throw ex;
        }
    }

    public static class SessionContext {

        private BasicHttpContext localContext;

        public SessionContext() {
            CookieStore cookieStore = new BasicCookieStore();
            this.localContext = new BasicHttpContext();
            this.localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T executeAnonymous(Context context, String serviceUrl, Request request, SessionContext sessionContext) throws ConnectionException {
        try {
            String url = calculateUrl(serviceUrl, request);

            ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
            new Persister().write(request, requestOutputStream, "UTF-8");
            StreamHelper.close(requestOutputStream);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new ByteArrayEntity(requestOutputStream.toByteArray()));

            DefaultHttpClient httpClient = HttpClientHelper.setupHttpClient(context);
            HttpResponse httpResponse = sessionContext != null ? httpClient.execute(httpPost, sessionContext.localContext) : httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String reason = httpResponse.getStatusLine().getReasonPhrase();

            if ((statusCode < 200) || (statusCode >= 300))
                throw new ConnectionException(statusCode + " " + reason);

            InputStream responseStream = null;
            Header encodingHeader = entity.getContentEncoding();
            if ((encodingHeader != null) && encodingHeader.getValue().equals("gzip")) {
                responseStream = new GZIPInputStream(entity.getContent());
            } else {
                responseStream = entity.getContent();
            }

            try {
                Response response = new Persister().read(Response.class, responseStream);
                if (response.isError()) {
                    throw new ActionException(response.getError());
                }
                return (T) response.getResult();
            } finally {
                StreamHelper.close(responseStream);
            }
        } catch (Exception e) {
            Log.error("Error: " + e.getMessage());
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private <T extends Result> T execute(ActionCode op, HttpEntity entity, File output, boolean must_retry, String... extraParams) throws ConnectionException {
        try {
	        HttpPost httpRequest = new HttpPost(calculateUrl(op, extraParams));
            if (entity != null)
                httpRequest.setEntity(entity);
            this.consumer.sign(httpRequest);

            DefaultHttpClient httpClient = HttpClientHelper.setupHttpClient(context);
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            return processResponse(httpResponse, output);
        } catch (NotLoggedException n) {
            if (must_retry) {
                try {
                    AccountManager.get(this.context).invalidateAuthToken(FederationAccountAuthenticator.ACCOUNT_TYPE, this.consumer.getToken());
                    this.initialize();
                } catch (Exception e) {
                    throw new ConnectionException(e.getMessage(), e);
                }
                return execute(op, entity, output, false);
            } else {
                throw new ConnectionException("Not connected, retry failed");
            }
        } catch (Exception e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Result> T processResponse(HttpResponse httpResponse, File output) throws Exception {

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String reason = httpResponse.getStatusLine().getReasonPhrase();

        if ((statusCode < 200) || (statusCode >= 300))
            throw new ConnectionException(statusCode + " " + reason);

        HttpEntity responseEntity = httpResponse.getEntity();

        InputStream responseStream = null;

        Header encodingHeader = responseEntity.getContentEncoding();
        if ((encodingHeader != null) && encodingHeader.getValue().equals("gzip")) {
            responseStream = new GZIPInputStream(responseEntity.getContent());
        } else {
            responseStream = responseEntity.getContent();
        }
        try {

            if (output != null) {
                FileOutputStream outputStream = new FileOutputStream(output);
                try {
                    StreamHelper.copyStream(responseStream, outputStream);
                } finally {
                    StreamHelper.close(outputStream);
                }

                String filename = null;
                Header[] headers = httpResponse.getHeaders("Content-Disposition");
                if (headers.length > 0) {
                    filename = headers[0].getValue();
                    if (!filename.contains("filename="))
                        filename = null;
                    else
                        filename = filename.substring(filename.indexOf("filename=") + 9);
                }

                String contentType = responseEntity.getContentType() != null ? responseEntity.getContentType().getValue() : null;
                return (T) new FileResult(output, contentType, filename, responseEntity.getContentLength());

            } else {
                Response response = this.persister.read(Response.class, responseStream);
                if (response.isError()) {
                    if (response.getError() instanceof NotLoggedError) {
                        throw new NotLoggedException();
                    } else {
                        throw new ActionException(response.getError());
                    }
                }
                return (T) response.getResult();
            }
        } finally {
            responseStream.close();
        }
    }

    private static String calculateUrl(String serviceUrl, Request request) {
        return serviceUrl + request.getAction().name() + "/";
    }

    private String calculateUrl(ActionCode op, String... extraParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.businessUnitUrl);
        builder.append(op.name());
        builder.append("/");
        for (String extraParam : extraParams) {
            builder.append(extraParam);
            builder.append("/");
        }
        return builder.toString();
    }

    private HttpEntity toEntity(Request request) throws SerializeException {
        try {
            ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
            this.persister.write(request, requestOutputStream, "UTF-8");
            StreamHelper.close(requestOutputStream);

            return new ByteArrayEntity(requestOutputStream.toByteArray());
        } catch (Exception e) {
            throw new SerializeException("Exception generating httpentity", e);
        }
    }

    public static HeloResult hello(Context context, String serverUrl) throws ConnectionException {
        return executeAnonymous(context, serverUrl + SERVICE_URL_SUFIX, new HeloRequest(), null);
    }

    public static org.monet.mobile.service.federation.Response login(Context context, String authorizationUrl, Map<String, String> parameters) throws NetworkErrorException {
        try {
            DefaultHttpClient client = HttpClientHelper.setupHttpClient(context);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            Preferences preferences = new Preferences(context);

            nameValuePairs.add(new BasicNameValuePair(DEVICE_ID, preferences.cgmToken()));
            for (Entry<String, String> param : parameters.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            HttpPost httpPost = new HttpPost(authorizationUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            String reason = response.getStatusLine().getReasonPhrase();

            if ((statusCode < 200) || (statusCode >= 300)) {
                preferences.setSentTokenToServer(false);
                throw new Exception(statusCode + " : " + reason);
            }

            preferences.setSentTokenToServer(true);

            InputStream contentStream = entity.getContent();
            return (new Persister()).read(org.monet.mobile.service.federation.Response.class, contentStream);
        } catch (Exception e) {
            throw new NetworkErrorException(e.getMessage(), e);
        }
    }

    public PrepareUploadTaskResult prepareUploadTask(File metadataFile) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.PrepareUploadTask, new FileEntity(metadataFile, "application/octect-stream"), null, true);
    }

    public AckResult uploadTaskSchema(String idTask, File dataFile) throws ConnectionException {
        FileEntity fileEntity = null;
        if (dataFile.exists() && (dataFile.length() > 0)) {
            fileEntity = new FileEntity(dataFile, "application/octet-stream");
        }
        return this.execute(ActionCode.UploadTaskSchema, fileEntity, null, true, idTask);
    }

    public AckResult uploadTaskFile(String idTask, File taskFile) throws ConnectionException {
        FileEntity fileEntity = null;
        if (taskFile.exists() && (taskFile.length() > 0)) {
            fileEntity = new FileEntity(taskFile, "application/octet-stream");
        }
        return this.execute(ActionCode.UploadTaskFile, fileEntity, null, true, idTask, taskFile.getName());
    }

    public AckResult uploadTaskPacked(File packageFile) throws ConnectionException {
        return this.execute(ActionCode.UploadTaskPacked, new FileEntity(packageFile, "application/octet-stream"), null, true);
    }

    public DownloadDefinitionsResult loadNewDefinitions(long lastSyncMarker) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadNewDefinitions, this.toEntity(new LoadNewDefinitionsRequest(lastSyncMarker)), null, true);
    }

    public DownloadTasksToDeleteResult loadAssignedTasksToDelete(List<String> taskIds) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadAssignedTasksToDelete, this.toEntity(new LoadAssignedTasksToDeleteRequest(taskIds)), null, true);
    }

    public DownloadTasksToDeleteResult loadFinishedTasksToDelete(ArrayList<String> taskIds) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadFinishedTasksToDelete, this.toEntity(new LoadFinishedTasksToDeleteRequest(taskIds)), null, true);
    }

    public DownloadTasksToDeleteResult loadUnassignedTasksToDelete(List<String> taskIds) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadUnassignedTasksToDelete, this.toEntity(new LoadUnassignedTasksToDeleteRequest(taskIds)), null, true);
    }

    public DownloadTasksResult loadNewAssignedTasks(long lastSyncMarker) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadNewAssignedTasks, this.toEntity(new LoadNewAssignedTasksRequest(lastSyncMarker)), null, true);
    }

    public DownloadTasksResult loadNewAvailableTasks(long lastSyncMarker) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadNewAvailableTasks, this.toEntity(new LoadNewAvailableTasksRequest(lastSyncMarker)), null, true);
    }

    public FileResult downloadTask(String jobServerId) throws ConnectionException, SerializeException, IOException {
        return this.execute(ActionCode.DownloadTaskPacked, this.toEntity(new DownloadTaskPackedRequest(jobServerId)), LocalStorage.createTempFile(this.context), true);
    }

    public AckResult assignTask(String taskId) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.AssignTask, this.toEntity(new AssignTaskRequest(taskId)), null, true);
    }

    public AckResult unassignTask(String taskId) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.UnassignTask, this.toEntity(new UnassignTaskRequest(taskId)), null, true);
    }

    public DownloadGlossariesResult loadNewGlossaries(long lastSyncMarker) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.LoadNewGlossaries, this.toEntity(new LoadNewGlossariesRequest(lastSyncMarker)), null, true);
    }

    public FileResult downloadGlossary(String glossaryCode, String glossaryContext, long lastSyncMarker) throws ConnectionException, SerializeException, IOException {
        return this.execute(ActionCode.DownloadGlossary, this.toEntity(new DownloadGlossaryRequest(glossaryCode, glossaryContext, lastSyncMarker)), LocalStorage.createTempFile(this.context), true);
    }

    public SyncChatsResult syncChats(List<ChatItem> chatItems, long lastSyncMarker) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.SyncChats, this.toEntity(new SyncChatsRequest(chatItems, lastSyncMarker)), null, true);
    }

    public AckResult registerDeviceId(String registrationId) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.Register, this.toEntity(new RegisterRequest(registrationId)), null, true);
    }

    public AckResult unregisterDeviceId(String registrationId) throws ConnectionException, SerializeException {
        return this.execute(ActionCode.Unregister, this.toEntity(new UnregisterRequest(registrationId)), null, true);
    }


}
