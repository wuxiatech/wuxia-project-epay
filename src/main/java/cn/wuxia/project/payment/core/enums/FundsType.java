/*
* Created on :2017年2月15日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.payment.core.enums;

/**
 * 费用操作类型
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public enum FundsType {
    //预告单扣费
    order_finish,

    //解冻
    order_unfreeze,
    //出仓完成订单
    order_complete,

    //充值
    charge,
    //退款
    refund;

}
