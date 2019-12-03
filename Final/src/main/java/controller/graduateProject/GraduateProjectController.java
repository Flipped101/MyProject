package controller.graduateProject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.GraduateProject;
import service.GraduateProjectService;
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
@WebServlet("/graduateProject.ctl")
public class GraduateProjectController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull���±�ҵ���","no":"05","remarks":""}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1�ı�ҵ���","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/graduateProject.ctl, ���ӱ�ҵ���
     * ����һ����ҵ��ƶ��󣺽�����ǰ�������JSON�������ӵ����ݿ����
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
        String graduateProject_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪGraduateProject����
        GraduateProject graduateProjectToAdd = JSON.parseObject(graduateProject_json, GraduateProject.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������GraduateProject����
        try {
            GraduateProjectService.getInstance().add(graduateProjectToAdd);
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
     * DELETE, http://49.235.240.61:8080/Flipped/graduateProject.ctl?id=1, ɾ��id=1�ı�ҵ���
     * ɾ��һ����ҵ��ƶ��󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
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

        //�����ݿ����ɾ����Ӧ�ı�ҵ���
        try {
            boolean deleted = GraduateProjectService.getInstance().delete(id);
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
     * PUT, http://49.235.240.61:8080/Flipped/graduateProject.ctl, �޸ı�ҵ���
     * <p>
     * �޸�һ����ҵ��ƶ��󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String graduateProject_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪGraduateProject����
        GraduateProject graduateProjectToAdd = JSON.parseObject(graduateProject_json, GraduateProject.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�GraduateProject�����Ӧ�ļ�¼
        try {
            GraduateProjectService.getInstance().update(graduateProjectToAdd);
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
     * GET, http://49.235.240.61:8080/Flipped/graduateProject.ctl?id=1, ��ѯid=1�ı�ҵ���
     * GET, http://49.235.240.61:8080/Flipped/graduateProject.ctl, ��ѯ���еı�ҵ���
     * ��һ�������б�ҵ��ƶ�����Ӧ��ǰ��
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

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        try {
            //���id = null, ��ʾ��Ӧ���б�ҵ��ƶ��󣬷�����Ӧidָ���ı�ҵ��ƶ���
            if (id_str == null) {
                responseGraduateProjects(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProject(id, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    //��Ӧһ����ҵ��ƶ���
    private void responseGraduateProject(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id���ұ�ҵ���
        GraduateProject graduateProject = GraduateProjectService.getInstance().find(id);
        String graduateProject_json = JSON.toJSONString(graduateProject);

        //��ӦgraduateProject_json��ǰ��
        response.getWriter().println(graduateProject_json);
    }

    //��Ӧ���б�ҵ��ƶ���
    private void responseGraduateProjects(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //������б�ҵ���
        Collection<GraduateProject> graduateProjects = GraduateProjectService.getInstance().findAll();
        String graduateProjects_json = JSON.toJSONString(graduateProjects, SerializerFeature.DisableCircularReferenceDetect);

        //��ӦgraduateProjects_json��ǰ��
        response.getWriter().println(graduateProjects_json);
    }
}
