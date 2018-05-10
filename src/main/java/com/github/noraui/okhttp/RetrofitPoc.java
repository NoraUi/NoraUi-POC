package com.github.noraui.okhttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitPoc {

    // http://square.github.io/okhttp/
    // https://api.github.com/search/users?q=location:rennes+language:java&page=1&per_page=1000
    public static void main(String[] args) throws IOException {
        String proxyAdress = null;
        // String proxyAdress = "www-cache-nrs.si.fr.intraorange";

        URL url = new URL("https://api.github.com" + "/search/users?q=location:rennes+language:java&page=1&per_page=1000");
        Builder OkHttpClient = new OkHttpClient.Builder();
        okhttp3.OkHttpClient client;
        if (proxyAdress != null) {
            client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAdress, 3128))).build();
        } else {
            client = new OkHttpClient.Builder().build();
        }
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();

        System.out.println("JSON response is: " + response.body().string());

    }
}
