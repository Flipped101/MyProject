package controller.teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Teacher;
import service.TeacherService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * �����з�����֯��һ��Controller(Servlet)��
 */
@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"degree":{"description":"��ʿ","id":3,"no":"01","remarks":""},"department":{"description":"��������","id":3,"no":"0203","remarks":"","school":{"description":"����","id":2,"no":"02"}},"name":"idΪnull���½�ʦ","title":{"description":"����","id":1,"no":"01","remarks":""}}
    //��ʹ������JSON�����޸Ĺ���
    //{"degree":{"description":"��ʿ","id":3,"no":"01","remarks":""},"department":{"description":"��������","id":3,"no":"0203","remarks":"","school":{"description":"����","id":2,"no":"02"}},"name":"idΪnull�Ľ�ʦ","title":{"description":"����","id":1,"no":"01","remarks":""}}

    /**
     * POST, http://49.235.240.61:8080/Flipped/teacher.ctl, ���ӽ�ʦ
     * ����һ����ʦ���󣺽�����ǰ�������JSON�������ӵ����ݿ����
     *
     * @param request  �������
     * @param response ��Ӧ����
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //����request���󣬻�ô��������JSON�ִ�
        String teacher_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪTeacher����
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������Teacher����
        try {
            boolean added = TeacherService.getInstance().add(teacherToAdd);
            if (added){
                message.put("message", "���ӳɹ�!");
            }else {
                message.put("message", "����ʧ��!");
            }
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://49.235.240.61:8080/Flipped/teacher.ctl?id=1, ɾ��id=1�Ľ�ʦ
     * ɾ��һ����ʦ���󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();

        //�����ݿ����ɾ����Ӧ�Ľ�ʦ
        try {
            boolean deleted = TeacherService.getInstance().delete(id);
            if (deleted){
                message.put("message", "ɾ���ɹ�!");
            }else {
                message.put("message", "������¼�ѱ�ɾ���򲻴���!");
            }
        } catch (Exception e) {
            message.put("message", "�����쳣!");
            e.printStackTrace();
        }
        //��Ӧmessage��ǰ��
        response.getWriter().println(message);
    }


    /**
     * PUT, http://49.235.240.61:8080/Flipped/teacher.ctl, �޸Ľ�ʦ
     * <p>
     * �޸�һ����ʦ���󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪTeacher����
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�Teacher�����Ӧ�ļ�¼
        try {
            TeacherService.getInstance().update(teacherToAdd);
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

    /**
     * GET, http://49.235.240.61:8080/Flipped/teacher.ctl?id=1, ��ѯid=1�Ľ�ʦ
     * GET, http://49.235.240.61:8080/Flipped/teacher.ctl, ��ѯ���еĽ�ʦ
     * ��һ�������н�ʦ������Ӧ��ǰ��
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        String department_id_str = request.getParameter("department_id");
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        try {
            //���id = null, ��ʾ��Ӧ����ѧԺ���󣬷�����Ӧidָ����ѧԺ����
            if (id_str == null) {
                responseTeachers(response);
//            } else if(department_id_str != null) {
//                int school_id = Integer.parseInt(department_id_str);
//                responseTeacherByDepartment(school_id, response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeacher(id, response);
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

    //��Ӧһ����ʦ����
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id���ҽ�ʦ
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);

        //��Ӧteacher_json��ǰ��
        response.getWriter().println(teacher_json);
    }

    //��Ӧ���н�ʦ����
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //������н�ʦ
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);

        //��Ӧteachers_json��ǰ��
        response.getWriter().println(teachers_json);
    }

//    private void responseTeacherByDepartment(int department_id,HttpServletResponse response)
//            throws ServletException, IOException, SQLException {
//        //��ö�Ӧ������ϵ
//        Collection<Teacher> teachers = TeacherService.getInstance().findAllByDepartment(department_id);
//        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
//
//        //��Ӧdepartments_json��ǰ��
//        response.getWriter().println(teachers_json);
//    }

}
