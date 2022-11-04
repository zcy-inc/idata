package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobExtInfoDto extends JobInfoDto {
    /**
     * 作业版本
     */
    private Integer version;
    /**
     * 作业版本描述
     */
    private String versionDisplay;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVersionDisplay() {
        return versionDisplay;
    }

    public void setVersionDisplay(String versionDisplay) {
        this.versionDisplay = versionDisplay;
    }

    public static JobExtInfoDto from(JobInfo info) {
        JobExtInfoDto dto = new JobExtInfoDto();
        BeanUtils.copyProperties(info, dto);
        Optional<JobTypeEnum> typeEnumOptional = JobTypeEnum.getEnum(info.getJobType());
        typeEnumOptional.ifPresent(typeEnum -> dto.setJobType(typeEnum));
        return dto;
    }

}