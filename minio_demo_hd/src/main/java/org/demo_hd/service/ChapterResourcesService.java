package org.demo_hd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.demo_hd.entity.ChapterResources;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

public interface ChapterResourcesService extends IService<ChapterResources> {
    public boolean save(ChapterResources chapterResources);
}
