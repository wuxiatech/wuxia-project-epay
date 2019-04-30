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
 * 消费类型
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public enum ExpenseType {
    //预扣运费
    prefreight("预扣运费"),
    //预扣税费
    pretaxes("预扣税费"),

    //实际运费
    freight("运费"),
    //跨境电商综合税
    acrosstax("跨境电商综合税"),
    //行邮税BE、BU
    posttax("行邮税"),
    //其他费用
    other("其它费用"),

    //线下汇款
    transfer("线下汇款"), transfer_wxpay("微信支付"), transfer_alipay("支付宝支付"),

    refund("线下退款"), refund_wxpay("微信退款"), refund_alipay("支付宝退款");

    private String displayName;

    ExpenseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
