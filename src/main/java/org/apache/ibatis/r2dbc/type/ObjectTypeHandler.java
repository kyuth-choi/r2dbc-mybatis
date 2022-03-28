package org.apache.ibatis.r2dbc.type;

import io.r2dbc.spi.R2dbcException;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.r2dbc.spi.Statement;
import org.apache.ibatis.type.JdbcType;


public class ObjectTypeHandler extends org.apache.ibatis.type.ObjectTypeHandler implements R2DBCTypeHandler<Object> {

    @Override
    public void setParameter(Statement statement, int i, Object parameter, JdbcType jdbcType) throws R2dbcException {
    }

    @Override
    public Object getResult(Row row, String columnName, RowMetadata rowMetadata) throws R2dbcException {
        return null;
    }

    @Override
    public Object getResult(Row row, int columnIndex, RowMetadata rowMetadata) throws R2dbcException {
        return null;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }
}
