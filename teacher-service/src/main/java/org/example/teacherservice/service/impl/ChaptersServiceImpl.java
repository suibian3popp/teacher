package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.Chapters;
import org.example.teacherservice.mapper.ChaptersMapper;
import org.example.teacherservice.service.ChaptersService;
import org.springframework.stereotype.Service;

/**
 * @author tang_
 * @description 针对表【chapters(课程章节表)】的数据库操作Service实现
 * @createDate 2025-07-05 11:02:48
 */
@Service
public class ChaptersServiceImpl extends ServiceImpl<ChaptersMapper, Chapters>
        implements ChaptersService {

}
