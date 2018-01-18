package me.spacegame.databases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * Created by Felix on 18/01/2018.
 */

public class API {

    public static void send(String data) {
        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest().method(Net.HttpMethods.GET).url(data).build();
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse resp) {
                System.out.println("[API] HTTP Response: " + resp.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("[API] HTTP Error");
            }

            @Override
            public void cancelled() {

            }
        });

    }

}
