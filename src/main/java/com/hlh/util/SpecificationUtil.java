package com.hlh.util;

import com.hlh.domain.base.BasePageParam;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Component
public class SpecificationUtil {
    private CheckUtil checkUtil = CheckUtil.dao;

    /**
     * 默认条件查询
     *
     * @param root
     * @param cb
     * @param predicates
     * @param basePageParam
     * @param fields
     */
    public <T> void initWhere(
            Root<T> root,
            CriteriaBuilder cb,
            List<Predicate> predicates,
            BasePageParam basePageParam,
            Object... fields) {
        if (checkUtil.isNotNull(basePageParam.getFlag())) {
            predicates.add(cb.equal(root.get("flag"), basePageParam.getFlag()));
        }
        if (checkUtil.isNotNull(basePageParam.getIsDel())) {
            predicates.add(cb.equal(root.get("isDel"), basePageParam.getIsDel()));
        }

        if (checkUtil.isStrNotNull(basePageParam.getStartDate()) && basePageParam.getStartDate().trim().length() == 10) {
            basePageParam.setStartDate(basePageParam.getStartDate().trim() + " 00:00:00");
        }

        if (checkUtil.isStrNotNull(basePageParam.getEndDate()) && basePageParam.getEndDate().trim().length() == 10) {
            basePageParam.setEndDate(basePageParam.getEndDate().trim() + " 23:59:59");
        }

        String fieldDate = ""; // 时间范围搜索字段
        if (checkUtil.isNotNull(fields) && fields.length > 0) {
            String f1 = String.valueOf(fields[0]);
            fieldDate = checkUtil.isStrNotNull(f1) ? f1 : "operateTime";

            if (fields.length > 1) {
                boolean isdel = (boolean) fields[1]; // 删除状态
                predicates.add(cb.equal(root.get("isDel"), isdel));
            }
        }
        String std = checkUtil.isStrNotNull(basePageParam.getStartDate()) ? basePageParam.getStartDate() : "1970-01-01 00:00:00";
        String end = checkUtil.isStrNotNull(basePageParam.getEndDate()) ? basePageParam.getEndDate() : DateUtil.getDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (checkUtil.isStrNotNull(basePageParam.getStartDate()) || checkUtil.isStrNotNull(basePageParam.getEndDate())) {
            predicates.add(cb.between(root.get(fieldDate),
                    DateUtil.str2Date(std, "yyyy-MM-dd HH:mm:ss"),
                    DateUtil.str2Date(end, "yyyy-MM-dd HH:mm:ss")));
        }
    }
}
