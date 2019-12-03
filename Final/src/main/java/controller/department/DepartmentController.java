package controller.department;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Department;
import service.DepartmentService;
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
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull��ϵ","no":"0201","remarks":"","school":{"description":"������","id":2,"no":"02","remarks":"��õ�ѧԺ"}}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1��ϵ","id":1,"no":"0201","remarks":"","school":{"description":"������","id":2,"no":"02","remarks":"��õ�ѧԺ"}}

    /**
     * POST, http://49.235.240.61:8080/Flipped/department.ctl, ����ϵ
     * ����һ��ϵ���󣺽�����ǰ�������JSON�������ӵ����ݿ����
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
        String department_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪDepartment����
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������Department����
        try {
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("message", "���ӳɹ�!");
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
     * DELETE, http://49.235.240.61:8080/Flipped/department.ctl?id=1, ɾ��id=1��ϵ
     * ɾ��һ��ϵ���󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡ����id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();

        //�����ݿ����ɾ����Ӧ��ϵ
        try {
            boolean deleted = DepartmentService.getInstance().delete(id);
            if (deleted){
                message.put("message", "ɾ���ɹ�!");
            }else {
                message.put("message", "������¼�ѱ�ɾ���򲻴���!");
            }
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
     * PUT, http://49.235.240.61:8080/Flipped/department.ctl, �޸�ϵ
     * <p>
     * �޸�һ��ϵ���󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String department_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪDepartment����
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�Department�����Ӧ�ļ�¼
        try {
            DepartmentService.getInstance().update(departmentToAdd);
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
     * GET, http://49.235.240.61:8080/Flipped/department.ctl?id=1, ��ѯid=1��ϵ
     * GET, http://49.235.240.61:8080/Flipped/department.ctl, ��ѯ���е�ϵ
     * ��һ��������ϵ������Ӧ��ǰ��
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
        String school_id_str = request.getParameter("school_id");

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        try {
            //���id = null, ��ʾ��Ӧ����ϵ���󣬷�����Ӧidָ����ϵ����
            if (id_str == null&&school_id_str == null) {
                responseDepartments(response);
            } else if(school_id_str != null) {
                int school_id = Integer.parseInt(school_id_str);
                responseDepartmentBySchool(school_id, response);
            } else {
                int id = Integer.parseInt(id_str);
                responseDepartment(id, response);
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

    //��Ӧһ��ϵ����
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ϵ
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);

        //��Ӧdepartment_json��ǰ��
        response.getWriter().println(department_json);
    }

    //��Ӧ����ϵ����
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ϵ
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);

        //��Ӧdepartments_json��ǰ��
        response.getWriter().println(departments_json);
    }
    private void responseDepartmentBySchool(int school_id,HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //��ö�Ӧ������ϵ
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(school_id);
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);

        //��Ӧdepartments_json��ǰ��
        response.getWriter().println(departments_json);
    }
}
