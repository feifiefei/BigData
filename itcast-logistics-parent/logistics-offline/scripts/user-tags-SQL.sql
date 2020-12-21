-- 用户标签
-- =========================================================================
-- 1. 用户首单时间
WITH tmp AS
    (SELECT tcsi.ciid AS cid,
         MIN(tcsi.cdt) AS cdt
    FROM tbl_consumer_sender_info tcsi
    GROUP BY  tcsi.ciid
    ORDER BY  cdt ASC )
SELECT t1.cid,
         t1.cdt,
         t2.name
FROM tmp t1
LEFT JOIN tbl_customer t2
    ON t2.id = t1.cid
WHERE NAME IS NOT NULL;

-- =========================================================================
-- 2. 用户首单地址
WITH tmp1 AS (
   SELECT
        tcsi.ciid,
         min(tcsi.cdt) AS cdt
    FROM tbl_consumer_sender_info tcsi
    GROUP BY  tcsi.ciid
), tmp3 AS (
SELECT
        consumerid,
         min(tcam.cdt) AS cdt,
         ta.detailaddr
    FROM tbl_consumer_address_map tcam
    LEFT JOIN tbl_address ta
        ON tcam.addressid=ta.id
    GROUP BY  consumerid,ta.detailaddr
)
SELECT t1.ciid,
         t2.name,
         t1.cdt,
         t3.detailaddr
FROM
   tmp1 t1
LEFT JOIN tbl_customer t2
    ON t1.ciid=t2.id
LEFT JOIN
    tmp3 t3
    ON t3.consumerid=t2.id ;

-- =========================================================================
-- 3. 用户首单来源
SELECT
  tc.id,
  tc.name,
  tas2.id AS province_id,
  tas2.name,
  tcs.cdt
FROM
  (SELECT
    ciid,
    MIN(cdt) AS cdt
  FROM
    tbl_consumer_sender_info
  GROUP BY ciid) tcs
  LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
  LEFT JOIN
    (SELECT
      tcm1.consumerid,
      MIN(tcm1.cdt) AS cdt,
      MIN(tcm1.addressid) AS address_id
    FROM
      tbl_consumer_address_map tcm1
    GROUP BY tcm1.consumerid) tcm
    ON tcm.consumerid = tc.id
  LEFT JOIN tbl_address ta
    ON ta.id = tcm.address_id
  LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid
  LEFT JOIN tbl_areas tas1
    ON tas.pid = tas1.id
  LEFT JOIN tbl_areas tas2
    ON tas1.pid = tas2.id
WHERE tc.name IS NOT NULL;

-- =========================================================================
-- 4. 用户首单省份
WITH tmp1 AS
    (SELECT ciid,
         MIN(cdt) AS cdt
    FROM tbl_consumer_sender_info
    GROUP BY  ciid ), tmp2 AS
    (SELECT tcm1.consumerid,
         MIN(tcm1.cdt) AS cdt,
         MIN(tcm1.addressid) AS address_id
    FROM tbl_consumer_address_map tcm1
    GROUP BY  tcm1.consumerid )
SELECT tc.id,
         tc.name,
         tas2.id AS province_id,
         tas2.name,
         tcs.cdt
FROM tmp1 tcs
LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
LEFT JOIN tmp2 tcm
    ON tcm.consumerid = tc.id
LEFT JOIN tbl_address ta
    ON ta.id = tcm.address_id
LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid
LEFT JOIN tbl_areas tas1
    ON tas.pid = tas1.id
LEFT JOIN tbl_areas tas2
    ON tas1.pid = tas2.id
WHERE tc.name IS NOT NULL;

-- =========================================================================
--5. 用户首单城市
WITH tmp AS (
SELECT ciid,
         MIN(CAST(cdt AS INT)) AS cdt
    FROM tbl_consumer_sender_info
    GROUP BY  ciid
)
SELECT tc.id,
         tc.name,
         tas1.id,
         tas1.name AS city_name,
         tcs.cdt
FROM
    tmp tcs
LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
LEFT JOIN tbl_consumer_address_map tcm
    ON tcm.consumerid = tc.id
LEFT JOIN tbl_address ta
    ON ta.id = tcm.addressid
LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid
LEFT JOIN tbl_areas tas1
    ON tas.pid = CAST(tas1.id AS INT);

-- =========================================================================
--6. 用户首单地区
WITH tmp AS (
SELECT
    ciid,
    MIN(CAST(cdt AS INT)) AS cdt
  FROM
    tbl_consumer_sender_info
  GROUP BY ciid
)
SELECT
  tc.id,
  tc.name,
  tas.id,
  tas.name AS area_name,
  tcs.cdt
FROM
  tmp tcs
  LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
  LEFT JOIN tbl_consumer_address_map tcm
    ON tcm.consumerid = tc.id
  LEFT JOIN tbl_address ta
    ON ta.id = tcm.addressid
  LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid;


-- =========================================================================
-- 7. 最后一次收货省份
WITH tmp1 AS(
SELECT
    ciid,
    MAX(cdt) AS cdt
  FROM
    tbl_consumer_sender_info
  GROUP BY ciid
), tmp2 AS(
SELECT
      tcm1.consumerid,
      MIN(tcm1.cdt) AS cdt,
      MIN(tcm1.consumerid) AS address_id
    FROM
      tbl_consumer_address_map tcm1
    GROUP BY tcm1.consumerid
)
SELECT
  tc.id,
  tc.name,
  tas2.id AS province_id,
  tas2.name,
  tcs.cdt
FROM
  tmp1 tcs
  LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
  LEFT JOIN
    tmp2 tcm
    ON tcm.consumerid = tc.id
  LEFT JOIN tbl_address ta
    ON ta.id = tcm.address_id
  LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid
  LEFT JOIN tbl_areas tas1
    ON tas.pid = CAST(tas1.id AS INT)
  LEFT JOIN tbl_areas tas2
    ON tas1.pid = CAST(tas2.id AS INT)
WHERE tc.name IS NOT NULL;


-- =========================================================================
-- 8. 最后一次收货地区
WITH tmp AS
    (SELECT ciid,
         MAX(CAST(cdt AS INT)) AS cdt
    FROM tbl_consumer_sender_info
    GROUP BY  ciid )
SELECT tc.id,
         tc.name,
         tas1.id,
         tas1.name AS city_name,
         tcs.cdt
FROM tmp tcs
LEFT JOIN tbl_customer tc
    ON tcs.ciid = tc.id
LEFT JOIN tbl_consumer_address_map tcm
    ON tcm.consumerid = tc.id
LEFT JOIN tbl_address ta
    ON ta.id = tcm.addressid
LEFT JOIN tbl_areas tas
    ON tas.id = ta.areaid
LEFT JOIN tbl_areas tas1
    ON tas.pid = CAST(tas1.id AS INT);

-- =========================================================================
-- 9. 常用收货省份
WITH tmp AS
    (SELECT tep.cid,
         tep.recvaddressid,
         COUNT(tep.recvaddressid) AS cnt,
         MAX(tep.cdt) AS cdt
    FROM tbl_express_package tep
    GROUP BY  tep.cid, tep.recvaddressid ), tmp2 AS
    (SELECT t1.cid,
         t1.recvaddressid,
         t1.cnt
    FROM tmp t1
    WHERE t1.cid = 73
    ORDER BY  t1.cnt, t1.cdt DESC LIMIT 1 )
SELECT t2.cid,
         t2.recvaddressid,
         t2.cnt,
         tas3.id AS province_id,
         tas3.name AS province_name
FROM tmp2 t2
LEFT JOIN tbl_address ta
    ON (ta.id = t2.recvaddressid)
LEFT JOIN tbl_areas tas1
    ON (tas1.id = ta.areaid)
LEFT JOIN tbl_areas tas2
    ON (tas1.pid = tas1.id)
LEFT JOIN tbl_areas tas3
    ON (tas2.pid = tas3.id);

-- =========================================================================
-- 10. 常用收货地区
WITH tmp1 AS
    (SELECT tep.cid,
         tep.recvaddressid,
         COUNT(tep.recvaddressid) AS cnt,
         MAX(tep.cdt) AS cdt
    FROM tbl_express_package tep
    GROUP BY  tep.cid, tep.recvaddressid ), tmp2 AS
    (SELECT t1.cid,
         t1.recvaddressid,
         t1.cnt
    FROM tmp1 t1
    WHERE t1.cid = 73
    ORDER BY  t1.cnt, t1.cdt DESC LIMIT 1 )
SELECT t2.cid,
         t2.recvaddressid,
         t2.cnt,
         tas2.id AS city_id,
         tas2.name AS city_name
FROM tmp2 t2
LEFT JOIN tbl_address ta
    ON (ta.id = t2.recvaddressid)
LEFT JOIN tbl_areas tas1
    ON (tas1.id = ta.areaid)
LEFT JOIN tbl_areas tas2
    ON (tas1.pid = tas1.id);

-- =========================================================================
-- 11. 最后一次使用户号码
WITH tmp AS
    (SELECT cid,
         sendaddressid,
         cdt
    FROM tbl_express_package
    WHERE cid = 30
    ORDER BY  cdt DESC LIMIT 1 )
SELECT t1.cid,
         tc.name,
         ta.id AS address_id,
         ta.detailaddr,
         ta.tel,
         t1.cdt
FROM tmp t1
LEFT JOIN tbl_customer tc
    ON (tc.id = t1.cid)
LEFT JOIN tbl_address ta
    ON (ta.id = t1.sendaddressid);

-- =========================================================================
-- 12. 最后一次使用户号码
WITH tmp1 AS
    (SELECT tep.cid,
         tep.recvaddressid,
         COUNT(tep.recvaddressid) AS cnt,
         MAX(tep.cdt) AS cdt
    FROM tbl_express_package tep
    GROUP BY  tep.cid, tep.recvaddressid ), tmp2 AS
    (SELECT t1.cid,
         t1.recvaddressid,
         t1.cnt
    FROM tmp1 t1
    WHERE t1.cid = 73
    ORDER BY  t1.cnt, t1.cdt DESC LIMIT 1 )
SELECT t2.cid,
         t2.recvaddressid,
         t2.cnt,
         tc.name,
         tc.tel
FROM tmp2 t2
LEFT JOIN tbl_customer tc
    ON (tc.id = t2.cid);

-- =========================================================================
-- 13. 常用手机号运营商
WITH tmp1 AS
    (SELECT tep.cid,
         tep.recvaddressid,
         COUNT(tep.recvaddressid) AS cnt,
         MAX(tep.cdt) AS cdt
    FROM tbl_express_package tep
    GROUP BY  tep.cid, tep.recvaddressid ), tmp2 AS
    (SELECT t1.cid,
         t1.recvaddressid,
         t1.cnt
    FROM tmp1 t1
    WHERE t1.cid = 73
    ORDER BY  t1.cnt, t1.cdt DESC LIMIT 1 )
SELECT t2.cid,
         t2.recvaddressid,
         t2.cnt,
         tc.name,
         (case cast(substr(tc.tel, 1, 3) AS INT)
    WHEN 134 THEN '移动'
    WHEN 135 THEN '移动'
    WHEN 136 THEN '移动'
    WHEN 137 THEN '移动'
    WHEN 138 THEN '移动'
    WHEN 139 THEN '移动'
    WHEN 147 THEN '移动'
    WHEN 150 THEN '移动'
    WHEN 151 THEN '移动'
    WHEN 152 THEN '移动'
    WHEN 157 THEN '移动'
    WHEN 158 THEN '移动'
    WHEN 159 THEN '移动'
    WHEN 182 THEN '移动'
    WHEN 187 THEN '移动'
    WHEN 188 THEN '移动'
    WHEN 130 THEN '联通'
    WHEN 131 THEN '联通'
    WHEN 132 THEN '联通'
    WHEN 155 THEN '联通'
    WHEN 156 THEN '联通'
    WHEN 185 THEN '联通'
    WHEN 186 THEN '联通'
    WHEN 133 THEN '电信'
    WHEN 153 THEN '电信'
    WHEN 180 THEN '电信'
    WHEN 189 THEN '电信'
    ELSE '未知' end) AS tel
FROM tmp2 t2
LEFT JOIN tbl_customer tc
    ON (tc.id = t2.cid);

-- =========================================================================
-- 14. 不同手机号数量
SELECT tc.id,
         tc.name,
         COUNT(DISTINCT (tc.mobile)) AS cnt
FROM tbl_consumer_sender_info tcsi
LEFT JOIN tbl_customer tc
    ON tc.id = tcsi.ciid
GROUP BY  tc.id, tc.name, tc.mobile;


