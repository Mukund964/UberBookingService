package org.example.uberbookingservice.configurations;

import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.example.uberbookingservice.apis.LocationServiceApi;
import org.example.uberbookingservice.apis.SocketServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetroFitConfig {

    @Autowired
    private EurekaClient eurekaClient;

    private String getServiceUrl(String ServiceName){
        return eurekaClient.getNextServerFromEureka(ServiceName, false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi(){

        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("UBERLOCATIONSERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }

    @Bean
    public SocketServiceApi uberSocketApi(){
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("SOCKETSTARTER"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(SocketServiceApi.class);
    }
}
