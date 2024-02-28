package dialect;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {

    /**
     * 사용자 정의 함수 정의
     * @param functionContributions
     */
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions
                .getFunctionRegistry()
                .register("group_concat",
                        new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
