#{#isMulPartition ? 'set hive.exec.dynamic.partition=true; set hive.exec.dynamic.partition.mode=nonstrict; set hive.exec.max.dynamic.partitions.pernode=1000;' : ''}
alter table #{#tmpTable} drop if exists partition(pt<'${day-#{#days}d}');

insert overwrite table #{#tmpTable} partition(pt='${day}')
select #{#alisColumns} #{#isMulPartition ? ' ,t1.num' : ''}
from
(
select #{#columns} #{#isMulPartition ? ' ,num' : ''}
from #{#tmpTable} where pt='${day-1d}'
) t1
left join
(
select #{#columns} #{#isMulPartition ? ' ,num' : ''}
from #{#destTable}
) t2
on #{#keyCondition} #{#isMulPartition ? 'and t1.num=t2.num' : ''}
where #{#whereKeyConditionParam}
union all
select #{#columns} #{#isMulPartition ? ' ,num' : ''}
from #{#destTable};

insert overwrite table #{#destTable} #{#isMulPartition ? 'partition(num)' : ''}
select #{#columns} #{#isMulPartition ? ' ,num' : ''}
from #{#tmpTable} where pt='${day}';