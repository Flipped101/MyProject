package controller.graduateProjectType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.GraduateProjectType;
import service.GraduateProjectTypeService;
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
@WebServlet("/graduateProjectType.ctl")
public class GraduateProjectTypeController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull����ѧԺ","no":"05","remarks":""}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1��ѧԺ","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/graduateProjectType.ctl, ����ѧԺ
     * ����һ��ѧԺ���󣺽�����ǰ�������JSON�������ӵ����ݿ����
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
        String graduateProjectType_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪGraduateProjectType����
        GraduateProjectType graduateProjectTypeToAdd = JSON.parseObject(graduateProjectType_json, GraduateProjectType.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������GraduateProjectType����
        try {
            GraduateProjectTypeService.getInstance().add(graduateProjectTypeToAdd);
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
     * DELETE, http://49.235.240.61:8080/Flipped/graduateProjectType.ctl?id=1, ɾ��id=1��ѧԺ
     * ɾ��һ��ѧԺ���󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
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

        //�����ݿ����ɾ����Ӧ��ѧԺ
        try {
            boolean deleted = GraduateProjectTypeService.getInstance().delete(id);
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
     * PUT, http://49.235.240.61:8080/Flipped/graduateProjectType.ctl, �޸�ѧԺ
     * <p>
     * �޸�һ��ѧԺ���󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String graduateProjectType_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪGraduateProjectType����
        GraduateProjectType graduateProjectTypeToAdd = JSON.parseObject(graduateProjectType_json, GraduateProjectType.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�GraduateProjectType�����Ӧ�ļ�¼
        try {
            GraduateProjectTypeService.getInstance().update(graduateProjectTypeToAdd);
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
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectType.ctl?id=1, ��ѯid=1��ѧԺ
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectType.ctl, ��ѯ���е�ѧԺ
     * ��һ��������ѧԺ������Ӧ��ǰ��
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
            //���id = null, ��ʾ��Ӧ����ѧԺ���󣬷�����Ӧidָ����ѧԺ����
            if (id_str == null) {
                responseGraduateProjectTypes(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProjectType(id, response);
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

    //��Ӧһ��ѧԺ����
    private void responseGraduateProjectType(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(id);
        String graduateProjectType_json = JSON.toJSONString(graduateProjectType);

        //��ӦgraduateProjectType_json��ǰ��
        response.getWriter().println(graduateProjectType_json);
    }

    //��Ӧ����ѧԺ����
    private void responseGraduateProjectTypes(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<GraduateProjectType> graduateProjectTypes = GraduateProjectTypeService.getInstance().findAll();
        String graduateProjectTypes_json = JSON.toJSONString(graduateProjectTypes, SerializerFeature.DisableCircularReferenceDetect);

        //��ӦgraduateProjectTypes_json��ǰ��
        response.getWriter().println(graduateProjectTypes_json);
    }
}
