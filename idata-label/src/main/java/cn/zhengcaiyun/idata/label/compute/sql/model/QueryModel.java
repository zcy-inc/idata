package cn.zhengcaiyun.idata.label.compute.sql.model;

import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:47
 **/
public class QueryModel implements ModelRender {
    private final SelectModel selectModel;
    private final TableModel tableModel;
    private final WhereModel whereModel;
    private final GroupModel groupModel;
    private final HavingModel havingModel;
    private final OrderModel orderModel;
    private final PagingModel pagingModel;

    private QueryModel(Builder builder) {
        this.selectModel = builder.selectModel;
        this.tableModel = builder.tableModel;
        this.whereModel = builder.whereModel;
        this.groupModel = builder.groupModel;
        this.havingModel = builder.havingModel;
        this.orderModel = builder.orderModel;
        this.pagingModel = builder.pagingModel;
    }

    @Override
    public String renderSql() {
        StringBuilder builder = new StringBuilder();
        builder.append(selectModel.renderSql()).append(" ")
                .append(tableModel.renderSql()).append(" ");
        if (!Objects.isNull(whereModel)) {
            builder.append(whereModel.renderSql()).append(" ");
        }
        if (!Objects.isNull(groupModel)) {
            builder.append(groupModel.renderSql()).append(" ");
        }
        if (!Objects.isNull(havingModel)) {
            builder.append(havingModel.renderSql()).append(" ");
        }
        if (!Objects.isNull(orderModel)) {
            builder.append(orderModel.renderSql()).append(" ");
        }
        if (!Objects.isNull(pagingModel)) {
            builder.append(pagingModel.renderSql()).append(" ");
        }
        return builder.toString();
    }

    public static class Builder {
        private final SelectModel selectModel;
        private final TableModel tableModel;
        private WhereModel whereModel;
        private GroupModel groupModel;
        private HavingModel havingModel;
        private OrderModel orderModel;
        private PagingModel pagingModel;

        public Builder(SelectModel selectModel, TableModel tableModel) {
            this.selectModel = selectModel;
            this.tableModel = tableModel;
        }

        public Builder where(WhereModel whereModel) {
            this.whereModel = whereModel;
            return this;
        }

        public Builder group(GroupModel groupModel) {
            this.groupModel = groupModel;
            return this;
        }

        public Builder having(HavingModel havingModel) {
            this.havingModel = havingModel;
            return this;
        }

        public Builder order(OrderModel orderModel) {
            this.orderModel = orderModel;
            return this;
        }

        public Builder paging(PagingModel pagingModel) {
            this.pagingModel = pagingModel;
            return this;
        }

        public QueryModel build() {
            return new QueryModel(this);
        }
    }
}
