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
import java.util.stream.Collectors;

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
    public static void copyDiProperties(DIJobContent diJobContent, JobInfoExecuteDetailDto.DiJobDetailsDto diResponse) {
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
                    List<MappingColumnDto> columnDtoList = JSON.parseArray(srcColumns, MappingColumnDto.class);
                    List<MappingColumnDto> mappedColumnDtoList = columnDtoList.stream().filter(e -> e.getMappedColumn() != null).collect(Collectors.toList());
                    diResponse.setSrcCols(mappedColumnDtoList);
                }
                diResponse.setDiQuery(diJobContent.getSrcQuery());
                diResponse.setMergeSql(diJobContent.getMergeSql());
                break;
        }
    }


    /**
     * 通过BeanUtils.copyProperties 拷贝对象，简易封装
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        try {
            T target = clazz.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <E, T> List<T> copyList(List<E> list, Class<T> clazz) {
        return list.stream().map(e -> copy(e, clazz)).collect(Collectors.toList());
    }

}
