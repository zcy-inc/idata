/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.AggregatorEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.LogicOperatorEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.StringMatchTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.query.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 待重构
 * @author: yangjianhua
 * @create: 2021-07-21 17:58
 **/
public class PrestoDSL {

    private StringBuilder sb = new StringBuilder();

    public String toString() {
        return sb.toString();
    }

    PrestoDSL append(String str) {
        sb.append(str);
        return this;
    }

    private String columnQuote(String columnName) {
        return "\"" + columnName + "\"";
    }

    PrestoDSL select(List<DimColumnDto> dimensions, List<MeasureColumnDto> measures, Boolean aggregate) throws SQLException {
        sb.append("/* Query from ITable */ select\n");
        int selectIndex = 1;
        if (dimensions != null) {
            for (DimColumnDto dimension : dimensions) {
                fieldSeparator(selectIndex);
                dimension(dimension, true);
                selectIndex++;
            }
        }
        if (measures != null) {
            for (MeasureColumnDto measure : measures) {
                fieldSeparator(selectIndex);
                measure(measure, aggregate);
                selectIndex++;
            }
        }
        return this;
    }

    private void dimension(DimColumnDto dimension, boolean hasAlias) throws SQLException {
        if (WideDataTypeEnum.Date.equals(dimension.getDataType()) || WideDataTypeEnum.DateTime.equals(dimension.getDataType())) {
//            if (TruncatorEnum.TRUNC_YEAR.equals(dimension.getTruncator())) {
//                sb.append(String.format("date_trunc('year', %s)", columnQuote(dimension.getColumnName())));
//                if (hasAlias) {
//                    sb.append(" ").append(columnQuote(dimension.getColumnName()));
//                }
//            }
//            else if (TruncatorEnum.TRUNC_MONTH.equals(dimension.getTruncator())) {
//                sb.append(String.format("date_trunc('month', %s)", columnQuote(dimension.getColumnName())));
//                if (hasAlias) {
//                    sb.append(" ").append(columnQuote(dimension.getColumnName()));
//                }
//            }
//            else if (TruncatorEnum.TRUNC_DAY.equals(dimension.getTruncator())) {
//                sb.append(String.format("date_trunc('day', %s)", columnQuote(dimension.getColumnName())));
//                if (hasAlias) {
//                    sb.append(" ").append(columnQuote(dimension.getColumnName()));
//                }
//            }
//            else {
//                sb.append(columnQuote(dimension.getColumnName()));
//            }
            sb.append(columnQuote(dimension.getColumnName()));
        } else {
            sb.append(columnQuote(dimension.getColumnName()));
        }
    }

    private void measure(MeasureColumnDto measure, Boolean aggregate) {
        if (aggregate != null && aggregate) {
            String aggregateStr = "sum(";
            if (measure.getAggregator() != null && AggregatorEnum.AVG.equals(measure.getAggregator())) {
                aggregateStr = "avg(";
            }
            sb.append(aggregateStr).append(columnQuote(measure.getColumnName())).append(") ").append(columnQuote(measure.getColumnName()));
        } else {
            sb.append(columnQuote(measure.getColumnName()));
        }
    }

    private void fieldSeparator(int fieldIndex) throws SQLException {
        if (fieldIndex == 1) {
            sb.append("  ");
        } else if (fieldIndex % 4 == 0) {
            sb.append(",\n  ");
        } else {
            sb.append(", ");
        }
    }

    PrestoDSL from(String fromTable) throws SQLException {
        sb.append(" \nfrom ").append(fromTable);
        return this;
    }

    PrestoDSL where(List<FilterDto> filters) throws SQLException {
        if (filters != null && filters.size() > 0) {
            sb.append(" \nwhere\n");
            int filterIndex = 1;
            for (FilterDto filter : filters) {
                filter(filter, filterIndex);
                filterIndex++;
            }
        }
        return this;
    }

    private void filter(FilterDto filter, int filterIndex) throws SQLException {
        if (filterIndex == 1) {
            sb.append("  ");
        } else {
            sb.append(" \n  and ");
        }
        if (filter.getMaxBound() != null || filter.getMinBound() != null) {
            continuousFilter(filter);
        } else {
            discreteFilter(filter);
        }
    }

    private void continuousFilter(FilterDto filter) throws SQLException {
        boolean equal = false;
        boolean notEqual = false;
        if (filter.getMinBound() != null && filter.getMinBound().equals(filter.getMaxBound())) {
            if ((filter.getExcludeMax() != null && filter.getExcludeMax()) ||
                    (filter.getExcludeMin() != null && filter.getExcludeMin())) {
                notEqual = true;
            } else {
                equal = true;
            }
        }
        String maxBound = filter.getMaxBound();
        String minBound = filter.getMinBound();
        if (WideDataTypeEnum.Decimal.equals(filter.getDataType()) || WideDataTypeEnum.Whole.equals(filter.getDataType())) {
            // do nothing;
        } else if (WideDataTypeEnum.Date.equals(filter.getDataType())) {
            maxBound = (maxBound != null ? String.format("date(from_unixtime(%d, 'Asia/Shanghai'))", Long.parseLong(maxBound) / 1000) : null);
            minBound = (minBound != null ? String.format("date(from_unixtime(%d, 'Asia/Shanghai'))", Long.parseLong(minBound) / 1000) : null);
        } else if (WideDataTypeEnum.DateTime.equals(filter.getDataType())) {
            maxBound = (maxBound != null ? String.format("from_unixtime(%d, 'Asia/Shanghai')", Long.parseLong(maxBound) / 1000) : null);
            minBound = (minBound != null ? String.format("from_unixtime(%d, 'Asia/Shanghai')", Long.parseLong(minBound) / 1000) : null);
        } else {
            throw new SQLException("连续筛选器不支持的数据类型" + filter.getDataType());
        }
        if (equal) {
            sb.append(columnQuote(filter.getColumnName())).append(" = ").append(minBound);
        } else if (notEqual) {
            sb.append(columnQuote(filter.getColumnName())).append(" <> ").append(minBound);
        } else {
            if (minBound != null) {
                if (filter.getExcludeMin() != null && filter.getExcludeMin()) {
                    sb.append(columnQuote(filter.getColumnName())).append(" > ").append(minBound);
                } else {
                    sb.append(columnQuote(filter.getColumnName())).append(" >= ").append(minBound);
                }
            }
            if (maxBound != null) {
                if (minBound != null) {
                    sb.append(" and ");
                }
                if (filter.getExcludeMax() != null && filter.getExcludeMax()) {
                    sb.append(columnQuote(filter.getColumnName())).append(" < ").append(maxBound);
                } else {
                    sb.append(columnQuote(filter.getColumnName())).append(" <= ").append(maxBound);
                }
            }
        }
    }

    private void discreteFilter(FilterDto filter) throws SQLException {
        if (filter.getSelect() != null && filter.getSelect().size() > 0) {
            select(columnQuote(filter.getColumnName()), filter.getSelect(), false);
        } else if (filter.getUnselect() != null && filter.getUnselect().size() > 0) {
            select(columnQuote(filter.getColumnName()), filter.getUnselect(), true);
        } else if (filter.getMatch() != null && filter.getMatch().size() > 0) {
            LogicOperatorEnum logicOperator = LogicOperatorEnum.and;
            for (StringMatchDto stringMatch : filter.getMatch()) {
                if (LogicOperatorEnum.or.equals(stringMatch.getLogicOp())) {
                    logicOperator = LogicOperatorEnum.or;
                    break;
                }
            }
            sb.append("(");
            int stringMatchIndex = 1;
            for (StringMatchDto stringMatch : filter.getMatch()) {
                if (stringMatchIndex != 1) {
                    sb.append(" ").append(logicOperator.name()).append(" ");
                }
                sb.append(columnQuote(filter.getColumnName()));
                strMatch(stringMatch);
                stringMatchIndex++;
            }
            sb.append(")");
        } else {
            throw new SQLException("离散筛选器信息不完整");
        }
    }

    private void select(String columnName, List<String> select, boolean unselect) {
        List<String> nonNullSelect = select.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (nonNullSelect.size() != select.size() && nonNullSelect.size() > 0) {
            sb.append("(");
        }
        if (nonNullSelect.size() > 0) {
            if (unselect) {
                sb.append(columnName).append(" not in ('");
            } else {
                sb.append(columnName).append(" in ('");
            }
            sb.append(String.join("', '", nonNullSelect)).append("')");
        }
        if (nonNullSelect.size() != select.size() && nonNullSelect.size() > 0) {
            if (unselect) {
                sb.append(" and ");
            } else {
                sb.append(" or ");
            }
        }
        if (nonNullSelect.size() != select.size()) {
            if (unselect) {
                sb.append(columnName).append(" is not null");
            } else {
                sb.append(columnName).append(" is null");
            }
        }
        if (nonNullSelect.size() != select.size() && nonNullSelect.size() > 0) {
            sb.append(")");
        }
    }

    private void strMatch(StringMatchDto stringMatch) throws SQLException {
        if (StringMatchTypeEnum.contains.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" like '%%%s%%'", stringMatch.getMatchStr()));
        } else if (StringMatchTypeEnum.without.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" not like '%%%s%%'", stringMatch.getMatchStr()));
        } else if (StringMatchTypeEnum.startWith.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" like '%s%%'", stringMatch.getMatchStr()));
        } else if (StringMatchTypeEnum.endWith.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" like '%%%s'", stringMatch.getMatchStr()));
        } else if (StringMatchTypeEnum.isJust.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" = '%s'", stringMatch.getMatchStr()));
        } else if (StringMatchTypeEnum.isNot.equals(stringMatch.getMatchType())) {
            sb.append(String.format(" <> '%s'", stringMatch.getMatchStr()));
        } else {
            throw new SQLException("不支持的文本匹配类型");
        }
    }

    PrestoDSL groupBy(List<DimColumnDto> dimensions, Boolean aggregate) throws SQLException {
        if (aggregate != null && aggregate && dimensions != null && dimensions.size() > 0) {
            sb.append(" \ngroup by\n");
            int groupByIndex = 1;
            for (DimColumnDto dimension : dimensions) {
                fieldSeparator(groupByIndex);
                dimension(dimension, false);
                groupByIndex++;
            }
        }
        return this;
    }

    PrestoDSL orderBy(OrderByDto orderBy) throws SQLException {
        if (orderBy != null) {
            sb.append(" \norder by\n  ").append(columnQuote(orderBy.getColumnName()));
            if (orderBy.getDesc() != null && orderBy.getDesc()) {
                sb.append(" desc");
            }
        }
        return this;
    }

    PrestoDSL limit(Long limit, Long offset) throws SQLException {
        sb.append(" \noffset ").append(offset);
        sb.append(" limit ").append(limit);
        return this;
    }

}
