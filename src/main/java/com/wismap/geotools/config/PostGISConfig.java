package com.wismap.geotools.config;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PostGISConfig {

    private DataStore dataStore;

    @Bean
    public DataStore CreateDataStore()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "postgis");
        params.put("host", "localhost");
        params.put("port", 5432);
        params.put("schema", "public");
        params.put("database", "postgis");
        params.put("user", "postgis");
        params.put("passwd", "wismap123");
        try{
            this.dataStore=DataStoreFinder.getDataStore(params);
            return this.dataStore;
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    public SimpleFeatureSource CityFeatureSource()
    {
        try {
            return dataStore.getFeatureSource("COUA");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

}
