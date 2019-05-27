package cn.wuxia.project.payment.core.dao;

import cn.wuxia.common.spring.SpringContextHolder;
import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.payment.core.bean.MyAmountDetail;
import cn.wuxia.project.payment.core.entity.FundsDetail;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * FundsDetail Dao.
 * @author  songlin.li
 * @since 2017-02-16
 */
@Component
public class FundsDetailDao extends CommonDao<FundsDetail, String> {
    private Map<String, String> queryMap = SpringContextHolder.getBean("fundsDetail-query");

    /**
     * availableAmount & frozenAmount
     * @author songlin
     * @param userId
     * @return
     */
    public MyAmountDetail findMyAvailableAmount(String userId) {
        String sql = queryMap.get("findMyAvailableAmount_sql");
        MyAmountDetail amount = queryUnique(sql, MyAmountDetail.class, userId, userId);
        if (null == amount) {
            return new MyAmountDetail();
        } else
            return amount;
    }
}
