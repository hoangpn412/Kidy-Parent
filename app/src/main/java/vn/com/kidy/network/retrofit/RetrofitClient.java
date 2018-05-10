package vn.com.kidy.network.retrofit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Family on 4/22/2017.
 */

public class RetrofitClient {

    private RetrofitService retrofitService;

    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    public static final String READ_TIMEOUT = "READ_TIMEOUT";
    public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";


    public RetrofitClient(String baseUrl) {
        initRetrofit(baseUrl);
    }

    private void initRetrofit(String baseUrl) {
        Retrofit retrofit = retrofitBuilder(baseUrl);
        retrofitService = retrofit.create(getRetrofitServiceClass());
    }

    private Retrofit retrofitBuilder(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor).build();

        OkHttpClient client = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder())
                .addInterceptor(interceptor)
                .build();


        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private Class<RetrofitService> getRetrofitServiceClass() {
        return RetrofitService.class;
    }

    protected RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
