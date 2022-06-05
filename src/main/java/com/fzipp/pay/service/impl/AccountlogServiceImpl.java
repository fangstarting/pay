package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.entity.Accountlog;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.mapper.AccountlogMapper;
import com.fzipp.pay.service.AccountlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class AccountlogServiceImpl extends ServiceImpl<AccountlogMapper, Accountlog> implements AccountlogService {
}
