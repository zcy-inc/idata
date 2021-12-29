import type { FC } from 'react';
import React, { useEffect } from 'react';
import { gantt } from 'dhtmlx-gantt';
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css';
gantt.plugins({
  tooltip: true
});
interface GanttProps {
  tasks: {
    data: Record<string, any>[];
  },
  config?: Record<string, any>,
  templates?: Record<string, any>,
  zoom: string
}

const Gantt: FC<GanttProps> = ({tasks, config, zoom, templates}) => {
  let ganttContainer: any;
  let mouse_on_grid = false;
  const generateGantt = async ()  =>{

    gantt.i18n.setLocale("cn"); // 设置中文编码
    gantt.config.date_scale = '%d/%m/%Y 周%D'; // 设置日期格式
   
    gantt.config.tooltip_offset_x = -30;
    gantt.config.tooltip_offset_y = 2;

    // 添加tooltip
    gantt.attachEvent('onGanttReady', function () {
      const tooltips = gantt.ext.tooltips;
      tooltips.tooltip.setViewport((gantt as any).$task_data);
    }, null);

    // column上悬浮的时候不展示tooltip
    gantt.attachEvent("onMouseMove", function (id, e){
      if (e.clientX >= gantt.config.grid_width + 320) mouse_on_grid = true;
      else mouse_on_grid = false;
    }, null);
    
    gantt.clearAll();
    gantt.init(ganttContainer); // 初始化 dhtmlxGantt 到 ganttContainer 容器中
    gantt.parse(tasks); // 将数据注入到甘特图
    gantt.scrollTo(0, 0);
  }

  useEffect(() => {
    generateGantt();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  useEffect(() => {
    gantt.config = {
      ...gantt.config,
      ...config,
    };
   
    gantt.templates = {
      ...gantt.templates,
      ...templates,
    };
    if(templates?.tooltip_text) {
      gantt.templates.tooltip_text = (start: Date, end: Date, task: any) => {
        return templates?.tooltip_text(start, end, task, mouse_on_grid)
      }
    }
    generateGantt();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [config, tasks, templates])

  useEffect(() => {
    switch (zoom) {
      case 'day':
        gantt.config.scale_unit = 'day';
        gantt.config.date_scale = '%d %M';
        gantt.config.scale_height = 60;
        gantt.config.min_column_width = 30;
        gantt.config.subscales = [
          { unit:'hour', step:1, date:'%H:00' }
        ];
      break;
      default:
      break;
    }
    generateGantt();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [zoom])

  return (
    <div
      ref={ el=> { ganttContainer = el } }
      className='gantt-container'
    />
  )
}

export default Gantt;