package com.wismap.geotools.service.Impl;

import com.wismap.geotools.service.ISplitService;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.process.geometry.GeometryFunctions;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SplitServiceImpl implements ISplitService {

    private DataStore dataStore;
    private SimpleFeatureSource CityFeatureSource;

    public SplitServiceImpl(DataStore dataStore,SimpleFeatureSource CityFeatureSource)
    {
        this.dataStore=dataStore;
        this.CityFeatureSource= CityFeatureSource;
    }

    public String GetUserRegion()throws IOException
    {
        SimpleFeature sf=GetUserRegionFeature();
        FeatureJSON fjson = new FeatureJSON();
        StringWriter writer = new StringWriter();
        fjson.writeFeature(sf, writer);

        return writer.toString();
    }

    public SimpleFeature GetUserRegionFeature() throws IOException
    {
        SimpleFeatureCollection result = CityFeatureSource.getFeatures();
        FeatureIterator<SimpleFeature> itertor = result.features();
        ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            featureList.add(feature);
        }
        itertor.close();
        return featureList.get(0);
    }

    public String SpilitByLines(String lines)throws IOException
    {
        //切割线
        GeometryJSON fjson = new GeometryJSON();
        Reader reader = new StringReader(lines);
        GeometryCollection Lines = fjson.readGeometryCollection(reader);

        //行政区为多面
        SimpleFeature originRegion = GetUserRegionFeature();
        Geometry originReginGeometry = (Geometry)originRegion.getDefaultGeometry();

        //暂时假定他是MultiPolygon
        //if (originReginGeometry.getGeometryType()=="MultiPolygon")
        List<Geometry> resultPolygons=new ArrayList<>();
        resultPolygons.add(originReginGeometry);

        //遍历线，一个线如果切割完了，应该和其他的面不再存在相交关系
        for (int i=0;i<Lines.getNumGeometries();i++)
        {
            List<Geometry> temerotory =new ArrayList<>();

            for (Geometry polygon:resultPolygons)
            {
                Geometry result = GeometryFunctions.splitPolygon(polygon,(LineString) Lines.getGeometryN(i));
                GeometryCollection geometryCollection=((GeometryCollection)result);

                for (int j=0;j<geometryCollection.getNumGeometries();j++)
                {
                    temerotory.add(geometryCollection.getGeometryN(j));
                }
            }
            resultPolygons=temerotory;
        }

        Geometry[] geometries = resultPolygons.toArray(new Geometry[resultPolygons.size()]);

        GeometryFactory factory = JTSFactoryFinder.getGeometryFactory( null );


        GeometryCollection resultcollection = new GeometryCollection(geometries, factory);

        GeometryJSON geometryJSON=new GeometryJSON();
        StringWriter writer = new StringWriter();
        geometryJSON.write(resultcollection,writer);


         return writer.toString();
    }

}
