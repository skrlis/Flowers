package com.lm.flowershop.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Period;
import java.util.Date;

/**
 * @author lm
 * @create 2022-03-23 11:01
 */
@Component
public class TokenUtils {
    private static IManagerService staticManagerService;
    @Resource
    private IManagerService managerService;

    @PostConstruct
    public void setManagerService(){
        staticManagerService=managerService;
    }
    //生成token
    public static String genToken(String adid,String sign){
        return JWT.create().withAudience(adid) // 将 adid 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //两小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 adpwd 作为 token 的密钥
    }
    /**
     * 获取当前登录的用户信息
     * 静态方法不能调用非静态成员变量
     */
    public static Manager getCurrentManager() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                    String adid = JWT.decode(token).getAudience().get(0);
                    return staticManagerService.getById(Integer.valueOf(adid));
                }
            }catch(Exception e){
            return null;
        }
        return null;
    }
}