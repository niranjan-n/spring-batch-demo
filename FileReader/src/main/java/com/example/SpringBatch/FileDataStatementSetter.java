package com.example.SpringBatch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.example.SpringBatch.model.FileData;

public class FileDataStatementSetter implements ItemPreparedStatementSetter<FileData> {

	@Override
	public void setValues(FileData item, PreparedStatement ps) throws SQLException {
		ps.setString(1, item.getName());
		ps.setString(2, item.getInfo());
	}
}
