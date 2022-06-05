package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.entity.SysConfigs;
import com.fzipp.pay.mapper.SysConfigsMapper;
import com.fzipp.pay.service.SysConfigsService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigsServiceImpl extends ServiceImpl<SysConfigsMapper,SysConfigs> implements SysConfigsService {
}
