alter table dev_label
    add hidden tinyint default 0 null comment '是否隐藏不展示';



INSERT INTO idata.dev_label_define (del, creator, create_time, editor, edit_time, label_code, label_name, label_tag,
                                    label_param_type, label_attributes, special_attribute, subject_type, label_index,
                                    label_required, label_scope, folder_id)
VALUES (1, '系统管理员', '2021-08-18 22:30:35.267', '', '2021-11-26 13:55:13.704', 'hiveColumnComment:LABEL',
        '同步至hive的注释', 'STRING_LABEL', 'STRING', '[]', '{}', 'COLUMN', null, 0, null, 0);

INSERT INTO idata.dev_label_define (del, creator, create_time, editor, edit_time, label_code, label_name, label_tag,
                                    label_param_type, label_attributes, special_attribute, subject_type, label_index,
                                    label_required, label_scope, folder_id)
VALUES (1, '系统管理员', '2021-08-18 22:30:35.267', '', '2021-11-26 13:55:13.704', 'hiveColumnComment:LABEL',
        '同步至hive的注释', 'STRING_LABEL', 'STRING', '[]', '{}', 'COLUMN', null, 0, null, 0);

INSERT INTO idata.dev_label_define (del, creator, create_time, editor, edit_time, label_code, label_name, label_tag,
                                    label_param_type, label_attributes, special_attribute, subject_type, label_index,
                                    label_required, label_scope, folder_id)
VALUES (1, '系统管理员', '2021-08-18 22:30:35.267', '', '2021-11-26 13:55:13.396', 'hiveColumnType:LABEL',
        '同步至hive的类型', 'STRING_LABEL', 'STRING', '[]', '{}', 'COLUMN', null, 0, null, 0);

INSERT INTO idata.dev_label_define (del, creator, create_time, editor, edit_time, label_code, label_name, label_tag,
                                    label_param_type, label_attributes, special_attribute, subject_type, label_index,
                                    label_required, label_scope, folder_id)
VALUES (1, '系统管理员', '2021-08-18 22:30:35.267', '', '2021-11-26 13:55:13.555', 'hiveColumnName:LABEL',
        '同步至hive的名称', 'STRING_LABEL', 'STRING', '[]', '{}', 'COLUMN', null, 0, null, 0);

