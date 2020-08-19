package com.wismap.geotools.service.Impl;

import com.wismap.geotools.service.IMergeShp;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCFeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MergeShpImpl implements IMergeShp {

    @Value("${testdata}")
    private String strFolder;

    private SimpleFeatureSource aanp;
    private SimpleFeatureSource agnp;
    private SimpleFeatureSource hyda;
    private SimpleFeatureSource hydl;
    private SimpleFeatureSource hydp;
    private SimpleFeatureSource lrdl;
    private SimpleFeatureSource lrrl;
    private SimpleFeatureSource resa;
    private SimpleFeatureSource resp;

    public MergeShpImpl(SimpleFeatureSource aanp,
                        SimpleFeatureSource agnp,
                        SimpleFeatureSource hyda,
                        SimpleFeatureSource hydl,
                        SimpleFeatureSource hydp,
                        SimpleFeatureSource lrdl,
                        SimpleFeatureSource lrrl,
                        SimpleFeatureSource resa,
                        SimpleFeatureSource resp){
        this.aanp=aanp;
        this.agnp=agnp;
        this.hyda=hyda;
        this.hydl=hydl;
        this.hydp=hydp;
        this.lrdl=lrdl;
        this.lrrl=lrrl;
        this.resa=resa;
        this.resp=resp;
    }

    public Boolean MergeShp() throws IOException
    {
        File folder = new File(strFolder);
        Merge(folder);
        return true;

    }

    private Boolean Merge(File file)throws IOException
    {
        File[] files = file.listFiles();
        for(File shpfile:files) {
            if (shpfile.isDirectory())
                Merge(shpfile);
            if (shpfile.isFile()&&shpfile.getName().substring(shpfile.getName().lastIndexOf(".")+1).equals("shp")) {
                Date dNow = new Date( );
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                System.out.println(ft.toString() + shpfile.getAbsolutePath());
                importToDB(shpfile);
            }
        }
        return true;
    }

    private boolean importToDB(File shpfile)throws IOException
    {
        SimpleFeatureSource targerSource=getTargerSource(shpfile);
        SimpleFeatureSource originSource =getOriginSource(shpfile);

        if (originSource==null)
            return false;

        SimpleFeatureCollection originCollection = originSource.getFeatures();
        SimpleFeatureIterator iterator = originCollection.features();

        List<SimpleFeature> newtFeatures=new ArrayList<>();
        while(iterator.hasNext()) {
            SimpleFeature originFeature = iterator.next();

            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(targerSource.getSchema());

            for (int i=0;i<originFeature.getAttributeCount();i++)
            {
                if (originSource.getSchema().getDescriptor(i).getLocalName().equals("the_geom"))
                {
                    featureBuilder.add(originFeature.getDefaultGeometry());
                }
                else if (originSource.getSchema().getDescriptor(i).getLocalName().toLowerCase().equals("id"))
                {
                }
                else
                {
                    String attributeName = originSource.getSchema().getDescriptor(i).getLocalName().toLowerCase();
                    if (attributeName.equals("objectid"))
                        continue;
                    if (targerSource.getSchema().getDescriptor(attributeName)==null)
                        continue;
                    featureBuilder.set(attributeName,originFeature.getAttribute(i));
                }
            }
            newtFeatures.add(featureBuilder.buildFeature(null));
        }
        iterator.close();

        JDBCFeatureStore jdbcFeatureStore = (JDBCFeatureStore)targerSource;
        Transaction transaction=jdbcFeatureStore.getTransaction();
        jdbcFeatureStore.addFeatures(newtFeatures);
        transaction.commit();
        return true;
    }

    private SimpleFeatureSource getOriginSource(File shpfile) throws IOException
    {
        Map map = new HashMap();
        map.put( "url", shpfile.toURL());
        ShapefileDataStore dataStore = new ShapefileDataStore(shpfile.toURL());
        Charset charset = Charset.forName("GBK");
        dataStore.setCharset(charset);
        SimpleFeatureSource originSource = dataStore.getFeatureSource(shpfile.getName().substring(0,shpfile.getName().lastIndexOf(".")));
        return originSource;
    }

    private SimpleFeatureSource getTargerSource(File shpfile) {
        SimpleFeatureSource targerSource;
        String filename = shpfile.getName().substring(0,shpfile.getName().lastIndexOf("."));
        switch(filename) {
            case "aanp":
                targerSource=aanp;
                break;
            case "agnp":
                targerSource=agnp;
                break;
            case "hyda":
                targerSource=hyda;
                break;
            case "hydl":
                targerSource=hydl;
                break;
            case "hydp":
                targerSource=hydp;
                break;
            case "lrdl":
                targerSource=lrdl;
                break;
            case "lrrl":
                targerSource=lrrl;
                break;
            case "resa":
                targerSource=resa;
                break;
            case "resp":
                targerSource=resp;
                break;
            default:
                targerSource=null;
        }
        return targerSource;
    }


}
