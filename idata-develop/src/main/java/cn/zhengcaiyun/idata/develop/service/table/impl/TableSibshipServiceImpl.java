package cn.zhengcaiyun.idata.develop.service.table.impl;

import cn.zhengcaiyun.idata.connector.util.SparkSqlHelper;
import cn.zhengcaiyun.idata.connector.util.SparkSqlUtil;
import cn.zhengcaiyun.idata.connector.util.SqlDynamicParamTool;
import cn.zhengcaiyun.idata.connector.util.model.SqlTypeEnum;
import cn.zhengcaiyun.idata.connector.util.model.TableObj;
import cn.zhengcaiyun.idata.connector.util.model.TableSib;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.TableSibshipDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.TableSibshipVO;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.query.JobOutputQuery;
import cn.zhengcaiyun.idata.develop.dal.repo.job.SqlJobRepo;
import cn.zhengcaiyun.idata.develop.dto.table.TableSibship;
import cn.zhengcaiyun.idata.develop.service.table.TableSibshipService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author makejava
 * @since 2022-09-29 10:36:32
 */
@Service
public class TableSibshipServiceImpl implements TableSibshipService {
    @Autowired
    private TableSibshipDao tableSibshipDao;

    @Autowired
    private SqlJobRepo sqlJobRepo;

    @Autowired
    private JobOutputMyDao jobOutputMyDao;


    @Override
    public TableSibshipVO getByTableName(String fullTableName, Integer upper, Integer lower) {
        String[] arr = fullTableName.split("\\.");
        String db = "";
        String tableName = "";
        if (arr.length == 2) {
            db = arr[0];
            tableName = arr[1];
        } else {
            tableName = arr[0];
        }
        List<TableSibshipVO> list = new ArrayList<>();
        list.addAll(this.getChildren(db, tableName, lower, 0));
        list.addAll(this.getParents(db, tableName, upper, 0));

        TableSibshipVO cur = new TableSibshipVO();
        cur.setTableName(fullTableName);
        cur.setChildren(list);
        return cur;
    }

    @Override
    public Set<TableSibshipVO> getJobs(String fullTableName) {
        String[] arr = fullTableName.split("\\.");
        String db = "";
        String tableName = "";
        if (arr.length == 2) {
            db = arr[0];
            tableName = arr[1];
        } else {
            tableName = arr[0];
        }
        List<TableSibshipVO> sqlJobs = tableSibshipDao.getSqlJobs(db, tableName);
        List<TableSibshipVO> diJobs = tableSibshipDao.getDIJobs(fullTableName);
        List<TableSibshipVO> jobs = tableSibshipDao.getJobs(fullTableName);

        Set<TableSibshipVO> allSet = new HashSet<>();
        allSet.addAll(sqlJobs);
        allSet.addAll(diJobs);
        allSet.addAll(jobs);

        //血缘表获取作业
        return allSet;
    }

    private List<TableSibshipVO> getParents(String db, String tableName, int level, int curLevel) {
        List<TableSibshipVO> paernts = new ArrayList<>();

        curLevel++;
        if (curLevel > level) {
            return paernts;
        }

        List<TableSibship> curList = tableSibshipDao.getParents(db, tableName);
        for (TableSibship cur : curList) {
            TableSibshipVO vo = new TableSibshipVO();
            vo.setRelation("prev");
            vo.setTableName(StringUtils.isEmpty(cur.getSourceDb()) ? cur.getSourceTableName() : cur.getSourceDb() + "." + cur.getSourceTableName());
            vo.setChildren(this.getChildren(cur.getSourceDb(), cur.getSourceTableName(), level, curLevel));
            paernts.add(vo);
        }

        return paernts;
    }

    private List<TableSibshipVO> getChildren(String sourceDb, String sourceTableName, int level, int curLevel) {
        List<TableSibshipVO> children = new ArrayList<>();

        curLevel++;
        if (curLevel > level) {
            return children;
        }

        List<TableSibship> curList = tableSibshipDao.getChildren(sourceDb, sourceTableName);
        for (TableSibship cur : curList) {
            TableSibshipVO vo = new TableSibshipVO();
            vo.setRelation("next");
            vo.setTableName(StringUtils.isEmpty(cur.getDb()) ? cur.getTableName() : cur.getDb() + "." + cur.getTableName());
            vo.setChildren(this.getChildren(cur.getDb(), cur.getTableName(), level, curLevel));
            children.add(vo);
        }

        return children;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sibParse(Long jobId, Integer jobVersion, String jobTypeCode, String environment, String nickname) {
        if (!JobTypeEnum.SQL_SPARK.getCode().equals(jobTypeCode) || "stag".equals(environment)) {
            return;
        }

        DevJobContentSql jobContent = sqlJobRepo.query(jobId, jobVersion);
        if (jobContent == null) {
            return;
        }
        String content = jobContent.getSourceSql();
        // 替换动态参数，避免影响SQL解析
        String replacedSql = SqlDynamicParamTool.replaceDynamicParam(content, null, (extParam) -> "dummy_param");
        String[] sqlArr = SparkSqlUtil.splitToMultiSql(replacedSql);

        List<TableSibship> sibList = new ArrayList<>();
        for (String sql : sqlArr) {
            TableSib tableSib = null;
            try {
                tableSib = SparkSqlHelper.getTableSib(sql);
            } catch (Exception e) {
                System.out.println();
            }

            if (tableSib == null || tableSib.getSqlType() == null) {
                continue;
            }
            List<TableObj> inputTableList = tableSib.getInputTables();
            if (CollectionUtils.isEmpty(inputTableList)) {
                continue;
            }

            String outputDb = "";
            String outputTableName = "";
            if (tableSib.getSqlType() == SqlTypeEnum.INSERT_SELECT || tableSib.getSqlType() == SqlTypeEnum.CREATE_TABLE_AS_SELECT) {
                TableObj outputTable = tableSib.getOutputTable();
                outputDb = outputTable.getDb();
                outputTableName = outputTable.getTableName();

            } else if (tableSib.getSqlType() == SqlTypeEnum.SELECT) {
                JobOutputQuery query = new JobOutputQuery();
                query.setJobId(jobId);
                query.setEnvironment(environment);
                query.setDel(0);
                JobOutput jobOutput = jobOutputMyDao.queryOne(query);
                String dest = jobOutput.getDestTable();
                if (StringUtils.isNotEmpty(dest)) {
                    String[] arr = dest.split("\\.");
                    if (arr.length == 2) {
                        outputDb = arr[0];
                        outputTableName = arr[1];
                    } else {
                        outputTableName = arr[0];
                    }
                }
            }

            if ("".equals(outputTableName)) {
                continue;
            }

            for (TableObj input : inputTableList) {
                if (outputDb.equals(input.getDb()) && outputTableName.equals(input.getTableName())) {
                    continue;
                }

                TableSibship sibship = new TableSibship();
                sibship.setJobId(jobId);
                sibship.setDb(outputDb);
                sibship.setTableName(outputTableName);
                sibship.setSourceDb(input.getDb());
                sibship.setSourceTableName(input.getTableName());
                sibship.setCreator(nickname);
                sibship.setEditor(nickname);

                sibList.add(sibship);
            }
        }
        tableSibshipDao.delByJobId(jobId);
        if (CollectionUtils.isNotEmpty(sibList)) {
            tableSibshipDao.insertBatch(sibList);
        }
    }

    @Override
    public List<JobPublishRecord> getPublishedJobs(Integer startIndex) {
        return tableSibshipDao.getPublishedJobs(startIndex);
    }


}
