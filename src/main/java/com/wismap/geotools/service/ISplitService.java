package com.wismap.geotools.service;

import java.io.IOException;
import java.util.List;

public interface ISplitService {

    String GetUserRegion()throws IOException;

    String SpilitByLines(String lines)throws IOException;

}
