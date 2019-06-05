package com.cloud.common.constants;

/**
 * 公共常量类
 *
 * @author S.J.
 * @date 2019/02/20
 */
public class CommConstants {

	public static final String RET_CODE = "retCode";
	public static final String RET_MSG = "retMsg";
	public static final String RESULT = "result";

	//操作成功-7个0-返回给app接口
	public static final String OK = "0000000";
	//操作成功
	public static final String OK_MSG = "success";

	//分号
	public static final String SEMICOLON = ";";
	//逗号
	public static final String COMMOA = ",";
	//点号
  	public static final String DOT = ".";

	//Content-Type
	public static final String CONTENT_TYPE = "Content-Type";

  	/*----------------------------- 系统统一错误编码 -----------------------------*/
    /**
     * 错误编码-系统错误
     */
    public static final String SYSTEM_ERROR = "00000001";
    /**
     * 错误描述-系统错误
     */
    public static final String SYSTEM_ERROR_MSG = "服务竟然出错了，请刷新页面";

    /**
     * 错误编码-数据操作失败(数据不存在)
     */
    public static final String DATABASE_ERROR = "00000002";
    /**
     * 错误描述-数据操作失败(数据不存在)
     */
    public static final String DATABASE_ERROR_MSG = "数据竟然操作失败";

    /**
     * 错误编码-数据不存在
     * 数据库操作失败通用型编码，如果需要返回详细错误，请使用详细错误编码
     */
    public static final String DATABASE_NOTEXIST = "00000003";
    /**
     * 错误描述-数据不存在
     */
    public static final String DATABASE_NOTEXIST_MSG = "数据不存在";

    /*----------------------------- 系统统一错误编码 -----------------------------*/

}
