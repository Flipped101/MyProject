package controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.User;
import service.UserService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数
        String id_str = request.getParameter("id");
        String userName_str = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseUserByUserName(userName_str,response);
            } else {
                int id = Integer.parseInt(id_str);
                responseUserById(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToChange = JSON.parseObject(user_json, User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改User对象对应的记录
        try {
            UserService.getInstance().changePassWord(userToChange);
            message.put("message", "修改成功!");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常!");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String User_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        User userToCheck = JSON.parseObject(User_json, User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表匹配User对象对应的记录
        try{
            User loginUser = UserService.getInstance().login(userToCheck.getUsername(), userToCheck.getPassword());
            if (loginUser == null){
                message.put("message", "用户名或密码错误!");
            }
            else {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", userToCheck);

                UserService.getInstance().updateDate(userToCheck.getUsername());
                responseUserByUserName(userToCheck.getUsername(), response);
                message.put("message", "登录成功!");
            }
        }catch (SQLException e) {
            message.put("message", "数据库操作异常!");
            e.printStackTrace();
        }

        //响应message到前端
        response.getWriter().println(message);
    }

    //通过id响应一个用户对象
    private void responseUserById(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找用户
        User user = UserService.getInstance().findUserById(id);
        String user_json = JSON.toJSONString(user);
        //响应user_json到前端
        response.getWriter().println(user_json);
    }

    //通过userName响应一个用户对象
    private void responseUserByUserName(String userName, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据userName查找用户
        User user = UserService.getInstance().findUserByUserName(userName);
        String user_json = JSON.toJSONString(user);
        //响应user_json到前端
        response.getWriter().println(user_json);
    }

}
