package com.gzj.demo.common;

public enum ResultCode {

    /*
     * 错误信息
     * */
    CODE_200(200, "操作成功"),
    CODE_201(201,"对象创建成功"),
    CODE_202(202,"请求已经被接受"),
    CODE_400(400,"参数列表错误（缺少，格式不匹配）"),
    CODE_401(401,"未授权"),
    CODE_403(403,"访问受限，授权过期"),
    CODE_404(404,"资源，服务未找到"),
    CODE_500(500,"操作失败"),
    ;

    private Integer Code;
    private String Msg;
    ResultCode(Integer Code, String Msg) {
        this.Code = Code;
        this.Msg = Msg;
    }
    public Integer getCode() {
        return Code;
    }
    public String getMsg() {
        return Msg;
    }
}