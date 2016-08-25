package com.hw.chineseLearn.interfaces;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求
 * 
 * @author
 */
public class RestClient {

    private Gson gson = null;
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;
    private HttpResponse httpResponse;

    public String httpurl;

    public static CookieStore cookieStore;

    public String getHttpurl() {
        return httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }

    public String getResponse() {
        // if (gson == null) {
        // gson = new Gson();
        // }
        // SimpleModel model = gson.fromJson(response, SimpleModel.class);
        // if (model == null) {
        // return response;
        // }
        // String status = model.getStatus();
        // Log.e("==RestClient==", "=========status========" + status);
        // if (!"notLogin".equals(status)) {
        // return response;
        // }
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public enum RequestMethod {
        GET, POST, PUT, DELETE, POSTFILE
    }

    public RestClient(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void AddParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    private void executeRequest(HttpUriRequest request, String url)
            throws UnknownHostException, ConnectTimeoutException {

        DefaultHttpClient client = new DefaultHttpClient();
        // Represents a collection of HTTP protocol and framework parameters
        HttpParams params = null;
        params = client.getParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);//
        // set timeout
        /*
         * HttpConnectionParams.setConnectionTimeout(params, 5000);
         * HttpConnectionParams.setSoTimeout(params, 5000);
         */

        // // 创建一个本地Cookie存储的实例
        // BasicCookieStore cookieStore = new BasicCookieStore();
        // //创建一个本地上下文信息
        // HttpContext localContext = new BasicHttpContext();
        // //在本地上下问中绑定一个本地存储
        // localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        try {
            try {
                if (cookieStore != null) {
                    client.setCookieStore(cookieStore);
                }
                httpResponse = client.execute(request);
                // httpResponse = client.execute(request, localContext);
            } catch (ConnectTimeoutException e) {
                throw e;
            }
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();
            if (cookieStore == null) {
                cookieStore = client.getCookieStore();
            }
            // 获取cookie中的各种信息
            List<Cookie> cookies = cookieStore.getCookies();
            if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    String value = cookie.getValue();

                    if (name.equals("SHAREJSESSIONID")) {
                        Log.d("liupeng:", value);
                    }
                    Log.d("==-cookie-==", name + "-" + value);
                    // String[] nvp = cookies.get(0).split("=");
                    // BasicClientCookie c = new BasicClientCookie(nvp[0],
                    // nvp[1]);

                    // BasicClientCookie c = new BasicClientCookie(name,
                    // value);
                    // c.setDomain(c.getDomain());
                    // c.setPath("/");
                    // cookieStore.addCookie(c);
                }
            }
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                // Closing the input stream will trigger connection release
                instream.close();
            }
        } catch (SocketTimeoutException e) {
//            CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            client.getConnectionManager().shutdown();
//            CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
            throw e;
        } catch (ClientProtocolException e) {
            client.getConnectionManager().shutdown();
//            CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
//            CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
            e.printStackTrace();
        } catch (Exception e) {
            client.getConnectionManager().shutdown();
//            CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
            e.printStackTrace();
        }

    }

    public static String convertStreamToString(InputStream is)
            throws SocketTimeoutException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void Execute(RequestMethod method) throws UnknownHostException,
            Exception {
        String combinedParams = "";
        switch (method) {
        case GET:
            // add parameters
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            try {
                HttpGet request_get = new HttpGet(url + combinedParams);
                Log.d("==Execute-url==", url + combinedParams);
                setHttpurl(url + combinedParams);
                // add headers
                for (NameValuePair h : headers) {
                    request_get.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request_get, url);
            } catch (Exception e) {
//                CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
                e.printStackTrace();
            }
            break;

        case POST:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), HTTP.UTF_8);// "UTF-8"
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            try {
                HttpPost request_post = new HttpPost(url);// + combinedParams

                // StringEntity entity = new StringEntity(param);
                // entity.setContentType("application/x-www-form-urlencoded");
                // request_post.setEntity(entity);

                // request_post.addHeader("Authorization", "your token");
                // //认证token
                // request_post.addHeader("Content-Type",
                // "text/html;charset=utf-8");
                // request_post.addHeader("User-Agent",
                // "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
                // request_post.addHeader("User-Agent", "imgfornote");
                // request_post.addHeader("Charset", "UTF-8");

                setHttpurl(url);// + combinedParams
                // add headers
                for (NameValuePair h : headers) {
                    request_post.addHeader(h.getName(), h.getValue());
                }

                if (!params.isEmpty()) {
                    // StringEntity entity = new StringEntity(params.toString(),
                    // HTTP.UTF_8);
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            params, HTTP.UTF_8);
                    request_post.setEntity(entity);
                }

                executeRequest(request_post, url);
            } catch (Exception e) {
//                CustomApplication.app.showTimeoutMsg("网络超时，请重试！");
                e.printStackTrace();
            }
            break;

        case DELETE:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpDelete request_delete = new HttpDelete(url + combinedParams);
            setHttpurl(url + combinedParams);
            // add headers
            for (NameValuePair h : headers) {
                request_delete.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request_delete, url);

            break;

        case PUT:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpPut request_put = new HttpPut(url + combinedParams);
            setHttpurl(url + combinedParams);
            // add headers
            for (NameValuePair h : headers) {
                request_put.addHeader(h.getName(), h.getValue());
            }

            executeRequest(request_put, url);

            break;
        case POSTFILE:
            HttpPost file_post = new HttpPost(url);
            String filepath = "";
            String type = "";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置游览器兼容模式
            if (!params.isEmpty()) {
                for (NameValuePair p : params) {
                    if (p.getName().equals("type")) {
                        type = p.getValue();
                    } else if (p.getName().equals("filePath")) {// 图像本地地址
                        filepath = p.getValue();
                    }
                }

                for (NameValuePair p : params) {
                    builder.addTextBody(p.getName(),
                            URLEncoder.encode(p.getValue(), "UTF-8"));
                }

                file_post.setEntity(builder.build());
            }

            setHttpurl(url);
            // add headers
            for (NameValuePair h : headers) {
                file_post.addHeader(h.getName(), h.getValue());
            }

            executeRequest(file_post, url);
            break;
        }

    }

    public void Execute_url(RequestMethod method) throws UnknownHostException,
            Exception {
        String combinedParams = "";
        switch (method) {
        case GET:
            // add parameters
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request_get = new HttpGet(url + combinedParams);
            Log.d("--url--", url + combinedParams);
            setHttpurl(url + combinedParams);
            break;

        case POST:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpPost request_post = new HttpPost(url + combinedParams);
            setHttpurl(url + combinedParams);
            break;

        case DELETE:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpDelete request_delete = new HttpDelete(url + combinedParams);
            setHttpurl(url + combinedParams);
            break;

        case PUT:
            combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";
                for (NameValuePair p : params) {
                    String paramString = p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1) {
                        combinedParams += "&" + paramString;
                    } else {
                        combinedParams += paramString;
                    }
                }
            }

            HttpPut request_put = new HttpPut(url + combinedParams);
            setHttpurl(url + combinedParams);
            break;

        }
    }

    /**
     * 清除cookie
     */
    public static void clearCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

}