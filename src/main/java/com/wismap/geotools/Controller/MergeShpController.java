package com.wismap.geotools.Controller;

import com.wismap.geotools.service.IMergeShp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/merge",produces = "application/json")
public class MergeShpController {

    private IMergeShp mergeShp;

    public MergeShpController(IMergeShp mergeShp)
    {
        this.mergeShp=mergeShp;
    }

    @GetMapping("/merge")
    public void Merge()
    {
        mergeShp.MergeShp();
    }

}
