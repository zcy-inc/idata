import cn.zhengcaiyun.idata.connector.util.SparkSqlHelper;
import cn.zhengcaiyun.idata.connector.util.model.TableSib;
import org.junit.Test;

public class SparkSqlUtilTest {
    @Test
    public void createTableTest() {
        String sql = " CREATE TABLE my_tab(a INT COMMENT 'test', b STRING) ";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res.toString());
    }

    @Test
    public void createTableAsSelectTest() {
        String sql = "create table IF NOT EXISTS tdl_users_1 as select * from users a left outer join address b on a.addr_id = b.id";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }

    @Test
    public void selectTest() {
        String sql = "select d.id from file_download d \n" + "left join file_transfer t on t.id = d.transfer_id\n" + "where d.id in (select id from test)";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }

    @Test
    public void withTest() {
        String sql = "with pb_budget as(\n" +
                "SELECT \n" +
                "  order_id,min(budget_amount) as budget_amount\n" +
                "  from dwd.dwd_trade_bid_purchase_detail\n" +
                "  GROUP by order_id\n" +
                "),\n" +
                "t_plan as (\n" +
                "  select \n" +
                "  \t* \n" +
                "  from (\n" +
                "    select \n" +
                "      a.trade_id order_id\n" +
                "      ,a.biz_type\n" +
                "      ,a.purchaseplan_apply_year as apply_year\n" +
                "      ,a.purchase_plan_id as plan_id\n" +
                "      ,a.gp_catalog_code gpcatalog_code\n" +
                "      ,row_number() over(partition by a.trade_id,a.biz_type order by purchase_plan_id) as rn\n" +
                "    from dwd.dwd_trade_purchaseplan_detail a\n" +
                "  ) t \n" +
                "  where rn = 1\n" +
                "),\n" +
                "--关联多个资金性质时获取第一个资金性质\n" +
                "zjlx as (\t\t\t\t\t\t\t\t   \n" +
                "select purchaseplan_id,min(capital_code) as capital_code  from ods_db_purchaseplan.sync_purchaseplan_capital a group by purchaseplan_id\n" +
                "),\n" +
                "--关联多个采购方式取第一个\n" +
                "pre_m_code as (\t\t\t\t\t\t\t\n" +
                "SELECT trade_id order_id, min(code) as code,biz_type\n" +
                "FROM dwd.dwd_trade_order_detail\n" +
                "LATERAL VIEW explode(split(procurement_method_codes, ',')) procurement_method_code  AS code\n" +
                "group by trade_id,biz_type),\n" +
                "--关联多个组织形式取第一个\n" +
                "pre_t_code as (\t\t\t\t\t\t\t\n" +
                "SELECT trade_id order_id, min(code) as code,biz_type\n" +
                "FROM dwd.dwd_trade_order_detail\n" +
                "LATERAL VIEW explode(split(procurement_type_codes, ',')) procurement_type_code AS code\n" +
                "group by trade_id,biz_type),\n" +
                "pre_zjxz as (\n" +
                "\t\tSELECT\n" +
                "  \t\tt_order_d.trade_id order_id,\n" +
                "  \t\tt_order_d.biz_type,\n" +
                "  \t\tsum(\n" +
                "          case when t_order_d.biz_type = '项目采购' then pb.budget_amount\n" +
                "            else COALESCE(t_tra_d.item_amount,0)\n" +
                "          end \n" +
                "        ) as budget_amount, \t\t\n" +
                "  \t\tsum(t_tra_d.item_amount) as actual_amount \n" +
                "  \t\tfrom dwd.dwd_trade_all_detail t_tra_d\n" +
                "        left outer join dwd.dwd_trade_order_detail t_order_d\n" +
                "        on t_tra_d.trade_id = t_order_d.trade_id \n" +
                "        and t_tra_d.biz_type = t_order_d.biz_type\n" +
                "  \t\tleft outer join pb_budget pb on t_tra_d.trade_id = pb.order_id\n" +
                "  \t\tgroup by \n" +
                "  \t\t\tt_order_d.trade_id,\n" +
                "  \t\t\tt_order_d.biz_type\n" +
                "),\n" +
                "zjxz as (\n" +
                "\n" +
                "  \t\tSELECT\n" +
                "  \t\t    t_order_d.trade_id order_id,\n" +
                "      \t\tt_order_d.biz_type,\n" +
                "  \t\t\tcsmp.czb_name,\n" +
                "  \t\t\tt_plan.apply_year as purchaseplan_apply_year,\n" +
                "  \t\t\tt_plan.gpcatalog_code,\n" +
                "  \t\t\tsplit(gpcm.rela_catalog_code,',')[0] as pur_item,\n" +
                "      \t\tsplit(gpcm.rela_catalog_code,',')[0] as pur_contract_item,\n" +
                "      \t\tsubstring(split(gpcm.rela_catalog_code,',')[0],0,1) as pur_item_root,\n" +
                "      \t\tsplit(gpcm.unit,',')[0] as measure_unit,\n" +
                "  \t\t\tsplit(gpcm.rela_catalog_name,',')[0] as pur_item_name,\n" +
                "      \t\tsplit(gpcm.rela_catalog_name,',')[0] as pur_contract_item_name,\n" +
                "  \t\t\tt_plan.plan_id as purchaseplan_id\n" +
                "  \t\tfrom dwd.dwd_trade_order_detail t_order_d\n" +
                " \t\tLEFT outer JOIN t_plan \n" +
                " \t\ton t_order_d.trade_id = t_plan.order_id \n" +
                "  \t\tand t_order_d.biz_type = t_plan.biz_type\n" +
                "  \t\tleft outer join zjlx on zjlx.purchaseplan_id = t_plan.plan_id\n" +
                "  \t\tleft outer join ods_db_purchaseplan.sync_capital_stat_map csmp on \n" +
                "\t\tcsmp.district_code = t_order_d.district_code \n" +
                "  \t\tand t_plan.apply_year = csmp.stat_year \n" +
                "\t\tand zjlx.capital_code= stat_code\n" +
                "  \t\tleft outer join (\n" +
                "  \t\t\tselect \n" +
                "          \t\t*\n" +
                "          \t\t,row_number() over(partition by district_code,year,catalog_code order by district_code) as num\n" +
                "          \tfrom dim.dim_gpcatalog_mapping\n" +
                "        ) gpcm \n" +
                "  \t\ton gpcm.district_code = t_order_d.district_code \t\t\t\n" +
                "  \t\tand gpcm.year = t_plan.apply_year and t_plan.gpcatalog_code = gpcm.catalog_code\n" +
                "  \t\twhere gpcm.num=1\n" +
                "),\n" +
                "rpt_contract as (\n" +
                "select \n" +
                "    case when t_order_d.biz_type = '协议供货' and d.fst_value = 'zzxs001' then '集中采购代理机构'\n" +
                "  when t_order_d.biz_type = '协议供货' and d.fst_value = 'zzxs002' then '部门集中采购代理机构'\n" +
                "  when t_order_d.biz_type = '协议供货' then '集中采购代理机构'\n" +
                "  when d.fst_value = 'zzxs001' then '集中采购代理机构'\n" +
                "  when d.fst_value = 'zzxs002' then '部门集中采购代理机构'\n" +
                "  else '社会代理机构'\n" +
                "  end as procurement_agency_type_name ,\n" +
                "    case when t_order_d.biz_type = '协议供货' and d.fst_value = 'zzxs001' then 'dljglx001'\n" +
                "  when t_order_d.biz_type = '协议供货' and d.fst_value = 'zzxs002' then 'dljglx002'\n" +
                "  when t_order_d.biz_type = '协议供货' then 'dljglx001'\n" +
                "  when d.fst_value = 'zzxs001' then 'dljglx001'\n" +
                "  when d.fst_value = 'zzxs002' then 'dljglx002'\n" +
                "  else 'dljglx003'\n" +
                "  end as procurement_agency_type_code,\n" +
                "  d.fst_value as procurement_type_code,\n" +
                "  case when d.fst_value = 'zzxs001' then '集中采购'\n" +
                "  \tWhen d.fst_value = 'zzxs002' then '部门集中采购'\n" +
                "    When d.fst_value = 'zzxs003' then '分散采购'\n" +
                "    end as procurement_type_name,\n" +
                "  e.fst_value as procurement_method_code,\n" +
                "  \tcase when e.fst_value = 'cgfs001' then '公开招标' \n" +
                "    when e.fst_value = 'cgfs002' then '邀请招标'\n" +
                "    when e.fst_value = 'cgfs003' then '竞争性谈判'\n" +
                "    when e.fst_value = 'cgfs004' then '询价'\n" +
                "    when e.fst_value = 'cgfs005' then '单一来源'\n" +
                "    when e.fst_value = 'cgfs006' then '协议供货'\n" +
                "    when e.fst_value = 'cgfs007' then '定点采购'\n" +
                "    when e.fst_value = 'cgfs009' then '电子卖场'\n" +
                "    when e.fst_value = 'cgfs008' then '竞争性磋商'\n" +
                "    END  as procurement_method_name,\n" +
                "  case when t_order_d.biz_type = '网上超市' then concat('WC-',t_order_d.trade_id) \n" +
                "   when t_order_d.biz_type = '医展馆' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '协议供货' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '反向竞价' then concat('RE-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '合同备案' then concat('BA-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '在线询价' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '大宗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '定点服务' then concat('FH-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '教育装备' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车保险' then concat('FI-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车维修' then concat('FR-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '疫苗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '通用定点' then concat('FU-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '通用行业馆' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '项目采购' then concat('PB-',t_order_d.trade_id)\n" +
                "   end as id,\n" +
                "   case when t_order_d.biz_type = '网上超市' then concat('WC-',t_order_d.trade_id) \n" +
                "   when t_order_d.biz_type = '医展馆' then concat('IN-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '协议供货' then concat('IN-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '反向竞价' then concat('RE-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '合同备案' then concat('BA-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '在线询价' then concat('IN-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '大宗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '定点服务' then concat('FH-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '教育装备' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车保险' then concat('FI-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车维修' then concat('FR-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '疫苗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '通用定点' then concat('FU-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '通用行业馆' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '项目采购' then concat('PB-',t_order_d.trade_id)\n" +
                "   end as contract_id,\n" +
                "     case when t_order_d.biz_type = '网上超市' then concat('WC-',t_order_d.trade_id) \n" +
                "   when t_order_d.biz_type = '医展馆' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '协议供货' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '反向竞价' then concat('RE-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '合同备案' then concat('BA-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '在线询价' then concat('IN-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '大宗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '定点服务' then concat('FH-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '教育装备' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车保险' then concat('FI-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车维修' then concat('FR-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '疫苗' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '通用定点' then concat('FU-',t_order_d.contract_no)\n" +
                "   when t_order_d.biz_type = '通用行业馆' then concat('WC-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '项目采购' then concat('PB-',t_order_d.trade_id)\n" +
                "   end as contract_no,\n" +
                "   case when t_order_d.biz_type = '网上超市' then concat('网超-',t_order_d.trade_id) \n" +
                "   when t_order_d.biz_type = '医展馆' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '协议供货' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '反向竞价' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '合同备案' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '在线询价' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '大宗' then concat('大宗-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '定点服务' then concat('定点会议-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '教育装备' then concat('教育装备-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车保险' then concat('定点车辆保险-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '汽车维修' then concat('定点车辆维修-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '疫苗' then concat('疫苗-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '通用定点' then t_order_d.contract_name\n" +
                "   when t_order_d.biz_type = '通用行业馆' then concat('通用-',t_order_d.trade_id)\n" +
                "   when t_order_d.biz_type = '项目采购' then t_order_d.project_name\n" +
                "   end as contract_name,\n" +
                "   case when t_order_d.creator_name = '' \n" +
                "   or t_order_d.creator_name is null then '经办人' \n" +
                "   else t_order_d.purchaser_name end as handler,\n" +
                "   case when t_order_d.creator_name = '' \n" +
                "   or t_order_d.creator_name is null then '经办人' \n" +
                "   else t_order_d.purchaser_name end as entry_man,\n" +
                "   '' as implementation_form,\n" +
                "   '' as project_memo,\n" +
                "   '' as contract_memo,\n" +
                "   t_order_d.purchaser_org_name as purchaser_org_name,\n" +
                "   t_order_d.purchaser_org_id as purchaser_org_id,\n" +
                "   t_order_d.district_code as district_code,\n" +
                "   case when dim_pur.org_type in('事业单位','自收自支组织') then 'dwxz002'\n" +
                "    when dim_pur.org_type in ('团体组织','其他') then 'dwxz003'\n" +
                "    --when entrust_pur_unit_property in ('国家单位','') then \n" +
                "    else 'dwxz001' end as purchaser_org_property_code,\n" +
                "   case when dim_pur.org_type in('事业单位','自收自支组织') then '事业单位'\n" +
                "    when dim_pur.org_type in ('团体组织','其他') then '团体组织'\n" +
                "    --when entrust_pur_unit_property in ('国家单位','') then \n" +
                "    else '国家机关' \n" +
                "   end as purchaser_org_property_name,\n" +
                "   case when  dim_pur.administration_level_id = '1' then 'dwjb001'\n" +
                "   when dim_pur.administration_level_id = '2' then 'dwjb002'\n" +
                "   when dim_pur.administration_level_id = '3' then 'dwjb003'\n" +
                "   when dim_pur.administration_level_id = '4' then 'dwjb004'\n" +
                "   when dim_pur.administration_level_id = '9' then 'dwjb004'\n" +
                "   else 'dwjb004'\n" +
                "   end as purchaser_org_level_code,\n" +
                "   case when  dim_pur.administration_level_id = '1' then '中央级'\n" +
                "   when dim_pur.administration_level_id = '2' then '省级'\n" +
                "   when dim_pur.administration_level_id = '3' then '地（市）级'\n" +
                "   when dim_pur.administration_level_id = '4' then '县（区）级'\n" +
                "   when dim_pur.administration_level_id = '9' then '县（区）级'\n" +
                "   else '县（区）级'\n" +
                "   end as purchaser_org_level_name,\n" +
                "   case when dim_pnb.pur_unit_branch is null then 'ssxt008'\n" +
                "   else  dim_pnb.pur_unit_branch end as purchaser_org_branch_code,\n" +
                "   case when dim_pnb.pur_unit_branch = 'ssxt001' then '教科文'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt002' then '行政政法'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt003' then '经济建设'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt004' then '农业'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt005' then '社会保障'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt006' then '企业'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt007' then '金融'\n" +
                "   when dim_pnb.pur_unit_branch = 'ssxt008' then '其他'\n" +
                "   when dim_pnb.pur_unit_branch is null then '其他'\n" +
                "   end as purchaser_org_branch_name,\n" +
                "   t_order_d.gmt_updated_time as contract_sign_time,\n" +
                "   t_order_d.gmt_updated_time as contract_record_time,\n" +
                "   case when t_order_d.gp_content_types = '货物' then 'htxz001'\n" +
                "   \t\twhen t_order_d.gp_content_types = '工程' then 'htxz002'\n" +
                "        when t_order_d.gp_content_types = '服务' then 'htxz003'\n" +
                "        when t_order_d.gp_content_types like '%货物%' then 'htxz001'\n" +
                "   \t\twhen t_order_d.gp_content_types like '%工程%' then 'htxz002'\n" +
                "        when t_order_d.gp_content_types like '%服务%' then 'htxz002'\n" +
                "        else 'htxz003'\n" +
                "        end as contract_property_code,\n" +
                "       case when t_order_d.gp_content_types = '货物' then '货物'\n" +
                "   \t\twhen t_order_d.gp_content_types = '工程' then '工程'\n" +
                "        when t_order_d.gp_content_types = '服务' then '服务'\n" +
                "        when t_order_d.gp_content_types like '%货物%' then '货物'\n" +
                "   \t\twhen t_order_d.gp_content_types like '%工程%' then '工程'\n" +
                "        when t_order_d.gp_content_types like '%服务%' then '服务'\n" +
                "        else '货物'\n" +
                "        end as contract_property_name,\n" +
                "    date_format(t_order_d.gmt_updated_time,'yyyy') as sign_year,\n" +
                "    date_format(t_order_d.gmt_updated_time,'yyyyMM') as sign_month,\n" +
                "    date_format(t_order_d.gmt_updated_time,'yyyy0') || QUARTER(t_order_d.gmt_updated_time) as sign_quarter,\n" +
                "   t_order_d.total_amount as contract_amount,\n" +
                "   \t\tpre_zjxz.budget_amount as budget_total_amount,\n" +
                "        pre_zjxz.actual_amount as actual_total_amount,\n" +
                "        t_order_d.order_status_name as contract_status,\n" +
                "   \t\tt_order_d.trade_status_name as contract_status_type,\n" +
                "        case when t_order_d.total_quantity >0 \n" +
                "        then t_order_d.total_quantity\n" +
                "\t\telse 1 \n" +
                "        END as actual_total_quantity,\n" +
                "        t_order_d.supplier_org_name as supplier_org_name,\n" +
                "        case when t_s.enterprise_scale_name ='大型企业' then 'gysgm004' \n" +
                "    \twhen t_s.enterprise_scale_name ='中型企业' then 'gysgm003' \n" +
                "    \telse 'gysgm002' end as supplier_supply_scale_code,\n" +
                "        case when t_s.enterprise_scale_name ='大型企业' then '大型企业' \n" +
                "    \twhen t_s.enterprise_scale_name ='中型企业' then '中型企业' \n" +
                "    \telse '小微型企业' end as supplier_supply_scale_name,\n" +
                "        t_s.first_settled_district_code as supplier_org_district_code,\n" +
                "        t_s.first_settled_district_name as supplier_org_district_name,\n" +
                "        zjxz.purchaseplan_apply_year,\n" +
                "        zjxz.pur_item,\n" +
                "        zjxz.pur_contract_item,\n" +
                "        zjxz.pur_item_root,\n" +
                "        zjxz.measure_unit,\n" +
                "        zjxz.pur_item_name,\n" +
                "        zjxz.pur_contract_item_name,\n" +
                "        t_order_d.trade_id order_id,\n" +
                "        t_order_d.biz_type,\n" +
                "        zjxz.purchaseplan_id,\n" +
                "        zjxz.czb_name\n" +
                " from \n" +
                " dwd.dwd_trade_order_detail t_order_d\n" +
                " left outer join dim.dim_purchaser_org dim_pur \n" +
                " on t_order_d.purchaser_org_id = dim_pur.purchaser_org_id \n" +
                " left outer join dim.dim_pur_unit_branch dim_pnb \n" +
                " on dim_pnb.pur_org_id = t_order_d.purchaser_org_id\n" +
                " left outer join pre_m_code on t_order_d.trade_id = pre_m_code.order_id and t_order_d.biz_type = pre_m_code.biz_type\n" +
                " left outer join pre_t_code on t_order_d.trade_id = pre_t_code.order_id and t_order_d.biz_type = pre_t_code.biz_type\n" +
                " left outer join dim.dim_lkp_common_table d on d.code = pre_t_code.code and d.type = 'input.organ_form_status'\n" +
                "left outer join dim.dim_lkp_common_table e on e.code = pre_m_code.code and e.type = 'input.organ_mode_status'\n" +
                "left outer join zjxz on t_order_d.trade_id = zjxz.order_id  and t_order_d.biz_type = zjxz.biz_type\n" +
                "left outer join dim.dim_supplier_org t_s on t_s.supplier_org_id = t_order_d.supplier_org_id\n" +
                "left outer join pre_zjxz on pre_zjxz.biz_type=t_order_d.biz_type and pre_zjxz.order_id=t_order_d.trade_id\n" +
                "where t_order_d.is_purchaseplan = 1 )\n" +
                "\n" +
                "SELECT procurement_agency_type_name,\n" +
                "procurement_agency_type_code,\n" +
                "case \n" +
                "when  procurement_type_code ='zzxs003' and contract_property_code='htxz003'\n" +
                "and procurement_method_code in('cgfs004','cgfs006') then 'zzxs001'\n" +
                "when procurement_type_code ='zzxs003' and procurement_method_code in ('cgfs006','cgfs007','cgfs009') then 'zzxs001' \n" +
                "when contract_property_code='htxz003' and procurement_type_code ='-' and procurement_method_code in('cgfs004','cgfs006') then '-'\n" +
                "else procurement_type_code end as procurement_type_code,\n" +
                "case \n" +
                "when  procurement_type_name ='分散采购' and contract_property_name='服务'\n" +
                "and procurement_method_name in('询价','协议供货') then '集中采购'\n" +
                "when procurement_type_name ='分散采购' and procurement_method_name in ('协议供货','定点采购','电子卖场') then '集中采购' \n" +
                "when contract_property_name ='服务' and procurement_type_name ='-' and procurement_method_name in('询价','协议供货') then '-'\n" +
                "else procurement_type_name end as procurement_type_name,\n" +
                "case \n" +
                "when contract_property_code='htxz003' and procurement_type_code='-' and procurement_method_code in('cgfs004','cgfs006') then 'cgfs009'\n" +
                "when \n" +
                "contract_property_code = 'htxz003' and procurement_method_code\n" +
                "in('cgfs004','cgfs006') THEN 'cgfs009'\n" +
                "else procurement_method_code end as procurement_method_code,\n" +
                "case \n" +
                "when contract_property_name ='服务' and procurement_type_name ='-' and procurement_method_name in('询价','协议供货') then '电子卖场'\n" +
                "when \n" +
                "contract_property_name = '服务' and procurement_method_name\n" +
                "in('询价','协议供货') THEN '电子卖场'\n" +
                "else procurement_method_name end as procurement_method_name,\n" +
                "id,\n" +
                "contract_id,\n" +
                "contract_no,\n" +
                "contract_name,\n" +
                "handler,\n" +
                "entry_man,\n" +
                "implementation_form,\n" +
                "project_memo,\n" +
                "contract_memo,\n" +
                "purchaser_org_name,\n" +
                "purchaser_org_id,\n" +
                "district_code,\n" +
                "purchaser_org_property_code,\n" +
                "purchaser_org_property_name,\n" +
                "purchaser_org_level_code,\n" +
                "purchaser_org_level_name,\n" +
                "purchaser_org_branch_code,\n" +
                "purchaser_org_branch_name,\n" +
                "contract_sign_time,\n" +
                "contract_record_time,\n" +
                "contract_property_code,\n" +
                "contract_property_name,\n" +
                "sign_year,\n" +
                "sign_month,\n" +
                "sign_quarter,\n" +
                "contract_amount,\n" +
                "budget_total_amount,\n" +
                "actual_total_amount,\n" +
                "contract_status,\n" +
                "contract_status_type,\n" +
                "actual_total_quantity,\n" +
                "supplier_org_name,\n" +
                "supplier_supply_scale_code,\n" +
                "supplier_supply_scale_name,\n" +
                "supplier_org_district_code,\n" +
                "supplier_org_district_name,\n" +
                "purchaseplan_apply_year,\n" +
                "pur_item,\n" +
                "pur_contract_item,\n" +
                "pur_item_root,\n" +
                "measure_unit,\n" +
                "pur_item_name,\n" +
                "pur_contract_item_name,\n" +
                "order_id,\n" +
                "biz_type,\n" +
                "purchaseplan_id,\n" +
                "czb_name\n" +
                "from rpt_contract";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }

    @Test
    public void withInsertTest() {
        String sql = "WITH tmp1 AS\n" +
                "    (SELECT cast(member_id AS string) AS member_id,\n" +
                "            account_created_date,\n" +
                "            case when account_created_date is null then 0 else 1 end as is_openaccount,\n" +
                "            CASE\n" + "                WHEN accum_deposit_times>0 THEN 1\n" +
                "                ELSE 0\n" + "            END AS is_deposit,\n" +
                "            CASE\n" +
                "                WHEN accum_rollin_times>0 THEN 1\n" +
                "                ELSE 0\n" +
                "            END AS is_rollin,\n" +
                "            CASE\n" +
                "                WHEN accum_trade_times>0 THEN 1\n" +
                "                ELSE 0\n" +
                "            END AS is_deal,\n" +
                "            CASE\n" +
                "                WHEN accum_withdraw_times>0 THEN 1\n" +
                "                ELSE 0\n" +
                "            END AS is_withdraw,\n" +
                "            CASE\n" +
                "                WHEN accum_rollout_times>0 THEN 1\n" +
                "                ELSE 0\n" +
                "            END AS is_rollout\n" +
                "     FROM lb_bi_presto.member_properties\n" +
                "     WHERE pt='20210812'\n" +
                "     GROUP BY cast(member_id AS string),\n" +
                "              account_created_date,\n" +
                "              case when account_created_date is null then 1 else 0 end,\n" +
                "              CASE\n" +
                "                  WHEN accum_deposit_times>0 THEN 1\n" +
                "                  ELSE 0\n" +
                "              END,\n" +
                "              CASE\n" +
                "                  WHEN accum_rollin_times>0 THEN 1\n" +
                "                  ELSE 0\n" +
                "              END,\n" +
                "              CASE\n" +
                "                  WHEN accum_trade_times>0 THEN 1\n" +
                "                  ELSE 0\n" +
                "              END,\n" +
                "              CASE\n" +
                "                  WHEN accum_withdraw_times>0 THEN 1\n" +
                "                  ELSE 0\n" +
                "              END,\n" +
                "              CASE\n" +
                "                  WHEN accum_rollout_times>0 THEN 1\n" +
                "                  ELSE 0\n" + "              END),\n" +
                "        tmp2 AS\n" +
                "    (SELECT *\n" +
                "     FROM lb_bi_user.dws_user_profile_attribution_invite_d\n" +
                "     WHERE pt='20210812'),\n" +
                "     tmp3 as (select member_id,\n" +
                "                     case when state=0 then '初始化' when state=1 then '身份信息已认证' when state=2 then '已通过活体验证' when state=3 then '用户提交中'\n" +
                "                     when state=4 then '用户提交待确认' when state=5 then '用户已提交' when state=6 then '等待提交给辉立' when state=7 then '辉立审核中'\n" +
                "                     when state=8 then '审核失败' when state=9 then '开户完成'\n" +
                "                     end as open_account_status\n" +
                "              from lb_member_service.account_applications\n" + "     )\n" +
                "    INSERT overwrite TABLE lb_bi_user.dim_user_profile_attribution_dynamic_d partition(pt='20210812')\n" +
                "    SELECT tmp1.member_id,\n" +
                "         tmp1.account_created_date,\n" +
                "         tmp1.is_openaccount,\n" +
                "         tmp1.is_deposit,\n" +
                "         tmp1.is_rollin,\n" +
                "         tmp1.is_deal,\n" +
                "         tmp1.is_withdraw,\n" +
                "         tmp1.is_rollout,\n" +
                "         CASE\n" +
                "             WHEN tmp2.invite_num_all>0 THEN 1\n" +
                "             ELSE 0\n" +
                "         END AS is_inviter,\n" +
                "         nvl(tmp2.invite_num_all,0) AS invite_num_all,\n" +
                "         nvl(tmp2.invite_num_30,0) AS invite_num_30,\n" +
                "         tmp3.open_account_status\n" +
                "    FROM tmp1\n" +
                "    LEFT JOIN tmp2 ON tmp1.member_id=tmp2.member_id\n" +
                "    left join tmp3 on tmp1.member_id=tmp3.member_id\n" +
                "    GROUP BY tmp1.member_id,\n" +
                "         tmp1.account_created_date,\n" +
                "         tmp1.is_openaccount,\n" +
                "         tmp1.is_deposit,\n" +
                "         tmp1.is_rollin,\n" +
                "         tmp1.is_deal,\n" +
                "         tmp1.is_withdraw,\n" +
                "         tmp1.is_rollout,\n" +
                "         CASE\n" +
                "             WHEN tmp2.invite_num_all>0 THEN 1\n" +
                "             ELSE 0\n" +
                "         END,\n" +
                "         nvl(tmp2.invite_num_all,0),\n" +
                "         nvl(tmp2.invite_num_30,0),\n" +
                "         tmp3.open_account_status";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }

    @Test
    public void insertTest() {
        String sql = "insert into table users PARTITION(ds='20170220') values('NAME')";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }

    @Test
    public void insertSelectTest() {
        String sql = "insert into table test select d.id from file_download d  left join file_transfer t on t.id = d.transfer_id";
        sql = "insert overwrite table cite select * from (select a from tab2) c";

        TableSib res = SparkSqlHelper.getTableSib(sql);
        System.out.println(res);
    }
}
