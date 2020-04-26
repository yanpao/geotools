package com.wismap.geotools;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GeotoolApplication {

    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "postgis");
        params.put("host", "localhost");
        params.put("port", 5432);
        params.put("schema", "public");
        params.put("database", "postgis");
        params.put("user", "postgis");
        params.put("passwd", "wismap123");

        try {
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            SimpleFeatureSource featureSource =dataStore.getFeatureSource("COUA");
            SimpleFeatureCollection result = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> itertor = result.features();
            ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();
            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                featureList.add(feature);
            }
            itertor.close();
            //计算得到多边形面积
            SimpleFeature sf = featureList.get(0);
            MultiPolygon geom=(MultiPolygon)sf.getDefaultGeometry();
            System.out.println(geom.getArea());
        }
        catch (Exception ex)
        {

        }

        SpringApplication.run(GeotoolApplication.class, args);
    }

}
