package holy.bible.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: chenzhongkai
 * Date: 2019-11-14
 * Describe: TODO
 */
class AnalysisDataManger {
    private static AnalysisDataManger instance = new AnalysisDataManger();

    public static AnalysisDataManger getInstance() {
        return instance;
    }

    public List<String> getRuleList() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("ceshi");
        ArrayList list1 = new ArrayList();

        return list;
    }
}
