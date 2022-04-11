package cn.zhengcaiyun.idata.develop.integration.schedule.dolphin;

public class DIJobContentServiceTest {

    public static void main(String[] args) {
        testMergeSql();
    }

    private static void testMergeSql() {
        String templateSQL = "" +
                "[" +
                "set hive.exec.dynamic.partition=true;\n" +
                "set hive.exec.dynamic.partition.mode=nonstrict;\n" +
                "set hive.exec.max.dynamic.partitions.pernode=1000;" +
                "]\n" +

                "alter table %tmpTable drop if exists partition(pt<'${day-3d}');\n" +

                "insert overwrite table %tmpTable partition(pt='${day}'[, num]) \n" +
                "select %selectCoalesceStr" +
                "from \n" +
                "(select \n%selectStr" +
                "from %hiveTable) t1 \n" +
                "full join \n" +
                "(select \n %selectStr" +
                "from %tmpTable where pt='${day-1d}') t2 \n" +
                "on t1.id=t2.id [and t1.num=t2.num];\n" +
                "insert overwrite table %hiveTable [partition(num)]\n" +
                "select \n%selectStr" +
                "from %tmpTable where pt='${day}';";
        String s1 = "2623tableName2,2626tableName2";
        String s2 = "shfhfgf[1-2]dkdkdkdkd[df nfn]";
        System.out.println(templateSQL.replaceAll("\\[[^]]*\\]", ""));
        System.out.println(templateSQL.replaceAll("\\[([^]]*)\\]", ""));
        System.out.println(templateSQL.replaceAll("\\]", "").replaceAll("\\[", ""));
//        System.out.println(ReUtil.contains(",", s1));
//        System.out.println(ReUtil.contains(",", s2));
//        System.out.println(ReUtil.isMatch("(\\w+)\\[(\\d+-\\d+)\\]", s2));
//        System.out.println(ReUtil.isMatch("(\\w+)\\[(\\d+-\\d+)\\]", s1));
//        System.out.println(ReUtil.isMatch("(\\d+(\\w+)[,]{0,1})+", s1));

//        System.out.println(ReUtil.get("(\\w+)\\[\\d+-\\d+\\]", s2, 1));
//        System.out.println(ReUtil.get("(\\d+(\\w+)[,]{0,1})+", s1, 0));
//        System.out.println(ReUtil.get("(\\d+(\\w+)[,]{0,1})+", s1, 1));
//        System.out.println(ReUtil.get("(\\d+(\\w+)[,]{0,1})+", s1, 2));
    }

}
