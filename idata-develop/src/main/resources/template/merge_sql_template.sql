#{#isMulPartition ? 'set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.max.dynamic.partitions.pernode=1000;' : ''}

alter table #{#tmpTable} drop if exists partition(pt<'${day-3d}');

insert overwrite table #{#tmpTable} partition(pt='${day}' #{#isMulPartition ? ',num' : ''})
select #{#coalesceColumns}
       #{#isMulPartition ? ',coalesce(t1.num, t2.num) num' : ''}
from  
    (select
        #{#columns}
        #{#isMulPartition ? ',num' : ''}
    from #{#destTable}) t1
    full join
    (select
        #{#columns}
        #{#isMulPartition ? ',num' : ''}
    from #{#tmpTable}
    where pt='${day-1d}') t2
    on #{#keyCondition} #{#isMulPartition ? 'and t1.num=t2.num' : ''};

insert overwrite table #{#destTable} #{#isMulPartition ? 'partition(num)' : ''}
select
    #{#columns}
    #{#isMulPartition ? ',num' : ''}
from #{#tmpTable} where pt='${day}';
