package controller.major;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Major;
import service.MajorService;
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
@WebServlet("/major.ctl")
public class MajorController extends HttpServlet {
    //��ʹ������JSON�������ӹ��ܣ�idΪ�գ�
    //{"description":"idΪnull����ѧԺ","no":"05","remarks":""}
    //��ʹ������JSON�����޸Ĺ���
    //{"description":"�޸�id=1��ѧԺ","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.235.240.61:8080/Flipped/major.ctl, ����ѧԺ
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
        String major_json = JSONUtil.getJSON(request);

        //��JSON�ִ�����ΪMajor����
        Major majorToAdd = JSON.parseObject(major_json, Major.class);


        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ��������Major����
        try {
            MajorService.getInstance().add(majorToAdd);
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
     * DELETE, http://49.235.240.61:8080/Flipped/major.ctl?id=1, ɾ��id=1��ѧԺ
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
            boolean deleted = MajorService.getInstance().delete(id);
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
     * PUT, http://49.235.240.61:8080/Flipped/major.ctl, �޸�ѧԺ
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
        String major_json = JSONUtil.getJSON(request);
        //��JSON�ִ�����ΪMajor����
        Major majorToAdd = JSON.parseObject(major_json, Major.class);

        //����JSON����message���Ա���ǰ����Ӧ��Ϣ
        JSONObject message = new JSONObject();
        //�����ݿ���޸�Major�����Ӧ�ļ�¼
        try {
            MajorService.getInstance().update(majorToAdd);
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
     * GET, http://49.235.240.61:8080/Flipped/major.ctl?id=1, ��ѯid=1��ѧԺ
     * GET, http://49.235.240.61:8080/Flipped/major.ctl, ��ѯ���е�ѧԺ
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
                responseMajors(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseMajor(id, response);
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
    private void responseMajor(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //����id����ѧԺ
        Major major = MajorService.getInstance().find(id);
        String major_json = JSON.toJSONString(major);

        //��Ӧmajor_json��ǰ��
        response.getWriter().println(major_json);
    }

    //��Ӧ����ѧԺ����
    private void responseMajors(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //�������ѧԺ
        Collection<Major> majors = MajorService.getInstance().findAll();
        String majors_json = JSON.toJSONString(majors, SerializerFeature.DisableCircularReferenceDetect);

        //��Ӧmajors_json��ǰ��
        response.getWriter().println(majors_json);
    }
}
