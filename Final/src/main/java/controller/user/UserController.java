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
        //��ȡ����
        String id_str = request.getParameter("id");
        String userName_str = request.getParameter("username");
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        try {
            //���id = null, ��ʾ��Ӧ����ѧԺ���󣬷�����Ӧidָ����ѧԺ����
            if (id_str == null) {
                responseUserByUserName(userName_str,response);
            } else {
                int id = Integer.parseInt(id_str);
                responseUserById(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
            //��Ӧmessage��ǰ��
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
            //��Ӧmessage��ǰ��
            response.getWriter().println(message);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪUser����
        User userToChange = JSON.parseObject(user_json, User.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�User�����Ӧ�ļ�¼
        try {
            UserService.getInstance().changePassWord(userToChange);
            message.put("message", "�޸ĳɹ�!");
        } catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String User_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪTeacher����
        User userToCheck = JSON.parseObject(User_json, User.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��ƥ��User�����Ӧ�ļ�¼
        try{
            User loginUser = UserService.getInstance().login(userToCheck.getUsername(), userToCheck.getPassword());
            if (loginUser == null){
                message.put("message", "�û������������!");
            }
            else {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", userToCheck);

                UserService.getInstance().updateDate(userToCheck.getUsername());
                responseUserByUserName(userToCheck.getUsername(), response);
                message.put("message", "��¼�ɹ�!");
            }
        }catch (SQLException e) {
            message.put("message", "���ݿ�����쳣!");
            e.printStackTrace();
        }

        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    //ͨ��id��Ӧһ���û�����
    private void responseUserById(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id�����û�
        User user = UserService.getInstance().findUserById(id);
        String user_json = JSON.toJSONString(user);
        //��Ӧuser_json��ǰ��
        response.getWriter().println(user_json);
    }

    //ͨ��userName��Ӧһ���û�����
    private void responseUserByUserName(String userName, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����userName�����û�
        User user = UserService.getInstance().findUserByUserName(userName);
        String user_json = JSON.toJSONString(user);
        //��Ӧuser_json��ǰ��
        response.getWriter().println(user_json);
    }

}
