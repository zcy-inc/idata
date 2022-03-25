-- 迁移原数仓设计模块-表 文件夹数据
INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.TABLE", 10005 from dev_folder f WHERE f.folder_type = 'TABLE' and f.del = 0 and f.parent_id = 0;

INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.TABLE", f.parent_id from dev_folder f WHERE f.folder_type = 'TABLE' and f.del = 0 and f.parent_id != 0;

-- 迁移原数仓设计模块-标签 文件夹数据
INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.LABEL", 10006 from dev_folder f WHERE f.folder_type = 'LABEL' and f.del = 0 and f.parent_id = 0;

INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.LABEL", f.parent_id from dev_folder f WHERE f.folder_type = 'LABEL' and f.del = 0 and f.parent_id != 0;

-- 迁移原数仓设计模块-枚举 文件夹数据
INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.ENUM", 10007 from dev_folder f WHERE f.folder_type = 'ENUM' and f.del = 0 and f.parent_id = 0;

INSERT INTO dev_composite_folder (id, del, creator, create_time, editor, edit_time, name, type, belong, parent_id) SELECT f.id, f.del, f.creator, f.create_time, f.editor, f.edit_time, f.folder_name, "FOLDER", "DESIGN.ENUM", f.parent_id from dev_folder f WHERE f.folder_type = 'ENUM' and f.del = 0 and f.parent_id != 0;