package cn.fei.bean

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/22 0022 11:18
 */
/**
 * 封装电影评分数据
 *
 * @param userId    用户ID
 * @param itemId    电影ID
 * @param rating    用户对电影评分
 * @param timestamp 评分时间戳
 */
case class MovieRating(
                        userId: String,
                        itemId: String,
                        rating: Double,
                        timestamp: Long
                      )
