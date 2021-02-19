package com.yidiantong.api;

public class ApiConstants {
    // 1.0接口和AXB呼叫域名
    public static String rootUrl = "http://yidiantong.geewise.com";  // 正式
    public static String host = "yidiantong.geewise.com";  // 正式
//    public static String host = "yidiantong.geewise.com";  // 正式
    // 2.0 双向呼叫接口IP
    public static String url_2 = "http://139.196.56.167:10090";  // 生产


//    // 1.0接口和AXB呼叫域名
//    public static String rootUrl = "http://test.yidiantong.geewise.com";  // 测试

//    public static String host = "test.yidiantong.geewise.com";  // 测试
//    // 2.0 双向呼叫接口IP
//    public static String url_2 = "http://106.14.191.23:10090";  //  测试

    // 2.0 双向呼叫接口url
    public static String rootUrl_2 = url_2 + "/api/phone";

    public static String appid = "203835085";
    public static String secret = "pEuBc6PDx597sC1Vu37iBru461vj3GyN";


    // 登录
    public static String login = "/login";
    // 发送验证码
    public static String sendCode = "/sendCode";
    // 退出登录
    public static String logout = "/logout";
    // 账号剩余通话时长zaza
    public static String talktimeInfo = "/talktimeInfo";
    // 线索详情编辑
    public static String updateCluesInfo = "/updateCluesInfo";
    // 获取产品列表接口
    public static String getIndustrylist = "/getIndustrylist";
    // 获取地区列表
    public static String getRegionList = "/getRegionList";
    // 文件上传接口
    public static String uploadFile = "/uploadFile";
    // 个人信息修改
    public static String userInfoUpdate = "/userInfoUpdate";
    // 获取个人基本信息
    public static String userInfo = "/userInfo";
    // 获取企业基本信息
    public static String enterprisesInfo = "/enterprisesInfo";
    // 版本检测
    public static String checkVersion = "/checkVersion";
    // 线索详情
    public static String cluesInfo = "/cluesInfo";
    // 线索列表
    public static String cluesList = "/cluesList";
    // 获取筛选列表
    public static String getFilterList = "/getFilterList";
    // 更新通话记录
    public static String updateCallRecords = "/updateCallRecords";

    // 2.0
    // 文件上传接口
    public static String uploadAvatar = "/uploadAvatar";
    // 呼叫接口
    public static String callRequest = "/callRequest";
    // 导入通讯录接口
    public static String importContacts = "/import";
    // 批量查询号码是否可以导入
    public static String search = "/search";
    // 拨打状态接口查询
    public static String callStatus = "/callStatus";
    // 添加单个手机号
    public static String importOnePhone = "/importOnePhone";
    // 双呼拨打提交通话时长
    public static String duration = "/duration";
}
