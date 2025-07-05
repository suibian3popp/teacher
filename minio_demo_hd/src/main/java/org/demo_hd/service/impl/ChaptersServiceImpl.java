package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.demo_hd.entity.Chapters;
import org.demo_hd.service.ChaptersService;
import org.demo_hd.mapper.ChaptersMapper;
import org.springframework.stereotype.Service;

/**
* @author tang_
* @description 针对表【chapters(课程章节表)】的数据库操作Service实现
* @createDate 2025-07-05 11:02:48
*/
@Service
public class ChaptersServiceImpl extends ServiceImpl<ChaptersMapper, Chapters>
    implements ChaptersService{

}




