package com.king.wanandroid.api;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Timber.i(String.format("%1$s->%2$s",request.method(),request.url()));
        if(request.headers()!=null){
            Timber.i("Headers:" + request.headers());
        }
        if(request.body()!=null){
            Timber.i("RequestBody:" + bodyToString(request.body()));
        }

        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String responseBody = response.body().string();
        Timber.d("ResponseBody:" + responseBody);

        return response.newBuilder()
                .body(ResponseBody.create(mediaType, responseBody))
                .build();
    }

    private String bodyToString(final RequestBody request) {
        if(request!=null){
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                Timber.e(e,"Did not work.");
            }
        }
        return null;
    }
}