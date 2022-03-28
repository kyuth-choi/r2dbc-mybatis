package org.apache.ibatis.r2dbc.type;

import io.r2dbc.spi.R2dbcException;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.r2dbc.spi.Statement;
import org.apache.ibatis.type.JdbcType;

public class StringTypeHandler extends org.apache.ibatis.type.StringTypeHandler implements R2DBCTypeHandler<String> {

    @Override
    public void setParameter(Statement statement, int i, String parameter, JdbcType jdbcType) throws R2dbcException {
        if (parameter == null) {
            statement.bindNull(i, null);
        } else {
            statement.bind(i, parameter);
        }
    }

    @Override
    public String getResult(Row row, String columnName, RowMetadata rowMetadata) throws R2dbcException {
        return row.get(columnName, String.class);
    }

    @Override
    public String getResult(Row row, int columnIndex, RowMetadata rowMetadata) throws R2dbcException {
        return row.get(columnIndex, String.class);
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }
}
