package com.mua.mobileattendance.manager;


import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mua.mobileattendance.BuildConfig;
import com.mua.mobileattendance.exception.RetrofitNoConnectivityException;
import com.mua.mobileattendance.util.NetworkUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.tls.Certificates;
//import okhttp3.tls.*;
//import okhttp3.tls.HandshakeCertificates;

public class RetrofitManager {

    public static final String TAG = "retrofit-manager";
    private static final boolean logEnabled = BuildConfig.DEBUG;
    //private static final boolean logEnabled = true;

    public static Retrofit newInstance(final Context context) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        String baseUrl = ServiceManager.BASE_URL;
        baseUrl = baseUrl.endsWith("/")
                ? baseUrl
                : baseUrl + "/";

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));
        /*
        HandshakeCertificates certificates = new HandshakeCertificates.Builder()
                .addTrustedCertificate(letsEncryptCertificateAuthority)
                .addTrustedCertificate(entrustRootCertificateAuthority)
                .addTrustedCertificate(comodoRsaCertificationAuthority)
                .addPlatformTrustedCertificates()
                .build();
        */


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2 * 60, TimeUnit.SECONDS);
        httpClient.readTimeout(5 * 60, TimeUnit.SECONDS);
        httpClient.writeTimeout(5 * 60, TimeUnit.SECONDS);

        httpClient.addInterceptor(
                new ConnectivityInterceptor(Observable.just(NetworkUtil.isNetworkConnected(context))));

        httpClient.addInterceptor(chain -> {
            Request request
                    = chain.request().newBuilder().addHeader(
                    AuthenticationManager.AUTH_HEADER,
                    AuthenticationManager.getAuthKey(context)).build();
            return chain.proceed(request);
        });

        if (logEnabled) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                    message -> Log.d(TAG, message)
            );
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(loggingInterceptor);
        }

        retrofitBuilder.client(httpClient.build());

        return retrofitBuilder.build();
    }

    static class ConnectivityInterceptor implements Interceptor {

        private boolean isNetworkActive;

        ConnectivityInterceptor(Observable<Boolean> isNetworkActive) {
            isNetworkActive.subscribe(
                    _isNetworkActive -> this.isNetworkActive = _isNetworkActive,
                    _error -> Log.e(TAG, "NetworkActive error " + _error.getMessage())
            );
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            if (!isNetworkActive) {
                throw new RetrofitNoConnectivityException();
            } else {
                return chain.proceed(chain.request());
            }
        }
    }


}
