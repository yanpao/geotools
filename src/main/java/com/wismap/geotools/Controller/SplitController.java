package com.wismap.geotools.Controller;

import com.wismap.geotools.service.ISplitService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping(value = "split",produces = "application/json")
public class SplitController {

    private ISplitService splitService;

    public SplitController(ISplitService splitService)
    {
        this.splitService=splitService;
    }

    @GetMapping("myregion")
    public String getMyRegion() throws IOException
    {
        return splitService.GetUserRegion();
    }

    @PostMapping("/splitbyline")
    public  String Splitbyline(String linecollection)throws IOException
    {
        return splitService.SpilitByLines(linecollection);
    }

}
