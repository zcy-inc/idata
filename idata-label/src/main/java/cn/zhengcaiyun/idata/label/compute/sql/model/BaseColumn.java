package cn.zhengcaiyun.idata.label.compute.sql.model;

import java.util.Optional;

/**
 * @description: 参考mybatis dynamic sql，BasicColumn
 * @author: yangjianhua
 * @create: 2021-06-24 15:24
 **/
public interface BaseColumn extends ModelRender {
    Optional<String> alias();

    default String renderSqlWithAlias() {
        String renderName = renderSql();
        return alias().map(a -> renderName + " as " + a)
                .orElse(renderName);
    }
}
