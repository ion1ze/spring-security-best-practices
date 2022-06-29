DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(500) NOT NULL COMMENT '用户名',
    `password` VARCHAR(500) NOT NULL COMMENT '密码',
    `enabled`  BOOLEAN      NOT NULL DEFAULT 1 COMMENT '启用',
    `locked`   BOOLEAN      NOT NULL DEFAULT 1 COMMENT '锁定',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `member_user`;
CREATE TABLE `member_user`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(500) NOT NULL COMMENT '用户名',
    `password` VARCHAR(500) NOT NULL COMMENT '密码',
    `enabled`  BOOLEAN      NOT NULL DEFAULT 1 COMMENT '启用',
    `locked`   BOOLEAN      NOT NULL DEFAULT 1 COMMENT '锁定',
    PRIMARY KEY (`id`)
);