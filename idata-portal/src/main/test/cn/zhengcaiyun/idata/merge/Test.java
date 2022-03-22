package cn.zhengcaiyun.idata.merge;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhanqian
 * @date 2022/3/14 5:13 PM
 * @description
 */
public class Test {

    public static void main(String[] args) {
        String mergeSqlTemplate = "";
        try (InputStream inputStream = new Test().getClass().getClassLoader().getResourceAsStream("template/merge_sql_template2.sql");) {
            byte[] buff = new byte[1024];
            int btr = 0;
            while ((btr = inputStream.read(buff)) != -1) {
                mergeSqlTemplate += new String(buff, 0, btr, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException("模版文件解析失败");
        }

        // 通过SpEL解析
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("isMulPartition", false);
        context.setVariable("destTable", "ods.ods_db_item_parana_skus");
        context.setVariable("columns", " id\n" +
                ",sku_code\n" +
                ",item_id\n" +
                ",shop_id\n" +
                ",status\n" +
                ",specification\n" +
                ",model\n" +
                ",outer_sku_id\n" +
                ",outer_shop_id\n" +
                ",image\n" +
                ",name\n" +
                ",extra_price_json\n" +
                ",price\n" +
                ",attrs_json\n" +
                ",stock_type\n" +
                ",stock_quantity\n" +
                ",extra\n" +
                ",created_at\n" +
                ",updated_at\n" +
                ",thumbnail\n" +
                ",layer\n" +
                ",full_price_json\n" +
                ",base_sku_id\n" +
                ",channel_sku_id\n" +
                ",instance_code\n" +
                ",cspu_id\n" +
                ",commerce_url\n" +
                ",r_add_time\n" +
                ",r_modified_time");
        context.setVariable("tmpTable", "src.ods_db_item_parana_skus_pt");
        context.setVariable("alisColumns", " t1.id\n" +
                ",t1.sku_code\n" +
                ",t1.item_id\n" +
                ",t1.shop_id\n" +
                ",t1.status\n" +
                ",t1.specification\n" +
                ",t1.model\n" +
                ",t1.outer_sku_id\n" +
                ",t1.outer_shop_id\n" +
                ",t1.image\n" +
                ",t1.name\n" +
                ",t1.extra_price_json\n" +
                ",t1.price\n" +
                ",t1.attrs_json\n" +
                ",t1.stock_type\n" +
                ",t1.stock_quantity\n" +
                ",t1.extra\n" +
                ",t1.created_at\n" +
                ",t1.updated_at\n" +
                ",t1.thumbnail\n" +
                ",t1.layer\n" +
                ",t1.full_price_json\n" +
                ",t1.base_sku_id\n" +
                ",t1.channel_sku_id\n" +
                ",t1.instance_code\n" +
                ",t1.cspu_id\n" +
                ",t1.commerce_url\n" +
                ",t1.r_add_time\n" +
                ",t1.r_modified_time");
        context.setVariable("keyCondition", "t1.id=t2.id");
        context.setVariable("whereKeyConditionParam", "t2.id is null");
        context.setVariable("days", 3);
        context.setVariable("br", "\n");

        Expression expression = parser.parseExpression(mergeSqlTemplate, new TemplateParserContext());
        System.out.println(expression.getValue(context, String.class));
    }
}
