package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.entity.Auditlog;
import com.fzipp.pay.mapper.AuditlogMapper;
import com.fzipp.pay.service.AuditlogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class AuditlogServiceImpl extends ServiceImpl<AuditlogMapper, Auditlog> implements AuditlogService {

}
