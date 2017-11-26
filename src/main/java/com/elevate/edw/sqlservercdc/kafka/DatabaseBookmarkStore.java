package com.elevate.edw.sqlservercdc.kafka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * Bookmark store is implemented to persist the kafka offset into the
 * relationable database table. the table of bookmark store is defined as
 * 
 * unique constraint is established at consumergroup , topic and partnum
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| consumergroup | varchar(100) | NO   | PRI | NULL    |       |
| topic         | varchar(100) | NO   | PRI | NULL    |       |
| partnum       | int(11)      | NO   | PRI | NULL    |       |
| offset        | bigint(20)   | NO   |     | NULL    |       |
| metastr       | varchar(200) | YES  |     | NULL    |       |
+---------------+--------------+------+-----+---------+-------+

additionally, a bookmark history is also required
+---------------+--------------+------+-----+----------------------+----------------+
| Field         | Type         | Null | Key | Default              | Extra          |
+---------------+--------------+------+-----+----------------------+----------------+
| bookmarkid    | bigint(20)   | NO   | PRI | NULL                 | auto_increment |
| consumergroup | varchar(100) | NO   |     | NULL                 |                |
| topic         | varchar(100) | NO   |     | NULL                 |                |
| partnum       | int(11)      | NO   |     | NULL                 |                |
| offset        | bigint(20)   | NO   |     | NULL                 |                |
| metastr       | varchar(200) | YES  |     | NULL                 |                |
| inst_ts       | timestamp(6) | YES  |     | CURRENT_TIMESTAMP(6) |                |
+---------------+--------------+------+-----+----------------------+----------------+

 * 
 * @author ywu
 *
 */
public class DatabaseBookmarkStore {
	private DataSource ds;
	private String consumerGroup;
	private String storeTableName;
	private String historyTableName;
	//bookmark sql
	private String bmInsSql;
	private String bmUpdSql;
	private String bmSelSql;
	
	//bookmark history sql
	private String bhInsSql;
	
	/**
	 * Constructor
	 * @param ds
	 * @param consumerGroup
	 * @param storeTableName
	 * @param historyTableName
	 */
	public DatabaseBookmarkStore(DataSource ds, String consumerGroup, String storeTableName,
			String historyTableName) {
		this.ds = ds;
		this.consumerGroup = consumerGroup;
		this.historyTableName = historyTableName;
		this.storeTableName = storeTableName;
		this.buildSqls();
		
	}

	
	/**
	 * Persist history bookmark info into history table
	 * @param b
	 * @throws BookmarkException
	 */
	public void storeBookmarkHistory(Bookmark b)throws BookmarkException{
		try (Connection con = ds.getConnection();				
				PreparedStatement instStmt = con.prepareStatement(bhInsSql)) {
			
			int rowCnt = 0;		
			instStmt.setString(1, this.consumerGroup);
			instStmt.setString(2, b.getTopicPartition().topic());
			instStmt.setInt(3, b.getTopicPartition().partition());
			instStmt.setLong(4, b.getOffset().offset());
			instStmt.setString(5, b.getOffset().metadata());
			rowCnt = instStmt.executeUpdate();			
			// if this happens... something is wrong!
			assert (rowCnt == 1);
		} catch (SQLException e) {
			throw new BookmarkException(e);
		}
	}
	
	
	public void storeOffsetHistory(TopicPartition p, OffsetAndMetadata offset)throws BookmarkException{
		try (Connection con = ds.getConnection();				
				PreparedStatement instStmt = con.prepareStatement(bhInsSql)) {
			
			int rowCnt = 0;		
			instStmt.setString(1, this.consumerGroup);
			instStmt.setString(2, p.topic());
			instStmt.setInt(3, p.partition());
			instStmt.setLong(4, offset.offset());
			instStmt.setString(5, offset.metadata());
			rowCnt = instStmt.executeUpdate();			
			// if this happens... something is wrong!
			assert (rowCnt == 1);
		} catch (SQLException e) {
			throw new BookmarkException(e);
		}
	}
	
	/**
	 * store bookmark into bookmar store
	 * @param b
	 * @throws BookmarkException
	 */
	public void storeBookmark(Bookmark b) throws BookmarkException {
		try (Connection con = ds.getConnection();
				PreparedStatement checkExsistingStmt = con.prepareStatement(bmSelSql);
				PreparedStatement instStmt = con.prepareStatement(bmInsSql);
				PreparedStatement updStmt = con.prepareStatement(bmUpdSql)) {
			checkExsistingStmt.setString(1, this.consumerGroup);
			checkExsistingStmt.setString(2, b.getTopicPartition().topic());
			checkExsistingStmt.setInt(3, b.getTopicPartition().partition());
			ResultSet rs = checkExsistingStmt.executeQuery();
			int rowCnt = 0;
			// if rs next reach end, there is no row , perform insert
			if (!rs.next()) {
				instStmt.setString(1, this.consumerGroup);
				instStmt.setString(2, b.getTopicPartition().topic());
				instStmt.setInt(3, b.getTopicPartition().partition());
				instStmt.setLong(4, b.getOffset().offset());
				instStmt.setString(5, b.getOffset().metadata());
				rowCnt = instStmt.executeUpdate();
			} else {
				updStmt.setLong(1, b.getOffset().offset());
				updStmt.setString(2, b.getOffset().metadata());
				updStmt.setString(3, this.consumerGroup);
				updStmt.setString(4, b.getTopicPartition().topic());
				updStmt.setInt(5, b.getTopicPartition().partition());
				rowCnt = updStmt.executeUpdate();
			}
			// if this happens... something is wrong!
			assert (rowCnt == 1);

		} catch (SQLException e) {
			throw new BookmarkException(e);
		}
	}

	/**
	 * Reconstruct a bookmark object from database.
	 * @param consuemrgroup
	 * @param topic
	 * @param partnum
	 * @return
	 * @throws BookmarkException
	 */
	public Bookmark retrieveBookmark(String topic, int partnum) throws BookmarkException {
		Bookmark ret = null;
		try (Connection con = ds.getConnection();
				PreparedStatement selStmt = con.prepareStatement(bmSelSql)
				) {
			selStmt.setString(1, this.consumerGroup);
			selStmt.setString(2, topic);
			selStmt.setInt(3, partnum);
			ResultSet rs = selStmt.executeQuery();
			long offset = -1;
			String meta =null;
			if(rs.next()){
				offset = rs.getLong(1);
				meta = rs.getString(2);
			}
			if(offset!=-1){
				ret = new Bookmark(new TopicPartition(topic,partnum),new OffsetAndMetadata(offset,meta));
			}
		} catch (SQLException e) {
			throw new BookmarkException(e);
		}
		return ret;
	}


	/**
	 * private method to construct sqls
	 */
	private void buildSqls() {
		bmInsSql = "INSERT INTO " + this.storeTableName + " ( consumergroup, topic, partnum,offset,metastr) VALUES (?,?,?,?,?)";
		bmUpdSql = "UPDATE " + this.storeTableName
				+ " SET offset = ? , metastr = ? WHERE consumergroup = ? AND topic =? AND partnum = ?";
		bmSelSql = "SELECT offset ,metastr FROM " + this.storeTableName
				+ " WHERE consumergroup = ? and topic = ? and partnum = ?";
		bhInsSql = "INSERT INTO "+this.historyTableName+" ( consumergroup, topic, partnum, offset, metastr) VALUES (?,?,?,?,?)";
	}
}
