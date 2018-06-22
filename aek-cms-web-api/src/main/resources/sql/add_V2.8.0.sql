#内容表增加to_tenant_ids_copy(指定的机构的ids副本)
ALTER TABLE `cms_content` ADD COLUMN `to_tenant_ids_copy` text COMMENT '指定的机构的ids副本' AFTER `to_tenant_ids`;
COMMIT;