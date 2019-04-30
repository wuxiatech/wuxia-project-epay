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
 * 交易类型
 * @author songlin
 * @ Version : V<Ver.No> <2017年5月10日>
 */
public enum TradeType {
    /**
     * 冻结
     */
    frozen("冻结"),
    /**
     * 开支扣费
     */
    expense("扣费"),
    /**
     * 解冻
     */
    unfreeze("解冻"),
    /**
     * 充值
     */
    topup("充值"), refund("退款");

    private String displayName;

    TradeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
