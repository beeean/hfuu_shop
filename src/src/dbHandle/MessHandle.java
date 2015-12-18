package src.dbHandle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import src.dbc.DatabaseConnection;
import src.vo.*;

public class MessHandle {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    public MessHandle() {
        try {
            this.conn = new DatabaseConnection().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean doCreate(Mess mess) throws Exception {
        boolean flag = false;
        String sql = "INSERT INTO `message`(mess_from_id,mess_to_id,mess_text,send_time) VALUES (?,?,?,?)";
        this.pstmt = this.conn.prepareStatement(sql);
        //pstmt.setInt(1, mess.getMessId());
        pstmt.setInt(1, mess.getMessFromId());
        pstmt.setInt(2, mess.getMessToId());
        pstmt.setString(3, mess.getMessText());
        pstmt.setTimestamp(4,new java.sql.Timestamp(mess.getSendDate().getTime()));
        if (this.pstmt.executeUpdate() > 0) {
            flag = true;
        }
        this.pstmt.close();
        return flag;
    }
    
    public List<Mess> findAllMessByUser(User user) throws Exception {
        List<Mess> all = new ArrayList<Mess>();
        String sql = "SELECT mess_id,mess_from_id,mess_to_id,send_time,mess_text from `message`  where mess_to_id=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setInt(1,user.getId());
        ResultSet rs = this.pstmt.executeQuery();
        while (rs.next()) {
            Mess mess = new Mess();
            mess.setMessId(rs.getInt(1));
            mess.setMessFromId(rs.getInt(2));
            mess.setMessToId(rs.getInt(3));
            java.sql.Timestamp timeStamp=rs.getTimestamp(4);
            java.util.Date date=new  java.util.Date(timeStamp.getTime());
            mess.setSendDate(date);
            mess.setMessText(rs.getString(5));
            all.add(mess);
        }
        this.pstmt.close();
        return all;
    }
}