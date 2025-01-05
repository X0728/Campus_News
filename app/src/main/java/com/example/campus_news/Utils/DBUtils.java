package com.example.campus_news.Utils;



import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import com.example.campus_news.StringEntity.CommentInfo;
import com.example.campus_news.StringEntity.NewsInfo;
import com.example.campus_news.StringEntity.BannerDataInfo;
import com.example.campus_news.StringEntity.ProclamationData;
import com.example.campus_news.StringEntity.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//数据库工具类：连接数据库用、获取数据库数据用
public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动
    private static String user = "root";// 用户名
    private static String password = "123456";// 密码
    public static PreparedStatement pStmt, pStmt2;  //命令集 继承自Statement数据库操作接口，用于向数据库发送sql语句

    private static Connection getConn(String dbname){
        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "172.30.80.1203333333";
            connection = DriverManager.getConnection("jdbc:mysql://" + ip
                    + ":3306/" + dbname, user, password);
        }catch (Exception e){
            Log.i("DBUtils","Exception");
            e.printStackTrace();
        }
        return connection;
    }
    public static HashMap<String, String> getAllInfo(String u_phone){
        HashMap<String, String> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn("newscampus");
        try {
            String sql = "select u_phone, u_name, u_sex, u_birthday, u_email, u_picture from userinfo where u_phone = ?;";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 执行sql查询语句并返回结果集
                    ps.setNString(1, u_phone);
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        while (rs.next()){
//                            String rsm = rs.getMetaData().getColumnName(1);
//                            Log.i("DBUtils","记录rsm ：" + rsm);
                            // 通过字段检索
                            String picture = rs.getString("u_picture");
                            String phone  = rs.getString("u_phone");
                            String name = rs.getString("u_name");
                            String sex = rs.getString("u_sex");
                            String birthday  = rs.getString("u_birthday");
                            String email = rs.getString("u_email");
                            Log.i("DBUtils","记录全 ：" + phone + name + sex + birthday + email);
                            map.put("phone",phone);
                            map.put("name", name);
                            map.put("sex", sex);
                            map.put("birthday", birthday);
                            map.put("email", email);
                            map.put("picture", picture);
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        Log.i("DBUtils","结果为空");
                        return null; }
                }else {
                    Log.i("DBUtils","sql");
                    return  null; }
            }else {
                Log.i("DBUtils","连接失败");
                return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }

    }
    public static HashMap<String, Object> getInfoByName(String names){  //哈希表，键值对存储数据
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn("new_cumpus");
        try {
            // mysql简单的查询语句。这里是根据users表的name字段来查询某条记录
            String sql = "select * from user_info where u_name = ? ;";
            if (connection != null){  // connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
//                    ps.setString(1, names);
                    ps.setNString(1,names);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
//                    rs.beforeFirst();
                    if (rs != null && rs.next()){
                        Log.i("DBUtils","记录 count ：" + rs.getMetaData().getColumnCount());
                        rs.previous();
                        while (rs.next()){
                            // 通过字段检索
                            String id  = rs.getString("u_phone");
                            String name = rs.getString("u_name");
                            String passw = rs.getString("i_sex");
                            int age  = rs.getInt("i_password");
                            String adress = rs.getString("i_email");
                            Log.i("DBUtils","记录全 ：" + id + name + passw + age + adress);
                            map.put(id,","+name+","+passw+","+age+","+adress);
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        Log.i("DBUtils","结果为空");
                        return null; }
                }else {
                    Log.i("DBUtils","sql");
                    return  null; }
            }else {
                Log.i("DBUtils","连接失败");
                return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }
    //登录验证
    public static HashMap<String, String> getLogInfo(String account){
        HashMap<String, String> map = new HashMap<>();
        Connection connection = getConn("newscampus");
        try {
            // mysql简单的查询语句。这里是根据users表的name字段来查询某条记录
            String sql = "select u_password from userinfo where u_phone = ? ";
            if (connection != null){  // connection不为null表示与数据库建立了连接
                Log.i("DBUtils","111");
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
//                    ps.setString(1, names);
                    Log.i("DBUtils","112 ");
                    ps.setNString(1,account);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
//                    rs.beforeFirst();
                    if (rs != null && rs.next()){
                        Log.i("DBUtils","记录 count ：" + rs.getMetaData().getColumnCount());
                        rs.previous();
                        while (rs.next()){
                            // 通过字段检索
                            //String phone  = rs.getString("u_phone");
                            String password = rs.getString("u_password");
                            Log.i("DBUtils","记录全 ：" + password);
                            map.put("password",password);
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        Log.i("DBUtils","结果为空");
                        return null; }
                }else {
                    Log.i("DBUtils","sql");
                    return  null; }
            }else {
                Log.i("DBUtils","连接失败");
                return  null; }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }
    //记录用户创建时间
    public static int createAccount(UserInfo item) throws SQLException {
        int Row = 0;
        Connection connection = null;
        try {
            connection = getConn("newscampus");
            String sql = "insert into user_create_dt(u_phone, u_name, u_time) values(?, ?, ?)";
            pStmt = connection.prepareStatement(sql);
            pStmt.setNString(1, item.getPhone());
            pStmt.setNString(2, item.getU_name());
            pStmt.setNString(3, CommonUtils.getDateStrFromNow());
            Log.i("112", CommonUtils.getDateStrFromNow());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            pStmt.close();
        }
        return Row;
    }
    //创建账户信息
    public static int insertUserInfo(UserInfo item) throws SQLException {
        int Row = 0;
        pStmt =null;
        Connection connection = null;
        Log.i("112","2");
        try {
            connection = getConn("newscampus");
            String sql = "insert into userinfo(u_phone, u_name, u_password, u_sex, u_birthday, u_email) values(?, ?, ?, ?, ?, ?)";
            pStmt = connection.prepareStatement(sql);
            Log.i("112","3");
            pStmt.setNString(1, item.getPhone());
            pStmt.setNString(2, item.getU_name());
            pStmt.setNString(3, item.getU_password());
            Log.i("112","4");
            pStmt.setNString(4, item.getU_sex());
            pStmt.setNString(5, item.getU_birthday());
            pStmt.setNString(6, item.getU_email());
            Log.i("112",item.getU_email());
            Row = pStmt.executeUpdate();
            Log.i("112","5");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            pStmt.close();
            Log.i("112","6");
        }
        return Row;
    }
    //修改密码
    public static int editPassword(UserInfo item) throws SQLException {
        int Row = 0;
        pStmt = null;
        Connection connection = null;
        Log.i("112","2");
        try {
            connection = getConn("newscampus");
            String sql = "update userinfo set u_password = ?  where u_phone = ?";
            //UPDATE userinfo SET u_password = '1234567' WHERE u_phone = '18093538247'
            pStmt = connection.prepareStatement(sql);
            Log.i("112","3");
            pStmt.setNString(1, item.getU_password());
            pStmt.setNString(2, item.getPhone());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            pStmt.close();
        }
        return Row;
    }
    //修改头像
    public static int picture(UserInfo item) throws SQLException {
        int Row = 0;
        pStmt = null;
        Connection connection = null;
        Log.i("112","2");
        try {
            connection = getConn("newscampus");
            String sql = "update userinfo set u_picture = ? where u_phone = ?";
            //UPDATE userinfo SET u_password = '1234567' WHERE u_phone = '18093538247'
            pStmt = connection.prepareStatement(sql);
            Log.i("112","3");
            pStmt.setNString(1, item.getU_picture());
            pStmt.setNString(2, item.getPhone());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            pStmt.close();
        }
        return Row;
    }
    //修改个人信息
    public static int modifyInfo(UserInfo item) throws SQLException {
        int Row = 0;
        pStmt = null;
        Connection connection = null;
        Log.i("112","2");
        try {
            connection = getConn("newscampus");
            String sql = "update userinfo set u_name = ?, u_phone = ?, u_sex = ?, u_birthday = ?, u_email = ? where u_phone = ?";
            //UPDATE userinfo SET u_password = '1234567' WHERE u_phone = '18093538247'
            pStmt = connection.prepareStatement(sql);
            Log.i("112","3");
            pStmt.setNString(1, item.getU_name());
            pStmt.setNString(2, item.getPhone());
            pStmt.setNString(3, item.getU_sex());
            pStmt.setNString(4, item.getU_birthday());
            pStmt.setNString(5, item.getU_email());
            pStmt.setNString(6, item.getO_phone());
            //pStmt.setNString(2, item.getPhone());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            pStmt.close();
        }
        return Row;
    }
    //发布新闻
    public static int newsInfo(NewsInfo item) throws SQLException {
        int Row = 0;
        pStmt = null;
        Log.i("112","2");
        try (Connection connection = getConn("newscampus")) {
            String sql = "insert into newsinfo(title, content, u_phone, picture, time, browse, comments, label) values(?, ?, ?, ?, ?, ?, ?, ?)";
            //UPDATE userinfo SET u_password = '1234567' WHERE u_phone = '18093538247'
            pStmt = connection.prepareStatement(sql);
            Log.i("112", "3");
            pStmt.setNString(1, item.getTitle());
            pStmt.setNString(2, item.getContent());
            pStmt.setNString(3, item.getUser());
            pStmt.setNString(4, item.getPicture());
            pStmt.setNString(5, CommonUtils.getDateStrFromNow());
            pStmt.setInt(6, 0);
            pStmt.setInt(7, 0);
            pStmt.setNString(8, item.getLabel());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pStmt.close();
        }
        return Row;
    }
    //首页获取全部新闻
    public synchronized static List<NewsInfo> getAllNews() throws SQLException {
        List<NewsInfo> list = new ArrayList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT t.*, k.u_name,k.u_picture  FROM newsinfo t LEFT JOIN userinfo k ON t.u_phone = k.u_phone ORDER BY id DESC;";
            pStmt = connection.prepareStatement(sql);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                NewsInfo item = new NewsInfo();
                item.setId(rs.getInt("id"));
                item.setUser(rs.getString("u_name"));
                item.setTitle(rs.getString("title"));
                item.setPhone(rs.getString("u_phone"));
                item.setPicture(rs.getString("picture"));
                item.setContent(rs.getNString("content"));
                item.setHead(rs.getString("u_picture"));
                item.setTime(rs.getDate("time"));
                item.setBrowse(rs.getInt("browse"));
                item.setComments(rs.getInt("comments"));
                item.setLabel(rs.getString("label"));
                list.add(item);
            }
            if (pStmt != null) {
                pStmt.close();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }
    //获得新闻内容
    public synchronized static HashMap<String, String> getNewsItem(String id) throws SQLException {
        HashMap<String, String> map = new HashMap<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT t.*, k.u_name FROM newsinfo t LEFT JOIN userinfo k ON t.u_phone = k.u_phone where t.id = ?;";
            pStmt = connection.prepareStatement(sql);
            pStmt.setNString(1, id);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String u_name = rs.getString("u_name");
                String title = rs.getString("title");
                String picture = rs.getString("picture");
                String content = rs.getNString("content");
                Date date = rs.getDate("time");
                int browse = rs.getInt("browse");
                int comments = rs.getInt("comments");
                String label = rs.getString("label");
                map.put("u_name", u_name);
                map.put("title", title);
                map.put("picture", picture);
                map.put("content", content);
                map.put("date", String.valueOf(date));
                map.put("browse", String.valueOf(browse));
                map.put("comments", String.valueOf(comments));
                map.put("label", label);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return map;
    }

    //获取用户个人动态
    public static List<NewsInfo> getUserNews(String phone) throws SQLException {
        List<NewsInfo> list = new ArrayList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT t.* FROM newsinfo t WHERE u_phone = ? ORDER BY time DESC, id DESC;";
            pStmt = connection.prepareStatement(sql);
            pStmt.setNString(1, phone);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                NewsInfo item = new NewsInfo();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("title"));
                item.setTime(rs.getDate("time"));
                item.setLabel(rs.getString("label"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    //用户删除新闻
    public static int deleteNews(String id) throws SQLException {
        int Row = 0;
        pStmt = null;
        Log.i("112","2");
        Connection connection =null;
        try  {
            connection = getConn("newscampus");
            String sql = "DELETE FROM newsinfo WHERE id = ?";
            pStmt = connection.prepareStatement(sql);
            Log.i("112", "3");
            pStmt.setNString(1, id);
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pStmt.close();
            connection.close();
        }
        return Row;
    }
    //创建用户评论
    public static int createComment(CommentInfo item) throws SQLException {
        int Row, Row2 = 0;
        pStmt = null;
        Connection connection =null;
        Log.i("112","2");
        try {
            connection = getConn("newscampus");
            String sql = "insert into comments(phone, content, time, news_id) values(?, ?, ?, ?);";
            String sql2 = "UPDATE newsinfo SET comments = (select count(id) FROM comments WHERE news_id = ?) WHERE id = ?;";
            pStmt = connection.prepareStatement(sql);
            pStmt2 = connection.prepareStatement(sql2);
            Log.i("112", "3");
            pStmt.setNString(1, item.getPhone());
            pStmt.setNString(2,item.getContent());
            pStmt.setNString(3,CommonUtils.getDateStrFromNow());
            pStmt.setNString(4, item.getNewsId());
            pStmt2.setNString(1, item.getNewsId());
            pStmt2.setNString(2, item.getNewsId());
            Row = pStmt.executeUpdate();
            Row2 = pStmt2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pStmt.close();
            pStmt2.close();
            connection.close();
        }
        return Row;
    }

    //获得新闻评论
    public static List<CommentInfo> getComments(String id) throws SQLException {
        List<CommentInfo> list= new ArrayList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT t.* , u.u_name FROM comments t, userinfo u where t.news_id = ? AND t.phone = u.u_phone  ORDER BY t.time DESC, t.id DESC;";
            pStmt = connection.prepareStatement(sql);
            pStmt.setNString(1, id);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                CommentInfo item = new CommentInfo();
                item.setName(rs.getString("u_name"));
                item.setDate(rs.getDate("time"));
                item.setContent(rs.getString("content"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    //搜索新闻结果
    @NonNull
    public static List<NewsInfo> SearchNews(String keyWord) throws SQLException {
        List<NewsInfo> list = new ArrayList<>();
        ResultSet rs;
        String key = "%"+keyWord+"%";
        pStmt = null;
        Connection connection =null;
        try  {
            connection = getConn("newscampus");
            String sql = "SELECT n.id, n.title ,n.content, u.u_name  FROM newsinfo n LEFT JOIN  userinfo u ON n.u_phone = u.u_phone  WHERE n.title LIKE '"+ key +"' OR n.content LIKE '"+key+"';";
            pStmt = connection.prepareStatement(sql);
            /*pStmt.setNString(1, keyWord);
            pStmt.setNString(2, keyWord);*/
            rs = pStmt.executeQuery();
            while (rs.next()) {
                NewsInfo item = new NewsInfo();
                item.setId(rs.getInt("id"));
                item.setUser(rs.getString("u_name"));
                item.setTitle(rs.getString("title"));
                item.setContent(rs.getString("content"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    //公告板
    public static List<ProclamationData> getProclamation() throws SQLException {
        List<ProclamationData> list= new ArrayList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT x.* FROM newscampus.proclamation x ORDER BY x.p_date DESC ";
            pStmt = connection.prepareStatement(sql);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                ProclamationData item = new ProclamationData();
                item.setId(rs.getInt("id"));
                item.setTitle(rs.getString("p_title"));
                item.setContent(rs.getString("p_content"));
                item.setDate(rs.getDate("p_date"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    //公告内容
    public static HashMap<String, String> ProclamationContent(String id) throws SQLException {
        HashMap<String, String> map = new HashMap<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT x.* FROM newscampus.proclamation x WHERE x.id = ? ";
            pStmt = connection.prepareStatement(sql);
            pStmt.setNString(1, id);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String title = rs.getNString("p_title");
                String content = rs.getNString("p_content");
                String publisher = rs.getNString("p_publisher");
                Date date = rs.getDate("p_date");
                map.put("title", title);
                map.put("content", content);
                map.put("publisher", publisher);
                map.put("date", String.valueOf(date));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return map;
    }

    //获取轮播图
    public synchronized static List<BannerDataInfo> Banner() throws SQLException {
        List<BannerDataInfo> bannerInfo = new LinkedList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "SELECT x.* FROM newscampus.banner x";
            pStmt = connection.prepareStatement(sql);
            Log.i("112", "3");
            rs = pStmt.executeQuery();
            while (rs.next()) {
                BannerDataInfo item = new BannerDataInfo();
                item.setTitle(rs.getNString("b_title"));
                item.setImage(rs.getString("b_picture"));
                bannerInfo.add(item);
            }
            if (pStmt != null) {
                pStmt.close();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return bannerInfo;
    }
    //获取收藏新闻
    public synchronized static List<NewsInfo> getCollections(String[] newsId) {
        List<NewsInfo> newsList = new ArrayList<>();
        ResultSet rs;
        pStmt = null;
        Connection connection = null;
        for (int i=0; i< newsId.length; i++){
            try{
                connection = getConn("newscampus");
                String sql = "SELECT * FROM newsinfo n WHERE n.id = ?";
                pStmt = connection.prepareStatement(sql);
                pStmt.setNString(1, newsId[i]);
                rs = pStmt.executeQuery();
                while (rs.next()){
                    NewsInfo item = new NewsInfo();
                    item.setId(rs.getInt("id"));
                    item.setTitle(rs.getNString("title"));
                    item.setLabel(rs.getString("label"));
                    newsList.add(item);
                }
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return newsList;
    }
    //创建用户评论
    public synchronized static int upDateThumbs(NewsInfo item) throws SQLException {
        int Row = 0;
        pStmt = null;
        Connection connection =null;
        try {
            connection = getConn("newscampus");
            String sql = "UPDATE newsinfo SET browse = ? WHERE id = ?;";
            pStmt = connection.prepareStatement(sql);
            pStmt.setInt(1, item.getBrowse());
            pStmt.setInt(2,item.getId());
            Row = pStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pStmt.close();
            connection.close();
        }
        return Row;
    }
}
