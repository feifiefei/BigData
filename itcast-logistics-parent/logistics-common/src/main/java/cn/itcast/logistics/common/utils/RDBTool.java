package cn.itcast.logistics.common.utils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 数据库操作工具类
 */
public class RDBTool {

    /**
     * 无条件查询
     * @param <T>
     * @param sql
     * @param func
     * @return
     */
    public static <T> List<T> query(String sql, Function<String, List<T>> func) {
        List<T> result = func.apply(sql);
        return result;
    }

    /**
     * 单条件查询
     * @param <T>
     * @param sql
     * @param params
     * @param func
     * @return
     */
    public static <T> T query(String sql, T params, BiFunction<String, T, T> func) {
        T result = null;
        try {
            result = func.apply(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 多条件查询
     * @param <T>
     * @param sql
     * @param params
     * @param func
     * @return
     */
    public static <T> List<T> query(String sql, List<T> params, BiFunction<String, List<T>, List<T>> func) {
        List<T> result = null;
        try {
            result = func.apply(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 单条数据写入
     * @param <T>
     * @param sql
     * @param func
     */
    public static <T> void save(String sql, Consumer<String> func) {
        try {
            func.accept(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单条数据写入
     * @param <T>
     * @param sql
     * @param data
     * @param func
     */
    public static <T> void save(String sql, T data, BiConsumer<String, T> func) {
        try {
            func.accept(sql, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量数据写入
     * @param <T>
     * @param sql
     * @param data
     * @param func
     */
    public static <T> void save(String sql, List<T> data, BiConsumer<String, List<T>> func) {
        if (null==data || data.size()<0) {
            System.err.println("==== Parameter data is null! ====");
        } else {
            System.out.println("==== 开始写入数据... ====");
            try {
                func.accept(sql, data);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("==== 写入数据异常! ====");
            }
            System.out.println("==== 数据写入完成. ====");
        }
    }

    /**
     * 单条数据修改
     * @param <T>
     * @param sql
     * @param data
     * @param func
     */
    public static <T> void update(String sql, T data, BiConsumer<String, T> func) {
        if (null==data) {
            System.err.println("==== Parameter data is null! ====");
        } else {
            try {
                func.accept(sql, data);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("==== 写入数据异常! ====");
            }
        }
    }

    /**
     * 批量数据修改
     * @param <T>
     * @param sql
     * @param data
     * @param func
     */
    public static <T> void update(String sql, List<T> data, BiConsumer<String, List<T>> func) {
        try {
            func.accept(sql, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID删除
     * @param sql
     * @param id
     */
    public static void deleteById(String sql, long id, BiConsumer<String, Long> func) {
        try {
            func.accept(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行写好的删除语句
     * @param sql
     * @param id
     */
    public static void delete(String sql, Consumer<String> func) {
        try {
            func.accept(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

