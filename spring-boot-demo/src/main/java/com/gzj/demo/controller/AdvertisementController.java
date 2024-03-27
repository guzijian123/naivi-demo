package com.gzj.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzj.demo.common.Result;
import com.gzj.demo.domain.DAdvertisement;
import com.gzj.demo.service.DAdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisement")
public class AdvertisementController {


    @Autowired
    private DAdvertisementService advertisementService;

    @PostMapping("/add")
    public Result<?> addAdvertisement(@RequestBody DAdvertisement advertisement) {
        advertisementService.save(advertisement);
        return Result.OK();
    }

    @PutMapping("update")
    public Result<?> updateAdvertisement(@RequestBody DAdvertisement advertisement) {
        advertisementService.updateById(advertisement);
        return Result.OK();
    }

    @DeleteMapping("/delete/{ids}")
    public Result<?> deleteAdvertisement(@PathVariable("ids") List<String> ids) {
        advertisementService.removeByIds(ids);
        return Result.OK();
    }

    @GetMapping("/list")
    public Result<List<DAdvertisement>> listAdvertisement() {
        LambdaQueryWrapper<DAdvertisement> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(DAdvertisement::getSort);
        return Result.OK(advertisementService.list(qw));
    }

    @GetMapping("/{id}")
    public Result<DAdvertisement> listAdvertisement(@PathVariable("id")String id) {
        return Result.OK(advertisementService.getById(id));
    }


}
