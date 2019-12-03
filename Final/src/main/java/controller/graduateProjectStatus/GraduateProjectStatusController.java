package controller.graduateProjectStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.GraduateProjectStatus;
import service.GraduateProjectStatusService;
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
@WebServlet("/graduateProjectStatus.ctl")
public class GraduateProjectStatusController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull���±�ҵ���״̬","no":"05","remarks":""}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1�ı�ҵ���״̬","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/graduateProjectStatus.ctl, ���ӱ�ҵ���״̬
     * ����һ����ҵ���״̬���󣺽�����ǰ�������JSON�������ӵ����ݿ����
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
        String graduateProjectStatus_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪGraduateProjectStatus����
        GraduateProjectStatus graduateProjectStatusToAdd = JSON.parseObject(graduateProjectStatus_json, GraduateProjectStatus.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������GraduateProjectStatus����
        try {
            GraduateProjectStatusService.getInstance().add(graduateProjectStatusToAdd);
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
     * DELETE, http://49.235.240.61:8080/Flipped/graduateProjectStatus.ctl?id=1, ɾ��id=1�ı�ҵ���״̬
     * ɾ��һ����ҵ���״̬���󣺸�������ǰ�������id��ɾ�����ݿ����id�Ķ�Ӧ��¼
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

        //�����ݿ����ɾ����Ӧ�ı�ҵ���״̬
        try {
            boolean deleted = GraduateProjectStatusService.getInstance().delete(id);
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
     * PUT, http://49.235.240.61:8080/Flipped/graduateProjectStatus.ctl, �޸ı�ҵ���״̬
     * <p>
     * �޸�һ����ҵ���״̬���󣺽�����ǰ�������JSON���󣬸������ݿ������ͬid�ļ�¼
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String graduateProjectStatus_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪGraduateProjectStatus����
        GraduateProjectStatus graduateProjectStatusToAdd = JSON.parseObject(graduateProjectStatus_json, GraduateProjectStatus.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�GraduateProjectStatus�����Ӧ�ļ�¼
        try {
            GraduateProjectStatusService.getInstance().update(graduateProjectStatusToAdd);
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
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectStatus.ctl?id=1, ��ѯid=1�ı�ҵ���״̬
     * GET, http://49.235.240.61:8080/Flipped/graduateProjectStatus.ctl, ��ѯ���еı�ҵ���״̬
     * ��һ�������б�ҵ���״̬������Ӧ��ǰ��
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
            //���id = null, ��ʾ��Ӧ���б�ҵ���״̬���󣬷�����Ӧidָ���ı�ҵ���״̬����
            if (id_str == null) {
                responseGraduateProjectStatuss(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseGraduateProjectStatus(id, response);
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

    //��Ӧһ����ҵ���״̬����
    private void responseGraduateProjectStatus(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id���ұ�ҵ���״̬
        GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(id);
        String graduateProjectStatus_json = JSON.toJSONString(graduateProjectStatus);

        //��ӦgraduateProjectStatus_json��ǰ��
        response.getWriter().println(graduateProjectStatus_json);
    }

    //��Ӧ���б�ҵ���״̬����
    private void responseGraduateProjectStatuss(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //������б�ҵ���״̬
        Collection<GraduateProjectStatus> graduateProjectStatuss = GraduateProjectStatusService.getInstance().findAll();
        String graduateProjectStatuss_json = JSON.toJSONString(graduateProjectStatuss, SerializerFeature.DisableCircularReferenceDetect);

        //��ӦgraduateProjectStatuss_json��ǰ��
        response.getWriter().println(graduateProjectStatuss_json);
    }
}
