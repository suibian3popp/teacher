package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.demo_hd.entity.ChapterResources;
import org.demo_hd.service.ChapterResourcesService;
import org.demo_hd.mapper.ChapterResourcesMapper;
import org.springframework.stereotype.Service;

@Service
public class ChapterResourcesServiceImpl extends ServiceImpl<ChapterResourcesMapper, ChapterResources>
    implements ChapterResourcesService{

    //关联前校验唯一性
    @Override
    public boolean save(ChapterResources chapterResources) {
        //校验同一章节同一资源是否已关联
        ChapterResources exist = this.getOne(
                new QueryWrapper<ChapterResources>()
                        .eq("chapter_id", chapterResources.getChapterId())
                        .eq("resource_id", chapterResources.getResourceId())
        );
        if (exist != null) {
            return false; //已关联，不重复保存
        }
        return super.save(chapterResources);
    }
}




