package com.clouddrive.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.clouddrive.dao.BaseDao;
import com.clouddrive.dao.RSProcessor;
import com.clouddrive.dao.ShareDao;
import com.clouddrive.entity.Share;
import com.clouddrive.entity.User;

public class ShareDaoImpl extends BaseDao implements ShareDao {

	@Override
	public int countShareByKey(String key) {
		String sql = "select count(*) as share_count from share where keyword=?";
		Object[] params = { key };

		RSProcessor countUserByNameProcessor = new RSProcessor() {

			public Object process(ResultSet rs) throws SQLException {
				int count = 0;
				if (rs != null) {
					if (rs.next()) {
						count = rs.getInt("share_count");
					}
				}

				return new Integer(count);
			}

		};

		return (Integer) this.executeQuery(countUserByNameProcessor, sql, params);
	}

	@Override
	public Share findShareByKey(String key) {
		String sql = "select * from share where keyword = ?";
		Object[] params = { key };

		RSProcessor getUserByNameProcessor = new RSProcessor() {

			public Object process(ResultSet rs) throws SQLException {
				Share share = null;

				if (rs != null) {
					if (rs.next()) {
						String user = rs.getString("user");
						String uuidName = rs.getString("uuidName");
						String url = rs.getString("url");
						String shareTime = rs.getString("shareTime");
						String key = rs.getString("keyword");
						int downloads = rs.getInt("downloads");
						long size = rs.getLong("size");
						share = new Share(user, uuidName, url, shareTime, key, downloads, size);
					}
				}

				return share;

			}
		};

		return (Share) this.executeQuery(getUserByNameProcessor, sql, params);
	}

	@Override
	public int insert(Share share) {
		String sql = "insert share (user, uuidName, url, shareTime, keyword, downloads, size) values(?,?,?,?,?,?,?)";
		Object[] params = { share.getUser(), share.getUuidName(), share.getUrl(), share.getShareTime(), share.getKey(), 0, share.getSize() };
		return this.executeUpdate(sql, params);
	}

	@Override
	public Vector<Share> findShareByUser(String name) {
		String sql = "select * from share where user = ?";
		Object[] params = { name };

		RSProcessor getUsersByNameProcessor = new RSProcessor() {

			public Object process(ResultSet rs) throws SQLException {
				Vector<Share> shares = new Vector<Share>();

				while (rs.next()) {
					
					String user = rs.getString("user");
					String uuidName = rs.getString("uuidName");
					String url = rs.getString("url");
					String shareTime = rs.getString("shareTime");
					String key = rs.getString("keyword");
					int downloads = rs.getInt("downloads");
					long size = rs.getLong("size");
					Share share = new Share(user, uuidName, url, shareTime, key, downloads, size);
					shares.add(share);
				}

				return shares;

			}
		};

		return (Vector<Share>) this.executeQuery(getUsersByNameProcessor, sql, params);
	}

	@Override
	public int updateDownloadByKey(String key) {
		String sql = "update share \r\n" + 
				"set downloads = downloads+1\r\n" + 
				"where keyword = ?";
		Object[] params = { key };
		return this.executeUpdate(sql, params);
	}

	@Override
	public int delShareByKey(String key) {
		String sql = "delete from share\r\n" + 
				"where keyword = ?";
		Object[] params = { key };
		return this.executeUpdate(sql, params);
	}

}