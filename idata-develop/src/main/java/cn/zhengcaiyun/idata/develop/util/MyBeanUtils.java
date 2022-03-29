package cn.zhengcaiyun.idata.develop.util;

import cn.zhengcaiyun.idata.develop.constant.enums.DiConfigModeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoExecuteDetailDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhanqian
 * @date 2022/3/25 3:30 PM
 * @description
 */
public class MyBeanUtils {

    /**
     * 拷贝对象
     * @param diJobContent
     * @param diResponse
     */
    public static void copyProperties(DIJobContent diJobContent, JobInfoExecuteDetailDto.DiJobDetailsDto diResponse) {
        BeanUtils.copyProperties(diJobContent, diResponse);
        DiConfigModeEnum modeEnum = DiConfigModeEnum.getByValue(diJobContent.getConfigMode());
        switch (modeEnum) {
            case SCRIPT:
                // 逗号分隔
                String scriptSelectColumns = diJobContent.getScriptSelectColumns();
                String scriptKeyColumns = diJobContent.getScriptKeyColumns();
                List<String> keyList = StringUtils.isNotBlank(scriptKeyColumns) ? Arrays.asList(scriptKeyColumns.split(",")) : new ArrayList<>();
                if (StringUtils.isNotBlank(scriptSelectColumns)) {
                    List<MappingColumnDto> srcCols = new ArrayList<>();

                    for (String column : scriptSelectColumns.split(",")) {
                        MappingColumnDto dto = new MappingColumnDto();
                        if (StringUtils.contains(column, " ")) {
                            String[] array = column.split(" ");
                            dto.setName(array[array.length - 1]);
                            dto.setMappingSql(column);
                        } else {
                            dto.setName(column);
                        }

                        if (keyList.contains(dto.getName())) {
                            dto.setPrimaryKey(true);
                        } else {
                            dto.setPrimaryKey(false);
                        }
                        srcCols.add(dto);
                    }

                    diResponse.setSrcCols(srcCols);
                }
                diResponse.setDiQuery(diJobContent.getScriptQuery());
                diResponse.setMergeSql(diJobContent.getScriptMergeSql());
                break;
            case VISIBLE:
                // json格式
                String srcColumns = diJobContent.getSrcColumns();
                if (StringUtils.isNotBlank(srcColumns)) {
                    diResponse.setSrcCols(JSON.parseArray(srcColumns, MappingColumnDto.class));
                }
                diResponse.setDiQuery(diJobContent.getSrcQuery());
                diResponse.setMergeSql(diJobContent.getMergeSql());
                break;
        }
    }
}
