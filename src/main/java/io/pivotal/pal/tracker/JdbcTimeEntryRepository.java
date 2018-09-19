package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTimeEntryRepository implements  TimeEntryRepository{

    private static final String INSERT_SQL =
        "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";

    private static final String READ_SQL =
            "SELECT * FROM time_entries WHERE project_id = ? AND user_id = ? AND date = ? AND hours = ?";

    private static final String DELETE_SQL = "DELETE FROM time_entries WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM time_entries";
    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM time_entries WHERE id = ?";
    private static final String UPDATE_SQL=
            "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?";

    private JdbcTemplate template;


    public JdbcTimeEntryRepository(DataSource dateSource) {
        this.template = new JdbcTemplate(dateSource);
    }
    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        Object[] params = new Object[] { timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours() };
        int[] types = new int[] { Types.BIGINT, Types.BIGINT, Types.DATE, Types.INTEGER};


        int row = template.update(INSERT_SQL, params, types);

        return find(timeEntry);
    }

    public TimeEntry find(TimeEntry timeEntry) {
        Object[] params = new Object[] { timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours() };
        int[] types = new int[] { Types.BIGINT, Types.BIGINT, Types.DATE, Types.INTEGER};
        List<TimeEntry> list = (List<TimeEntry>) template.query(READ_SQL, params, types, new TimeEntryRowMapper() );

        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TimeEntry find(long id) {
        List<TimeEntry> list = (List<TimeEntry>) template.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, new TimeEntryRowMapper());
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        return  (List<TimeEntry>) template.queryForList(FIND_ALL_SQL, new Object[]{}, TimeEntry.class);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        Object[] params = new Object[] { timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours(), id };
        int[] types = new int[] { Types.BIGINT, Types.BIGINT, Types.DATE, Types.INTEGER, Types.BIGINT};
        template.update(UPDATE_SQL, params, types);

        return find(id);
    }

    @Override
    public boolean delete(long id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.BIGINT };
        return template.update(DELETE_SQL, params, types ) == 1;
    }

    private static final class TimeEntryRowMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeEntry entry = new TimeEntry();

            if(null != rs) {
                entry.setId(rs.getInt("id"));
                entry.setProjectId(rs.getInt("project_id"));
                entry.setUserId(rs.getInt("user_id"));
                entry.setDate(rs.getDate("date").toLocalDate());
                entry.setHours(rs.getInt("hours"));
            }
            return entry;
        }
    }




}
