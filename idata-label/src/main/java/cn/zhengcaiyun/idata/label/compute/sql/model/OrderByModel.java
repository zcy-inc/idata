package cn.zhengcaiyun.idata.label.compute.sql.model;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:49
 **/
public class OrderByModel implements ModelRender {

    private final List<SortableColumn> columnList;

    private OrderByModel() {
        columnList = Lists.newArrayList();
    }

    public static OrderByModel of() {
        return new OrderByModel();
    }

    public OrderByModel addAscColumn(BaseColumn column) {
        columnList.add(SortableColumn.asc(column));
        return this;
    }

    public OrderByModel addDescColumn(BaseColumn column) {
        columnList.add(SortableColumn.desc(column));
        return this;
    }

    @Override
    public String renderSql() {
        checkArgument(!CollectionUtils.isEmpty(this.columnList), "columnList is empty.");
        StringBuilder builder = new StringBuilder();
        builder.append("order by ");
        int count = 0;
        for (SortableColumn column : this.columnList) {
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(column.getColumn().renderSql());
            builder.append(" ");
            builder.append(column.getSort());
        }
        return builder.toString();
    }

    private static class SortableColumn {
        private BaseColumn column;
        private String sort;

        private SortableColumn(BaseColumn column, String sort) {
            this.column = column;
            this.sort = sort;
        }

        public static SortableColumn asc(BaseColumn column) {
            return new SortableColumn(column, "asc");
        }

        public static SortableColumn desc(BaseColumn column) {
            return new SortableColumn(column, "desc");
        }

        public BaseColumn getColumn() {
            return column;
        }

        public String getSort() {
            return sort;
        }
    }
}
