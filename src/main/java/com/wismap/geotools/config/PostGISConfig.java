package com.wismap.geotools.config;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PostGISConfig {

    private DataStore dataStore;

    @Value("${database.host}")
    private String host;
    @Value("${database.port}")
    private int port;
    @Value("${database.instance}")
    private String instance;
    @Value("${database.user}")
    private String user;
    @Value("${database.pwd}")
    private String pwd;

    @Bean
    public DataStore CreateDataStore()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "postgis");
        params.put("host", host);
        params.put("port", port);
        params.put("schema", "public");
        params.put("database", instance);
        params.put("user", user);
        params.put("passwd", pwd);
        params.put( "max connections", 25);
        params.put( "min connections", 10);
        params.put( "connection timeout", 5);
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
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource aanp()
    {
        try {
            return dataStore.getFeatureSource("aanp");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource agnp()
    {
        try {
            return dataStore.getFeatureSource("agnp");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource hyda()
    {
        try {
            return dataStore.getFeatureSource("hyda");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource hydl()
    {
        try {
            return dataStore.getFeatureSource("hydl");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource hydp()
    {
        try {
            return dataStore.getFeatureSource("hydp");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource lrdl()
    {
        try {
            return dataStore.getFeatureSource("lrdl");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource lrrl()
    {
        try {
            return dataStore.getFeatureSource("lrrl");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource resa()
    {
        try {
            return dataStore.getFeatureSource("resa");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

    @Bean
    @DependsOn("CreateDataStore")
    public SimpleFeatureSource resp()
    {
        try {
            return dataStore.getFeatureSource("resp");
        }
        catch (IOException excption)
        {
            System.out.print(excption.getMessage());
            return null;
        }
    }

}
