package cn.wuxia.project.payment.core.service;

import java.util.List;

import cn.wuxia.project.payment.core.entity.FundsTradeDetail;
import cn.wuxia.project.common.service.CommonService;
import cn.wuxia.common.orm.query.Pages;

/**
 * 订单交易明细表 Service Interface.
 * @author songlin.li
 * @since 2017-02-16
 */
public interface FundsTradeDetailService extends CommonService<FundsTradeDetail, String> {

    /**
     * 根据订单号查找当前所有的交易记录
     * @author songlin
     * @param orderNo
     * @return
     */
    public List<FundsTradeDetail> findByOrderNo(String orderNo);

    public Pages findPages(Pages page);
}
