package cn.wuxia.project.payment.core.dao;

import java.util.List;
import java.util.Map;

import cn.wuxia.project.payment.core.entity.FundsTradeDetail;
import cn.wuxia.project.payment.core.enums.ExpenseType;
import cn.wuxia.project.payment.core.enums.TradeType;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import cn.wuxia.project.basic.core.common.BaseCommonDao;
import cn.wuxia.common.spring.SpringContextHolder;
import cn.wuxia.common.util.ListUtil;

/**
 * FundsTradeDetail Dao.
 * @author  songlin.li
 * @since 2017-02-16
 */
@Component
public class FundsTradeDetailDao extends BaseCommonDao<FundsTradeDetail, String> {
    /**
    * 在资源目录下配置自定义查询语句 /resources/query/fundsTradeDetail-query.xml
    */
    private Map<String, String> queryMap = SpringContextHolder.getBean("fundsTradeDetail-query");

    public List<FundsTradeDetail> findByOrderNo(String orderNo) {
        return createCriteria(Restrictions.eq("orderNo", orderNo)).addOrder(Order.desc("tradeTime")).list();
    }

    /**
     * 根据订单号，交易类型，费用类型查找是否已有记录
     * @author songlin
     * @param orderNo
     * @return
     */
    public boolean isExistsTrade(String orderNo, TradeType tradeType, ExpenseType expenseType) {
        List<FundsTradeDetail> list = createCriteria(Restrictions.eq("orderNo", orderNo), Restrictions.eq("tradeType", tradeType),
                Restrictions.eq("expenseType", expenseType)).list();
        return ListUtil.isNotEmpty(list);
    }
}
