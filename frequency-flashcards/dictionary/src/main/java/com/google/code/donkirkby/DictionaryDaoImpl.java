package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DictionaryDaoImpl extends JdbcDaoSupport implements DictionaryDao {
    private static final String DB_CONNECTION_ERR_STRING = "Unable to connect to database.  Please check your database preferences " +
	"(Window->Preferences->Database)\nand make sure that 'Current database' is pointing to a valid database.  Also check " +
	"that another version of the zdt isn't already running.";
    
	private static Log log = LogFactory.getLog(DictionaryDaoImpl.class);

    private JdbcTemplate jdbcTemplate;

	@Override
	protected void initDao() throws Exception {
    	jdbcTemplate = new JdbcTemplate(getDataSource());
	}
	
    /* (non-Javadoc)
	 * @see com.google.code.donkirkby.DictionaryDao#getAllEntryValuesByTraditionalCharacter(java.lang.String, int)
	 */
    public EntryValue[] findAllEntryValuesByTraditionalCharacter(
    		String character, 
    		int maxResults)
    {
        List<EntryValue> entryValues = new ArrayList<EntryValue>();
        try
        {
//        	Object[] params = new Object[] { maxResults, character, character};
        	String query2 = "SELECT TOP " + maxResults + " character_id, traditional_char, simplified_char, pinyin, definition FROM character " +
    		"WHERE traditional_char LIKE '" + character + "'";
        	List<?> l2 = jdbcTemplate.queryForList(query2);
        	for (Object result : l2)
        	{
        		Map<?, ?> resultMap = (Map<?, ?>) result;
        		try
        		{
	        		EntryValue ev = getEntryValueFromResultsMap(resultMap);
	        		entryValues.add(ev);
        		}catch (Exception ex)
        		{
        			log.warn(
        					"Failed to load one of the entries like '" + 
        					character + "' and skipped it.", 
        					ex);
        		}
        	}
//        	for (int i = 0; i < l.size(); i++) {
//        		Map resultMap = (Map) l.get(i);
//        		EntryValue ev = getEntryValueFromResultsMap(resultMap);
//        		entryValues.add(ev);
//        	}
        } 
        catch (DataAccessException e)
        {
        	throw new RuntimeException(DB_CONNECTION_ERR_STRING);
        } 
        
        return entryValues.toArray(new EntryValue[0]);
    }
    
	private EntryValue getEntryValueFromResultsMap(Map<?, ?> resultMap) {
		EntryValue ev = new EntryValue();
		ev.setId((Integer)resultMap.get("CHARACTER_ID"));
		ev.setTraditionalChars((String)resultMap.get("TRADITIONAL_CHAR"));
		ev.setSimplifiedChars((String)resultMap.get("SIMPLIFIED_CHAR"));
		
		String rawPinyin = (String)resultMap.get("PINYIN");
		ev.setPinyin(ParserUtils.addToneMarks(rawPinyin));

		String definition = (String)resultMap.get("DEFINITION");
		definition = definition.replaceAll("^/|/$", "");
		definition = definition.replaceAll("/", "; ");
		ev.setDefinition(definition);
		return ev;
	}
}
