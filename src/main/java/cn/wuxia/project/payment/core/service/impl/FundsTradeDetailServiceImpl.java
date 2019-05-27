package cn.wuxia.project.payment.core.service.impl;

import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.common.service.impl.CommonServiceImpl;
import cn.wuxia.project.payment.core.dao.FundsTradeDetailDao;
import cn.wuxia.project.payment.core.entity.FundsTradeDetail;
import cn.wuxia.project.payment.core.service.FundsTradeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单交易明细表 Service Implement class.
 * @author songlin.li
 * @since 2017-02-16
 */
@Service
public class FundsTradeDetailServiceImpl extends CommonServiceImpl<FundsTradeDetail, String> implements FundsTradeDetailService {

    @Autowired
    FundsTradeDetailDao fundsTradeDetailDao;

    @Override
    protected CommonDao getCommonDao() {
        return fundsTradeDetailDao;
    }

    /**
     * 根据订单号查找当前所有的交易记录
     * @author songlin
     * @param orderNo
     * @return
     */
    @Override
    public List<FundsTradeDetail> findByOrderNo(String orderNo) {
        List<FundsTradeDetail> list = fundsTradeDetailDao.findByOrderNo(orderNo);
        return list;
    }

    @Override
    public Pages findPages(Pages page) {
        return fundsTradeDetailDao.findAll(page);

    }
}
