package org.example.teacherservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.teacherservice.entity.ChapterResources;

public interface ChapterResourcesService extends IService<ChapterResources> {
    public boolean save(ChapterResources chapterResources);
}