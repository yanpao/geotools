package com.wismap.geotools;

import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.process.geometry.GeometryFunctions;
import org.geotools.process.vector.ClipProcess;
import org.locationtech.jts.geom.*;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
        params.put("database", "mygis");
        params.put("user", "mygis");
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

            SimpleFeature sf = featureList.get(0);
            MultiPolygon geom=(MultiPolygon)sf.getDefaultGeometry();
            Geometry polygon=geom.getGeometryN(0);

            SimpleFeatureSource featureSource2 =dataStore.getFeatureSource("clipline");
            SimpleFeatureCollection result2 = featureSource2.getFeatures();
            FeatureIterator<SimpleFeature> itertor2 = result2.features();
            DefaultFeatureCollection featureList2 = new DefaultFeatureCollection();
            SimpleFeature featuressss=itertor2.next();
            MultiLineString line = (MultiLineString)featuressss.getDefaultGeometry();

            Geometry resultss = GeometryFunctions.splitPolygon(polygon,(LineString) line.getGeometryN(0));
            FeatureStore featureStore=(FeatureStore)dataStore.getFeatureSource("COUA3");


            Transaction session = new DefaultTransaction("Adding");
            featureStore.setTransaction( session );
            DefaultFeatureCollection result2222=new DefaultFeatureCollection();

            GeometryCollection geometryCollection=((GeometryCollection)resultss);
            for (int i=0;i<geometryCollection.getNumGeometries();i++)
            {
                SimpleFeatureType TYPE = dataStore.getFeatureSource("COUA3").getSchema();
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                featureBuilder.add(geometryCollection.getGeometryN(i));
                SimpleFeature feature = featureBuilder.buildFeature(null);
                result2222.add(feature);

            }

            featureStore.addFeatures(result2222);
            session.commit();
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }

        SpringApplication.run(GeotoolApplication.class, args);
    }

    public static SimpleFeatureCollection getLines(DataStore dataStore) throws Exception
    {
        SimpleFeatureSource featureSource =dataStore.getFeatureSource("LRRL");
        SimpleFeatureCollection result = featureSource.getFeatures();
        FeatureIterator<SimpleFeature> itertor = result.features();
        DefaultFeatureCollection featureList = new DefaultFeatureCollection();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            featureList.add(feature);
        }
        itertor.close();
        return featureList;
    }

}
