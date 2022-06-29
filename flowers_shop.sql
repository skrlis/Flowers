/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : flowers_shop

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2022-06-29 18:18:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fl_admin
-- ----------------------------
DROP TABLE IF EXISTS `fl_admin`;
CREATE TABLE `fl_admin` (
  `adid` int NOT NULL AUTO_INCREMENT,
  `adname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nikename` varchar(20) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `adpwd` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `avatar_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT '0',
  `enable` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`adid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_admin
-- ----------------------------
INSERT INTO `fl_admin` VALUES ('10', 'admin', '管理员', '13955676789', '123321', 'ROLE_ADMIN', '2022-02-26 22:10:14', 'http://localhost:9090/file/dabe2182328841f39c74760430014b8a.png', '0', '1');
INSERT INTO `fl_admin` VALUES ('13', '祖一一', '采购员', '13955676789', '33333', 'ROLE_BUYER', '2022-03-25 09:22:15', 'http://localhost:9090/file/dabe2182328841f39c74760430014b8a.png', '0', '1');
INSERT INTO `fl_admin` VALUES ('16', '王贝贝', '销售员', '13955676789', '123456', 'ROLE_SALER', '2022-04-13 10:12:10', 'http://localhost:9090/file/9562f11e04324af69f0607b8d23415da.png', '0', '1');

-- ----------------------------
-- Table structure for fl_buy
-- ----------------------------
DROP TABLE IF EXISTS `fl_buy`;
CREATE TABLE `fl_buy` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `buyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购单号',
  `buy_date` date NOT NULL COMMENT '采购日期',
  `buy_apply` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '申请人',
  `buyer` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '采购人',
  `buy_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '采购商品',
  `buy_supplier` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '供应商',
  `buy_count` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '采购数量',
  `buy_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '数量单位',
  `buy_price` double NOT NULL DEFAULT '0' COMMENT '单价',
  `buy_status` varchar(5) NOT NULL,
  `reviewer` varchar(20) DEFAULT NULL,
  `reviewdate` date DEFAULT NULL,
  `remarks` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_buy
-- ----------------------------
INSERT INTO `fl_buy` VALUES ('36', 'CG2022005272877', '2022-05-27', '祖一一', '祖一一', '6', '唐山花卉', '6', '盆', '16', '已通过', '哈哈', '2022-05-27', '');
INSERT INTO `fl_buy` VALUES ('37', 'CG202200527893', '2022-05-27', '张三三', '张三三', '8', '天天花卉', '34', '箱/瓶', '12', '未审核', null, null, null);
INSERT INTO `fl_buy` VALUES ('38', 'CG2022005278850', '2022-05-25', '张三三', '张三三', '14', '天天花卉', '2', '盆', '300', '未审核', null, null, null);
INSERT INTO `fl_buy` VALUES ('39', 'CG2022005272397', '2022-05-27', '祖一一', '祖一一', '11', '北京花卉', '2', '盆', '80', '未审核', null, null, null);
INSERT INTO `fl_buy` VALUES ('40', 'CG202200528827', '2022-05-28', '祖一一', '祖一一', '15', '北京花卉', '8', '件/24盆', '130', '已通过', '哈哈', '2022-05-28', '');
INSERT INTO `fl_buy` VALUES ('41', 'CG2022005284012', '2022-05-28', '祖一一', '祖一一', '36', '天天花卉', '6', '盆', '23', '已通过', '哈哈', '2022-05-28', '');

-- ----------------------------
-- Table structure for fl_customer
-- ----------------------------
DROP TABLE IF EXISTS `fl_customer`;
CREATE TABLE `fl_customer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '顾客编号',
  `cus_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `cus_sort` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类别',
  `cus_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系方式',
  `cus_address` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `cus_cost` int NOT NULL COMMENT '总花费',
  `cus_pay` int NOT NULL COMMENT '总支付',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_customer
-- ----------------------------

-- ----------------------------
-- Table structure for fl_foster
-- ----------------------------
DROP TABLE IF EXISTS `fl_foster`;
CREATE TABLE `fl_foster` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sender` varchar(10) NOT NULL COMMENT '寄养人',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系方式',
  `fos_Name` varchar(20) NOT NULL COMMENT '寄养植物名称',
  `fos_Variety` varchar(10) NOT NULL COMMENT '品种',
  `fos_Url` varchar(225) NOT NULL COMMENT '图片',
  `fos_Note` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '养护备注',
  `fos_Starttime` date NOT NULL COMMENT '寄养开始日期',
  `fos_Endtime` date DEFAULT NULL COMMENT '寄养结束日期',
  `fos_Price` double NOT NULL COMMENT '单天价格',
  `fos_Total` double DEFAULT NULL COMMENT '总价',
  `fos_Status` varchar(3) NOT NULL COMMENT '支付状态',
  `fos_Oper` varchar(10) NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_foster
-- ----------------------------

-- ----------------------------
-- Table structure for fl_login
-- ----------------------------
DROP TABLE IF EXISTS `fl_login`;
CREATE TABLE `fl_login` (
  `id` int NOT NULL AUTO_INCREMENT,
  `adname` varchar(50) NOT NULL,
  `create_Time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_login
-- ----------------------------
INSERT INTO `fl_login` VALUES ('6', '哈哈', '2022-04-21 08:55:39');
INSERT INTO `fl_login` VALUES ('7', '哈哈', '2022-04-21 09:06:45');
INSERT INTO `fl_login` VALUES ('73', '王贝贝', '2022-05-02 09:57:58');

-- ----------------------------
-- Table structure for fl_out
-- ----------------------------
DROP TABLE IF EXISTS `fl_out`;
CREATE TABLE `fl_out` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_Id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出库单编号',
  `out_Oper` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `out_Date` date NOT NULL COMMENT '出库日期',
  `out_Name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出库产品名称',
  `out_Customer` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户',
  `out_Supplier` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商',
  `out_Num` int DEFAULT NULL COMMENT '出库数量',
  `out_Unit` varchar(20) NOT NULL COMMENT '数量单位',
  `out_Price` double(20,0) DEFAULT NULL COMMENT '单价',
  `out_Total` double(20,0) DEFAULT NULL COMMENT '总价',
  `out_Profit` double(5,0) DEFAULT NULL COMMENT '利润',
  `out_Paid` double(255,0) DEFAULT NULL COMMENT '已付款',
  `status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_out
-- ----------------------------
INSERT INTO `fl_out` VALUES ('19', '2022005114444', '王贝贝', '2022-05-11', '31', '3', '北京花卉', '2', '盆', '25', '50', '10', '50', '已出库');
INSERT INTO `fl_out` VALUES ('20', '2022005117941', '王贝贝', '2022-05-26', '7', '3', '天天花卉', '16', '盆', '45', '720', '352', '91', '已出库');
INSERT INTO `fl_out` VALUES ('21', '2022005126918', '王贝贝', '2022-05-12', '6', '3', '北京花卉', '4', '盆', '17', '68', '20', '17', '已出库');
INSERT INTO `fl_out` VALUES ('22', '202200527308', '王贝贝', '2022-05-27', '10', '3', '北京花卉', '5', '盆', '200', '1000', '400', '1000', '已出库');
INSERT INTO `fl_out` VALUES ('23', '2022005275825', '王贝贝', '2022-05-26', '14', '1', '天天花卉', '7', '盆', '300', '2100', '700', '2100', '已出库');
INSERT INTO `fl_out` VALUES ('24', '2022005273104', '王贝贝', '2022-05-19', '9', '3', '唐山花卉', '7', '盆', '56', '392', '147', '392', '已出库');
INSERT INTO `fl_out` VALUES ('25', '2022005276575', '王贝贝', '2022-05-20', '10', '3', '北京花卉', '3', '盆', '200', '600', '240', '600', '已出库');
INSERT INTO `fl_out` VALUES ('26', '2022005271397', '王贝贝', '2022-05-26', '10', '3', '北京花卉', '4', '盆', '200', '800', '320', '800', '已出库');
INSERT INTO `fl_out` VALUES ('27', '2022005271375', '王贝贝', '2022-05-27', '9', '12', '唐山花卉', '4', '盆', '56', '224', '84', '224', '已出库');
INSERT INTO `fl_out` VALUES ('28', '2022005276557', '王贝贝', '2022-05-20', '9', '3', '唐山花卉', '4', '盆', '56', '224', '84', '224', '已出库');
INSERT INTO `fl_out` VALUES ('29', '2022005289308', '王贝贝', '2022-05-28', '36', '1', '天天花卉', '6', '盆', '45', '270', '132', '270', '已出库');

-- ----------------------------
-- Table structure for fl_product
-- ----------------------------
DROP TABLE IF EXISTS `fl_product`;
CREATE TABLE `fl_product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '花材编号',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '花材名称',
  `psort` int NOT NULL COMMENT '所属类别',
  `pro_cost` double NOT NULL COMMENT '进价',
  `pro_price` double NOT NULL COMMENT '售价',
  `pro_stock` int DEFAULT '0' COMMENT '库存',
  `pro_from` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商',
  `pro_sales` int DEFAULT '0' COMMENT '销量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_product
-- ----------------------------
INSERT INTO `fl_product` VALUES ('6', '蝴蝶兰', '4', '12', '16', '1', '唐山花卉', '24');
INSERT INTO `fl_product` VALUES ('7', '杜鹃花', '4', '23', '45', '2', '天天花卉', '27');
INSERT INTO `fl_product` VALUES ('8', '水仙花', '4', '12', '15', '20', '天天花卉', '11');
INSERT INTO `fl_product` VALUES ('9', '仙人指', '2', '35', '56', '19', '唐山花卉', '38');
INSERT INTO `fl_product` VALUES ('10', '倒挂金钟', '4', '120', '200', '99', '北京花卉', '23');
INSERT INTO `fl_product` VALUES ('11', '小丽花', '4', '80', '100', '10', '北京花卉', '11');
INSERT INTO `fl_product` VALUES ('12', '元宝树', '13', '100', '150', '100', '唐山花卉', '10');
INSERT INTO `fl_product` VALUES ('13', '巴西木', '13', '20', '80', '20', '桃山小娟', '11');
INSERT INTO `fl_product` VALUES ('14', '非洲茉莉', '13', '200', '300', '83', '天天花卉', '87');
INSERT INTO `fl_product` VALUES ('15', '金钻', '13', '130', '200', '10', '北京花卉', '11');
INSERT INTO `fl_product` VALUES ('16', '玉露', '2', '20', '60', '23', '天天花卉', '11');
INSERT INTO `fl_product` VALUES ('17', '法师', '2', '20', '50', '12', '北京花卉', '1');
INSERT INTO `fl_product` VALUES ('31', '金银花', '13', '20', '30', '18', '北京花卉', '2');
INSERT INTO `fl_product` VALUES ('32', '蝴蝶兰1', '4', '12', '16', '1', '唐山花卉', '24');
INSERT INTO `fl_product` VALUES ('33', '杜鹃花1', '4', '23', '45', '2', '天天花卉', '27');
INSERT INTO `fl_product` VALUES ('34', '金银花', '2', '20', '30', '0', '天天花卉', '0');
INSERT INTO `fl_product` VALUES ('35', '蝴蝶兰3', '4', '12', '16', '1', '唐山花卉', '24');
INSERT INTO `fl_product` VALUES ('36', '杜鹃花3', '4', '23', '45', '2', '天天花卉', '33');

-- ----------------------------
-- Table structure for fl_put
-- ----------------------------
DROP TABLE IF EXISTS `fl_put`;
CREATE TABLE `fl_put` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `put_Id` varchar(225) NOT NULL COMMENT '入库单号',
  `put_Name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入库商品',
  `put_Date` date NOT NULL COMMENT '入库日期',
  `put_Supplier` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商',
  `put_Oper` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `put_Num` int NOT NULL COMMENT '入库数量',
  `put_Unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数量单位',
  `put_Price` double NOT NULL COMMENT '单价',
  `put_Total` double NOT NULL COMMENT '总价',
  `status` varchar(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_put
-- ----------------------------
INSERT INTO `fl_put` VALUES ('23', 'RK202200527926', '17', '2022-05-26', '北京花卉', '张三三', '11', '盆', '20', '220', '已入库');
INSERT INTO `fl_put` VALUES ('24', 'RK2022005281159', '15', '2022-05-28', '北京花卉', '祖一一', '8', '件/24盆', '130', '96', '已入库');
INSERT INTO `fl_put` VALUES ('25', 'RK2022005284501', '36', '2022-05-28', '天天花卉', '祖一一', '6', '盆', '23', '138', '已入库');

-- ----------------------------
-- Table structure for fl_sale
-- ----------------------------
DROP TABLE IF EXISTS `fl_sale`;
CREATE TABLE `fl_sale` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sale_Id` varchar(255) NOT NULL COMMENT '出库通知单编号',
  `saler` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '申请人',
  `sale_date` date NOT NULL COMMENT '出库申请日期',
  `sale_Name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出库产品名称',
  `sale_Supplier` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商',
  `sale_Num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出库数量',
  `sale_Unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数量单位',
  `sale_price` double NOT NULL COMMENT '单价',
  `sale_Total` double NOT NULL COMMENT '总价',
  `sale_Paid` double(20,0) DEFAULT NULL COMMENT '已付款',
  `reviewer` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核人',
  `results` varchar(5) NOT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核人备注',
  `reviewdate` date DEFAULT NULL COMMENT '审核日期',
  `applynote` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_sale
-- ----------------------------
INSERT INTO `fl_sale` VALUES ('9', 'CK2022004296360', '王贝贝', '2022-05-27', '13', '桃山小娟', '18', '盆', '80', '1440', '1440', '哈哈', '未审核', '同意出库', '2022-04-29', null);
INSERT INTO `fl_sale` VALUES ('11', 'CK20220050028900', '王贝贝', '2022-05-02', '31', '北京花卉', '18', '盆', '30', '540', '540', '哈哈', '已通过', '', '2022-05-27', null);
INSERT INTO `fl_sale` VALUES ('13', 'CK2022005263676', '王贝贝', '2022-05-26', '7', '天天花卉', '16', '盆', '23', '368', '20', '哈哈', '未审核', '', '2022-05-26', null);
INSERT INTO `fl_sale` VALUES ('17', 'CK2022005288773', '王贝贝', '2022-05-28', '15', '北京花卉', '6', '盆', '200', '1200', '200', null, '未审核', null, null, null);

-- ----------------------------
-- Table structure for fl_sort
-- ----------------------------
DROP TABLE IF EXISTS `fl_sort`;
CREATE TABLE `fl_sort` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sortname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '花材类别名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_sort
-- ----------------------------
INSERT INTO `fl_sort` VALUES ('2', '多肉');
INSERT INTO `fl_sort` VALUES ('4', '鲜花');
INSERT INTO `fl_sort` VALUES ('13', '绿植');

-- ----------------------------
-- Table structure for fl_supplier
-- ----------------------------
DROP TABLE IF EXISTS `fl_supplier`;
CREATE TABLE `fl_supplier` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商名称',
  `smanager` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人',
  `sup_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系方式',
  `sup_address` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for fl_unit
-- ----------------------------
DROP TABLE IF EXISTS `fl_unit`;
CREATE TABLE `fl_unit` (
  `unitsid` int NOT NULL AUTO_INCREMENT,
  `unitname` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unitpid` int DEFAULT NULL,
  PRIMARY KEY (`unitsid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of fl_unit
-- ----------------------------
INSERT INTO `fl_unit` VALUES ('1', '盆', null);
INSERT INTO `fl_unit` VALUES ('2', '箱', null);
INSERT INTO `fl_unit` VALUES ('7', '棵', null);
INSERT INTO `fl_unit` VALUES ('8', '瓶', '2');
INSERT INTO `fl_unit` VALUES ('9', '件', null);
INSERT INTO `fl_unit` VALUES ('10', '24盆', '9');
INSERT INTO `fl_unit` VALUES ('11', '箱子', null);
INSERT INTO `fl_unit` VALUES ('12', '瓶', '11');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `value` varchar(255) DEFAULT NULL COMMENT '内容',
  `type` varchar(255) DEFAULT NULL COMMENT '类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('user', 'el-icon-user-solid', 'icon');
INSERT INTO `sys_dict` VALUES ('house', 'el-icon-house', 'icon');
INSERT INTO `sys_dict` VALUES ('menu', 'el-icon-menu', 'icon');
INSERT INTO `sys_dict` VALUES ('custom', 'el-icon-s-custom', 'icon');
INSERT INTO `sys_dict` VALUES ('document', 'el-icon-document', 'icon');
INSERT INTO `sys_dict` VALUES ('buy', 'el-icon-shopping-cart-full', 'icon');
INSERT INTO `sys_dict` VALUES ('sale', 'el-icon-sell', 'icon');
INSERT INTO `sys_dict` VALUES ('sort', 'el-icon-notebook-1', 'icon');
INSERT INTO `sys_dict` VALUES ('supplier', 'el-icon-coordinate', 'icon');
INSERT INTO `sys_dict` VALUES ('customer', 'el-icon-truck', 'icon');
INSERT INTO `sys_dict` VALUES ('role', 'el-icon-star-on', 'icon');
INSERT INTO `sys_dict` VALUES ('system', 'el-icon-s-tools', 'icon');
INSERT INTO `sys_dict` VALUES ('good', 'el-icon-s-shop', 'icon');
INSERT INTO `sys_dict` VALUES ('business', 'el-icon-mobile', 'icon');
INSERT INTO `sys_dict` VALUES ('product', 'el-icon-goods', 'icon');
INSERT INTO `sys_dict` VALUES ('totaldata', 'el-icon-s-data', 'icon');
INSERT INTO `sys_dict` VALUES ('out', 'el-icon-shopping-cart-2', 'icon');
INSERT INTO `sys_dict` VALUES ('put', 'el-icon-sold-out', 'icon');
INSERT INTO `sys_dict` VALUES ('forest', 'el-icon-box', 'icon');
INSERT INTO `sys_dict` VALUES ('unit', '\r\nel-icon-collection', 'icon');
INSERT INTO `sys_dict` VALUES ('inventory', 'el-icon-receiving', 'icon');
INSERT INTO `sys_dict` VALUES ('logindata', 'el-icon-date', 'icon');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '文件名称\r\n',
  `type` varchar(255) DEFAULT NULL COMMENT '文件类型',
  `size` bigint DEFAULT NULL COMMENT '文件大小（KB）',
  `url` varchar(255) DEFAULT NULL COMMENT '下载连接',
  `md5` varchar(255) DEFAULT NULL COMMENT '文件md5',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `enable` tinyint(1) DEFAULT '1' COMMENT '是否禁用链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES ('61', 'dabe2182328841f39c74760430014b8a.png', 'png', '14', 'http://localhost:9090/file/dabe2182328841f39c74760430014b8a.png', 'cea02d67a5082d121e9c821aeaae0394', '1', '1');
INSERT INTO `sys_file` VALUES ('62', 'dabe2182328841f39c74760430014b8a.png', 'png', '14', 'http://localhost:9090/file/dabe2182328841f39c74760430014b8a.png', 'cea02d67a5082d121e9c821aeaae0394', '1', '1');
INSERT INTO `sys_file` VALUES ('79', '24gf-portraitMalePlus.png', 'png', '5', 'http://localhost:9090/file/a2f54569128c4da99b05ccfaecbcc060.png', '6b45bd0f0677403725cf6e9ed1c5f5dd', '0', '0');
INSERT INTO `sys_file` VALUES ('80', '花1.jpg', 'jpg', '99', 'http://localhost:9090/file/1a096b72ca384adcb904fee73af09def.jpg', '269cdf2703bb85f1e7e7467fa0f7147b', '0', '1');
INSERT INTO `sys_file` VALUES ('81', '花1.jpg', 'jpg', '99', 'http://localhost:9090/file/1a096b72ca384adcb904fee73af09def.jpg', '269cdf2703bb85f1e7e7467fa0f7147b', '0', '1');
INSERT INTO `sys_file` VALUES ('82', 'mmexport1652267892448.jpg', 'jpg', '136', 'http://localhost:9090/file/808ff821fd014b9fa5b7c6f4924ddbe5.jpg', '1c7ef5192f3fa74a00496c843238b29b', '0', '1');
INSERT INTO `sys_file` VALUES ('83', '头像 女孩 (1).png', 'png', '11', 'http://localhost:9090/file/9562f11e04324af69f0607b8d23415da.png', 'b4a9aa2cce6bf90dd438196e8eafa9b7', '0', '1');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `pid` int DEFAULT NULL COMMENT '父级id',
  `page_path` varchar(255) DEFAULT NULL COMMENT '页面路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=230 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '主页', '/home', 'el-icon-house', null, null, 'Home');
INSERT INTO `sys_menu` VALUES ('2', '系统管理', '', 'el-icon-s-tools', '', null, null);
INSERT INTO `sys_menu` VALUES ('3', '用户管理', '/manager', 'el-icon-user-solid', '', '2', 'User');
INSERT INTO `sys_menu` VALUES ('4', '角色管理', '/role', 'el-icon-star-on', null, '2', 'Role');
INSERT INTO `sys_menu` VALUES ('5', '菜单管理', '/menu', 'el-icon-menu', null, '2', 'Menu');
INSERT INTO `sys_menu` VALUES ('7', '业务管理', null, 'el-icon-mobile', null, null, null);
INSERT INTO `sys_menu` VALUES ('8', '客户管理', '/customer', 'el-icon-truck', null, '7', 'Customer');
INSERT INTO `sys_menu` VALUES ('9', '供应商管理', '/supplier', 'el-icon-coordinate', null, '7', 'Supplier');
INSERT INTO `sys_menu` VALUES ('10', '采购管理', '/buy', 'el-icon-shopping-cart-full', null, '7', 'Buy');
INSERT INTO `sys_menu` VALUES ('11', '销售管理', '/sale', 'el-icon-shopping-cart-2', null, '7', 'Sale');
INSERT INTO `sys_menu` VALUES ('12', '入库管理', '/putstorage', 'el-icon-sold-out', null, '7', 'Putstorage');
INSERT INTO `sys_menu` VALUES ('13', '入库统计', '/puttotal', 'el-icon-s-data', null, '7', 'Puttotal');
INSERT INTO `sys_menu` VALUES ('14', '出库管理', '/outstorage', 'el-icon-sell', null, '7', 'Outstorage');
INSERT INTO `sys_menu` VALUES ('15', '出库统计', '/outtotal', 'el-icon-s-data', null, '7', 'Outtotal');
INSERT INTO `sys_menu` VALUES ('16', '寄养服务管理', '/foster', 'el-icon-box', null, '7', 'Foster');
INSERT INTO `sys_menu` VALUES ('17', '商品管理', null, 'el-icon-s-shop', null, null, null);
INSERT INTO `sys_menu` VALUES ('18', '花材管理', '/flower', 'el-icon-goods', null, '17', 'Flower');
INSERT INTO `sys_menu` VALUES ('19', '花材类别管理', '/sort', 'el-icon-notebook-1', null, '17', 'Sort');
INSERT INTO `sys_menu` VALUES ('20', '单位管理', '/unit', 'el-icon-notebook-1', null, '17', 'Unit');
INSERT INTO `sys_menu` VALUES ('21', '库存盘点', '/inventory', 'el-icon-receiving', null, '17', 'Inventory');
INSERT INTO `sys_menu` VALUES ('22', '登录日志', '/loginlog', 'el-icon-date', null, null, 'Loginlog');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `flag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '主管理员', 'ROLE_ADMIN');
INSERT INTO `sys_role` VALUES ('2', '采购员', '采购员', 'ROLE_BUYER');
INSERT INTO `sys_role` VALUES ('3', '销售员', '销售员', 'ROLE_SALER');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '7');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '9');
INSERT INTO `sys_role_menu` VALUES ('1', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '11');
INSERT INTO `sys_role_menu` VALUES ('1', '12');
INSERT INTO `sys_role_menu` VALUES ('1', '13');
INSERT INTO `sys_role_menu` VALUES ('1', '14');
INSERT INTO `sys_role_menu` VALUES ('1', '15');
INSERT INTO `sys_role_menu` VALUES ('1', '16');
INSERT INTO `sys_role_menu` VALUES ('1', '17');
INSERT INTO `sys_role_menu` VALUES ('1', '18');
INSERT INTO `sys_role_menu` VALUES ('1', '19');
INSERT INTO `sys_role_menu` VALUES ('1', '20');
INSERT INTO `sys_role_menu` VALUES ('1', '21');
INSERT INTO `sys_role_menu` VALUES ('1', '22');
INSERT INTO `sys_role_menu` VALUES ('2', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '7');
INSERT INTO `sys_role_menu` VALUES ('2', '8');
INSERT INTO `sys_role_menu` VALUES ('2', '10');
INSERT INTO `sys_role_menu` VALUES ('2', '12');
INSERT INTO `sys_role_menu` VALUES ('2', '13');
INSERT INTO `sys_role_menu` VALUES ('2', '17');
INSERT INTO `sys_role_menu` VALUES ('2', '18');
INSERT INTO `sys_role_menu` VALUES ('2', '19');
INSERT INTO `sys_role_menu` VALUES ('2', '20');
INSERT INTO `sys_role_menu` VALUES ('2', '21');
INSERT INTO `sys_role_menu` VALUES ('2', '22');
INSERT INTO `sys_role_menu` VALUES ('3', '1');
INSERT INTO `sys_role_menu` VALUES ('3', '7');
INSERT INTO `sys_role_menu` VALUES ('3', '8');
INSERT INTO `sys_role_menu` VALUES ('3', '11');
INSERT INTO `sys_role_menu` VALUES ('3', '14');
INSERT INTO `sys_role_menu` VALUES ('3', '15');
INSERT INTO `sys_role_menu` VALUES ('3', '16');
INSERT INTO `sys_role_menu` VALUES ('3', '17');
INSERT INTO `sys_role_menu` VALUES ('3', '21');
