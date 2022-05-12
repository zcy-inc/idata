import { useEffect, useState } from 'react';
import { getTask, getTaskVersions } from '@/services/datadev';
import { VersionStatusDisplayMap, VersionStatus, EnvRunningState } from '@/constants/datadev';
import type { TaskVersion, Task } from '@/types/datadev';
import { usePersistFn } from '@/hooks';

export type VersionOption = TaskVersion & {
  label: string;
  value: string;
};

export const useJob = ({
  jobId,
  getContent,
}: {
  jobId: number;
  getContent: (basic: Task, jobId: number, version: number) => Promise<any>;
}) => {
  const [jobBasic, setJobBasic] = useState<Task>(); // 作业基本信息
  const [versionOption, setVersionOption] = useState<VersionOption>();
  const [versions, setVersions] = useState<VersionOption[]>([]);
  const [content, setContent] = useState<any>({});
  const version = versionOption?.version;

  const transformLabel = ({
    environment = '',
    versionStatus,
    envRunningState,
    versionDisplay,
  }: TaskVersion) => {
    const verState = VersionStatusDisplayMap[versionStatus];
    let runState = '';
    if (versionStatus === VersionStatus.PUBLISHED && envRunningState === EnvRunningState.PAUSED) {
      runState = '(暂停)';
    }
    return `${versionDisplay}-${environment}-${verState}${runState}`;
  };

  const transormValue = ({ version, environment }: TaskVersion) => `${version}#${environment}`;

  // 刷新基本信息
  const refreshTaskBasic = usePersistFn(async () => {
    const { data: basic } = await getTask({ id: jobId });
    setJobBasic(basic);
  }) as () => Promise<void>;

  // 刷新版本信息、详细信息
  const refreshTask = usePersistFn(async () => {
    const { data } = await getTaskVersions({ jobId });
    const versions = data.map((item) => ({
      ...item,
      label: transformLabel(item),
      value: transormValue(item),
    }));
    setVersions(versions);
    const cur = Array.isArray(versions) && versions.length > 0 ? versions[0] : undefined;
    setVersionOption(cur);
  }) as () => Promise<void>;

  // 刷新详细信息
  const refreshContent = usePersistFn(async (version: number) => {
    if (jobBasic) {
      const content = await getContent(jobBasic, jobId, version);
      setContent(content);
    }
  });

  useEffect(() => {
    if (versionOption) {
      refreshContent?.(versionOption.version);
    }
  }, [versionOption]);

  useEffect(() => {
    (async function () {
      if (jobId) {
        await refreshTaskBasic();
        await refreshTask();
      }
    })();
  }, [jobId, refreshTask, refreshTaskBasic]);

  return {
    jobBasic,
    versionOption,
    setVersionOption,
    version,
    versions,
    content,
    setContent,
    refreshTaskBasic,
    refreshTask,
  };
};
