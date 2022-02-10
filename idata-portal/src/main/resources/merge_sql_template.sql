[set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict; 
set hive.exec.max.dynamic.partitions.pernode=1000;]

alter table %tmpTable% drop if exists partition(pt<'${day-3d}');

insert overwrite table %tmpTable% partition(pt='${day}' [,num])
select %selectCoalesceStr%
from  
    (select
        %selectStr%
    from %hiveTable%) t1
    full join
    (select
        %selectStr%
    from %tmpTable%
    where pt='${day-1d}') t2
    on %keyCondition% [and t1.num=t2.num];

insert overwrite table %hiveTable% [partition(num)]
select  
    %selectStr%
from %tmpTable% where pt='${day}';;