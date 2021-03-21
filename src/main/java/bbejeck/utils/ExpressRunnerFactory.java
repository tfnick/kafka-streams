package bbejeck.utils;

import com.ql.util.express.ExpressRunner;

/**
 * 单例线程安全的表达式执行器
 */
public class ExpressRunnerFactory {

    private static final ExpressRunner singleInstance = new ExpressRunner();

    public static ExpressRunner getInstance(){
        return singleInstance;
    }
}
