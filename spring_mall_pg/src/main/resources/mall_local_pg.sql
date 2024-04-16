CREATE TABLE mall.mall_cart
(
    id          SERIAL PRIMARY KEY,
    product_id  INT       NOT NULL,
    user_id     INT       NOT NULL,
    quantity    INT       NOT NULL DEFAULT 1,
    selected    INT       NOT NULL DEFAULT 1,
    create_time TIMESTAMP NOT NULL DEFAULT NOW(),
    update_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE mall.mall_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(32) NOT NULL DEFAULT '',
    type        INT         NOT NULL,
    parent_id   INT         NOT NULL,
    order_num   INT         NOT NULL,
    create_time TIMESTAMP   NOT NULL DEFAULT NOW(),
    update_time TIMESTAMP   NOT NULL DEFAULT NOW()
);


CREATE TABLE mall.mall_order (
    id               serial       NOT NULL PRIMARY KEY,
    order_no         varchar(128)  NOT NULL DEFAULT '',
    user_id          integer       NOT NULL,
    total_price      integer       NOT NULL,
    receiver_name    varchar(32)   NOT NULL,
    receiver_mobile  varchar(32)   NOT NULL,
    receiver_address varchar(128)  NOT NULL DEFAULT '',
    order_status     integer       NOT NULL DEFAULT 10,
    postage          integer       DEFAULT 0,
    payment_type     integer       NOT NULL DEFAULT 1,
    delivery_time   timestamp,
    pay_time         timestamp,
    end_time         timestamp,
    create_time      timestamp     NOT NULL DEFAULT NOW(),
    update_time      timestamp     NOT NULL DEFAULT NOW()
);

CREATE TABLE mall.mall_order_item (
    id           serial      NOT NULL PRIMARY KEY,
    order_no     varchar(128) NOT NULL DEFAULT '',
    product_id   integer      NOT NULL,
    product_name varchar(100) NOT NULL DEFAULT '',
    product_img  varchar(128) NOT NULL DEFAULT '',
    unit_price   integer      NOT NULL,
    quantity     integer      NOT NULL DEFAULT 1,
    total_price  integer      NOT NULL DEFAULT 0,
    create_time  timestamp    NOT NULL DEFAULT NOW(),
    update_time  timestamp    NOT NULL DEFAULT NOW()
);

CREATE TABLE mall.mall_product (
    id          serial      NOT NULL PRIMARY KEY,
    name        varchar(100) NOT NULL,
    image       varchar(500) NOT NULL DEFAULT '',
    detail      varchar(500) DEFAULT '',
    category_id integer      NOT NULL,
    price       integer      NOT NULL,
    stock       integer      NOT NULL,
    status      integer      NOT NULL DEFAULT 1,
    create_time timestamp    NOT NULL DEFAULT NOW(),
    update_time timestamp    NOT NULL DEFAULT NOW()
);


CREATE TABLE mall.mall_user (
    id                     serial      NOT NULL PRIMARY KEY,
    username               varchar(32)  NOT NULL DEFAULT '',
    password               varchar(50)  NOT NULL,
    email_address          varchar(100) DEFAULT '',
    personalized_signature varchar(50)  NOT NULL DEFAULT '',
    role                   integer      NOT NULL DEFAULT 1,
    create_time            timestamp    NOT NULL DEFAULT NOW(),
    update_time            timestamp    NOT NULL DEFAULT NOW()
);


INSERT INTO mall.mall_category (name, type, parent_id, order_num, create_time, update_time)
VALUES ('新鲜水果', 1, 0, 1, '2019-12-18 01:17:00.000000', '2019-12-28 17:11:26.000000'),
       ('橘子橙子', 2, 3, 1, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:10.000000'),
       ('海鲜水产', 1, 0, 2, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:20.000000'),
       ('精选肉类', 1, 0, 3, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:21.000000'),
       ('螃蟹', 2, 5, 1, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:15.000000'),
       ('鱼类', 2, 5, 2, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:16.000000'),
       ('冷饮冻食', 1, 0, 4, '2019-12-20 13:45:28.000000', '2019-12-28 16:25:22.000000'),
       ('蔬菜蛋品', 1, 0, 5, '2019-12-20 13:45:28.000000', '2019-12-28 16:25:23.000000'),
       ('草莓', 2, 3, 2, '2019-12-18 01:17:00.000000', '2019-12-28 15:44:42.000000'),
       ('奇异果', 2, 3, 3, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:12.000000'),
       ('海参', 2, 5, 3, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:17.000000'),
       ('车厘子', 2, 3, 4, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:12.000000'),
       ('火锅食材', 2, 27, 5, '2019-12-18 01:17:00.000000', '2020-02-11 00:42:33.000000'),
       ('牛羊肉', 2, 6, 1, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:18.000000'),
       ('冰淇淋', 2, 9, 1, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:18.000000'),
       ('蔬菜综合', 2, 10, 1, '2019-12-18 01:17:00.000000', '2020-02-11 00:48:27.000000'),
       ('果冻橙', 3, 4, 1, '2019-12-18 01:17:00.000000', '2020-02-11 00:37:02.000000'),
       ('美味菌菇', 1, 0, 7, '2019-12-20 13:45:28.000000', '2020-02-10 23:20:36.000000'),
       ('其他水果', 2, 3, 4, '2019-12-18 01:17:00.000000', '2019-12-28 16:25:12.000000');


       
INSERT INTO mall.mall_user (username, password, personalized_signature, role, create_time, update_time)
VALUES 
('1', '1', '3', 1, '2019-12-16 02:37:33', '2020-02-09 18:41:12'),
('xiaomu', 'AWRuqaxc6iryhHuA4OnFag==', '更新了我的签名', 2, '2019-12-17 15:11:32', '2020-02-10 09:52:12'),
('你好', 'AWRuqaxc6iryhHuA4OnFag==', '', 1, '2019-12-20 13:41:03', '2020-02-10 09:52:15'),
('111', 'G72IZGCCcBXl1gXtRCUiUQ==', '', 1, '2019-12-27 19:34:56', '2019-12-27 19:34:56'),
('444', 'uFfu1clAXB8rmASKrlBnkg==', 'cecc', 1, '2019-12-27 19:38:03', '2019-12-28 01:04:06'),
('你好2', 'JdVa0oOqQAr0ZMdtcTwHrQ==', '', 1, '2020-02-08 17:47:06', '2020-02-08 17:47:06'),
('你好3', 'JdVa0oOqQAr0ZMdtcTwHrQ==', '', 1, '2020-02-08 17:49:15', '2020-02-08 17:49:15'),
('你好4', '12345678', '', 1, '2020-02-09 19:49:54', '2020-02-09 19:49:54'),
('xiaomu2', 'AWRuqaxc6iryhHuA4OnFag==', '祝你今天好心情', 2, '2020-02-09 20:39:47', '2020-02-11 00:56:02');


INSERT INTO mall.mall_product (id, name, image, detail, category_id, price, stock, status, create_time,
                               update_time)
VALUES (2, '澳洲进口大黑车厘子大樱桃包甜黑樱桃大果多汁 500g 特大果', 'http://111.231.103.117:8081/images/chelizi2.jpg',
        '商品毛重：1.0kg货号：608323093445原产地：智利类别：美早热卖时间：1月，11月，12月国产/进口：进口售卖方式：单品', 14, 50,
        100, 1, '2019-12-18 16:08:15', '2020-02-11 00:08:25'),
       (3, '茶树菇 美味菌菇 东北山珍 500g', 'http://111.231.103.117:8081/images/chashugu.jpg',
        '商品名：茶树菇 商品特点：美味菌菇 东北山珍 500g', 15, 1000, 6, 1, '2019-12-18 16:10:50', '2020-02-11 00:42:42'),
       (14, 'Zespri佳沛 新西兰阳光金奇异果 6个装', 'http://111.231.103.117:8081/images/mihoutao2.jpg',
        '商品编号：4635056商品毛重：0.71kg商品产地：新西兰类别：金果包装：简装国产/进口：进口原产地：新西兰', 12, 39, 77, 1,
        '2019-12-18 16:11:13', '2020-02-10 23:36:48'),
       (17, '红颜奶油草莓 约重500g/20-24颗 新鲜水果', 'http://111.231.103.117:8081/images/caomei2.jpg',
        '商品毛重：0.58kg商品产地：丹东/南通/武汉类别：红颜草莓包装：简装国产/进口：国产', 11, 99, 84, 1,
        '2019-12-18 16:11:13', '2020-02-10 23:37:48'),
       (21, '智利原味三文鱼排（大西洋鲑）240g/袋 4片装', 'http://111.231.103.117:8081/images/sanwenyu2.jpg',
        '商品毛重：260.00g商品产地：中国大陆保存状态：冷冻国产/进口：进口包装：简装类别：三文鱼海水/淡水：海水烹饪建议：煎炸，蒸菜，烧烤原产地：智利',
        8, 499, 1, 1, '2019-12-28 15:13:07', '2020-02-10 23:38:46'),
       (22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 'http://111.231.103.117:8081/images/haishen.jpg',
        '商品毛重：1.5kg商品产地：中国大陆贮存条件：冷冻重量：50-99g国产/进口：国产适用场景：养生滋补包装：袋装原产地：辽宁年限：9年以上等级：特级食品工艺：冷冻水产热卖时间：9月类别：即食海参固形物含量：70%-90%特产品类：大连海参售卖方式：单品',
        13, 699, 3, 1, '2019-12-28 15:16:29', '2020-02-11 00:04:29'),
       (23, '澳大利亚直采鲜橙 精品澳橙12粒 单果130-180g', 'http://111.231.103.117:8081/images/chengzi.jpg',
        '商品毛重：2.27kg商品产地：澳大利亚类别：脐橙包装：简装国产/进口：进口原产地：澳大利亚', 4, 12, 12, 1,
        '2019-12-28 16:02:13', '2020-02-11 00:40:15'),
       (24, '智利帝王蟹礼盒装4.4-4.0斤/只 生鲜活鲜熟冻大螃蟹', 'http://111.231.103.117:8081/images/diwangxie.jpg',
        '商品毛重：3.0kg商品产地：智利大闸蟹售卖方式：公蟹重量：2000-4999g套餐份量：5人份以上国产/进口：进口海水/淡水：海水烹饪建议：火锅，炒菜，烧烤，刺身，加热即食包装：简装原产地：智利保存状态：冷冻公单蟹重：5.5两及以上分类：帝王蟹特产品类：其它售卖方式：单品',
        7, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:05:05'),
       (25, '新疆库尔勒克伦生无籽红提 国产新鲜红提葡萄 提子 5斤装', 'http://111.231.103.117:8081/images/hongti.jpg',
        '商品毛重：2.5kg商品产地：中国大陆货号：XZL201909002重量：2000-3999g套餐份量：2人份国产/进口：国产是否有机：非有机单箱规格：3个装，4个装，5个装类别：红提包装：简装原产地：中国大陆售卖方式：单品',
        28, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:44:05'),
       (26, '越南进口红心火龙果 4个装 红肉中果 单果约330-420g',
        'http://111.231.103.117:8081/images/hongxinhuolongguo.jpg',
        '商品毛重：1.79kg商品产地：越南重量：1000-1999g类别：红心火龙果包装：简装国产/进口：进口', 28, 222, 222, 1,
        '2019-12-28 16:06:34', '2020-02-11 00:44:11'),
       (27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材',
        'http://111.231.103.117:8081/images/yangrouchuan.jpg',
        '商品毛重：0.585kg商品产地：内蒙古巴彦淖尔市保存状态：冷冻重量：500-999g套餐份量：3人份国产/进口：国产烹饪建议：烧烤原产地：内蒙古品种：其它热卖时间：4月，5月，6月，7月，8月，9月，10月，11月，12月饲养方式：圈养类别：羊肉串包装：简装套餐周期：12个月',
        16, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:11:30'),
       (28, '玛琪摩尔新西兰进口冰淇淋大桶装', 'http://111.231.103.117:8081/images/bingqilin.jpg',
        '商品毛重：1.04kg商品产地：新西兰国产/进口：进口包装：量贩装', 17, 222, 222, 1, '2019-12-28 16:06:34',
        '2020-02-11 00:10:40'),
       (29, '西兰花沙拉菜 350g 甜玉米粒 青豆豌豆 胡萝卜冷冻方便蔬菜', 'http://111.231.103.117:8081/images/shalacai.jpg',
        '商品毛重：370.00g商品产地：浙江宁波重量：500g以下套餐份量：家庭装类别：速冻玉米/豌豆包装：简装烹饪建议：炒菜，炖菜，煎炸，蒸菜售卖方式：单品',
        18, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:34:01'),
       (36, '四川果冻橙 吹弹可破', 'http://111.231.103.117:8081/images/guodongcheng.jpg',
        '商品毛重：370.00g商品产地：四川 重量：1000g', 19, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:38:14'),
       (37, '进口牛油果 中果6粒装 单果约130-160g ', 'http://111.231.103.117:8081/images/niuyouguo.jpg',
        '商品名称：京觅进口牛油果 6个装商品编号：3628240商品毛重：1.2kg商品产地：秘鲁、智利、墨西哥重量：1000g以下国产/进口：进口',
        28, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:47:42'),
       (38, '中街1946网红雪糕冰淇淋', 'http://111.231.103.117:8081/images/bingqilin2.jpg',
        '商品名称：中街1946网红雪糕冰淇淋乐享系列半巧*5牛乳*5阿棕*2冰激凌冷饮冰棍冰棒商品编号：52603405444店铺： 中街1946官方旗舰店商品毛重：1.3kg商品产地：中国大陆国产/进口：国产包装：量贩装售卖方式：组合',
        17, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:50:54'),
       (39, '福建六鳌红薯5斤', 'http://111.231.103.117:8081/images/hongshu.jpg',
        '商品名称：京觅福建六鳌红薯5斤商品编号：4087121商品毛重：2.8kg商品产地：福建省漳浦县六鳌镇重量：2500g及以上烹饪建议：煎炸，蒸菜，烧烤包装：简装分类：地瓜/红薯售卖方式：单品',
        18, 40, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:51:59'),
       (40, '胡萝卜', 'http://111.231.103.117:8081/images/huluobo.jpg',
        '商品名称：绿鲜知胡萝卜商品编号：4116192商品毛重：1.07kg商品产地：北京包装：简装分类：萝卜烹饪建议：火锅，炒菜，炖菜',
        18, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:53:25'),
       (41, '羊肉卷 内蒙羔羊肉 鲜嫩 500g/袋 首农出品 羊排肉卷 火锅食材',
        'http://111.231.103.117:8081/images/yangroujuan.jpg',
        '商品名称：首食惠羊排片商品编号：4836347商品毛重：0.51kg商品产地：辽宁省大连市保存状态：冷冻品种：其它国产/进口：进口饲养方式：散养类别：羊肉片/卷包装：简装烹饪建议：火锅，炒菜，炖菜原产地：新西兰',
        16, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:48:03'),
       (42, '甜玉米 切好 香甜', 'http://111.231.103.117:8081/images/tianyumi.jpg',
        '品牌： 绿鲜知（greenseer）\n商品名称：绿鲜知甜玉米商品编号：4983604商品毛重：1.1kg商品产地：云南玉溪类别：玉米', 18,
        240, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:52:19');