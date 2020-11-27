package cn.fei.sougou

/**
 * 用户搜索点击网页记录Record
 *
 * @param queryTime  访问时间，格式为：HH:mm:ss
 * @param userId     用户ID
 * @param queryWords 查询词
 * @param resultRank 该URL在返回结果中的排名
 * @param clickRank  用户点击的顺序号
 * @param clickUrl   用户点击的URL
 */
case class SogouRecord(
	                      queryTime: String, //
	                      userId: String, //
	                      queryWords: String, //
	                      resultRank: Int, //
	                      clickRank: Int, //
	                      clickUrl: String //
                      )
