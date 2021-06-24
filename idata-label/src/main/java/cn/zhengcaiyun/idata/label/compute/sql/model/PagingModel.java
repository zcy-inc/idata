package cn.zhengcaiyun.idata.label.compute.sql.model;

import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:50
 **/
public class PagingModel implements ModelRender {
    private final Long limit;
    private final Long offset;

    private PagingModel(Long limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String renderSql() {
        String pageSql = "";
        if (!Objects.isNull(offset)) {
            pageSql = pageSql + " offset " + offset;
        }
        if (!Objects.isNull(limit)) {
            pageSql = pageSql + " limit " + limit;
        }
        return pageSql;
    }

    public static PagingModel of(Long limit, Long offset) {
        return new PagingModel(limit, offset);
    }

    public static PagingModel of(Long limit) {
        return of(limit, null);
    }

}
