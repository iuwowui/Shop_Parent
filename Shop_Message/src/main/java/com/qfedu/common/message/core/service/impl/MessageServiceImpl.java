package com.qfedu.common.message.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qfedu.common.config.ProjectConfig;
import com.qfedu.common.message.core.dao.MessageLogMapper;
import com.qfedu.common.message.core.dao.MessageMapper;
import com.qfedu.common.message.core.dao.MsgReceiveMapper;
import com.qfedu.common.message.core.entity.Message;
import com.qfedu.common.message.core.entity.MessageLog;
import com.qfedu.common.message.core.entity.MsgReceive;
import com.qfedu.common.message.core.service.MessageService;
import com.qfedu.common.model.ActiveCode;
import com.qfedu.common.model.EmailMsg;
import com.qfedu.common.util.*;

import com.qfedu.common.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageLogMapper messageLogMapper;
    @Autowired
    private MsgReceiveMapper msgReceiveMapper;
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional
    public R sendMsg(Message message,String ip) {
        int count = 0;
        if (message.getType() < 4){
            // 短信的话 需要校验 一个手机号一分钟只能发一次
            // 一个手机号一天只能发20条
            String c = jedisUtil.get(ProjectConfig.SMSPREDAY + message.getReceive());
            if (c != null && c.matches("[0-9],{1,2}")){
                count = Integer.parseInt(c);
                if (count >= 20){
                    return R.setERROR("亲，一个手机号一天只能发送指定数量的短信");
                }
            }
            // 一个手机号 一分钟只能发送一次
            if (jedisUtil.exists(ProjectConfig.SMSPRELIMIT + message.getReceive())){
                // 存在，说明一分钟发送过短信
                return R.setERROR("一个手机号一分钟之内只能发送一次短信");
            }
        }
        // 检查信息是否可以发送
        int code = CodeUtil.createNum(6);
        int flag = 3;
        String info = "";
        boolean isFlag = false;
        try {
            //发送信息
            switch (message.getType()){
                case 1: // 短信验证码
                    if (jedisUtil.exists(ProjectConfig.SMSCODE + message.getReceive())){
                        // 上次的验证码还没失效
                        code = Integer.parseInt(jedisUtil.get(ProjectConfig.SMSCODE + message.getReceive()));
                    } else {
                        // 生成验证码
                        code = CodeUtil.createNum(6);
                        isFlag = true;
                    }
                    info = "发送短信验证码：" + code;
                    // 发送短信验证码
                    SmsUtil.mobileQuery(message.getReceive(),code);
                    flag = 1;  // 发送成功
                    // 一分钟限定
                    // 可以发送
                    jedisUtil.setex(ProjectConfig.SMSPRELIMIT + message.getReceive(),60,"短信发送限制");
                    // 一天
                    jedisUtil.setex(ProjectConfig.SMSPREDAY + message.getReceive(),TimeUtil.getLastSeconds(),(count+1) + "");
                    if (isFlag){
                        // 记录验证码
                        jedisUtil.setex(ProjectConfig.SMSCODE + message.getReceive(),180,code+"");
                    }
                break;
                case 4: // 邮箱激活码
                    info = "发送邮箱激活码：" + code;
                    EmailMsg emailMsg = new EmailMsg();
                    emailMsg.setCompany(ProjectConfig.projects.get(message.getPcode()));
                    ActiveCode code1 = new ActiveCode();
                    code1.setEmail(message.getReceive());
                    code1.setCode(code);
                    String mw = EncryptionUtil.AESEnc(ProjectConfig.AESKEYACTIVECODE,JSON.toJSONString(code1));
//                    String u = ProjectConfig.ACTIVEURL + "?email=" + emailMsg.getEmail() + "&code="+code;
                    String u = ProjectConfig.ACTIVEURL + "?data=" + mw + "&email=" + EncryptionUtil.AESEnc(ProjectConfig.AESKEYACTIVECODE,"aaaaaaaaaa");  // 后面是干扰的邮箱
                    emailMsg.setContent("欢迎注册：" + emailMsg.getCompany() + ",请激活使用，<a href=''></a>");
                    EmailUtil.sendEmail(emailMsg);
                    flag = 1; // 发送成功
                    break;
            }
        }catch (Exception e){
            flag = 2;  // 发送失败
        }finally {
            // 记录消息发送信息
            messageMapper.insert(message);
            // 记录日志
            MessageLog log = new MessageLog();
            log.setMid(message.getId());
            log.setInfo(info);
            log.setIp(ip);
            messageLogMapper.insert(log);
            // 记录收件人
            MsgReceive receive = new MsgReceive();
            receive.setNo(message.getReceive());
            receive.setFlag(message.getType() < 4?1:2);
            msgReceiveMapper.insert(receive);
        }
        // 记录到数据库
        return null;
    }

    @Override
    public R page(int page, int count) {
        PageHelper.startPage(page,count);
        PageInfo<Message> pageInfo = new PageInfo<Message>();
        return R.setOK("OK",pageInfo);
    }

    @Override
    public R checkCode(String phone, int code) {
        String key = ProjectConfig.SMSCODE + phone;
        if (jedisUtil.exists(key)){
            String v = jedisUtil.get(key);
            if (v != null){
                if (Integer.parseInt(v) == code){
                    return R.setOK("验证码正确",null);
                }else {
                    return R.setERROR("验证码输入错误");
                }
            }
        }
        return R.setERROR("亲，验证码过期，请输入获取验证码");
    }

    @Override
    public R sendMessage(Message message, String ip) {
        if (message.getType() < 4){
            // 短信
            return sendSms(message,ip);
        }else{
            // 邮箱
            return sendEmail(message,ip);
        }
    }

    // 发送短信验证码
    private R sendSms(Message message, String ip){
        int count = 0;
        // 短信的话  需要校验，一个手机号一分钟只能发1次
        // 一个手机号一天只能发20次
        // 获取当前手机号今天发送的次数
        String c = jedisUtil.get(ProjectConfig.SMSPREDAY + message.getReceive());
        if (c != null && c.matches("[0-9]{1,2}")){
            count = Integer.parseInt(c);
            if (count >= 20){
                return R.setERROR("亲，一个手机号一天只能发送指定数量的短信");
            }
        }
        // 一个手机号一分钟只能发送一次
        if (jedisUtil.exists(ProjectConfig.SMSPRELIMIT + message.getReceive())){
            // 说明一分钟内发送过短信
            return R.setERROR("一个手机号一分钟内只能发送一次短信");
        }

        int code = 0;
        String info = "";
        int flag = 3;
        boolean isflag = false;
        if (jedisUtil.exists(ProjectConfig.SMSPRELIMIT+message.getReceive())){
            // 上次的验证码还没失效
            code = Integer.parseInt(jedisUtil.get(ProjectConfig.SMSPRELIMIT+message.getReceive()));
        }else {
            code = CodeUtil.createNum(6);
            isflag = true;
        }
        info = "发送短信验证码" + code;
        // 发送验证码 三分钟有效
        SmsUtil.mobileQuery(message.getReceive(),code);
        flag = 1; // 发送成功
        // 一分钟
        String s1 = jedisUtil.setex(ProjectConfig.SMSPRELIMIT+message.getReceive(),60,"短信发送限制");
        // 一天
        String s2 = jedisUtil.setex(ProjectConfig.SMSPREDAY+message.getReceive(),TimeUtil.getLastSeconds(),(count+1)+"");

        if (isflag){
            // 记录验证码
            String s3 = jedisUtil.setex(ProjectConfig.SMSCODE+message.getReceive(),600,code+"");
        }
        save(message,info,ip);
        return R.setOK("OK",null);
    }
    // 新增数据库记录
    private void save(Message message,String info,String ip){
        //  记录消息发送消息
        messageMapper.insert(message);
        // 记录日志
        MessageLog log = new MessageLog();
        log.setMid(message.getId());
        log.setInfo(info);
        log.setIp(ip);
        messageLogMapper.insert(log);
        // 记录收件人
        MsgReceive receive = new MsgReceive();
        receive.setNo(message.getReceive());
        receive.setFlag(message.getType() < 4?1:2);
        msgReceiveMapper.insert(receive);
    }
    // 发送邮箱
    private R sendEmail(Message message,String ip){
        String info;
        int code = CodeUtil.createNum(6);
        int flag = 2;
        info = "发送邮箱激活码：" + code;
        EmailMsg emailMsg = new EmailMsg();
        emailMsg.setContent(ProjectConfig.projects.get(message.getPcode()));
        ActiveCode code1 = new ActiveCode();
        code1.setEmail(message.getReceive());
        code1.setCode(code);
        String mw = EncryptionUtil.AESEnc(ProjectConfig.AESKEYACTIVECODE,JSON.toJSONString(code1));
        String u = ProjectConfig.ACTIVEURL + "?data=" + mw + "&email=" + EncryptionUtil.AESEnc(ProjectConfig.AESKEYACTIVECODE,"ssssssss");
        emailMsg.setContent("欢迎注册：" + emailMsg.getCompany() + ",请激活使用，<a href=''></a>");
        EmailUtil.sendEmail(emailMsg);
        flag = 1;
        save(message,info,ip);
        return R.setOK("OK",null);
    }


}
