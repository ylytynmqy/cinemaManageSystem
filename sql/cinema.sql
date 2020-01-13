/*
Navicat MySQL Data Transfer

Source Server         : cinema
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : cinema

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-06-13 09:48:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(45) NOT NULL,
  `a_description` varchar(255) NOT NULL,
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `coupon_id` int(11) DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for activity_movie
-- ----------------------------
DROP TABLE IF EXISTS `activity_movie`;
CREATE TABLE `activity_movie` (
  `activity_id` int(11) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of activity_movie
-- ----------------------------
INSERT INTO `activity_movie` VALUES ('5', '1');

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `target_amount` float DEFAULT NULL,
  `discount_amount` float DEFAULT NULL,
  `start_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `coupon_user`;
CREATE TABLE `coupon_user` (
  `coupon_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of coupon_user
-- ----------------------------

-- ----------------------------
-- Table structure for hall
-- ----------------------------
DROP TABLE IF EXISTS `hall`;
CREATE TABLE `hall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `column` int(11) DEFAULT NULL,
  `row` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of hall
-- ----------------------------
INSERT INTO `hall` VALUES ('1', '1号厅', '14', '20');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` varchar(255) NOT NULL,
  `been_read` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poster_url` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `screen_writer` varchar(255) DEFAULT NULL,
  `starring` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `length` int(11) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `description` text,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie` VALUES ('1', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E4%B8%80%E5%A4%A9.jpg', '罗勒', '大卫', '安妮海瑟薇', '爱情', '美国', null, '1', '2019-06-12 00:51:33', '一天', '1988年7月15日，一所大学为毕业生的狂热所充盈。籍着这股躁动，轻浮的德克斯特（吉姆·斯特吉斯 Jim Sturgess 饰）穿过层层人墙，寻找可以与之共度这最后狂欢与浪漫之夜的尤物。他将目光锁定在艾玛（安妮·海瑟薇 Anne Hathaway 饰）的身上，一个来自普 通工薪家庭的聪慧女孩。然而短短的交流过后，他们之间什么也没发生，却又注定改变了彼此的一生。在之后的岁月里，两人以友人知己的身份时聚时散，彼此分享着人生的苦辣酸甜和各种感悟。', '0');
INSERT INTO `movie` VALUES ('2', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E5%A4%A9%E4%BD%BF%E7%88%B1%E7%BE%8E%E4%B8%BD.jpg', '让皮埃尔', '纪尧姆·洛朗/让皮埃尔', '奥黛丽塔图', '爱情', '法国', null, '1', '2019-06-12 22:45:33', '天使爱美丽', '艾米莉（奥黛丽·塔图 Audrey Tautou 饰）有着别人看来不幸的童年——父亲给她做健康检查时，发现她心跳过快，便断定她患上心脏病，从此艾米莉与学校绝缘。随后因为一桩意外，母亲在她眼前突然死去。这一切都毫不影响艾米莉对生活的豁达乐观。 ', '0');
INSERT INTO `movie` VALUES ('3', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E6%B5%B7%E8%BE%B9%E7%9A%84%E6%9B%BC%E5%BD%BB%E6%96%AF%E7%89%B9.jpg', '肯尼斯', '肯尼斯', '卡西', '家庭', '美国', null, '120', '2019-06-13 08:57:26', '海边的曼彻斯特', '李（卡西·阿弗莱克 Casey Affleck 饰）是一名颓废压抑的修理工，在得知哥哥乔伊（凯尔·钱德勒 Kyle Chandler 饰）去世的消息后，李回到了故乡——海边的曼彻斯特处理乔伊的后事。根据乔伊的遗嘱，李将会成为乔伊的儿子帕特里克（卢卡斯·赫奇斯 Lucas Hedg es 饰）的监护人，李打算将帕特里克带回波士顿，但很显然帕特里克并不愿意离开家乡和朋友们，但李亦不愿在这片伤心地久留。 ', '0');
INSERT INTO `movie` VALUES ('4', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E7%8E%9B%E4%B8%BD%E5%92%8C%E9%A9%AC%E5%85%8B%E6%80%9D.jpg', '亚当', '亚当', '托尼', '动画', '澳大利亚', null, '120', '2019-06-13 08:57:28', '玛丽和马克思', '976年，8岁的玛丽·黛西·丁格尔（贝丝妮·惠特摩尔 Bethany Whitmore 配音）是澳大利亚墨尔本的一个小女孩，喜欢动画片“诺布利特”、甜炼乳和巧克力。玛丽的妈妈是个酒鬼，而在茶叶包装厂工作的父亲平日只喜欢制作鸟标本。孤独的玛丽没有朋友，某一天心血来潮给美国纽约市的马克思·杰瑞·霍罗威茨（菲利普·塞默·霍夫曼 Philip Seymour Hoffman 配音）写了一封信询问美国小孩从哪里来，并附上一根樱桃巧克力棒。44岁的马克思患有自闭症及肥胖，碰巧也喜欢看“诺布利特”动画片及吃巧克力。', '0');
INSERT INTO `movie` VALUES ('5', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E8%B6%85%E8%84%B1.jpg', '托尼', '卡尔', '艾德里安', '剧情', '美国', null, '120', '2019-06-13 08:57:29', '超脱', '亨利•巴赫特（艾德里安•布洛迪 Adrien Brody 饰）来到社区学校担任代课老师。学校因学生反叛、经营不善而濒临绝境，多丽丝（刘玉玲 饰）为首的一批教师备受煎熬。亨利的第一堂课就遭遇了顽劣学生的挑衅，但是被他巧妙地化解。他赢得了女教师萨沙（克里斯蒂娜•亨德里克斯 Christina Hendricks）的好感。同时，他的教学方式引起了同学们的兴趣，其中就包括因肥胖而苦闷的梅丽迪斯。她一直默默地承受着因体重带来的各种责骂与嘲笑。但是因为老师，她似乎又找到了一切的理由…… ', '0');
INSERT INTO `movie` VALUES ('6', 'http://101.37.19.32:10080/cinema_system/cinema_system/raw/725e82d2d746675e460ea46f1d79ea106b7392e4/%E5%9B%BE%E7%89%87/%E7%94%B5%E5%BD%B1%E6%B5%B7%E6%8A%A5%E5%9B%BE/%E8%BE%9B%E5%BE%B7%E5%8B%92%E7%9A%84%E5%90%8D%E5%8D%95.jpg', '史蒂文', '托马斯', '连姆', '战争', '德国', null, '120', '2019-06-13 08:57:31', '辛德勒的名单', '1939年，波兰在纳粹德国的统治下，党卫军对犹太人进行了隔离统治。德国商人奥斯卡·辛德勒（连姆·尼森 Liam Neeson 饰）来到德军统治下的克拉科夫，开设了一间搪瓷厂，生产军需用品。凭着出众的社交能力和大量的金钱，辛德勒和德军建立了良好的关系，他的工厂雇用犹太人工作，大发战争财。\n　　1943年，克拉科夫的犹太人遭到了惨绝人寰的大屠杀，辛德勒目睹这一切，受到了极大的震撼，他贿赂军官，让自己的工厂成为集中营的附属劳役营，在那些疯狂屠杀的日子里，他的工厂也成为了犹太人的避难所。\n　　1944年，德国战败前夕，屠杀犹太人的行动越发疯狂，辛德勒向德军军官开出了1200人的名单，倾家荡产买下了这些犹太人的生命。在那些暗无天日的岁月里，拯救一个人，就是拯救全世界。', '0');

-- ----------------------------
-- Table structure for movie_like
-- ----------------------------
DROP TABLE IF EXISTS `movie_like`;
CREATE TABLE `movie_like` (
  `movie_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `like_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movie_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of movie_like
-- ----------------------------
INSERT INTO `movie_like` VALUES ('1', '2', '2019-06-08 01:55:45');

-- ----------------------------
-- Table structure for ordert
-- ----------------------------
DROP TABLE IF EXISTS `ordert`;
CREATE TABLE `ordert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `total` float NOT NULL,
  `state` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `join_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  `pay_method` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ordert
-- ----------------------------

-- ----------------------------
-- Table structure for refund_strategy
-- ----------------------------
DROP TABLE IF EXISTS `refund_strategy`;
CREATE TABLE `refund_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` int(11) NOT NULL,
  `percent` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of refund_strategy
-- ----------------------------
INSERT INTO `refund_strategy` VALUES ('1', '30', '0.8');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hall_id` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  `start_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NOT NULL,
  `fare` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES ('1', '1', '1', '2019-06-08 20:13:00', '2019-06-08 20:15:00', '23');
INSERT INTO `schedule` VALUES ('2', '1', '1', '2019-06-12 23:22:00', '2019-06-12 23:25:00', '23');

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `user_id` int(11) DEFAULT NULL,
  `schedule_id` int(11) DEFAULT NULL,
  `column_index` int(11) DEFAULT NULL,
  `row_index` int(11) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ticket
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(6) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_uindex` (`id`),
  UNIQUE KEY `user_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', 'root', '1');
INSERT INTO `user` VALUES ('2', 'user', 'test', '1');
INSERT INTO `user` VALUES ('3', 'clerk', 'c', '1');

-- ----------------------------
-- Table structure for view
-- ----------------------------
DROP TABLE IF EXISTS `view`;
CREATE TABLE `view` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of view
-- ----------------------------
INSERT INTO `view` VALUES ('1', '7');

-- ----------------------------
-- Table structure for vip_card
-- ----------------------------
DROP TABLE IF EXISTS `vip_card`;
CREATE TABLE `vip_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `vip_kind_id` int(2) NOT NULL,
  `balance` float DEFAULT NULL,
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vip_card_user_id_uindex` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of vip_card
-- ----------------------------

-- ----------------------------
-- Table structure for vip_card_kind
-- ----------------------------
DROP TABLE IF EXISTS `vip_card_kind`;
CREATE TABLE `vip_card_kind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `price` int(11) NOT NULL,
  `target_amount` int(11) NOT NULL,
  `bonus_amount` int(11) NOT NULL,
  `percent` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of vip_card_kind
-- ----------------------------
INSERT INTO `vip_card_kind` VALUES ('1', '橘子卡', '12', '100', '20', '0.7');
INSERT INTO `vip_card_kind` VALUES ('2', '西瓜卡', '23', '50', '23', '0.95');

DROP TABLE IF EXISTS `clerk_rank`;
CREATE TABLE `clerk_rank`(
 `clerk_id` int(11) NOT NULL,
 `c_rank` int(11) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE `verification_code`(
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `v_code` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;