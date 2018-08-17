package org.intermine.item.domain.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;

public class ItemPreparedStatementSetter implements PreparedStatementSetter{

	
	private static final Logger log = Logger.getLogger(ItemPreparedStatementSetter.class);
	
	@Override
	public void setValues(Map<Integer,Object> param,PreparedStatement ps) throws SQLException {
		
		if(param!= null && param.size() > 0){
       		
			log.info("Setting query parameters." );
            for(Integer key : param.keySet()){
            	
            	log.info("Setting query parameters: "  + "index: " + key + ";" + "value: " + param.get(key));
            	ps.setObject(key, param.get(key));
            }
        } else{
        	log.info("No Query Parameters have been set." );
        }

		
	}

	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
				
	}

}
