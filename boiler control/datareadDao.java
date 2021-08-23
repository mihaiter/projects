package servertest;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import java.lang.*;

public class datareadDao {
//	private ComboPooledDataSource source;
	private JdbcTemplate jdbcTemp;
	public void setjdbcTemp(JdbcTemplate jdbcTemp) {
		this.jdbcTemp=jdbcTemp;
	}
	public datareadDao(JdbcTemplate jdbcTemp) {
		// TODO Auto-generated constructor stub
		this.jdbcTemp=jdbcTemp;
	}
	public datareadDao() {}
	public Boolean saveData(final dataread data) {
		
		String querry="insert into system.tabelfoc values(?,?)";
		return jdbcTemp.execute(querry,new PreparedStatementCallback<Boolean>() {
			@Override
			public  Boolean doInPreparedStatement(PreparedStatement ps)  throws SQLException,DataAccessException{
				ps.setFloat(1, data.getTemp());
				Timestamp timestamp= new Timestamp(data.getMiliseconds());
				ps.setTimestamp(2, timestamp);
				return ps.execute();
			}
		});
		
	}
	public  List<dataread> getAllDataRowMApper(){
		return jdbcTemp.query("select * from system.tabelfoc order by data", new RowMapper<dataread>() {
			@Override
			public dataread mapRow(ResultSet rs, int rownumber) throws SQLException{
				dataread da= new dataread();
				da.setTemp(rs.getFloat(1));
				da.setMiliseconds(rs.getTimestamp(2).getTime());
				return da;
			}
			
		});
	}

}
